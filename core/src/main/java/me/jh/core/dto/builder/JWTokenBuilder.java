package me.jh.core.dto.builder;

import io.jsonwebtoken.lang.Builder;
import me.jh.core.dto.JWToken;

public class JWTokenBuilder implements Builder<JWToken> {

	private String grantType;
	private String accessToken;
	private String refreshToken;

	public JWTokenBuilder grantType(String grantType) {
		this.grantType = grantType;
		return this;
	}

	public JWTokenBuilder accessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public JWTokenBuilder refreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

	@Override
	public JWToken build() {
		return new JWToken(grantType, accessToken, refreshToken);
	}
}
