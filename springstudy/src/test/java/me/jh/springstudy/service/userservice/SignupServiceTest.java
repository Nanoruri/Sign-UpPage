package me.jh.springstudy.service.userservice;

import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.repositorydao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignupServiceTest {


	@Mock
	private UserDao userDao;
	@Mock
	private User successTestUser;
	@Mock
	private User failTestUser;
	;
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	SignupService signupService;


	@BeforeEach
	public void setUp() {
		successTestUser = new User("test", "testName", "hashedPassword", "010-1234-5678",
				LocalDate.of(1990, 11, 21), "test@test.com", LocalDateTime.now(), LocalDateTime.now());

		failTestUser = new User("test", "testName", "unHashedPassword", "010-1234-5678",
				LocalDate.of(1990, 11, 21), "test@test.com", LocalDateTime.now(), LocalDateTime.now());
	}

	/**
	 * 회원가입 성공 테스트
	 */
	//회원가입에 관한 테스트
	@Test
	public void signupServiceSuccesseTest() {

		when(signupService.isDuplicate(successTestUser.getUserId())).thenReturn(false);
		when(signupService.isDuplicate(successTestUser.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(successTestUser.getPassword())).thenReturn("hashedPassword");

		signupService.registerMember(successTestUser);

		assertEquals("test", successTestUser.getUserId(), "회원가입 성공해야합니다.");

//		verify(signupService,times(1)).isDuplicateId(successTestUser.getUserId());
		verify(passwordEncoder, times(1)).encode(successTestUser.getPassword());
		verify(userDao, times(1)).save(successTestUser);
	}


	/**
	 * 아이디가 중복인상태로 가입 시도시 예외를 발생 시키는지에 대한 테스트
	 */
	@Test
	public void signupServiceExceptionTest() {

		// 중복검사를 통과하지 못하면 예외를 던져야 함
		// when
		when(signupService.isDuplicate(failTestUser.getUserId())).thenReturn(true);

		try {
			// 예외가 발생하지 않았으므로 테스트 실패
			signupService.registerMember(failTestUser);
			fail("예외 발생 실패..");
		} catch (UserException e) {
			// then
			// 예외가 발생하여 테스트 성공
			assertEquals("이미 존재하는 아이디입니다.", e.getMessage());

//			assertThrows(UserException.class, () -> {
//				signupService.registerMember(testUser);
//			});todo : 이런식으로 작성 가능함... 람다식 공부하고 적용하기

		}
	}

	/**
	 * 이메일이 중복인 상태로 가입 시도시 예외를 발생 시키는지에 대한 테스트
	 */
	@Test
	public void isDuplicateEmail() {
		//중복검사에 관한 테스트
		when(signupService.isDuplicate(failTestUser.getEmail())).thenReturn(true);

		try {
			// 예외가 발생하지 않았으므로 테스트 실패
			signupService.registerMember(failTestUser);
			fail("예외 발생 실패..");
		} catch (UserException e) {
			// then
			// 예외가 발생하여 테스트 성공
			assertEquals("해당정보로 가입한 사용자가 이미 있습니다.", e.getMessage());
		}
	}

	/**
	 * 아이디 중복검사 (중복O)
	 */
	@Test
	public void isDuplicateIdTest() {
		//중복검사에 관한 테스트
		String userId = "test";

		when(userDao.existsById(userId)).thenReturn(true);

		boolean result = signupService.isDuplicate(userId);
		assertTrue(result);

		verify(userDao, times(1)).existsById(userId);
	}

	/**
	 * 아이디 중복검사 (중복X)
	 */
	@Test
	public void isNotDuplicateIdTest() {
		//중복검사에 관한 테스트
		String userId = "testingUser";

		when(userDao.existsById(userId)).thenReturn(false);

		boolean result = signupService.isDuplicate(userId);
		assertFalse(result, "중복된 아이디가 없어야 함");

		verify(userDao, times(1)).existsById(userId);
	}


	/**
	 * 이메일 중복검사 (중복O)
	 */
	@Test
	public void isDuplicateEmailTest() {
		//중복검사에 관한 테스트
		when(signupService.isDuplicate(failTestUser.getEmail())).thenReturn(true);

		boolean result = signupService.isDuplicate(failTestUser.getEmail());
		assertTrue(result);

		verify(userDao, times(1)).existsByEmail(failTestUser.getEmail());
	}
	/**
	 * 이메일 중복검사 (중복X)
	 */
	@Test
	public void isNotDuplicateEmailTest() {
		//중복검사에 관한 테스트
		when(signupService.isDuplicate(failTestUser.getEmail())).thenReturn(true);

		boolean result = signupService.isDuplicate(failTestUser.getEmail());
		assertTrue(result);

		verify(userDao, times(1)).existsByEmail(failTestUser.getEmail());
	}


}

