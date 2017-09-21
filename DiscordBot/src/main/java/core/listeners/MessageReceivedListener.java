package core.listeners;

import commands.CommandManager;
import commands.guildchannel.GuildChannelCommandManager;
import commands.privatechannel.PrivateChannelCommandManager;
import core.server.ServerManager;
import core.server.channel.ChannelInfo;
import core.server.guild.GuildInfo;
import games.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import tools.logging.Log;

/**
 * The event handler for receiving messages.
 *
 * @author Tom
 */
public class MessageReceivedListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        switch (event.getChannelType()) {
            case TEXT:
                // Ignore bot messages.
                if (event.getMember().getUser().isBot()) {
                    return;
                }

                // Add the guild channel to our records if it does not exists.
                if (!ServerManager.getInstance().has(event.getGuild().getIdLong()))
                    ServerManager.getInstance().add(event.getGuild());

                GuildInfo gd = ServerManager.getInstance().get(event.getGuild().getIdLong());

                // Add the member if it does not exist.
                if (gd.getMemberDetail(event.getAuthor().getIdLong()) == null)
                    gd.addMember(event.getMember(), CommandManager.DEFAULT_COMMAND_LEVEL);

                // Run the command
                if (GuildChannelCommandManager.getInstance().run(event.getGuild(), event.getTextChannel(), event.getMember(), event.getMessage(), gd.getPrefix()))
                    System.out.println("Chat command ran in guild channel #" + event.getGuild().getIdLong() + ": " + event.getMessage().getRawContent());

                GuildInfo guild = ServerManager.getInstance().get(event.getGuild().getIdLong());
                ChannelInfo channel = guild.getTextChannel(event.getChannel().getIdLong());
                Game game = channel.getGameManager().getGame();

                if (game != null)
                    game.onMessageReceived(event.getMessage());

                String m = event.getMessage().getRawContent().toLowerCase();
                if (m.contains("daddy"))
                    event.getChannel().sendMessage(":weary: :ok_hand:").submit();
                break;
            case PRIVATE:
                if (PrivateChannelCommandManager.getInstance().run(event.getPrivateChannel(), event.getMember(), event.getMessage(), "/"))
                    System.out.println("Chat command ran in private channel: " + event.getMessage().getRawContent());
                break;
            default:
                Log.getInstance().warn("Received message in " + event.getChannelType().name() + " channel.", "MessageReceivedListener");
                break;
        }
    }
}
