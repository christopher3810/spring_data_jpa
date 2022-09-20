package study.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUsername") //없애도 잘 동작함
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
    List<Member> findByNames(@Param("names") Collection<String> names); //Collection으로 두고 좀더 받을 범주를 열어준다.

    //반환 타입을 유연하게 활용가능
    List<Member> findListByUsername(String username); // 컬렉션으로 받기
    Member findMemberByUsername(String username); //단건으로 받기
    Optional<Member> findOptionalByUsername(String username); //단건인데 타입을 optional 로 받기
}
