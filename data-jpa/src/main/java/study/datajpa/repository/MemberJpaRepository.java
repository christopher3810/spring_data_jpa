package study.datajpa.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

//Bean 등록함 애너테이션 달아줘야
@Repository
public class MemberJpaRepository {

    @PersistenceContext //스프링 컨테이너가 Jpa에 영속성 컨테이너 엔티티 매니저를 가져다줌
    private EntityManager em;

    public Member save(Member member) { //entity 변경 감지 dirty check update 필요없
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    //전체 조회 or 특정 조건으로 filtering을 할때는 jpql을 활용해야 함.
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member); //null 일수도 아닐수도 있드아
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
        return em.createQuery(
                "select m from Member m where m.username = :username and m.age > :age")
            .setParameter("username", username)
            .setParameter("age",age)
            .getResultList();
    }
}
