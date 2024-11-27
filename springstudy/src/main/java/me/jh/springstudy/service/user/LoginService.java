package me.jh.springstudy.service.user;

import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entity.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 로그인관련 비즈니스로직을 처리하는 서비스 클래스.
 * 이 클래스는 사용자의 로그인을 처리하고, 로그인 정보를 검증하는 기능을 제공.
 */

@Service
public class LoginService {
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private static final Logger log = LoggerFactory.getLogger(LoginService.class);


	@Autowired//의존성 쭈와왑
	public LoginService(UserDao userDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 로그인을 처리하는 메서드.
	 *
	 * @param userId   로그인할 아이디
	 * @param password 로그인할 비밀번호
	 * @return 로그인 성공 시 true, 실패 시 false
	 * @throws UserException 아이디가 존재하지 않거나 비밀번호가 일치하지 않을 경우 예외 발생
	 * @implNote 이 메서드는 다음의 단계를 수행합니다:
	 * 1. 입력받은 아이디로 사용자를 조회합니다. {@link UserDao#findById(Object)}
	 * 2. 조회된 사용자가 없으면 로그인 실패 예외 발생.
	 * 3. 조회된 사용자의 비밀번호와 입력받은 비밀번호를 비교하여 일치 여부를 반환.
	 */
	public boolean loginCheck(String userId, String password) {
		Optional<User> user = userDao.findById(userId);


		if (user.isEmpty()) {
			log.warn("로그인 실패");
			throw new UserException(UserErrorType.USER_NOT_FOUND);
		}

		return passwordEncoder.matches(password, user.get().getPassword());
	}
}
