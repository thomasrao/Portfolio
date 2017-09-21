package commands.guildchannel;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.CommandResult;
import core.server.ServerManager;
import core.server.music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.List;

/**
 * Displays the list of songs in order
 *  they are going to be played.
 *
 * @author Jay
 */
public class AudioInQueued extends GuildChannelCommand {
    protected AudioInQueued() {
        super("inqueued", "", "Gives you all the songs in the queue", GuildChannelCommandSet.MUSIC, (byte) 0);
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

        // Check if the music manager is not connected.
        // If so, this command failed.
        if (mm == null || !mm.isConnected())
            return CommandResult.FAILURE;

        // Output queue information.
        List<AudioTrack> queue = mm.getQueue();
        if (queue.isEmpty()) {
            channel.sendMessage("The queue is empty.").submit();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("The queue is as follows:\n");

            for (AudioTrack track : queue) {
                sb.append('_').append(track.getInfo().title).append('_');
                sb.append(" by ").append(track.getInfo().author);
                sb.append(" (").append(track.getDuration() / 1000).append(" seconds)\n");
            }

            channel.sendMessage(sb.toString()).submit();
        }
        return CommandResult.SUCCESS;
    }
}