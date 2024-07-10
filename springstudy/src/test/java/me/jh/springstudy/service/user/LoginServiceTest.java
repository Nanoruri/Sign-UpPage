package me.jh.springstudy.service.user;

import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entitiy.User;
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

import static org.junit.jupiter.api.Assertions.*;
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
		validTestUser = new User("test", "testName", "validPassword", "010-1234-5678",
				LocalDate.of(1990, 11, 21), "test@test.com", LocalDateTime.now(), LocalDateTime.now());

		invalidTestUser = new User("test", "testName", "invalidPassword", "010-1234-5678",
				LocalDate.of(1990, 11, 21), "test@test.com", LocalDateTime.now(), LocalDateTime.now());
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

		verify(userDao, times(1)).findById(userId);
	}

	/**
	 * 로그인 실패 테스트(비밀번호 불일치)
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
		verify(userDao, times(2)).findById(userId);
	}


	/**
	 * 로그인 실패 테스트(사용자 없음)
	 */
	@Test
	public void loginServiceExceptionTest() {
		String userId = "NotExistedUser";
		String password = "HolyMoly";

		//사용자 정보가 없을 때
		when(userDao.findById(userId)).thenReturn(Optional.empty());

		//예외 발생 확인
		try {
			loginService.loginCheck(userId, password);
		} catch (Exception e) {
			assertEquals("해당 사용자 정보가 없습니다", e.getMessage());
		}

		//사용자 정보 조회가 한 번만 일어났는지 확인
		verify(userDao, times(1)).findById(userId);
	}
}
