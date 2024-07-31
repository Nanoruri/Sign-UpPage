package me.jh.springstudy.dao.auth;

import me.jh.springstudy.entitiy.auth.RefreshToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenDaoTest {

	@Mock
	private RefreshTokenDao refreshTokenDao;

	@Test
	public void findByTokenReturnsToken() {
		String token = "validToken";
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(token);

		when(refreshTokenDao.findById(token)).thenReturn(Optional.of(refreshToken));

		Optional<RefreshToken> result = refreshTokenDao.findById(token);
		assertTrue(result.isPresent());
		assertEquals(token, result.get().getToken());
	}

	@Test
	public void findByTokenReturnsEmptyForInvalidToken() {
		String token = "invalidToken";

		when(refreshTokenDao.findById(token)).thenReturn(Optional.empty());

		Optional<RefreshToken> result = refreshTokenDao.findById(token);
		assertFalse(result.isPresent());
	}

	@Test
	public void deleteByTokenDeletesToken() {
		String token = "validToken";

		doNothing().when(refreshTokenDao).deleteById(token);

		refreshTokenDao.deleteById(token);

		verify(refreshTokenDao, times(1)).deleteById(token);
	}

	@Test
	public void deleteByTokenDoesNothingForInvalidToken() {
		String token = "invalidToken";

		doNothing().when(refreshTokenDao).deleteById(token);

		refreshTokenDao.deleteById(token);

		verify(refreshTokenDao, times(1)).deleteById(token);
	}
}