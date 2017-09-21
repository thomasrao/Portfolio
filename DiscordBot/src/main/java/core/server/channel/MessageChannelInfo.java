package core.server.channel;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;

/**
 * Additional details kept regarding each message channel.
 *
 * @author Tom
 */
public class MessageChannelInfo extends ChannelInfo<TextChannel> {
    public MessageChannelInfo(TextChannel channel) {
        super(channel);
    }

    public RestAction<Message> sendMessage(String message) {
        return channel.sendMessage(message);
    }
}
