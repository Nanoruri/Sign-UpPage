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
		//given
		//이메일 중복 호출 시 중복이 아닌 상태로 설정
		when(signupService.isDuplicateEmail(successTestUser.getEmail())).thenReturn(false);
		//비밀번호 해시화
		when(passwordEncoder.encode(successTestUser.getPassword())).thenReturn("hashedPassword");

		//when
		//회원가입 메서드 실행
		signupService.registerMember(successTestUser);


		//then
		//회원가입 실행 후 회원가입 성공한 아이디와 같은지 확인
		assertEquals("test", successTestUser.getUserId(), "회원가입 성공해야합니다.");

		//verify
		//회원가입 성공시 중복검사 메서드가 각각 한번씩 실행되었는지 검증
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
		// given
		// 아이디 중복 호출 시 중복인 상태로 설정
		when(userDao.findById(failTestUser.getUserId())).thenReturn(java.util.Optional.of(failTestUser));


		// when
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
		// given
		// 이메일 중복 호출 시 중복인 상태로 설정
		when(signupService.isDuplicateEmail(failTestUser.getEmail())).thenReturn(true);

		// when
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

	@Test
	public void registMemberPattrnNotMatchTest(){
		//given
		//아이디 패턴이 일치하지 않는 상태로 설정
		failTestUser.setUserId("tex");

		//when
		//회원가입 메서드 실행
		try {
			signupService.registerMember(failTestUser);
			fail("예외 발생 실패..");
		} catch (UserException e) {
			//then
			//예외가 발생하여 테스트 성공
			assertEquals("입력된 값이 정규식과 일치하지 않습니다", e.getMessage());
		}
		//verify
		//예외가 잘 던져졌는지에대한 검증;
		verify(userDao, times(0)).save(failTestUser);
	}

	@Test
	public void registMemberEmailPattrnNotMatchTest(){
		//given
		//이메일 패턴이 일치하지 않는 상태로 설정
		failTestUser.setEmail("test.com");

		//when
		//회원가입 메서드 실행
		try {
			signupService.registerMember(failTestUser);
			fail("예외 발생 실패..");
		} catch (UserException e) {
			//then
			//예외가 발생하여 테스트 성공
			assertEquals("입력된 값이 정규식과 일치하지 않습니다", e.getMessage());
		}
		//verify
		//예외가 잘 던져졌는지에대한 검증;
		verify(userDao, times(0)).save(failTestUser);
	}




	/**
	 * 아이디 중복검사 (중복O)
	 */
	@Test
	public void isDuplicateIdTest() {
		//given
		//아이디를 test로 설정했을때 중복인 상태로 설정
		String userId = "test";
		when(userDao.existsById(userId)).thenReturn(true);

		//when
		//아이디 중복검사 메서드 실행
		boolean result = signupService.isDuplicateId(userId);
		//then
		//중복인 상태이므로 true가 반환되어야 함
		assertTrue(result);

		//verify
		//아이디 중복검사 메서드가 한번 실행되었는지 검증
		verify(userDao, times(1)).existsById(userId);
	}

	/**
	 * 아이디 중복검사 (중복X)
	 */
	@Test
	public void isNotDuplicateIdTest() {
		//given
		//아이디를 중복이 아닌 상태로 설정
		String userId = "testid";
		when(userDao.existsById(userId)).thenReturn(false);

		//when
		//아이디 중복검사 메서드 실행
		boolean result = signupService.isDuplicateId(userId);

		//then
		//중복이 아니므로 false가 반환되어야 함
		assertFalse(result, "중복된 아이디가 없어야 함");

		//verify
		//아이디 중복검사 메서드가 한번 실행되었는지 검증
		verify(userDao, times(1)).existsById(userId);
	}

	/**
	 * 아이디 패턴이 일치하지 않을 경우 예외를 발생시키는지에 대한 테스트
	 */
	@Test
	public void IdPattrnNotMatchTest() {
		//given
		//아이디를 4자 이하로 설정
		String userId = "tex";

		//when
		//아이디 중복검사 메서드 실행
		try {
			signupService.isDuplicateId(userId);
			fail("예외 발생 실패..");
		} catch (UserException e) {
			//then
			//예외가 발생하여 테스트 성공
			assertEquals("입력된 값이 정규식과 일치하지 않습니다", e.getMessage());
		}
		//verify
		//예외가 잘 던져졌는지에대한 검증;
		verify(userDao, times(0)).existsById(userId);
	}



	/**
	 * 이메일 중복검사 (중복O)
	 */
	@Test
	public void isDuplicateEmailTest() {
		//given
		//이메일을 중복인 상태로 설정
		String email = "failTest@naver.com";
		when(signupService.isDuplicateEmail(email)).thenReturn(true);

		//when
		//이메일 중복검사 메서드 실행
		boolean result = signupService.isDuplicateEmail(email);
		assertTrue(result);

		//verify
		//이메일 중복검사 메서드가 한번 실행되었는지 검증
		verify(userDao, times(1)).existsByEmail(email);
	}

	/**
	 * 이메일 중복검사 (중복X)
	 */
	@Test
	public void isNotDuplicateEmailTest() {
		//given
		//이메일을 중복이 아닌 상태로 설정
		String email = "test@test.com";
		when(signupService.isDuplicateEmail(failTestUser.getEmail())).thenReturn(true);

		//when
		//이메일 중복검사 메서드 실행
		boolean result = signupService.isDuplicateEmail(failTestUser.getEmail());
		assertTrue(result);

		//verify
		//이메일 중복검사 메서드가 한번 실행되었는지 검증
		verify(userDao, times(1)).existsByEmail(failTestUser.getEmail());
	}


	/**
	 * 이메일 패턴이 일치하지 않을 경우 예외를 발생시키는지에 대한 테스트
	 */
	@Test
	public void emailPatternNotMatchTest() {
		//given
		//이메일 패턴이 일치하지 않는 상태로 설정
		String email = "test.com";

		//when
		//이메일 패턴 확인 메서드 실행
		try {
			signupService.isDuplicateEmail(email);
			fail("예외 발생 실패..");
		} catch (UserException e) {
			//then
			//예외가 발생하여 테스트 성공
			assertEquals("입력된 값이 정규식과 일치하지 않습니다", e.getMessage());
		}

		//verify
		//예외가 잘 던져졌는지에대한 검증;
		verify(userDao, times(0)).existsByEmail(email);
	}

}

