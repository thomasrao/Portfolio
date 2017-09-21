package games.blackjack;

import core.server.ServerManager;
import games.Game;
import games.GameType;
import games.blackjack.commands.BlackjackCommandManager;
import games.cards.Card;
import games.cards.Deck;
import net.dv8tion.jda.core.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The blackjack game.
 * The goal of the game is to reach as close as
 *  possible without going over 21 points.
 *  The score is calculated by the sum of the value
 *  of each card in one's hand with the exceptions of
 *  face cards counting 10 points and aces counting
 *  as either 1 or 11, whichever suits one better.
 *
 * @author Tom
 */
public final class Blackjack extends Game {
    private static class BlackjackPlayer {
        public Member member;
        public List<Card> hand;
        public boolean standing;


        public BlackjackPlayer(Member member) {
            this.member = member;

            hand = new ArrayList<>(5);
            standing = false;
        }
    }


    private final static int BEST_SCORE = 21;
    private final static int MAXIMUM_SCORE_PER_CARD = 10;
    private final static int ACE_ALTERNATIVE_SCORE = 11;

    private Deck deck;
    private Map<Long, BlackjackPlayer> players;


    public Blackjack(String id, Guild guild, TextChannel channel, Member owner) {
        super(id, GameType.BLACKJACK, guild, channel, owner);

        deck = new Deck();
        players = new HashMap<>();
    }


    /**
     * Calculates the score of a given player.
     * @param memberId the member's id
     * @return the score of the given participant
     */
    public int getScore(long memberId) {
        int score = 0;
        boolean ace = false;
        for (Card card : players.get(memberId).hand) {
            score += Math.min(card.getValue(), MAXIMUM_SCORE_PER_CARD);
            if (card.getValue() == 1)
                ace = true;
        }

        // Checking if ace's alternative score will
        // help this member win.
        // Ace's value of one is already included
        // in the score calculation.
        if (ace && score <= BEST_SCORE - ACE_ALTERNATIVE_SCORE + 1)
            score += ACE_ALTERNATIVE_SCORE - 1;

        return score;
    }

