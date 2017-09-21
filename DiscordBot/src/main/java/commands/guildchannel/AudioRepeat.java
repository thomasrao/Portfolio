package commands.guildchannel;

import commands.CommandResult;
import core.server.ServerManager;
import core.server.music.MusicManager;
import core.server.music.TrackScheduler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * The bot will repeat its list of songs over and over
 *  when toggled on.
 *
 * @author Tom
 */
public class AudioRepeat extends GuildChannelCommand {
    protected AudioRepeat(){
        super("repeat", "", "Toggles the repeat mode", GuildChannelCommandSet.MUSIC, (byte) 0);
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

        // Toggle the bot's repeat mode.
        if (mm != null && mm.isConnected()) {
            mm.setFlag(TrackScheduler.PlayFlag.REPEAT, !mm.hasFlag(TrackScheduler.PlayFlag.REPEAT));
            channel.sendMessage("The repeat mode has been toggled " + (mm.hasFlag(TrackScheduler.PlayFlag.REPEAT) ? "on" : "off") + ".").submit();
            return CommandResult.SUCCESS;
        }
        return CommandResult.FAILURE;
    }
}
