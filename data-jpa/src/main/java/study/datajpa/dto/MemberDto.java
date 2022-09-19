package study.datajpa.dto;

import java.util.Objects;
import lombok.Getter;
import study.datajpa.entity.Member;

@Getter
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.teamName = member.getTeam().getName();
    }

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }
    

}
