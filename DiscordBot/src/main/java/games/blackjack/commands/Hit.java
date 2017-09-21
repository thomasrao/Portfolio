package games.blackjack.commands;

import commands.CommandResult;
import games.blackjack.Blackjack;

/**
 * @author Tom
 */
public class Hit extends BlackjackCommand {
    protected Hit() {
        super("hit", "", "You get another card from the deck.");
    }

    @Override
    public CommandResult handle(Blackjack game, long memberId, String message) {
        game.hit(memberId, false);
        return CommandResult.SUCCESS;
    }
}