    /**
     * This causes a member to pick a card from the deck.
     * It also checks if this participant has been busted,
     *  meaning the player's score is above 21.
     * @param memberId the member's id
     * @param silent flag to prevent others knowing what you picked.
     * @return true if picking a card was successful, false otherwise.
     */
    public boolean hit(long memberId, boolean silent) {
        if (players.containsKey(memberId) && !players.get(memberId).standing) {
            Card next = deck.pickNextCard();
            players.get(memberId).hand.add(next);

            String name = ServerManager.getInstance().get(getGuildId()).getMemberDetail(memberId).getUsername();
            if (!silent)
                sendMessage(name + ", you have picked a " + next.toString() + ".").submit();

            if (getScore(memberId) > 21) {
                players.get(memberId).standing = true;
                sendMessage(name + ", you have been busted!").submit();
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        if (!deck.hasRemainingCard())
            return true;

        for (Map.Entry<Long, BlackjackPlayer> player : players.entrySet()) {
            if (!player.getValue().standing)
                return false;
        }
        return true;
    }

    /**
     * Nothing happens.
     * @param channel the new channel.
     */
    @Override
    public void onTextChannelCreate(MessageChannel channel) {}

    /**
     * Nothing happens.
     * @param channel the deleted channel.
     */
    @Override
    public void onTextChannelDelete(MessageChannel channel) {}

    /**
     * Gets the number of people playing this game.
     * @return the number of players.
     */
    @Override
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Checks if the user is playing the game.
     * @param memberId the user's id
     * @return true if the user is a participant, false otherwise.
     */
    @Override
    public boolean isPlaying(long memberId) {
        return players.containsKey(memberId);
    }

    /**
     * Nothing happens.
     * @param member the new member.
     */
    @Override
    public void onGuildMemberJoin(Member member) {}

    /**
     * Nothing happens.
     * @param member the leaving member.
     */
    @Override
    public void onGuildMemberLeave(Member member) {
        if (isPlaying(member.getUser().getIdLong()))
            remove(member.getUser().getIdLong());
    }

    /**
     * Let participants have commands to execute.
     * @param message the new message.
     */
    @Override
    public void onMessageReceived(Message message) {
        if (message.getMember().getUser().isBot() || !isPlaying(message.getMember().getUser().getIdLong()))
            return;

        BlackjackCommandManager.getInstance().run(this, message.getMember().getUser().getIdLong(), message.getRawContent(), BlackjackCommandManager.BLACKJACK_COMMAND_PREFIX);

        if (isGameOver())
            stop();
    }

    /**
     * Add new data of the member who just joined.
     * @param member the joining participant.
     */
    @Override
    public void onParticipantJoin(Member member) {
        if (players.size() + 1 < type.getMaximumPlayerCount() && deck.hasRemainingCard(2)) {
            players.put(member.getUser().getIdLong(), new BlackjackPlayer(member));

            BlackjackPlayer player = players.get(member.getUser().getIdLong());

            hit(member.getUser().getIdLong(), false);
            hit(member.getUser().getIdLong(), true);

            player.member.getUser().openPrivateChannel().complete()
                    .sendMessage("[" + type.name() + "] You have a " + player.hand.get(1) + '.').submit();
        }
    }

    /**
     * Remove the data of the leaving member.
     * @param member the leaving participant.
     */
    @Override
    public void onParticipantLeave(Member member) {
        players.remove(member.getUser().getIdLong());

        if (isGameOver() || isOwner(member.getUser().getIdLong()))
            stop();
    }

    /**
     * When the game starts, this will hand out
     *  2 cards to every player: 1 in private
     *  while the other is revealed to everyone.
     */
    @Override
    public void onStart() {
        // Shuffle the deck
        deck.restart(true);
        for (int i = 1; i < 7; i++)
            deck.shuffle();

        // Give 2 cards to each participant
        for (BlackjackPlayer player : players.values()) {
            hit(player.member.getUser().getIdLong(), false);
            hit(player.member.getUser().getIdLong(), true);

            player.member.getUser().openPrivateChannel().complete()
                    .sendMessage("[" + type.name() + "] You have a " + player.hand.get(1) + '.').submit();
        }

        sendMessage("Use the " + BlackjackCommandManager.BLACKJACK_COMMAND_PREFIX + "help to get everyone who is playing started!").submit();
    }

    /**
     * When the game stops, this will output
     *  the winners of the current game session.
     */
    @Override
    public void onStop() {
        StringBuilder sb = new StringBuilder();
        sb.append("The scores for everyone are as follows:\n");

        // Find the winner(s) if any.
        int highestScore = Integer.MIN_VALUE;
        int smallestHand = Integer.MAX_VALUE;
        List<Map.Entry<Long, BlackjackPlayer>> winners = new ArrayList<>();
        for (Map.Entry<Long, BlackjackPlayer> player : players.entrySet()) {
            int score = getScore(player.getKey());
            int handSize = player.getValue().hand.size();

            if (score <= 21) {
                if (highestScore < score || highestScore == score && smallestHand > handSize) {
                    winners.clear();
                    winners.add(player);
                    highestScore = score;
                } else if (highestScore == score && smallestHand == handSize)
                    winners.add(player);
            }

            sb.append(player.getValue().member.getEffectiveName()).append(": ").append(score).append(" points.\n");
        }

        // Output the list of scores & winner(s) if any.
        if (winners.size() == 0) {
            sb.append("\n**There are no winners this round.** Everyone was busted.");
        } else if (winners.size() == 1) {
            String name = ServerManager.getInstance().get(getGuildId()).getMemberDetail(winners.get(0).getKey()).getUsername();
            sb.append("\nThe winner is **" + name + "** with " + highestScore + " points from " + smallestHand + " cards in their hands!");
        } else {
            sb.append("\nThe winners are the ones with " + highestScore + " points from " + smallestHand + " cards in their hands:\n");

            for (Map.Entry<Long, BlackjackPlayer> player : winners) {
                String name = ServerManager.getInstance().get(getGuildId()).getMemberDetail(player.getKey()).getUsername();
                sb.append("    ").append(name).append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
        }

        sendMessage(sb.toString()).submit();

        for (BlackjackPlayer player : players.values())
            player.hand.clear();
    }

    /**
     * This causes the participant to stand, meaning that the
     *  participant has finished picking cards.
     * @param memberId the member's id who is going to stand.
     * @return true if stands succeeds, false otherwise.
     */
    public boolean stand(long memberId) {
        if (players.containsKey(memberId) && !players.get(memberId).standing) {
            players.get(memberId).standing = true;
            String name = ServerManager.getInstance().get(getGuildId()).getMemberDetail(memberId).getUsername();
            sendMessage(name + " stands.").submit();
            return true;
        }
        return false;
    }
}
