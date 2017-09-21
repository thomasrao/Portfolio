package core.server.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.MessageChannel;
import tools.logging.Log;

/**
 * @author Tom
 */
public class AudioPlayerResultHandler implements AudioLoadResultHandler {
    private MessageChannel messageChannel;
    private TrackScheduler scheduler;


    public AudioPlayerResultHandler(MessageChannel messageChannel, TrackScheduler scheduler) {
        this.messageChannel = messageChannel;
        this.scheduler = scheduler;
    }


    @Override
    public void trackLoaded(AudioTrack track) {
        scheduler.enQueue(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        for (AudioTrack track : playlist.getTracks())
            scheduler.enQueue(track);
    }

    @Override
    public void noMatches() {
        // Nothing found
        messageChannel.sendMessage("No match.").submit();
    }

    @Override
    public void loadFailed(FriendlyException e) {
        messageChannel.sendMessage("Load failed.").submit();
        Log.getInstance().error(e, "Request");
    }
}
