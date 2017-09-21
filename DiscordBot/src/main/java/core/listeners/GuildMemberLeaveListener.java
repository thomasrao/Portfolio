package core.listeners;

import core.server.ServerManager;
import core.server.channel.ChannelInfo;
import core.server.guild.GuildInfo;
import games.Game;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for members leabing guilds in
 *  which the bot has been invited to.
 *
 * @author Tom
 */
public class GuildMemberLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (!event.getMember().getUser().isBot()) {
            long guildId = event.getGuild().getIdLong();
            long memberId = event.getMember().getUser().getIdLong();
            ServerManager.getInstance().get(guildId).removeMember(memberId);

            GuildInfo guild = ServerManager.getInstance().get(event.getGuild().getIdLong());
            for (ChannelInfo channel : guild.getMessageChannels()) {
                Game game = channel.getGameManager().getGame();
                if (game != null)
                    game.onGuildMemberLeave(event.getMember());
            }
        }
    }
}
