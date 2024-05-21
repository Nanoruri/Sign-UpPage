package me.jh.springstudy.service.userservice;

import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

	@Mock
	private UserDao userDao;
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	private User validTestUser;
	@Mock
	private User invalidTestUser;

	@InjectMocks
	private LoginService loginService;


	@BeforeEach
	public void setUp() {
		validTestUser = new User("test","testName", "validPassword", "010-1234-5678",
				LocalDate.of(1990,11,21), "test@test.com", LocalDateTime.now(),LocalDateTime.now());

		invalidTestUser = new User("test","testName", "invalidPassword", "010-1234-5678",
				LocalDate.of(1990,11,21), "test@test.com", LocalDateTime.now(),LocalDateTime.now());
	}


	/**
	 * 로그인 성공 테스트
	 */
	@Test
	public void loginServiceSuccesseTest() {

		String userId = "test";
		String password = "validPassword";

		when(userDao.findById(userId)).thenReturn(Optional.of(validTestUser));
		when(passwordEncoder.matches(password, validTestUser.getPassword())).thenReturn(true);

		boolean result = loginService.loginCheck(userId, password);
		assertTrue(result, "로그인 성공해야 함");

		verify(userDao,times(1)).findById(userId);
	}

	/**
	 * 로그인 실패 테스트
	 */
	@Test
	public void loginServiceFailedTest() {
		String userId = "test";
		String password = "HolyMoly";

		when(userDao.findById(userId)).thenReturn(Optional.of(invalidTestUser));
		when(passwordEncoder.matches(password, invalidTestUser.getPassword())).thenReturn(false);
		when(loginService.loginCheck(userId, password)).thenReturn(false);//

		boolean result = loginService.loginCheck(userId, password);
		assertFalse(result, "로그인 실패해야함");
		verify(userDao,times(2)).findById(userId);
	}

}
