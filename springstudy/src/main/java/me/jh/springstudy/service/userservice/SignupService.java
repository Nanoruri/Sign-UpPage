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
		if (assureIdPattern(user.getUserId())) {//user데이터를 가져오기 전에 패턴을 확인하여 예외를 던짐
			throw new UserException(UserErrorType.PATTERN_NOT_MATCHED);
		}else if (isDuplicateEmail(user.getEmail())) {
			throw new UserException(UserErrorType.USER_ALREADY_EXIST);
		}

		if (userDao.findById(user.getUserId()).isPresent()) {
			throw new UserException(UserErrorType.ID_ALREADY_EXIST);// todo : 이미 duplicatId의 매개변수가 pk인데 이 부분이 필요한가??
//		} else if (isDuplicateId(user.getUserId())) {
//			throw new UserException(UserErrorType.ID_ALREADY_EXIST);
		}  //isDuplicateId 메서드 findById 기능과 겹치므로 제거 = findById로 대체

		user.setPassword(passwordEncoder.encode(user.getPassword()));// 비밀번호를 해시화하여 저장
		user.setCreatedDate(LocalDateTime.now());
		user.setUpdateDate(LocalDateTime.now());
		userDao.save(user);// 사용자 엔티티를 데이터베이스에 저장
	}

//	/**
//	 *입력받은 값의 패턴을 확인하여 중복검사 메서드를 호출하는 메서드
//	 * @param identifier 중복 검사를 받을 입력값
//	 * @return 패턴에 따라 아이디나 이메일 중복검사 메서드 호출
//	 * @throws UserException 어느 패턴에도 해당하지 않을 경우 예외 발생
//	 */
//	public boolean isDuplicate(String identifier) {
//		if(assureEmailPattern(identifier)){
//			return isDuplicateEmail(identifier);
//		}else if (assureIdPattern(identifier)){
//			return isDuplicateId(identifier);
//		}else{
//			throw new UserException(UserErrorType.MISSING_INFORMATION);
//		}
//	}

	/**
	 * 아이디 패턴과 일치하는지 확인하고 중복검사를 실행하는 메서드
	 * @param userId 중복검사를 받을 아이디 문자열
	 * @throws UserException 아이디 패턴이 일치하지 않을 경우 예외 발생
	 * @return 검사를 통과한 ID문자열을 가지고 DB에서 사용자 조회
	 */
	public boolean isDuplicateId(String userId) {
		if(assureIdPattern(userId)){
			throw new UserException(UserErrorType.PATTERN_NOT_MATCHED);
		};
		return userDao.existsById(userId);//아이디 중복 검사 하는 메서드
	}

	/**
	 * 이메일 패턴과 일치하는지 확인하고 중복검사를 실행하는 메서드
	 * @param email 중복검사를 받을 이메일 문자열
	 * @throws UserException 이메일 패턴이 일치하지 않을 경우 예외 발생
	 * @return 검사를 통과한 이메일 문자열을 가지고 DB에서 사용자 조회
	 */
	public boolean isDuplicateEmail(String email) {
		if(!assureEmailPattern(email)){
			throw new UserException(UserErrorType.PATTERN_NOT_MATCHED);
		};
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