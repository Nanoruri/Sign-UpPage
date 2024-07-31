package me.jh.springstudy.entitiy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RefreshTokenTest {



	@Test
	public void createRefreshTokenWithAllFields() {
		String userId = "user123";
		String token = "token123";
		String ipAddress = "192.168.1.1";

		RefreshToken refreshToken = new RefreshToken(userId, token, ipAddress);

		assertEquals(userId, refreshToken.getUserId());
		assertEquals(token, refreshToken.getToken());
		assertEquals(ipAddress, refreshToken.getIpAddress());
	}

	@Test
	public void createRefreshTokenWithDefaultConstructorAndSetFields() {
		String userId = "user123";
		String token = "token123";
		String ipAddress = "192.168.1.1";

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUserId(userId);
		refreshToken.setToken(token);
		refreshToken.setIpAddress(ipAddress);

		assertEquals(userId, refreshToken.getUserId());
		assertEquals(token, refreshToken.getToken());
		assertEquals(ipAddress, refreshToken.getIpAddress());
	}

	@Test
	public void testGettersAndSetters() {
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setId(1L);
		refreshToken.setUserId("user123");
		refreshToken.setToken("token123");
		refreshToken.setIpAddress("192.168.1.1");

		assertEquals(1L, refreshToken.getId());
		assertEquals("user123", refreshToken.getUserId());
		assertEquals("token123", refreshToken.getToken());
		assertEquals("192.168.1.1", refreshToken.getIpAddress());
	}

}
