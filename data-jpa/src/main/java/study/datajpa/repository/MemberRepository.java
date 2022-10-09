package study.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUsername")
        //없애도 잘 동작함
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //Dto 로 조회하기
    //new 로 만든다음에 생성자로 매칭을 해줘야 합니다. 마치 객체를 생성해서 반환해주는것 처럼.
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(
        @Param("names") Collection<String> names); //Collection으로 두고 좀더 받을 범주를 열어준다.

    //반환 타입을 유연하게 활용가능
    List<Member> findListByUsername(String username); // 컬렉션으로 받기

    Member findMemberByUsername(String username); //단건으로 받기

    Optional<Member> findOptionalByUsername(String username); //단건인데 타입을 optional 로 받기

    //    Slice<Member> findByAge(int age, Pageable pageable);
    //반환 값을 List로 받아도 됨
//    List<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    // 카운트 쿼리 분리
    // @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    // Page<Member> findByAge(int age, Pageable pageable);


    //Modifying이 있어야 JPA .executeUpdate(); 를 실행함. 이게 없으면 result list 나 single list를 호출함
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    //Member와 연관된 team을 한방에 다 끌고 오자고
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //쿼리를 커스텀하게 짰는데 페치조인까지 하고싶은 경우
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 명으로 쿼리생성도 가능
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String Username);
}
