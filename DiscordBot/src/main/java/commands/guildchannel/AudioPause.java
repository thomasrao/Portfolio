package commands.guildchannel;

import commands.CommandResult;
import core.server.ServerManager;
import core.server.music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Pauses the bot's song that is being played.
 * It can later be resumed with the resume command.
 *
 * @author Jay
 */
public class AudioPause extends GuildChannelCommand {
    protected AudioPause() {
        super("pause", "", "Pauses the current song", GuildChannelCommandSet.MUSIC, (byte) 0);
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

        if (mm == null)
            return CommandResult.FAILURE;

        // Pause the bot's voice channel activity.
        if (mm.isConnected() && mm.isPlaying())
            mm.pause();

        return CommandResult.SUCCESS;
    }
}
