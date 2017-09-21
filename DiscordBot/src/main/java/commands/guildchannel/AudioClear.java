package commands.guildchannel;

import commands.CommandResult;
import core.server.ServerManager;
import core.server.music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Clears the queue of songs requested for that guild.
 *
 * @author Tom
 */
public class AudioClear extends GuildChannelCommand {
    protected AudioClear() {
        super("clear", "", ".", GuildChannelCommandSet.MUSIC, (byte) 0);
    }


    /**
     * Executes upon the request of this command through guild messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    @Override
    public CommandResult handle(Guild guild, MessageChannel channel, Member member, Message message) {
        MusicManager mm = ServerManager.getInstance().get(guild.getIdLong()).getMusicManager();

        // Clear the queue.
        if (mm != null) {
            mm.clear();
            channel.sendMessage("The queue has been cleared.").submit();
            return CommandResult.SUCCESS;
        }
        return CommandResult.FAILURE;
    }
}
