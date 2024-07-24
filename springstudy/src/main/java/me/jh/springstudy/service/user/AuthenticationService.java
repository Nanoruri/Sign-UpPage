package me.jh.springstudy.service.user;

import me.jh.springstudy.auth.JwtGenerator;
import me.jh.springstudy.dto.JWToken;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final JwtGenerator jwtGenerator;
	private final UserDetailsService userDetailsService;

	@Autowired
	public AuthenticationService(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator, UserDetailsService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.jwtGenerator = jwtGenerator;
		this.userDetailsService = userDetailsService;
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


	public JWToken authenticateAndGenerateToken(String userId) {
    if (userId == null || userId.isEmpty()) {
        throw new UserException(UserErrorType.USER_NOT_FOUND);
    }

    try {
        // Assuming the existence of a method to load UserDetails by userId
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        return jwtGenerator.generateToken(authentication);
    } catch (Exception e) {
        throw new RuntimeException("인증 실패", e);
    }
}
}
