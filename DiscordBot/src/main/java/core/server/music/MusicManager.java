package core.server.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Jay
 */
public final class MusicManager {
    private Guild guild;
    private AudioPlayerManager manager;
    private AudioPlayer player;
    private TrackScheduler scheduler;
    private AudioPlayerSendHandler handler;
    private AudioPlayerResultHandler aprh;
    private List<Long> skips;


    public MusicManager(Guild guild, MessageChannel channel) {
        this.guild = guild;
        manager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(manager);
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player, channel);
        player.addListener(scheduler);
        handler = new AudioPlayerSendHandler(player);
        aprh = new AudioPlayerResultHandler(channel, scheduler);
        skips = new ArrayList<>();
    }


    public void clear() {
        scheduler.clear();
    }

    public boolean connect(VoiceChannel channel) {
        if (!isConnected()) {
            guild.getAudioManager().openAudioConnection(channel);
            guild.getAudioManager().setSendingHandler(handler);
            return true;
        }
        return false;
    }

    public boolean disconnect() {
        if (isConnected()) {
            guild.getAudioManager().setSendingHandler(null);
            guild.getAudioManager().closeAudioConnection();
            skips.clear();
            scheduler.clear();
            return true;
        }
        return false;
    }

    public AudioTrack getPlayingTrack() {
        return player.getPlayingTrack();
    }

    public List<AudioTrack> getQueue() {
        return scheduler.getQueue();
    }

    public boolean hasFlag(TrackScheduler.PlayFlag flag) {
        return scheduler.hasFlag(flag);
    }

    public boolean hasSkipped(long memberId) {
        return skips.indexOf(memberId) >= 0;
    }

    public boolean isConnected() {
        return guild.getAudioManager().isConnected();
    }

    public boolean isPaused() {
        return player.getPlayingTrack() != null && player.isPaused();
    }

    public boolean isPlaying() {
        return player.getPlayingTrack() != null && !player.isPaused();
    }

    public boolean skip(long memberId, int veto, boolean forced) {
        // TODO: add flexibility to skipping? democratic, equality, dictatorship, etc.; use interface.
        if (forced || !hasSkipped(memberId)) {
            skips.add(memberId);

            if (veto <= skips.size()) {
                scheduler.next(true);

                if (scheduler.getQueue().isEmpty())
                    player.stopTrack();
                else
                    scheduler.play();
                skips.clear();
                return true;
            }
        }
        return false;
    }

    public void pause() {
        if (isConnected())
            player.setPaused(true);
    }

    public Future<Void> queue(String URL) {
        return manager.loadItem(URL, aprh);
    }

    public void resume() {
        if (isConnected())
            player.setPaused(false);
    }

    public void setFlag(TrackScheduler.PlayFlag flag, boolean value) {
        scheduler.setFlag(flag, value);
    }
}
