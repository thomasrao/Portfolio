package games.blackjack.commands;

import commands.CommandResult;
import games.blackjack.Blackjack;

/**
 * @author Tom
 */
public class Stand extends BlackjackCommand {
    protected Stand() {
        super("stand", "", "You cannot pick a card after this.");
    }

    @Override
    public CommandResult handle(Blackjack game, long memberId, String message) {
        game.stand(memberId);
        return CommandResult.SUCCESS;
    }
}
