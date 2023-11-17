package me.JH.SpringStudy.RepositoryDao;

import me.JH.SpringStudy.Entitiy.User;

import java.util.Optional;

public interface CustomDao {
	Optional<User> findByProperties(String userId, String name, String email);
}
