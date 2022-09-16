package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

//@Repository 애너테이션은 생략도가능 jpa interface 보고 체크함
public interface TeamRepository extends JpaRepository<Team, Long> {

}
