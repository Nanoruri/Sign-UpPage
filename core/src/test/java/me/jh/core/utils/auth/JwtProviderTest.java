package me.jh.core.utils.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import me.jh.core.dto.JWToken;
import me.jh.core.utils.auth.JwtGenerator;
import me.jh.core.utils.auth.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class JwtProviderTest {

	@Mock
	JwtGenerator jwtGenerator;

	@Mock
	Authentication authentication;

	@Mock
	JWToken token;

	@InjectMocks
	private JwtProvider jwtProvider;

	private SecretKey key;

	private String validToken;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		byte[] keyBytes = new byte[64]; // 512 bits for HS512
		new SecureRandom().nextBytes(keyBytes);
		key = Keys.hmacShaKeyFor(keyBytes);
		String encodedKey = Encoders.BASE64.encode(key.getEncoded());

		jwtGenerator = new JwtGenerator(encodedKey);
		jwtProvider = new JwtProvider(encodedKey);

	}

	@Test
	public void testGetAuthentication_ValidToken() {

		UserDetails userDetails = new User("testUser", "test1234", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

		when(authentication.getPrincipal()).thenReturn(userDetails);
		token = jwtGenerator.generateToken(authentication);
		validToken = token.getAccessToken();

		Authentication result = jwtProvider.getAuthentication(validToken);

		assertNotNull(result);
		assertEquals("testUser", result.getName());
		assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
	}

	@Test
	public void testGetAuthentication_WithNotRoleClaim() {

		String token = Jwts.builder()//role클레임이 없는 토큰
				.subject("testUser")
				.signWith(key)
				.compact();

		assertThrows(RuntimeException.class, () -> jwtProvider.getAuthentication(token));
	}

	@Test
	public void testGetAuthentication_ExpiredJwt() {
		String token = Jwts.builder()
				.subject("testUser")
				.claim("role", "User")
				.expiration(new Date(System.currentTimeMillis() - 1000))
				.signWith(key)
				.compact();

		Authentication reuslt = jwtProvider.getAuthentication(token);
	}


	@Test
	public void testValidateToken_ValidToken() {
		UserDetails userDetails = new User("testUser", "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

		when(authentication.getPrincipal()).thenReturn(userDetails);
		token = jwtGenerator.generateToken(authentication);
		validToken = token.getAccessToken();

		Authentication result = jwtProvider.getAuthentication(validToken);

		// When
		boolean isValid = jwtProvider.validateToken(validToken);

		// Then
		assertTrue(isValid);
	}

	@Test
	public void testValidateToken_InvalidToken() {
		String invalidToken = "invalid.token";

		try {
			// When
			jwtProvider.validateToken(invalidToken);
			fail("JwtException이 발생해야 합니다.");
		} catch (JwtException e) {
			// Then
			assertEquals("유효하지 않은 토큰입니다.", e.getMessage());
		}
	}


	@Test
	public void testValidateToken_ExpiredToken() {
		// Arrange
		String generateExpiredToken = Jwts.builder()
				.subject("user")
				.claim("role", "user")
				.expiration(new Date(System.currentTimeMillis() - 1000)) // expired one second ago
				.signWith(key)
				.compact();

		try {
			// When
			jwtProvider.validateToken(generateExpiredToken);
			fail("ExpiredTokenException이 발생해야 합니다.");
		} catch (JwtException e) {
			// Then
			assertEquals("만료된 토큰입니다.", e.getMessage());
		}

	}


	@Test
	public void testValidateToken_UnsupportedToken_ReturnsFalse() {

		String unsupportedToken = Jwts.builder()
				.content("Unsupported Token")
				.signWith(key) // 지원하지 않는 서명 알고리즘 사용
				.compact();

		try {
			// When
			jwtProvider.validateToken(unsupportedToken);
			fail("UnsupportedJwtException이 발생해야 합니다.");
		} catch (JwtException e) {
			// Then
			assertEquals("지원되지 않는 토큰입니다.", e.getMessage());
		}


	}


	@Test
	public void testValidateToken_IllegalArgumentException_ReturnsFalse() {

		try {
			jwtProvider.validateToken(null);
		} catch (JwtException e) {
			assertEquals("토큰이 비어있습니다.", e.getMessage());
		}
	}


	@Test
	public void testGetUserIdFromToken() {
		// given
		String expectedUserId = "user123";
		String token = Jwts.builder()
				.subject(expectedUserId)
				.signWith(key)
				.compact();
		// when
		String actualUserId = jwtProvider.getUserIdFromToken(token);

		// then
		assertEquals(expectedUserId, actualUserId, "토큰에서 추출한 사용자 ID가 예상과 다릅니다.");
	}

}
