package me.jh.springStudy.repositoryDao;

import me.jh.springStudy.entitiy.User;

import java.util.Optional;

public interface CustomDao {
	Optional<User> findByProperties(String userId, String name, String email);
}
