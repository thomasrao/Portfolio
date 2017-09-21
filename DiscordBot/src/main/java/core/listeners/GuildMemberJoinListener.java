package core.listeners;

import commands.CommandManager;
import core.server.ServerManager;
import core.server.channel.ChannelInfo;
import core.server.guild.GuildInfo;
import games.Game;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for new members joining
 *  a guild in which the bot has been added to.
 *
 * @author Tom
 */
public class GuildMemberJoinListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (!event.getMember().getUser().isBot()) {
            long guildId = event.getGuild().getIdLong();
            ServerManager.getInstance().get(guildId).addMember(event.getMember(), CommandManager.DEFAULT_COMMAND_LEVEL);

            GuildInfo guild = ServerManager.getInstance().get(event.getGuild().getIdLong());
            for (ChannelInfo channel : guild.getMessageChannels()) {
                Game game = channel.getGameManager().getGame();
                if (game != null)
                    game.onGuildMemberJoin(event.getMember());
            }
        }
    }
}
