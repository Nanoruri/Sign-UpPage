package me.jh.springstudy.service.userservice;

import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 사용자 정보를 찾는 서비스 클래스.
 * 이 클래스는 사용자의 정보를 조회하고 검증하며, 아이디를 찾아주는 기능이나 비밀번호 변경 등의 기능을 제공.
 */

@Service
public class FindService {
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	public final static Logger log = LoggerFactory.getLogger(FindService.class);


	@Autowired
	public FindService(UserDao userDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 사용자 이름과 전화번호를 이용하여 데이터베이스에서 사용자 아이디를 찾는 메서드.
	 * 입력된 이름과 전화번호로 정확히 일치하는 사용자를 찾아 해당 사용자의 아이디를 반환.
	 * 일치하는 사용자가 없을 경우 null을 반환.
	 *
	 * @param name     사용자의 이름
	 * @param phoneNum 사용자의 전화번호
	 * @return 찾은 아이디를 반환하거나 해당하는 사용자가 없을 경우 null반환.
	 * @implNote 이 메서드는 {@link UserDao#findByNameAndPhoneNum(String, String)}를 사용하여 사용자를 조회.
	 */
	public String findId(String name, String phoneNum) {
		User user = userDao.findByNameAndPhoneNum(name, phoneNum);
		return (user != null) ? user.getUserId() : null;
	}

	/**
	 * 입력된 사용자 아이디, 이름, 전화번호를 검증하여 사용자의 존재 여부를 확인하는 메서드.
	 * 입력된 정보로 정확히 일치하는 사용자가 데이터베이스에 존재하는지 여부를 반환.
	 *
	 * @param userId   사용자의 아이디
	 * @param name     사용자의 이름
	 * @param phoneNum 사용자의 전화번호
	 * @return 사용자가 존재할 경우 true, 존재하지 않을 경우 false 반환
	 * @implNote 이 메서드는 {@link UserDao#findByProperties(String, String, String)}를 사용하여 사용자를 검증.
	 */
	public boolean validateUser(String userId, String name, String phoneNum) {
		boolean isValid = userDao.findByProperties(userId, name, phoneNum).isPresent();
		log.info(isValid ? "사용자를 찾았습니다" + userId : "사용자를 찾을 수 없습니다.");
		return isValid;
	}

	/**
	 * 사용자의 비밀번호를 변경하는 메서드.
	 * 입력된 사용자 정보로 데이터베이스에서 사용자를 찾아 비밀번호를 새로운 비밀번호로 변경.
	 * 비밀번호 변경에 성공하면 true를 반환하고, 실패할 경우 예외 발생.
	 *
	 * @param changePasswordUser 비밀번호를 변경할 사용자 정보
	 * @param newPassword        새로운 비밀번호
	 * @return 비밀번호 변경 성공 시 true, 실패 시 false
	 * @throws UserException 사용자 정보가 일치하지 않아 비밀번호 변경에 실패할 경우 예외 발생
	 * @implNote 이 메서드는 {@link UserDao#findByProperties(String, String, String)}를 사용하여 사용자를 검증하고,
	 * {@link UserDao#save(Object)}를 사용하여 변경된 비밀번호를 저장합니다.
	 */
	public boolean changePassword(User changePasswordUser, String newPassword) {
		Optional<User> optionalUser = userDao.findByProperties(changePasswordUser.getUserId(),
				changePasswordUser.getName(),
				changePasswordUser.getPhoneNum());


		if (optionalUser.isEmpty()) {
			log.warn("사용자에 대한 정보가 없습니다.");
			throw new UserException(UserErrorType.USER_NOT_FOUND);
		}
		User user = optionalUser.get();

		log.info("사용자를 찾았습니다");
		log.info("사용자 ID :{}", user.getUserId());
		log.info("사용자 이름 :{}", user.getName());
		log.info("사용자 전화번호 :{}", user.getPhoneNum());

		// 새로운 비밀번호로 업데이트
		user.setPassword(passwordEncoder.encode(newPassword));
		log.info("새로운 비밀번호로 변경되었습니다");
		// 업데이트된 사용자 정보 저장
		userDao.save(user);
		return true;
	}
}


