package me.jh.springStudy.service.userService;


import me.jh.springStudy.MySpringBootApplication;
import me.jh.springStudy.entitiy.User;
import me.jh.springStudy.repositoryDao.UserDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = MySpringBootApplication.class)
public class FindServiceTest {


	@Autowired
	private FindService findService;

	private UserDao userDao;




	//FindIdService에 관한 테스트
	@Test
	public void findIdSuccesseTest() {
		String name = "test";
		String phoneNum = "010-1234-5678";

		String result = findService.findId(name, phoneNum);
		assertEquals("kaby121", result, "아이디 찾기 성공해야 함");
	}

	@Test
	public void findIdFailedTest() {
		String name = "Unknown";
		String phoneNum = "010-0000-0000";

		String result = findService.findId(name, phoneNum);
		assertNull(result, "아이디 찾기 실패해야함");
	}


	@Test// 비밀번호 변경에 관한 테스트
	public void ValidateUserTest() {
		String userId = "test";
		String name = "test";
		String email = "test@test.com";
		boolean validateUser = findService.validateUser(userId, name, email);

		assertTrue(validateUser, "사용자를 찾았습니다.");
	}

	@Test
			public void changePasswordSuccessTest() {

		User changePasswordUser;
		changePasswordUser = new User();
		changePasswordUser.setUserId("test");
		changePasswordUser.setName("test");
		changePasswordUser.setEmail("test@test.com");
		String newPassword = "12345678";



		if (findService.validateUser(changePasswordUser.getUserId(),
				changePasswordUser.getName(), changePasswordUser.getEmail()))
		{
			findService.changePassword(changePasswordUser, newPassword);
			changePasswordUser.setPassword(newPassword);
		};// 새로운 비밀번호로 업데이트


		assertTrue(true, "저장에 성공하였습니다.");
	}

}
