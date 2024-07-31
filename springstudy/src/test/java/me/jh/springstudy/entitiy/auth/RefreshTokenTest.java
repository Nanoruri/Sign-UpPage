package me.jh.springstudy.entitiy.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RefreshTokenTest {



	@Test
	public void createRefreshTokenWithAllFields() {
		String userId = "user123";
		String token = "token123";


		RefreshToken refreshToken = new RefreshToken(userId, token);

		assertEquals(userId, refreshToken.getUserId());
		assertEquals(token, refreshToken.getToken());
	}

	@Test
	public void createRefreshTokenWithDefaultConstructorAndSetFields() {
		String userId = "user123";
		String token = "token123";


		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUserId(userId);
		refreshToken.setToken(token);


		assertEquals(userId, refreshToken.getUserId());
		assertEquals(token, refreshToken.getToken());

	}

	@Test
	public void testGettersAndSetters() {
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setId(1L);
		refreshToken.setUserId("user123");
		refreshToken.setToken("token123");


		assertEquals(1L, refreshToken.getId());
		assertEquals("user123", refreshToken.getUserId());
		assertEquals("token123", refreshToken.getToken());

	}

}
