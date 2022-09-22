package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember(){
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("member1");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member ("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result =  memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloLBy(){
        List<Member> helloBy = memberRepository.findTop3HelloBy();

    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member ("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member ("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member ("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            assertThat(s).isEqualTo("AAA");
        }
    }

    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member m1 = new Member("AAA", 10, team);
        memberRepository. save(m1);

        MemberDto beforeDto = new MemberDto(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto afterDto : memberDto) {
            assertThat(afterDto.getUsername()).isEqualTo(beforeDto.getUsername());
            assertThat(afterDto.getId()).isEqualTo(beforeDto.getId());
            assertThat(afterDto.getTeamName()).isEqualTo(beforeDto.getTeamName());
        }

    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member ("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BB B"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member ("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        assertThat(aaa.get(0).getUsername()).isEqualTo(m1.getUsername());
        Member bbb = memberRepository.findMemberByUsername("BBB");
        assertThat(bbb.getUsername()).isEqualTo(m2.getUsername());
        Optional<Member> ccc = memberRepository.findOptionalByUsername("AAA");
        assertThat(ccc.isPresent()).isEqualTo(true);
        assertThat(ccc.get().getUsername()).isEqualTo(m1.getUsername());

        //동작방식에 빈컬렉션을 제공하고 null을 반환하지 않기 때문에 null check를 할 필요가 없다.
        List<Member> dfkdfkdjfdkjfkd = memberRepository.findListByUsername("dfkdfkdjfdkjfkd");
        System.out.println("dfkdfkdjfdkjfkd.size() = " + dfkdfkdjfdkjfkd.size());
        //단일 반환 값의 경우 Null 반환함
        //Member nullMember = memberRepository.findMemberByUsername("dfkdfkdjfdkjfkd");

    }

    @Test
    public void paging(){

        //given - 이런 데이터 있을떄
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age = 10;
        //0 페이지에서 3개 가져옴 소팅은 desc
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

        //when - 이렇게 하면
        //반환타입이 page면 total count를 이미 쿼리로 보냄 반환타입에 따라서 total count 여부 정할 수 있음
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> map = page.map(
            member -> new MemberDto(member.getId(), member.getUsername(), null));

        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

//        System.out.println("totalElements = " + totalElements);

        //페이지 계산 공식
        //totalPage = totalCount / size ...
        //마지막 페이지 ...
        //최초 페에지...
        //Spring Data Jpa에 이미 제공

        //then
        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }


}