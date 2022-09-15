package study.datajpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    protected Member() {
        //entity는 parameter 없이 default 생성자가 있어야되고
        //protected로 열어 둬야함
        //프록시 가술 사용시 jpa 구현체들이 객체를 강제로 만들어야 하는데
        //그때 객체 생성시 막힘 표준 스펙이 적혀 있음
    }

    public Member(String username) {
        this.username = username;
    }
}
