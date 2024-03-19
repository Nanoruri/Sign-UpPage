package me.jh.springStudy.repositoryDao;

import me.jh.springStudy.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String>, CustomDao {//이러면 JPA 쓰는 의미가 있는가..
//todo : dao네이밍 다시 생각해보기 UserDao..?

	//JPA는 메서드 이름을 기준으로  분석하여 쿼리를 자동으로 생성함. 그래서 엔티티 테이블과 매핑할 수 있는
	//엔티티 클래스가 필요한거라고 추즉 됨

	//
	User findByNameAndEmail(String name, String email);

	//찾기 서비스
//    User findByUserIdAndNameAndEmail(String userId, String name, String email);

//    Optional<User> findByProperties(String userId, String name, String email);
// todo : 이거를 추상클래스에서 구현()...or 인터페이스 다중 확장 기능 이용...

}
