package me.jh.springstudy.service.userservice;

import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.dao.UserDao;
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
	 */
	public boolean loginCheck(String userId, String password) {//true와 false만 반환하면 되니 boolean타입으로
		Optional<User> user = userDao.findById(userId);// 아이디 찾기 및 대조//todo : if문 사용시 여기 삭제
		//25번째 줄에서 에러 터짐, 로그 내용 : javax.persistence.NonUniqueResultException: query did not return a unique result: 2
		// 아이디 중복 검사로 해결

		if (user.isEmpty()) {//로그인 실패 시 예외처리
			log.info("로그인 실패");
			throw new UserException(UserErrorType.USER_NOT_FOUND);
		}//todo : 이렇게 처리 해도 되나??


//		return user.isPresent() && //해당 아이디가 값이 존재하는 지 검사//todo :if문 사용시 여기도 user부분 memberDao.findById(userId)로 바꿔주기
			return 	passwordEncoder.matches(password, user.get().getPassword());// 해시된 비밀번호 대조
	}
}
