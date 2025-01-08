package me.jh.springstudy.service.user;


import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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
				LocalDate.of(1990, 1, 1), "test@test.com", LocalDateTime.now(), LocalDateTime.now(), "USER");
	}


	/**
	 * 아이디 찾기 성공 테스트
	 */
	@Test
	public void findIdSuccessTest() {
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
		String name = "testName";
		String phoneNum = "010-1234-5678";
		User user = new User(userId, name, "hashedPassword", phoneNum,
				null, null, null, null,"USER");

		when(userDao.findById(user.getUserId())).thenReturn(Optional.ofNullable(changePasswordUser));

		boolean validUser = findService.validateUser(user);
		assertTrue(validUser, "사용자를 찾았습니다.");

		verify(userDao, times(1)).findById(user.getUserId());
	}

	/**
	 * 사용자 이름 불일치 테스트
	 */
	@Test
	public void validateUserNameMismatch() {
		String userId = "test";
		String name = "test";
		String phoneNum = "010-1234-5678";
		User user = new User(userId, name, "hashedPassword", phoneNum,
				null, null, null, null, "USER");


		when(userDao.findById(user.getUserId())).thenReturn(Optional.ofNullable(changePasswordUser));

		boolean validUser = findService.validateUser(user);
		assertFalse(validUser, "사용자를 찾지 못했습니다.");

		verify(userDao, times(1)).findById(user.getUserId());
	}

	/**
	 * 사용자 전화번호 불일치 테스트
	 */
	@Test
	public void validateUserPhoneNumMismatch() {
		String userId = "test";
		String name = "testName";
		String phoneNum = "010-4321-5678";
		User user = new User(userId, name, "hashedPassword", phoneNum,
				null, null, null, null, "USER");

		when(userDao.findById(user.getUserId())).thenReturn(Optional.ofNullable(changePasswordUser));

		boolean validUser = findService.validateUser(user);
		assertFalse(validUser, "사용자를 찾지 못했습니다.");

		verify(userDao, times(1)).findById(user.getUserId());
	}



	/**
	 * 사용자 정보 조회 실패 테스트
	 */
	@Test
	public void ValidateUserFailedTest() {
		String userId = "Unknown";
		String name = "testName";
		String phoneNum = "010-1234-5678";
		User user = new User(userId, name, "hashedPassword", phoneNum,
				null, null, null, null,"USER");

		when(userDao.findById(user.getUserId())).thenReturn(Optional.empty());

		boolean validUser = findService.validateUser(user);
		assertFalse(validUser, "사용자를 찾지 못했습니다.");

		verify(userDao, times(1)).findById(user.getUserId());
	}

	/**
	 * 비밀번호 변경 성공 테스트
	 */
	@Test
	public void changePasswordSuccessTest() {
		String userId = "test";
		String name = "testName";
		String phoneNum = "010-1234-5678";
		User user = new User(userId, name, null, phoneNum,
				null, null, null, null,"USER");

		when(userDao.findById(user.getUserId())).thenReturn(Optional.ofNullable(changePasswordUser));

		String newPassword = "changedPassword";

		boolean result = findService.changePassword(user, newPassword);

		assertTrue(result, "비밀번호 변경 성공해야함");
		verify(userDao, times(1)).save(changePasswordUser);
		verify(passwordEncoder, times(1)).encode(newPassword);
	}

	/**
	 * 비밀번호 변경 예외 테스트
	 */
	@Test
	public void changePasswordFailedTest() {

		//given
		// 사용자 정보가 없는 경우, 비밀번호 변경이 실패해야함
		when(userDao.findById(changePasswordUser.getUserId())).thenReturn(Optional.empty());

		// Act & Assert
		try {
			findService.changePassword(changePasswordUser, "changedPassword");
			fail("비밀번호 변경이 실패해야함");
		} catch (Exception e) {
			assertEquals("해당 사용자 정보가 없습니다", e.getMessage());
		}

		// Verify
		// 사용자 정보가 없는 경우, 비밀번호 변경이 실패하고 해당 예외가 발생하는지 확인
		verify(userDao, times(1)).findById(changePasswordUser.getUserId());
		verifyNoMoreInteractions(userDao);
	}

}

