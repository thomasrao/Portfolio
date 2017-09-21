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
 * Connects the bot to the voice channel in which
 *  the user is connected to.
 * If the user is not connected, the bot will not
 *  connect either, but send a message instead.
 *
 * @author Jay
 */
public class AudioJoin extends GuildChannelCommand {
    protected AudioJoin(){
        super("join", "", "Joins a channel to play music", GuildChannelCommandSet.MUSIC, (byte) 0);
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

        // Create music manager if it does not exist yet.
        if (mm == null)
            g.createMusicManager(channel);

        // Connect it to member's voice channel if possible.
        if (!mm.isConnected() || mm.getQueue().isEmpty()) {
            if (member.getVoiceState().inVoiceChannel()) {
                mm.connect(member.getVoiceState().getChannel());
                return CommandResult.SUCCESS;
            } else {
                channel.sendMessage(member.getEffectiveName() + ", you must be connected to a voice channel to let this bot connect.").submit();
            }
        } else
            channel.sendMessage(member.getEffectiveName() + ", either wait until the queue is empty or disconnect me from the current voice channel using /leave.").submit();
        return CommandResult.FAILURE;
    }
}
