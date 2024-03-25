package me.jh.springstudy.repositorydao;

import me.jh.springstudy.entitiy.User;

import java.util.Optional;

public interface CustomDao {
	Optional<User> findByProperties(String userId, String name, String phoneNum);
}
