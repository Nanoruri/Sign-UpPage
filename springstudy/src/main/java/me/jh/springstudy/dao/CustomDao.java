package me.jh.springstudy.dao;

import me.jh.springstudy.entitiy.User;

import java.util.Optional;

/**
 * 사용자 정보를 찾는 DAO 인터페이스. findByProperties로 검색 조건을 받아 사용자 정보를 찾는다.
 */
public interface CustomDao {//todo : 인터페이스명 변경하기
	Optional<User> findByProperties(String userId, String name, String phoneNum);
}
