package core.server;

import net.dv8tion.jda.core.entities.Member;

/**
 * @author Tom
 */
public class MemberManager {
    private Member member;
    private byte commandLevel;


    public MemberManager(Member member, byte commandLevel) {
        this.member = member;
        this.commandLevel = commandLevel;
    }


    public long getId() {
        return member.getUser().getIdLong();
    }

    public byte getCommandLevel() {
        return commandLevel;
    }

    public Member getMember() {
        return member;
    }

    public String getNickname() {
        return member.getNickname();
    }

    public String getUsername() {
        return member.getEffectiveName();
    }

    public void setCommandLevel(byte level) {
        this.commandLevel = level;
    }
}
