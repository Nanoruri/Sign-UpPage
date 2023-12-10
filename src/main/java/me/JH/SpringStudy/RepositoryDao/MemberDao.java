package me.JH.SpringStudy.RepositoryDao;

import me.JH.SpringStudy.Entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDao extends JpaRepository<User,Long> {


    // 특정 조건에 따른 회원 검색 메서드
    User findByUserId(String userId);// todo : optional로 쓸 필요 없이 member로 하도록

    //중복확인 검사 메서드/JPA는 메서드 이름을 기준으로  분석하여 쿼리를 자동으로 생성함. 그래서 엔티티 테이블과 매핑할 수 있는
    //엔티티 클래스가 필요한거라고 추즉 됨
    boolean existsByUserId(String userId);

}
