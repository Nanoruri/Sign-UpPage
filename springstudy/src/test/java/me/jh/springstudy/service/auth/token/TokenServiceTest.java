package me.jh.springstudy.service.auth.token;

import io.jsonwebtoken.JwtException;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.dao.auth.RefreshTokenDao;
import me.jh.springstudy.entitiy.auth.RefreshToken;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.service.auth.token.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TokenServiceTest {


	@Mock
	private RefreshTokenDao refreshTokenDao;
	@Mock
	private UserDao userDao;

	@InjectMocks
	private TokenService tokenService;


	@Test
	public void saveRefreshTokenWithValidInputs() {
		String userId = "user123";
		String token = "token123";


		tokenService.saveRefreshToken(userId, token);

		verify(refreshTokenDao, times(1)).save(any(RefreshToken.class));
	}

	@Test
	public void saveRefreshTokenWithNullUserIdThrowsException() {
		String userId = null;
		String token = "token123";

		assertThrows(UserException.class, () -> tokenService.saveRefreshToken(userId, token));
		verify(refreshTokenDao, never()).save(any(RefreshToken.class));
	}

	@Test
	public void saveRefreshTokenWithEmptyUserIdThrowsException() {
		String userId = "";
		String token = "token123";

		assertThrows(UserException.class, () -> tokenService.saveRefreshToken(userId, token));
		verify(refreshTokenDao, never()).save(any(RefreshToken.class));
	}

	@Test
	public void saveRefreshTokenWithNullTokenThrowsException() {
		String userId = "user123";
		String token = null;

		assertThrows(UserException.class, () -> tokenService.saveRefreshToken(userId, token));
		verify(refreshTokenDao, never()).save(any(RefreshToken.class));
	}

	@Test
	public void saveRefreshTokenWithEmptyTokenThrowsException() {
		String userId = "user123";
		String token = "";


		assertThrows(UserException.class, () -> tokenService.saveRefreshToken(userId, token));
		verify(refreshTokenDao, never()).save(any(RefreshToken.class));
	}



	@Test
	public void matchRefreshTokenWithValidInputs() {
		String token = "token123";

		RefreshToken refreshToken = new RefreshToken("user123", token);

		when(refreshTokenDao.findById(token)).thenReturn(Optional.of(refreshToken));
		when(userDao.existsById(refreshToken.getUserId())).thenReturn(true);

		String userId = tokenService.matchRefreshToken(token);

		assertEquals("user123", userId);
		verify(refreshTokenDao, times(1)).findById(token);
		verify(userDao, times(1)).existsById(refreshToken.getUserId());
	}

	@Test
	public void matchRefreshToken_UserDoesNotExist_ReturnsUserId() {
		String token = "validToken";
		RefreshToken refreshToken = new RefreshToken("userId", token);

		when(refreshTokenDao.findById(token)).thenReturn(Optional.of(refreshToken));
		when(userDao.existsById(refreshToken.getUserId())).thenReturn(false);

		assertThrows(UserException.class, () -> tokenService.matchRefreshToken(token));

		verify(refreshTokenDao, times(1)).findById(token);
		verify(userDao, times(1)).existsById(refreshToken.getUserId());
	}




	@Test
	public void matchRefreshTokenWithInvalidTokenThrowsException() {
		String token = "invalidToken";


		when(refreshTokenDao.findById(token)).thenReturn(Optional.empty());

		assertThrows(JwtException.class, () -> tokenService.matchRefreshToken(token));
		verify(refreshTokenDao, times(1)).findById(token);
		verify(userDao, never()).existsById(anyString());
	}

	@Test
	public void deleteRefreshTokenWithValidToken() {
		String token = "validToken";

		tokenService.deleteRefreshToken(token);

		verify(refreshTokenDao, times(1)).deleteById(token);
	}

	@Test
	public void deleteRefreshTokenWithNullToken() {
		String token = null;

		assertThrows(UserException.class, () -> tokenService.deleteRefreshToken(token));
		verify(refreshTokenDao, never()).deleteById(anyString());
	}

	@Test
	public void deleteRefreshTokenWithEmptyToken() {
		String token = "";

		assertThrows(UserException.class, () -> tokenService.deleteRefreshToken(token));
		verify(refreshTokenDao, never()).deleteById(anyString());
	}
}
