package core.server;

import core.server.guild.GuildInfo;
import net.dv8tion.jda.core.entities.Guild;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tom
 */
public class ServerManager {
    private final static ServerManager instance;

    private HashMap<Long, GuildInfo> guilds;


    static {
        instance = new ServerManager();
    }

    protected ServerManager() {
        this.guilds = new HashMap<>();
    }


    public void add(Guild guild) {
        GuildInfo details = new GuildInfo(null);
        details.load(guild);
        guilds.put(guild.getIdLong(), details);
    }

    public GuildInfo get(long guildId) {
        return guilds.get(guildId);
    }

    public Collection<GuildInfo> getGuilds() {
        return Collections.unmodifiableCollection(guilds.values());
    }

    public boolean has(long guildId) {
        return guilds.containsKey(guildId);
    }

    public void load(List<Guild> guildList) {
        for (Guild guild : guildList) {
            GuildInfo details = new GuildInfo(null);
            details.load(guild);
            guilds.put(guild.getIdLong(), details);
        }
    }

    public static ServerManager getInstance() {
        return instance;
    }

    public GuildInfo remove(long guildId) {
        if (guilds.containsKey(guildId)) {
            GuildInfo temp = get(guildId);
            guilds.remove(guildId);

            return temp;
        }
        return null;
    }

    public void save() {
        for (GuildInfo guild : guilds.values())
            guild.save();
    }
}
