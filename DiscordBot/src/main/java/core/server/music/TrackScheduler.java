package core.server.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.MessageChannel;
import tools.logging.Log;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * @author Jay
 */
public class TrackScheduler extends AudioEventAdapter {
    public enum PlayFlag {
        REPEAT,
        REPEAT_ONE,
        SHUFFLE
    }

    private final AudioPlayer audioPlayer;
    private final MessageChannel channel;
    private ArrayList<AudioTrack> queue = new ArrayList<>();
    private EnumSet<PlayFlag> playFlag;
    private int index;
    private Random random;
    private AudioTrack current;


    protected TrackScheduler(AudioPlayer player, MessageChannel channel) {
        this.audioPlayer = player;
        this.channel = channel;
        this.playFlag = EnumSet.noneOf(PlayFlag.class);
        this.index = 0;
        this.random = new Random();
        this.current = null;
    }


    public void clear() {
        this.queue.clear();
        this.index = 0;
    }

    public void enQueue(AudioTrack track) {
        this.queue.add(track);
        this.current = track.makeClone();

        if (audioPlayer.getPlayingTrack() == null)
            this.audioPlayer.playTrack(this.current);
        else
            this.channel.sendMessage("Queued _" + track.getInfo().title + "_ by " + track.getInfo().author + " (" + (track.getInfo().length / 1000) + " seconds).").submit();
    }

    public AudioTrack getNextTrack() {
        if (this.queue.isEmpty())
            return null;

        return this.queue.get(index);
    }

    public List<AudioTrack> getQueue() {
        return queue;
    }

    public boolean hasFlag(PlayFlag flag) {
        return playFlag.contains(flag);
    }

    public void next(boolean forced) {
        if (forced || !hasFlag(PlayFlag.REPEAT_ONE)) {
            if (getNextTrack() != null && hasFlag(PlayFlag.REPEAT))
                this.queue.add(getNextTrack());

            if (this.queue.size() > index)
                this.queue.remove(index);

            index = hasFlag(PlayFlag.SHUFFLE) ? random.nextInt(queue.size()) : 0;
        }
    }

    public boolean play() {
        if (!queue.isEmpty()) {
            this.current = getNextTrack().makeClone();
            this.audioPlayer.playTrack(this.current);
            return true;
        } else if (current != null) {
            this.current.stop();
            this.current = null;
        }
        return false;
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        // Player was paused
        this.channel.sendMessage("_" + player.getPlayingTrack().getInfo().title + "_ has been paused.").submit();
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        // Player was resumed
        long timeLeft = (player.getPlayingTrack().getDuration() - player.getPlayingTrack().getPosition()) / 1000;
        this.channel.sendMessage("_" + player.getPlayingTrack().getInfo().title + "_ (" + timeLeft + " seconds left) resumes playing.").submit();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        // A track started playing
        this.channel.sendMessage("Playing _" + track.getInfo().title + "_ by " + track.getInfo().author + " (" + (track.getInfo().length / 1000) + " seconds).").submit();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        next(false);

        if (endReason.mayStartNext) {
            if (!play())
                this.channel.sendMessage("The queue is now empty.").submit();
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        // An already playing track threw an exception (track end event will still be received separately)
        Log.getInstance().error(exception, "TrackScheduler");
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
        //this.onTrackEnd(this.audioPlayer, this.queue.get(0), AudioTrackEndReason.LOAD_FAILED);
        Log.getInstance().warn("Track was stuck for " + channel.getName() + " by playing " + track.getInfo().uri, "TrackScheduler");
    }

    public void setFlag(PlayFlag flag, boolean value) {
        if (value)
            playFlag.add(flag);
        else
            playFlag.remove(flag);
    }
}
