package me.jh.springstudy.dao;

import me.jh.springstudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 사용자 정보를 CRUD하는 DAO 인터페이스.
 * JpaRepository를 상속받아 사용자 정보를 저장하고 조회하는 메서드를 제공한다.
 * Jpa와 CustomDao를 상속받는다.
 */
@Repository
public interface UserDao extends JpaRepository<User, String>, UserPropertiesDao {

    User findByNameAndPhoneNum(String name, String phoneNum);

    boolean existsByEmail(String email);

}
