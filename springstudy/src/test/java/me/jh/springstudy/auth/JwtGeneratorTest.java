package me.jh.springstudy.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Encoders;
import me.jh.springstudy.MySpringBootApplication;
import me.jh.springstudy.dto.JWToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = MySpringBootApplication.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(locations = "classpath:application.yml")
public class JwtGeneratorTest {


	@InjectMocks
	private JwtGenerator jwtGenerator;

	@Mock
	private Authentication authentication;


	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this); // Mock 객체 초기화
		String testKey = "kimiNoNawabariwaOreGairudakaraWatashiNoNawabarida";
		String encodedKey = Encoders.BASE64.encode(testKey.getBytes());
		jwtGenerator = new JwtGenerator(encodedKey);
	}


	@Test
	public void testGenerateToken_happy() {
		// Mock UserDetails 객체 생성
		UserDetails userDetails = new User("testuser", "test", Collections.emptyList());

		when(authentication.getPrincipal()).thenReturn(userDetails);

		JWToken jwtToken = jwtGenerator.generateToken(authentication);

		assertNotNull(jwtToken);
		assertNotNull(jwtToken.getAccessToken());
		assertNotNull(jwtToken.getRefreshToken());
		verify(authentication, times(2)).getPrincipal();
	}

	@Test
	public void testGenerateToken_InvalidAuthentication() {

		// JWT 토큰 생성
		try {
			jwtGenerator.generateToken(null);
			fail("예외 발생 실패");
		} catch (JwtException e) {
			assertEquals("Invalid authentication details", e.getMessage());
		}
		// 추가적인 검증이 필요하다면 여기서 수행
	}

	@Test
	public void testGenerateToken_InvaildAuthenticationPrincipal() {

		// Mock Authentication 객체 설정
		when(authentication.getPrincipal()).thenReturn("invalidPrincipal");

		// JWT 토큰 생성
		try {
			JWToken jwtToken = jwtGenerator.generateToken(authentication);
			fail("예외 발생 실패");
		} catch (JwtException e) {
			assertEquals("Invalid authentication details", e.getMessage());
		}
		verify(authentication, times(1)).getPrincipal();
	}


	@Test
	public void testGenerateToken_InvalidSecretKeyFormat() {

		UserDetails userDetails = new User("testuser", "test", Collections.emptyList());


		when(authentication.getPrincipal()).thenReturn(userDetails);


		jwtGenerator = new JwtGenerator("invalidBase64SecretKey");

		// 예상되는 예외인 JwtException이 발생하는지 확인
		assertThrows(JwtException.class, () -> {
			jwtGenerator.generateToken(authentication);
		});
	}
}