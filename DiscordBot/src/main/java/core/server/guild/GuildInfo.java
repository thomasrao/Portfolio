package core.server.guild;

import commands.CommandManager;
import core.server.MemberManager;
import core.server.channel.MessageChannelInfo;
import core.server.channel.VoiceChannelInfo;
import core.server.music.MusicManager;
import net.dv8tion.jda.core.entities.*;
import tools.logging.Log;

import java.io.*;
import java.util.*;

/**
 * @author Tom
 */
public class GuildInfo {
    private final static boolean DEFAULT_RPG_VALUE = true;

    private final static String GUILD_FOLDER_PATH = "data/guilds";
    private final static String GUILD_FILE_NAME = "guild.txt";
    private final static String MEMBERS_FILE_NAME = "members.txt";
    //private final static String MEMBER_DATA_REGEX_VALIDATION = "^[\\d]+,[\\d]+$";

    private Guild guild;
    private Map<Long, MessageChannelInfo> messageChannels;
    private Map<Long, VoiceChannelInfo> voiceChannels;
    private Map<Long, MemberManager> members;
    private MusicManager musicManager;

    private String prefix;
    private boolean rpg;


    public GuildInfo(Guild guild, String prefix, boolean rpg) {
        this.guild = guild;
        setPrefix(prefix);
        voiceChannels = new HashMap<>();
        messageChannels = new HashMap<>();
        members = new HashMap<>();
        this.rpg = rpg;
    }

    public GuildInfo(Guild guild) {
        this(guild, CommandManager.DEFAULT_COMMAND_PREFIX, DEFAULT_RPG_VALUE);
    }


    public void addChannel(Channel channel) {
        switch (channel.getType()) {
            case TEXT:
                messageChannels.put(channel.getIdLong(), new MessageChannelInfo((TextChannel) channel));
                break;
            case VOICE:
                voiceChannels.put(channel.getIdLong(), new VoiceChannelInfo((VoiceChannel) channel));
                break;
            default:
                Log.getInstance().warn("Found a " + channel.getType().name() + " channel type in a guild.", "GuildManager");
                break;
        }
    }

    public void addMember(Member member, byte level) {
        members.put(member.getUser().getIdLong(), new MemberManager(member, level));
    }

    public void createMusicManager(MessageChannel channel) {
        musicManager = new MusicManager(guild, channel);
    }

    public Guild getGuild() {
        return guild;
    }

    public MessageChannelInfo getTextChannel(long channelId) {
        return messageChannels.get(channelId);
    }

    public Collection<MessageChannelInfo> getMessageChannels() {
        return Collections.unmodifiableCollection(messageChannels.values());
    }

    public VoiceChannelInfo getVoiceChannel(long channelId) {
        return voiceChannels.get(channelId);
    }

    public Collection<VoiceChannelInfo> getVoiceChannels() {
        return Collections.unmodifiableCollection(voiceChannels.values());
    }

    public long getId() {
        return guild.getIdLong();
    }

    public MemberManager getMemberDetail(long memberId) {
        return members.get(memberId);
    }

    public Map<Long, MemberManager> getMemberDetails() {
        return Collections.unmodifiableMap(members);
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public String getName() {
        return guild.getName();
    }

    public String getPrefix() {
        return prefix;
    }

    public void load(Guild guild) {
        // Loading guild details
        this.guild = guild;
        setPrefix(CommandManager.DEFAULT_COMMAND_PREFIX);
        File guildFile = new File(GUILD_FOLDER_PATH + "/" + guild.getIdLong() + "/" + GUILD_FILE_NAME);
        if (guildFile.exists()) {
            try (Scanner scanner = new Scanner(new FileReader(guildFile))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    int separatorIndex = line.indexOf('=');
                    if (separatorIndex == -1)
                        continue;

                    String key = line.substring(0, separatorIndex);
                    String value = line.substring(separatorIndex + 1);

                    switch (key) {
                        case "prefix":
                            setPrefix(value);
                            break;
                        default:
                            // Ignore these; they will disappear on next save.
                            Log.getInstance().warn("New property found: " + key + ".", "GuildManager");
                            break;
                    }
                }
            } catch (IOException ioe) {
                Log.getInstance().error(ioe, "GuildManager");
            }
        }


        // Loading channel details
        for (Channel channel : guild.getTextChannels())
            addChannel(channel);

        for (Channel channel : guild.getVoiceChannels())
            addChannel(channel);


        // Loading member details
        Map<Long, Member> memberMap = new HashMap<>();
        for (Member member : guild.getMembers())
            if (!member.getUser().isBot())
                memberMap.put(member.getUser().getIdLong(), member);

        members.clear();

        File membersFile = new File(GUILD_FOLDER_PATH + "/" + guild.getIdLong() + "/" + MEMBERS_FILE_NAME);
        if (membersFile.exists()) {
            try (Scanner scanner = new Scanner(new FileReader(membersFile))) {
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split(",");
                    long memberId = Long.parseLong(parts[0]);

                    if (memberMap.containsKey(memberId)) {
                        byte level = Byte.parseByte(parts[1]);

                        addMember(memberMap.get(memberId), level);
                        memberMap.remove(memberId);
                    }
                }
            } catch (IOException ioe) {
                Log.getInstance().error(ioe, "GuildManager");
            }
        }

        if (memberMap.size() > 0) {
            // Add new members
            for (Member member : memberMap.values())
                addMember(member, CommandManager.DEFAULT_COMMAND_LEVEL);
        }
    }

    public void removeMember(long memberId) {
        if (members.containsKey(memberId)) {
            members.remove(memberId);
        }
    }

    public void removeTextChannel(long channelId) {
        if (messageChannels.containsKey(channelId)) {
            messageChannels.remove(channelId);
        }
    }

    public void removeVoiceChannel(long channelId) {
        if (voiceChannels.containsKey(channelId)) {
            voiceChannels.remove(channelId);
        }
    }

    public void save() {
        // Write to guild.txt
        File guild = new File(GUILD_FOLDER_PATH + "/" + getId() + "/" + GUILD_FILE_NAME);
        if (!guild.getParentFile().exists())
            guild.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(guild))) {
            writer.write(toString());
        } catch (IOException ioe) {
            Log.getInstance().error(ioe, "GuildManager");
        }

        // Write to members.txt
        StringBuilder sb = new StringBuilder();
        for (MemberManager member : members.values())
            sb.append(member.getId()).append(',').append(member.getCommandLevel()).append('\n');
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        File members = new File(GUILD_FOLDER_PATH + "/" + getId() + "/" + MEMBERS_FILE_NAME);
        if (!members.getParentFile().exists())
            members.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(members))) {
            writer.write(sb.toString());
        } catch (IOException ioe) {
            Log.getInstance().error(ioe, "GuildManager");
            return;
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix.toLowerCase();
    }

    @Override
    public String toString() {
        return "prefix=" + getPrefix();
    }
}
