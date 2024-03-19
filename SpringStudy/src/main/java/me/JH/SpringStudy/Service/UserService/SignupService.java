package me.jh.springStudy.service.userService;

import me.jh.springStudy.entitiy.User;
import me.jh.springStudy.exception.user.UserErrorType;
import me.jh.springStudy.exception.user.UserException;
import me.jh.springStudy.repositoryDao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class SignupService {

	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public SignupService(UserDao userDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	public void registerMember(User user) {//로그인 할때에도 한번더 중복 검사 하게 끔하고 예외가 나오면 Catch하도록
		if (isDuplicateId(user.getUserId())) {//중복검사 한번 더 하는 로직
			throw new UserException(UserErrorType.ID_ALREADY_EXIST);
		} else if (userDao.findById(user.getUserId()).isPresent()) {
			throw new UserException(UserErrorType.USER_ALREADY_EXIST);
		}//todo : 이렇게 처리 해도 되나??

		user.setPassword(passwordEncoder.encode(user.getPassword()));// 비밀번호를 해시화하여 저장
		user.setCreatedDate(new Date());
		user.setUpdateDate(new Date());
		userDao.save(user);// Member 엔티티를 데이터베이스에 저장
	}

	public boolean isDuplicateId(String userId) {
		return userDao.existsById(userId);//아이디 중복 검사 하는 메서드
	}
}
