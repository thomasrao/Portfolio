package commands.guildchannel;

import commands.CommandResult;
import core.server.ServerManager;
import core.server.music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Resumes the song if it was paused.
 *
 * @author Jay
 */
public class AudioResume extends GuildChannelCommand {
    protected AudioResume() {
        super("resume", "", "Resumes the song if it has been paused", GuildChannelCommandSet.MUSIC, (byte) 0);
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

        // Resume the bot's voice activity.
        if (mm != null && mm.isConnected()) {
            mm.resume();
        }
        return CommandResult.SUCCESS;
    }
}
