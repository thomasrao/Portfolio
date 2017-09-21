package core.server.channel;

import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 * Additional details kept regarding each voice channel.
 *
 * @author Tom
 */
public class VoiceChannelInfo extends ChannelInfo<VoiceChannel> {
    public VoiceChannelInfo(VoiceChannel channel) {
        super(channel);
    }
}
