package me.jh.springstudy.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import me.jh.springstudy.dto.JWToken;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtGenerator {

	private final String SECRET_KEY;

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtGenerator.class);
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30; // 30분
	private static final long PASSWORD_CHANGE_EXPIRE_TIME = 1000L * 60 * 5; //5분
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7; // 7일
	private static final String GRANT_TYPE = "Bearer";

	@Autowired
	public JwtGenerator(@Value("${jwt.secret}") String secretKey) {
		this.SECRET_KEY = secretKey;
	}


	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public JWToken generateToken(Authentication authentication) {

		if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
			throw new JwtException("잘못된 인증 정보입니다.");
		}

		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String userId = userDetails.getUsername();
			long now = (new Date()).getTime();

			// Access Token 생성
			Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
			Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

			String accessToken = Jwts.builder()
					.subject(userId)
					.claim("role", userDetails.getAuthorities())
					.expiration(accessTokenExpiresIn)
					.signWith(getSigningKey())
					.compact();

			// Refresh Token 생성
			String refreshToken = Jwts.builder()
					.expiration(refreshTokenExpiresIn)
					.signWith(getSigningKey())
					.compact();

			return JWToken.builder()
					.grantType(GRANT_TYPE)
					.accessToken(accessToken)
					.refreshToken(refreshToken)
					.build();

		} catch (Exception e) {
			throw new JwtException("토큰 생성에 실패하였습니다.", e);
		}
	}


	public JWToken generateTokenForPassword(String userId) {

		if (userId == null || userId.isEmpty()) {
			throw new JwtException("잘못된 인증 정보입니다.");
		}

		long now = (new Date()).getTime();

		try {
			Date passwordTokenExpiresIn = new Date(now + PASSWORD_CHANGE_EXPIRE_TIME);

			String passwordToken = Jwts.builder()
					.subject(userId)
					.expiration(passwordTokenExpiresIn)
					.claim("wantChangePassword", "PasswordUser")
					.signWith(getSigningKey())
					.compact();

			return JWToken.builder()
					.grantType(GRANT_TYPE)
					.accessToken(passwordToken)
					.build();
		} catch (Exception e) {
			throw new JwtException("토큰 생성에 실패하였습니다.", e);
		}
	}
}


