package study.datajpa.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

//Bean 등록함 애너테이션 달아줘야
@Repository
public class MemberJpaRepository {

    @PersistenceContext //스프링 컨테이너가 Jpa에 영속성 컨테이너 엔티티 매니저를 가져다줌
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
