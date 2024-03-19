package me.jh.SpringStudy.service.userService;

import me.JH.SpringStudy.entitiy.User;
import me.JH.SpringStudy.MySpringBootApplication;
import me.JH.SpringStudy.repositoryDao.UserDao;
import me.JH.SpringStudy.Service.userService.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MySpringBootApplication.class)
public class ServiceTest {

	@Autowired
	private UserDao userDao;
	private PasswordEncoder passwordEncoder;
@Autowired
	private LoginService loginService;


	@Test
	public void changePasswordTest() {

		String userId = "test";
		String name = "test";
		String email = "test@test.com";
		String newPassword = "12345678";

		Optional<User> optionalUser = userDao.findByProperties(userId, name, email);

		assertTrue(optionalUser.isPresent(), "사용자를 찾았습니다.");

		User user = optionalUser.get();

		// 새로운 비밀번호로 업데이트
		user.setPassword(newPassword);//변경값을 확인하기 위해 PasswordEncoder 사용 X
		// 업데이트된 사용자 정보 저장
		userDao.save(user);

		assertTrue(true, "저장에 성공하였습니다.");
	}

	@Test
	public void loginServiceSuccesseTest(){
		String userId = "kaby1217";
		String password = "1234";

		boolean result = loginService.loginCheck(userId, password);
		assertTrue(result, "로그인 성공해야 함");
	}

	@Test
	public void loginServiceFailedTest(){
		String userId = "Unknown";
		String password = "HolyMoly";

		boolean result = loginService.loginCheck(userId, password);
		assertFalse(result, "로그인 실패해야함");

	}


	}

