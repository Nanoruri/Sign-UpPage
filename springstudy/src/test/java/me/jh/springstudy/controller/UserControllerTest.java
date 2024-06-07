package me.jh.springstudy.controller;


import me.jh.springstudy.config.SecurityConfig;
import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.service.userservice.FindService;
import me.jh.springstudy.service.userservice.LoginService;
import me.jh.springstudy.service.userservice.SignupService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = UserController.class) // YourController는 실제 컨트롤러 클래스명으로 대체해야 합니다.
@Import(SecurityConfig.class)
//@AutoConfigureMockMvc
//@SpringBootTest(classes = MySpringBootApplication.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SignupService signupService;
	@MockBean
	private LoginService loginService;
	@MockBean
	private FindService findService;


	@Test
	public void testIndexForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("index"));
	}


	@Test
	public void testLoginForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/login"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("login/loginPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("signin"))
				.andExpect(MockMvcResultMatchers.model().attribute("signin", Matchers.instanceOf(User.class)));
	}

	@Test
	public void testLoginSuccess() throws Exception {
		String userId = "test123";
		String password = "test123";

		Mockito.when(loginService.loginCheck(userId, password)).thenReturn(true);

		mockMvc.perform(post("/loginCheck")
						.param("userId", userId)
						.param("password", password))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/"));
	}

	@Test
	public void testLoginFail() throws Exception {
		String userId = "test123";
		String password = "test";

		Mockito.when(loginService.loginCheck(userId, password)).thenReturn(false);

		mockMvc.perform(post("/loginCheck")
						.param("userId", userId)
						.param("password", password))
				.andExpect(status().isForbidden())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("아이디 혹은 비밀번호가 잘못되었습니다."));
	}

	@Test
	public void testSignupForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/signup"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("signup/signupPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("user"))
				.andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.instanceOf(User.class)));
	}

	@Test
	public void testSignupSuccess() throws Exception {
		String userId = "test1234";
		String name = "test";
		String password = "test1234";
		String phoneNum = "010-1212-1212";
		LocalDate birth = LocalDate.of(1999, 11, 11);
		String email = "test1234@test.com";

		User user = new User(userId, name, password, phoneNum, birth, email, LocalDateTime.now(), LocalDateTime.now());

		Mockito.doNothing().when(signupService).registerMember(user);

		mockMvc.perform(post("/signup")
						.flashAttr("user", user))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/signupSuccess"));

		Mockito.verify(signupService, Mockito.times(1)).registerMember(user);
	}

	@Test
	public void testIdCheck() throws Exception {
		String userId = "test1234";

		Mockito.when(signupService.isDuplicateId(userId)).thenReturn(false);
		//실제 메서드의 return값은 true라서 테스트 코드에는 false로 설정해야 중복x가 나옴

		mockMvc.perform(post("/idCheck")
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\"}"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("사용가능한 ID입니다."));

		Mockito.verify(signupService, Mockito.times(1)).isDuplicateId(userId);
	}

	@Test
	public void testIdCheckConflict() throws Exception {
		String userId = "test123";

		Mockito.when(signupService.isDuplicateId(userId)).thenReturn(true);

		mockMvc.perform(post("/idCheck")
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\"}"))
				.andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("이미 존재하는 아이디입니다."));
		Mockito.verify(signupService, Mockito.times(1)).isDuplicateId(userId);
	}

	@Test
	public void testEmailCheck() throws Exception {
		String email = "test@test.com";

		Mockito.when(signupService.isDuplicateEmail(email)).thenReturn(false);

		mockMvc.perform(post("/emailCheck")
						.contentType("application/json")
						.content("{\"email\":\"" + email + "\"}"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("사용가능한 이메일입니다."));
		Mockito.verify(signupService, Mockito.times(1)).isDuplicateEmail(email);
	}

	@Test
	public void testEmailCheckConflict() throws Exception {
		String email = "testfaild@test.com";

		Mockito.when(signupService.isDuplicateEmail(email)).thenReturn(true);

		mockMvc.perform(post("/emailCheck")
						.contentType("application/json")
						.content("{\"email\":\"" + email + "\"}"))
				.andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("해당정보로 가입한 사용자가 이미 있습니다."));
		Mockito.verify(signupService, Mockito.times(1)).isDuplicateEmail(email);
	}

	@Test
	public void testFindIdForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/findId"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/findIdPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("findUserId"))
				.andExpect(MockMvcResultMatchers.model().attribute("findUserId", Matchers.instanceOf(User.class)));
	}

	@Test
	public void testFindIdSuccess() throws Exception {
		String name = "test";
		String phoneNum = "010-1234-5678";
		String userId = "test1234";

		Mockito.when(findService.findId(name, phoneNum)).thenReturn(userId);

		mockMvc.perform(post("/findId")
						.contentType("application/json")
						.content("{\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId));

		Mockito.verify(findService, Mockito.times(2)).findId(name, phoneNum);//findId메서드가 왜 두번 호출 되었는지 확인
	}

	@Test
	public void testFindIdFail() throws Exception {
		String name = "test";
		String phoneNum = "010-1234-5678";

		Mockito.when(findService.findId(name, phoneNum)).thenReturn(null);

		mockMvc.perform(post("/findId")
						.contentType("application/json")
						.content("{\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
				.andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("해당 사용자 정보가 없습니다"));
		Mockito.verify(findService, Mockito.times(1)).findId(name, phoneNum);
	}

	@Test
	public void testFindPasswordForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/findPassword"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/findPasswordPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("findUserPassword"))
				.andExpect(MockMvcResultMatchers.model().attribute("findUserPassword", Matchers.instanceOf(User.class)));
	}

	@Test
	public void testFindPwSuccess() throws Exception {
		String userId = "test1234";
		String name = "test";
		String phoneNum = "010-1234-5678";

		Mockito.when(findService.validateUser(userId, name, phoneNum)).thenReturn(true);

		mockMvc.perform(post("/findPassword")
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/newPasswordPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("passwordChangeUser"));

		Mockito.verify(findService, Mockito.times(1)).validateUser(userId, name, phoneNum);
	}

	@Test
	public void testFindPwFail() throws Exception {
		String userId = "test1234";
		String name = "test";
		String phoneNum = "010-1234-5678";

		Mockito.when(findService.validateUser(userId, name, phoneNum)).thenReturn(false);

		mockMvc.perform(post("/findPassword")
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
				.andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("해당 사용자 정보가 없습니다"));

		Mockito.verify(findService, Mockito.times(1)).validateUser(userId, name, phoneNum);
	}

	@Test
	public void testNewPasswordChange() throws Exception {
		String userId = "test1234";
		String name = "test";
		String phoneNum = "010-1234-5678";
		String newPassword = "test1234";

		User user = new User();
		user.setUserId(userId);
		user.setName(name);
		user.setEmail(phoneNum);

		Mockito.when(findService.validateUser(userId, name, phoneNum)).thenReturn(true);
		Mockito.when(findService.changePassword(user, newPassword)).thenReturn(true);


		mockMvc.perform(post("/passwordChange")
						.contentType("application/json")
						.content("{\"newPassword\":\"" + newPassword + "\"}")
						.flashAttr("passwordChangeUser", user))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/passwordChangeSuccess"));

		Mockito.verify(findService, Mockito.times(1)).changePassword(user, newPassword);
	}

	@Test
	public void testSignupSuccessPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/signupSuccess"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("signup/signupSuccessPage"));
	}

	@Test
	public void testPasswordChangeSuccessPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/passwordChangeSuccess"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/passwordChangeSuccessPage"));
	}
}
// todo : 빠진부분 없는지 확인하기
