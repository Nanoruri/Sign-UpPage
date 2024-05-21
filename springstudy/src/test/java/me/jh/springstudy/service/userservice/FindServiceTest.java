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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FindServiceTest {


	@Mock
	private UserDao userDao;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private User changePasswordUser;

	@InjectMocks
	private FindService findService;

	@BeforeEach
	public void setUp() {
		changePasswordUser = new User("test", "testName", "hashedPassword", "010-1234-5678",
				LocalDate.of(1990, 1, 1), "test@test.com", LocalDateTime.now(), LocalDateTime.now());
	}


	/**
	 * 아이디 찾기 성공 테스트
	 */
	@Test
	public void findIdSuccesseTest() {
		String name = "test";
		String phoneNum = "010-1234-5678";

		when(userDao.findByNameAndPhoneNum(name, phoneNum)).thenReturn(changePasswordUser);

		String result = findService.findId(name, phoneNum);
		assertEquals("test", result);

		verify(userDao, times(1)).findByNameAndPhoneNum(name, phoneNum);
	}

	/**
	 * 아이디 찾기 실패 테스트
	 */
	@Test
	public void findIdFailedTest() {
		String name = "Unknown";
		String phoneNum = "010-0000-0000";

		when(userDao.findByNameAndPhoneNum(name, phoneNum)).thenReturn(null);

		String result = findService.findId(name, phoneNum);
		assertNull(result, "아이디 찾기 실패해야함");

		verify(userDao, times(1)).findByNameAndPhoneNum(name, phoneNum);
	}


	/**
	 * 사용자 정보 조회 성공 테스트
	 */
	@Test
	public void ValidateUserTest() {
		String userId = "test";
		String name = "test";
		String phoneNum = "010-1234-5678";

		when(userDao.findByProperties(userId, name, phoneNum)).thenReturn(Optional.ofNullable(changePasswordUser));

		when(userDao.findByProperties(userId, name, phoneNum)).thenReturn(Optional.ofNullable(changePasswordUser));

		boolean validUser = findService.validateUser(userId, name, phoneNum);
		assertTrue(validUser, "사용자를 찾았습니다.");

		verify(userDao, times(1)).findByProperties(userId, name, phoneNum);
	}

	/**
	 * 사용자 정보 조회 실패 테스트
	 */
	@Test
	public void ValidateUserFailedTest() {
		String userId = "Unknown";
		String name = "test";
		String phoneNum = "010-1234-5678";

		when(userDao.findByProperties(userId, name, phoneNum)).thenReturn(Optional.ofNullable(null));

		boolean validUser = findService.validateUser(userId, name, phoneNum);
		assertFalse(validUser, "사용자를 찾지 못했습니다.");

		verify(userDao, times(1)).findByProperties(userId, name, phoneNum);
	}

	/**
	 * 비밀번호 변경 성공 테스트
	 */
	@Test
	public void changePasswordSuccessTest() {
		String userId = "test";
		String name = "testName";
		String phoneNum = "010-1234-5678";

		when(userDao.findByProperties(userId, name, phoneNum)).thenReturn(Optional.ofNullable(changePasswordUser));

		String newPassword = "changedPassword";

		boolean result = findService.changePassword(changePasswordUser, newPassword);

		assertTrue(result, "비밀번호 변경 성공해야함");
		verify(userDao, times(1)).save(changePasswordUser);
		verify(passwordEncoder, times(1)).encode(newPassword);
	}

}

