package me.jh.springstudy.service.userservice;

import me.jh.springstudy.MySpringBootApplication;
import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MySpringBootApplication.class)
public class SignupServiceTest {

	@Autowired
	private SignupService signupService;


	@Test
	public void isDuplicateIdTest() {
		//중복검사에 관한 테스트
		String userId = "test";
		boolean result = signupService.isDuplicateId(userId);
		assertTrue(result, "중복된 아이디가 있어야 함");
	}

	@Test
	public void isNotDuplicateIdTest() {
		//중복검사에 관한 테스트
		String userId = "Unknown";
		boolean result = signupService.isDuplicateId(userId);
		assertFalse(result, "중복된 아이디가 없어야 함");
	}


	//회원가입에 관한 테스트
	@Test
	public void signupServiceSuccesseTest() {

		User testUser;
		testUser = new User();
		testUser.setUserId("SignupTest");
		testUser.setName("test");
		testUser.setPassword("test");
		testUser.setEmail("Signup@test.com");
		testUser.setPhoneNum("010-1111-2222");
		testUser.setBirth(LocalDate.of(1999, 1, 1));//todo : 권장하지 않는 방법이라는 듯. 대체제 찾아보기
		testUser.setCreatedDate(LocalDateTime.now());
		testUser.setUpdateDate(LocalDateTime.now());
		signupService.registerMember(testUser);

		assertEquals("SignupTest", testUser.getUserId(), "회원가입 성공해야 함");

	}

	@Test
	public void signupServiceExceptionTest() {
		User testUser;
		testUser = new User();
		testUser.setUserId("SignupTest");

		// 중복검사를 통과하지 못하면 예외를 던져야 함
		// when
		try {
			signupService.registerMember(testUser);
			// 예외가 발생하지 않았으므로 테스트 실패
			fail("예외 발생 실패..");
		} catch (UserException e) {
			// then
			// 예외가 발생하여 테스트 성공
			assertEquals(UserErrorType.ID_ALREADY_EXIST, e.getExceptionType());

//			assertThrows(UserException.class, () -> {
//				signupService.registerMember(testUser);
//			});todo : 이런식으로 작성 가능함... 람다식 공부하고 적용하기

		}
	}
}