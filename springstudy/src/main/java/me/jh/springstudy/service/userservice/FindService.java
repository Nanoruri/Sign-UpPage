package me.jh.springstudy.service.userservice;

import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.repositorydao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 사용자 정보를 찾는 서비스 클래스.
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
	 * 아이디 찾기를 수행하는 메서드.
	 *
	 * @param name     사용자의 이름
	 * @param phoneNum 사용자의 전화번호
	 * @return 찾은 아이디 or null
	 */
	public String findId(String name, String phoneNum) {
		User user = userDao.findByNameAndPhoneNum(name, phoneNum);// TODO : 이름과 이메일이 같으면 500에러 발생, 이메일 말고 다른걸로 받게 하기
		return (user != null) ? user.getUserId() : null;//todo : 여기서 컨트롤러의 예외처리 해도 되나??
	}

	/**
	 * 비밀번호 찾기를 수행하는 메서드.
	 *
	 * @param userId   사용자의 아이디
	 * @param name     사용자의 이름
	 * @param phoneNum 사용자의 전화번호
	 * @return 찾은 비밀번호 or null
	 */
	public boolean validateUser(String userId, String name, String phoneNum) {//todo : findBy properties, Criteria 사용
		boolean isValid = userDao.findByProperties(userId, name, phoneNum).isPresent();
		log.info(isValid ? "사용자를 찾았습니다" + userId : "사용자를 찾을 수 없습니다.");
		return isValid;
	}

	/**
	 * 비밀번호 변경을 수행하는 메서드.
	 *
	 * @param changePasswordUser 비밀번호를 변경할 사용자 정보
	 * @param newPassword        새로운 비밀번호
	 * @return 비밀번호 변경 성공 시 true, 실패 시 false
	 */
	public boolean changePassword(User changePasswordUser, String newPassword) {
		Optional<User> optionalUser = userDao.findByProperties(changePasswordUser.getUserId(),
				changePasswordUser.getName(),
				changePasswordUser.getPhoneNum());


		if (optionalUser.isEmpty()) {
			log.info("사용자에 대한 정보가 없습니다.");
			throw new UserException(UserErrorType.USER_NOT_FOUND);
		}// todo : validateUser에서 한번 체크하는데 예외가 필요할까..
		User user = optionalUser.get();

		log.info("사용자를 찾았습니다");
		log.info("사용자 ID :" + user.getUserId());
		log.info("사용자 이름 :" + user.getName());
		log.info("사용자 전화번호 :" + user.getPhoneNum());

		// 새로운 비밀번호로 업데이트
		user.setPassword(passwordEncoder.encode(newPassword));
		log.info("새로운 비밀번호로 변경되었습니다");
		// 업데이트된 사용자 정보 저장
		userDao.save(user);
		return true;
	}
}

//	public void resetPassword(String presentPassword, String newPassword) {
//		String hashedRawPassword = passwordEncoder.encode(presentPassword);
//		User user = memberDao.findByPassword(presentPassword);
//
//		if (user != null && passwordEncoder.matches(presentPassword, user.getPassword())) {
//
//			// 새로운 비밀번호로 업데이트
//			user.setPassword(passwordEncoder.encode(newPassword));
//
//			// 업데이트된 사용자 정보 저장
//			memberDao.save(user);
//		}
//	}

