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
 * Requests the bot to play the audio from
 *  the URL given through the connected
 *  voice channel. Only works if the user
 *  is connected to the voice channel that
 *  the bot is present in.
 *
 * This connects the bot automatically to
 *  the user's current voice channel if
 *  the bot is not connected.
 *
 * @author Jay
 */
public class AudioRequest extends GuildChannelCommand {
    public AudioRequest(){
        super("request", "<URL>", "Plays a song from a website", GuildChannelCommandSet.MUSIC, (byte) 0);
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

        // Create music manager if it does not exist yet.
        if (g.getMusicManager() == null)
            g.createMusicManager(channel);

        MusicManager mm = g.getMusicManager();

        // Connect the bot if possible.
        if (!mm.isConnected()) {
            if (member.getVoiceState().inVoiceChannel()) {
                mm.connect(member.getVoiceState().getChannel());
            } else {
                channel.sendMessage("You must be connected to a voice channel to let this bot connect.").submit();
                return CommandResult.FAILURE;
            }
        }

        // Queue the song.
        String[] parts = message.getRawContent().split(" ");
        if (parts.length < 2)
            return CommandResult.SYNTAX;

        String website = parts[1];
        mm.queue(website);

        return CommandResult.SUCCESS;
    }
}
