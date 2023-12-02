package me.SpringStudy.RepositoryDao;

import me.SpringStudy.Entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberDao extends JpaRepository<Member,Long> {


    // 특정 조건에 따른 회원 검색 메서드
    Optional<Member> findByUserId(String userId);

    //중복확인 검사 메서드/JPA는 메서드 이름을 기준으로  분석하여 쿼리를 자동으로 생성함. 그래서 엔티티 테이블과 매핑할 수 있는
    //엔티티 클래스가 필요한거라고 추즉 됨
    boolean existsByUserId(String userId);
}
