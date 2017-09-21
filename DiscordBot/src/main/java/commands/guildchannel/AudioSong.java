package commands.guildchannel;

import commands.CommandResult;
import core.server.ServerManager;
import core.server.guild.GuildInfo;
import core.server.music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Displays information regarding the song that is being played.
 *
 * @author Jay
 */
public class AudioSong extends GuildChannelCommand {
    protected AudioSong() {
        super("song", "", "Gives you the current song name", GuildChannelCommandSet.MUSIC, (byte) 0);
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
        GuildInfo g = ServerManager.getInstance().get(guild.getIdLong());
        MusicManager mm = g.getMusicManager();

        // Display the current song's information.
        if (mm != null && mm.isConnected()) {
            channel.sendMessage("The current track's name is _" + mm.getPlayingTrack().getInfo().title + "_.").submit();
        }
        return CommandResult.SUCCESS;
    }
}
