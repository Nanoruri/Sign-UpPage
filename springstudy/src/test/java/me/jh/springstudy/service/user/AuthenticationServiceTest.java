package me.jh.springstudy.service.user;

import me.jh.springstudy.auth.JwtGenerator;
import me.jh.springstudy.dto.JWToken;
import me.jh.springstudy.exception.user.UserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthenticationServiceTest {


	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserDetails UserDetails;

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private JwtGenerator jwtGenerator;

	@InjectMocks
	private AuthenticationService authenticationService;

	@Test
	public void testAuthentication_ValidCredentialsShouldGenerateToken() {
		String userId = "validUser";
		String password = "validPassword";
		Authentication authentication = new UsernamePasswordAuthenticationToken(userId, password);
		JWToken expectedToken = new JWToken("Bearer", "accessToken", "refreshToken");

		when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
		when(jwtGenerator.generateToken(authentication)).thenReturn(expectedToken);

		JWToken actualToken = authenticationService.authenticateAndGenerateToken(userId, password);

		assertNotNull(actualToken);
		assertEquals(expectedToken.getAccessToken(), actualToken.getAccessToken());
		assertEquals(expectedToken.getRefreshToken(), actualToken.getRefreshToken());
		verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
		verify(jwtGenerator, times(1)).generateToken(authentication);
	}

	@Test
	public void testAuthentication_InvalidCredentialsShouldThrowException() {
		String userId = "invalidUser";
		String password = "invalidPassword";

		when(authenticationManager.authenticate(any(Authentication.class)))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		assertThrows(RuntimeException.class, () -> authenticationService.authenticateAndGenerateToken(userId, password));
		verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
		verify(jwtGenerator, never()).generateToken(any(Authentication.class));

	}

	@Test
	public void testAuthentication_EmptyCredentialsShouldThrowException() {
		String userId = "";
		String password = "test1234";

		assertThrows(UserException.class, () -> authenticationService.authenticateAndGenerateToken(userId, password));
		verify(authenticationManager, never()).authenticate(any(Authentication.class));
	}

	@Test
	public void testAuthentication_NullCredentialsShouldThrowException() {
		String userId = null;
		String password = "test1234";

		assertThrows(UserException.class, () -> authenticationService.authenticateAndGenerateToken(userId, password));
		verify(authenticationManager, never()).authenticate(any(Authentication.class));
	}

	@Test
	public void testAuthentication_NullUserIdShouldThrowException() {
		String userId = "test1234";
		String password = null;

		assertThrows(UserException.class, () -> authenticationService.authenticateAndGenerateToken(userId, password));
		verify(authenticationManager, never()).authenticate(any(Authentication.class));
	}

	@Test
	public void testAuthentication_EmptyPasswordShouldThrowException() {
		String userId = "test1234";
		String password = "";

		assertThrows(UserException.class, () -> authenticationService.authenticateAndGenerateToken(userId, password));
		verify(authenticationManager, never()).authenticate(any(Authentication.class));
	}

	@Test
	public void testAuthentication2_ValidUserIdShouldGenerateToken() {
		String userId = "validUserId";
		JWToken expectedToken = new JWToken("Bearer", "accessToken", "refreshToken");

		UserDetails = User.builder()
				.username(userId)
				.password("password")
				.roles("USER")
				.build();

		when(userDetailsService.loadUserByUsername(userId)).thenReturn(UserDetails);
		when(jwtGenerator.generateToken(any(Authentication.class))).thenReturn(expectedToken);

		JWToken actualToken = authenticationService.authenticateAndGenerateToken(userId);

		assertNotNull(actualToken);
		assertEquals(expectedToken.getAccessToken(), actualToken.getAccessToken());
		assertEquals(expectedToken.getRefreshToken(), actualToken.getRefreshToken());
	}


	@Test
	public void testAuthentication2_InvalidUserIdShouldThrowException() {
		String userId = "invalidUserId";

		when(userDetailsService.loadUserByUsername(userId)).thenThrow(new UsernameNotFoundException("사용자가 없습니다!{}" + userId));

		assertThrows(RuntimeException.class, () -> authenticationService.authenticateAndGenerateToken(userId));
		verify(jwtGenerator, never()).generateToken(any(Authentication.class));
	}

	@Test
	public void testAuthentication2_EmptyUserIdShouldThrowException() {
		String userId = "";

		assertThrows(UserException.class, () -> authenticationService.authenticateAndGenerateToken(userId));
		verify(userDetailsService, never()).loadUserByUsername(any());
		verify(jwtGenerator, never()).generateToken(any(Authentication.class));
	}

	@Test
	public void testAuthentication2_NullUserIdShouldThrowException() {
		String userId = null;

		assertThrows(UserException.class, () -> authenticationService.authenticateAndGenerateToken(userId));
		verify(userDetailsService, never()).loadUserByUsername(any());
		verify(jwtGenerator, never()).generateToken(any(Authentication.class));
	}
}
