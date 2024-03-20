package me.jh.springStudy.service.userService;

import me.jh.springStudy.MySpringBootApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MySpringBootApplication.class)
public class LoginServiceTest {

	@Autowired
	private LoginService loginService;

	//로그인에 관한 테스트
	@Test
	public void loginServiceSuccesseTest() {
		String userId = "test";
		String password = "test";

		boolean result = loginService.loginCheck(userId, password);
		assertTrue(result, "로그인 성공해야 함");
	}

	@Test
	public void loginServiceFailedTest() {
		String userId = "Unknown";
		String password = "HolyMoly";

		boolean result = loginService.loginCheck(userId, password);
		assertFalse(result, "로그인 실패해야함");
	}

}
