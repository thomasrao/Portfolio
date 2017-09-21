package commands.guildchannel;

import commands.CommandResult;
import core.server.ServerManager;
import core.server.music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Disconnects the bot from the voice channel it is
 *  connected to.
 *
 * @author Jay
 */
public class AudioLeave extends GuildChannelCommand {
    protected AudioLeave(){
        super("leave", "", "Disconnects the bot from the voice channel", GuildChannelCommandSet.MUSIC, (byte) 0);
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

        // Disconnect the bot if it is connected.
        if (mm != null && mm.isConnected()) {
            mm.disconnect();
            return CommandResult.SUCCESS;
        }
        return CommandResult.FAILURE;
    }
}
