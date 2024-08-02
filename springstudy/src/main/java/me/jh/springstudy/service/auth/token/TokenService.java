package me.jh.springstudy.service.auth.token;

import io.jsonwebtoken.JwtException;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.dao.auth.RefreshTokenDao;
import me.jh.springstudy.entitiy.auth.RefreshToken;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TokenService {

	private final RefreshTokenDao refreshTokenDao;
	private final UserDao userDao;

	@Autowired
	public TokenService(RefreshTokenDao refreshTokenDao, UserDao userDao) {
		this.refreshTokenDao = refreshTokenDao;
		this.userDao = userDao;
	}


	public void saveRefreshToken(String userId, String token) {

		if (userId == null || token == null) {
			throw new UserException(UserErrorType.MISSING_INFORMATION);
		} else if (userId.isEmpty() || token.isEmpty() ) {
			throw new UserException(UserErrorType.MISSING_INFORMATION);
		}

		refreshTokenDao.save(new RefreshToken(userId, token));
	}


	public String matchRefreshToken(String token) {//fixMe: DB가 아파할거같은 메서드. 개선하자
		Optional<RefreshToken> getRefreshToken = refreshTokenDao.findById(token);
		if (getRefreshToken.isEmpty()) {
			throw new JwtException("Refresh Token이 존재하지 않습니다.");
		}
		RefreshToken tokenInfo = getRefreshToken.get();
		if (!userDao.existsById(tokenInfo.getUserId())) {
			throw new UserException(UserErrorType.USER_NOT_FOUND);
		}

		return tokenInfo.getUserId();
	}

	public void deleteRefreshToken(String refreshToken) {

		if (refreshToken == null || refreshToken.isEmpty()) {
			throw new UserException(UserErrorType.MISSING_INFORMATION);
		}

		refreshTokenDao.deleteById(refreshToken);
	}
}
