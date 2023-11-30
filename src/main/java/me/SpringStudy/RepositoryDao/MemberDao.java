package me.SpringStudy.RepositoryDao;

import me.SpringStudy.Entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberDao extends JpaRepository<Member,Long> {


    // 중복된 아이디가 있는지 확인하는 메서드
    boolean existsByUserId(String userId);

    // 특정 조건에 따른 회원 검색 메서드
    Optional<Member> findByUserId(String userId);
}
