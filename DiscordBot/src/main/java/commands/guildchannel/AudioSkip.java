package commands.guildchannel;

import commands.CommandResult;
import core.server.ServerManager;
import core.server.music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Vote for a song skip. If the skip requirements are
 *  met, then the next request will be played.
 *
 * @author Jay
 */
public class AudioSkip extends GuildChannelCommand {
    private final static String FORCE_SKIP = "force".toLowerCase();


    protected AudioSkip(){
        super("skip", "", "Skips the song currently being played", GuildChannelCommandSet.MUSIC, (byte) 0);
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

        if (mm != null && mm.isConnected()) {
            // Check if this member has already voted for a skip.
            long memberId = member.getUser().getIdLong();
            if (mm.hasSkipped(memberId)) {
                channel.sendMessage(member.getEffectiveName() + ", you have already given your skip vote for this track.").submit();
                return CommandResult.FAILURE;
            }

            // Count how many members are in the voice channel,
            // excluding bots.
            int userCount = 0;
            for (Member m : guild.getAudioManager().getConnectedChannel().getMembers()) {
                if (!m.getUser().isBot())
                    userCount++;
            }

            // Check if the member wants to force a skip.
            String[] parts = message.getRawContent().split(" ");
            boolean forced = parts.length > 1 && FORCE_SKIP.startsWith(parts[1].toLowerCase());

            // Check if half of the members connected
            // wants to skip. If so or if forced, skip it.
            if (mm.skip(memberId, (int) Math.ceil(userCount / 2.0), forced)) {
                if (forced)
                    channel.sendMessage("By veto, the track is getting skipped.").submit();
                else
                    channel.sendMessage("The majority have decided to skip and so it is.").submit();
                return CommandResult.SUCCESS;
            } else
                channel.sendMessage(member.getAsMention() + " votes for skipping this track.").submit();
        }
        return CommandResult.FAILURE;
    }
}
