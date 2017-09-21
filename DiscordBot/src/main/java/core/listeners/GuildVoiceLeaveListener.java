package core.listeners;

import core.server.ServerManager;
import core.server.guild.GuildInfo;
import core.server.music.MusicManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

/**
 * The event handler for leaving voice chat.
 *
 * @author Tom
 */
public class GuildVoiceLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        GuildInfo g = ServerManager.getInstance().get(event.getGuild().getIdLong());
        MusicManager mm = g.getMusicManager();

        // Disconnect the bot if the users left, excluding bots.
        if (mm != null && mm.isConnected()) {
            List<Member> members = event.getChannelLeft().getMembers();

            for (Member member : members) {
                if (!member.getUser().isBot())
                    return;
            }

            mm.clear();
            mm.disconnect();
        }
    }
}
