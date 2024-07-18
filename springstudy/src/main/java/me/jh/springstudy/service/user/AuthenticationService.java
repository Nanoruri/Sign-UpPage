package me.jh.springstudy.service.user;

import me.jh.springstudy.auth.JwtGenerator;
import me.jh.springstudy.dto.JWToken;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final JwtGenerator jwtGenerator;

	@Autowired
	public AuthenticationService(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
		this.authenticationManager = authenticationManager;
		this.jwtGenerator = jwtGenerator;
	}


	public JWToken authenticateAndGenerateToken(String userId, String password) {

		if (userId == null || password == null || userId.isEmpty() || password.isEmpty()){
			throw new UserException(UserErrorType.USER_NOT_FOUND);
		}

		try{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userId, password));
		return jwtGenerator.generateToken(authentication);
		} catch (Exception e) {
			throw new RuntimeException("로그인 실패", e);
		}


	}
}
