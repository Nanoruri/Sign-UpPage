package me.jh.springstudy.service.userservice;

import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.repositorydao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 회원가입 관련 비즈니스로직을 처리하는 서비스 클래스.
 */
@Service
public class SignupService {

	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public SignupService(UserDao userDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 회원가입을 처리하는 메서드.
	 * @param user 회원가입 정보를 담은 Member 객체
	 */
	public void registerMember(User user) {//로그인 할때에도 한번더 중복 검사 하게 끔하고 예외가 나오면 Catch하도록
		if (isDuplicate(user.getUserId())) {//중복검사 한번 더 하는 로직
			throw new UserException(UserErrorType.ID_ALREADY_EXIST);
		} else if (isDuplicate(user.getEmail())) {
			throw new UserException(UserErrorType.USER_ALREADY_EXIST);
		} else if (userDao.findById(user.getUserId()).isPresent()) {
			throw new UserException(UserErrorType.USER_ALREADY_EXIST);// todo : 이미 duplicatId의 매개변수가 pk인데 이 부분이 필요한가??
		}//todo : 이렇게 처리 해도 되나??

		user.setPassword(passwordEncoder.encode(user.getPassword()));// 비밀번호를 해시화하여 저장
		user.setCreatedDate(LocalDateTime.now());
		user.setUpdateDate(LocalDateTime.now());
		userDao.save(user);// 사용자 엔티티를 데이터베이스에 저장
	}

	/**
	 * 아이디 중복 검사를 수행하는 메서드.
	 *
	 * @param identifier 중복 검사를 받을 입력한 문자열
	 * @return 중복된 아이디가 존재하면 true, 존재하지 않으면 false
	 */
	public boolean isDuplicate(String identifier) {
		if (!isEmail(identifier)) {
			return isDuplicateId(identifier);
		} else {
			return isDuplicateEmail(identifier);
		}
	}

	private boolean isDuplicateId(String userId) {
		return userDao.existsById(userId);//아이디 중복 검사 하는 메서드
	}
	private boolean isDuplicateEmail(String email) {
		return userDao.existsByEmail(email);
	}

	/**
	 * 아이디 패턴을 확인하는 메서드
	 * @param identifier 아이디 문자열
	 * @return 아이디패턴과 일치하면 true, 일치하지 않으면 false
	 */
	private boolean assureIdPattern(String identifier) {
		String idRegex = "^[a-zA-Z0-9]{4,20}$";
		return !identifier.matches(idRegex);
	}

	/**
	 * 이메일 패턴을 확인하는 메서드
	 * @param identifier 이메일 문자열
	 * @return 이메일 패턴과 일치하면 true, 일치하지 않으면 false
	 */
	private boolean assureEmailPattern(String identifier) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		return identifier.matches(emailRegex);
	}

}