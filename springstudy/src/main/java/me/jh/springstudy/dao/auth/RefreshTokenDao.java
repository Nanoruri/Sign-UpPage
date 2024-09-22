package me.jh.springstudy.dao.auth;

import me.jh.springstudy.entity.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken, String> {

}
