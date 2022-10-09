package study.datajpa.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;


@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

//    public MemberRepositoryImpl(EntityManager em) {
//        this.em = em; //RequiredArgsConstructor 사용하면 생략 가능
//    }

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
