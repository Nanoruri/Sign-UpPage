package me.jh.springstudy.controller.user;


import me.jh.springstudy.config.SecurityConfig;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.service.userservice.CustomUserDetailsService;
import me.jh.springstudy.service.userservice.FindService;
import me.jh.springstudy.service.userservice.LoginService;
import me.jh.springstudy.service.userservice.SignupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = ApiController.class) // YourController는 실제 컨트롤러 클래스명으로 대체해야 합니다.
@Import(SecurityConfig.class)
//@AutoConfigureMockMvc
//@SpringBootTest(classes = MySpringBootApplication.class)
public class ApiControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@MockBean
	private SignupService signupService;
	@MockBean
	private LoginService loginService;
	@MockBean
	private FindService findService;
	@MockBean
	private HttpSession session;
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	@MockBean
	private Authentication authentication;
	@MockBean
	private UserDao userDao;


	@Test
	public void testLoginSuccess() throws Exception {
		String userId = "test123";
		String password = "test123";

//		when(loginService.loginCheck(userId, password)).thenReturn(true);

		//사용자 정보가 있을 때
		when(customUserDetailsService.loadUserByUsername(userId)).
				thenReturn(org.springframework.security.core.userdetails.User.builder()
						.username(userId)
						.password(passwordEncoder.encode(password))
						.roles("USER")
						.build());

		mockMvc.perform(post("/loginCheck")
						.param("userId", userId)
						.param("password", password))
				.andExpect(status().isFound())//로그인에 성공하면 시큐리티는 302 리다이렉트를 반환
				.andExpect(redirectedUrl("/"))//로그인에 성공하면 메인 페이지로 리다이렉트
				.andExpect(request().sessionAttribute("SPRING_SECURITY_CONTEXT", notNullValue()))
				.andExpect(authenticated()
						.withAuthenticationName(userId)//  인증된 사용자 이름이 userId인지 확인
						.withRoles("USER"));//  인증된 사용자 권한이 ROLE_USER인지 확인
	}

	@Test
	public void testLoginFail() throws Exception {
		String userId = "test123";
		String password = "test";

		//사용자 정보가 없을 때
		when(customUserDetailsService.loadUserByUsername(userId)).
				thenReturn(null);

		mockMvc.perform(post("/loginCheck")
						.param("userId", userId)
						.param("password", password))
				.andExpect(status().isFound())//로그인에 실패하면 시큐리티는 302 리다이렉트를 반환
				.andExpect(redirectedUrl("/login"))//로그인에 실패하면 로그인 페이지로 리다이렉트
				.andExpect(request().sessionAttributeDoesNotExist("SPRING_SECURITY_CONTEXT"));
//				.andExpect(MockMvcResultMatchers.jsonPath("$").value("아이디 혹은 비밀번호가 잘못되었습니다."));//
	}

	@Test
	public void testLogout() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/logout"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/"))
				.andExpect(request().sessionAttributeDoesNotExist("userId"));
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

		when(signupService.isDuplicateId(userId)).thenReturn(false);
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

		when(signupService.isDuplicateId(userId)).thenReturn(true);

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

		when(signupService.isDuplicateEmail(email)).thenReturn(false);

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

		when(signupService.isDuplicateEmail(email)).thenReturn(true);

		mockMvc.perform(post("/emailCheck")
						.contentType("application/json")
						.content("{\"email\":\"" + email + "\"}"))
				.andExpect(status().isConflict())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("해당정보로 가입한 사용자가 이미 있습니다."));
		Mockito.verify(signupService, Mockito.times(1)).isDuplicateEmail(email);
	}


	@Test//아이디 찾기 성공
	public void testFindIdSuccess() throws Exception {
		String name = "test";
		String phoneNum = "010-1234-5678";
		String userId = "test1234";

		when(findService.findId(name, phoneNum)).thenReturn(userId);

		mockMvc.perform(post("/findId")
						.contentType("application/json")
						.content("{\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId));

		Mockito.verify(findService, Mockito.times(2)).findId(name, phoneNum);//findId메서드가 왜 두번 호출 되었는지 확인
	}

	@Test//아이디 찾기 실패
	public void testFindIdFail() throws Exception {
		String name = "test";
		String phoneNum = "010-1234-5678";

		when(findService.findId(name, phoneNum)).thenReturn(null);

		mockMvc.perform(post("/findId")
						.contentType("application/json")
						.content("{\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
				.andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("해당 사용자 정보가 없습니다"));
		Mockito.verify(findService, Mockito.times(1)).findId(name, phoneNum);
	}


	@Test//비밀번호 찾기 성공
	public void testFindPwSuccess() throws Exception {
		// 입력값 설정
		String userId = "test1234";
		String name = "test";
		String phoneNum = "010-1234-5678";
		String passwordChanger = "fixed-uuid-for-testing";  // 테스트용 UUID

		User testUser = new User(userId, name, phoneNum, null, null, null, null, null);
		when(findService.validateUser(userId, name, phoneNum)).thenReturn(true);

		//TODO : UUID 생성 과정에서 테스트 통과하게끔 하기(UUID.randomUUID()이 static 메서드라서 테스트가 어려움)

		// UUID.randomUUID()를 고정된 UUID로 변경
//		try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {
//			mockedUUID.when(UUID::randomUUID).thenReturn(UUID.fromString(passwordChanger));
//		}
		// 세션에 사용자 정보를 저장
//		session.setAttribute("passwordChangeUser" + passwordChanger, testUser);


		mockMvc.perform(post("/findPassword")
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(userId))
				.andExpect(jsonPath("$.name").value(name))
				.andExpect(jsonPath("$.phoneNum").value(phoneNum))
				.andExpect(cookie().exists("passwordChanger"));//쿠키가 생성되었는지 확인

		Mockito.verify(findService, Mockito.times(1)).validateUser(userId, name, phoneNum);
//		assertInstanceOf(User.class, session.getAttribute("passwordChangeUser" + passwordChanger));
//
//		User sessionUser = (User) mockSession.getAttribute("passwordChangeUser" + passwordChanger);
//		assertNotNull(sessionUser);
//		assertEquals(testUser.getUserId(), sessionUser.getUserId());
//		assertEquals(testUser.getName(), sessionUser.getName());
//		assertEquals(testUser.getPhoneNum(), sessionUser.getPhoneNum());

	}

	@Test//비밀번호 찾기 실패
	public void testFindPwFailure() throws Exception {
		String userId = "test1234";
		String name = "test";
		String phoneNum = "010-1234-5678";

		// 사용자 정보가 없을 때
		when(findService.validateUser(userId, name, phoneNum)).thenReturn(false);

		// 사용자 정보가 없을 때의 요청을 수행
		mockMvc.perform(post("/findPassword")
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}"))
				.andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$").value("해당 사용자 정보가 없습니다"));

		// 메서드가 실행되었는지 확인
		Mockito.verify(findService, Mockito.times(1)).validateUser(userId, name, phoneNum);
	}


	@Test//새 비밀번호로 변경 성공
	public void testNewPasswordChangeSuccess() throws Exception {
		String userId = "test1234";
		String name = "test";
		String phoneNum = "010-1234-5678";
		String newPassword = "test1234";

		//세션에 저장된 사용자 정보
		String passwordChangeUser = UUID.randomUUID().toString();
		User testUser = new User(userId, name, phoneNum, null, null, null, null, null);

		//when = 해당 메서드가 실행됬을때의 리턴값을 설정

		//세션에 저장된 사용자 정보를 가져오는 메서드
		when(session.getAttribute("passwordChangeUser" + passwordChangeUser)).thenReturn(testUser);

		//validateUser메서드, changePassword메서드가 실행됬을때의 리턴값을 설정
		when(findService.validateUser(userId, name, phoneNum)).thenReturn(true);
		when(findService.changePassword(testUser, newPassword)).thenReturn(true);


		//post요청을 보내는 부분
		mockMvc.perform(post("/passwordChange").cookie(new Cookie("passwordChanger", passwordChangeUser))
						.sessionAttr("passwordChangeUser" + passwordChangeUser, testUser)
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}")
						.content("{\"newPassword\":\"" + newPassword + "\"}")
						.flashAttr("passwordChangeUser", testUser))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.messege").value("비밀번호 변경 성공"));

		//메서드가 실행되었는지 확인
		Mockito.verify(findService, Mockito.times(1)).changePassword(testUser, newPassword);
		assertNull(session.getAttribute("passwordChangeUser123"));//세션에 저장된 사용자 정보가 삭제되었는지 확인
	}

}
// todo : 빠진부분 없는지 확인하기