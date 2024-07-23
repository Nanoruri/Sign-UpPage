package me.jh.springstudy.controller.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.jh.springstudy.auth.JwtGenerator;
import me.jh.springstudy.auth.JwtProvider;
import me.jh.springstudy.config.SecurityConfig;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.dto.JWToken;
import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.exception.user.UserErrorType;
import me.jh.springstudy.exception.user.UserException;
import me.jh.springstudy.service.user.AuthenticationService;
import me.jh.springstudy.service.user.FindService;
import me.jh.springstudy.service.user.SignupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
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
	private AuthenticationService authenticationService;
	@MockBean
	private FindService findService;
	@MockBean
	private HttpSession session;
	@MockBean
	private UserDetailsService userDetailsService;
	@MockBean
	private Authentication authentication;
	@MockBean
	private UserDao userDao;
	@MockBean
	private JwtProvider jwtProvider;
	@MockBean
	private JwtGenerator jwtGenerator;
	@MockBean
	private AuthenticationManager authenticationManager;


	@Captor
	private ArgumentCaptor<User> userCaptor;

	@Mock
	private User user;
	@Mock
	private Cookie cookie;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setUp() {
		user = new User("test", "testName", "hashedPassword", "010-1234-5678",
				LocalDate.of(1990, 11, 27), "test@test.com", LocalDateTime.now(), LocalDateTime.now());
	}

	@Test
	public void testLoginSuccess() throws Exception {
		String userId = "validUser";
		String password = "validPassword";


		when(authenticationService.authenticateAndGenerateToken(userId, password))
				.thenReturn(new JWToken("Bearer ", "accessToken", "refreshToken"));


		mockMvc.perform(post("/loginCheck")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"userId\":\"" + userId + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").value("accessToken"))
				.andExpect(jsonPath("$.refreshToken").value("refreshToken"));
	}

	@Test
	public void testLoginFail() throws Exception {
		String userId = "invalidUser";
		String password = "invalidPassword";

		when(authenticationService.authenticateAndGenerateToken(userId, password))
				.thenThrow(new BadCredentialsException("Authentication failed"));

		mockMvc.perform(post("/loginCheck")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"userId\":\"" + userId + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isUnauthorized())
				.andExpect(content().string("인증 실패"));
	}

	@Test
	public void testLoginFailWithEmptyId() throws Exception {
		String userId = "";
		String password = "validPassword";

		when(authenticationService.authenticateAndGenerateToken(userId, password))
				.thenThrow(new UserException(UserErrorType.ID_NULL));

		mockMvc.perform(post("/loginCheck")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"userId\":\"" + userId + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("아이디를 입력해주세요."));
	}

	@Test
	public void testLoginFailWithEmptyPassword() throws Exception {
		String userId = "validUser";
		String password = "";

		when(authenticationService.authenticateAndGenerateToken(userId, password))
				.thenThrow(new UserException(UserErrorType.PASSWORD_NULL));

		mockMvc.perform(post("/loginCheck")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"userId\":\"" + userId + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("비밀번호를 입력해주세요."));
	}


	@Test
	public void testLogout() throws Exception {
		mockMvc.perform(post("/logout"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/"))
				.andExpect(request().sessionAttributeDoesNotExist("userId"));
	}

	@Test
	public void testRefreshToken() throws Exception {
		String refreshToken = "validToken";
		JWToken jwToken = new JWToken("Bearer ", "newAccessToken", "newRefreshToken");

		when(jwtProvider.validateToken(refreshToken)).thenReturn(true);
		when(jwtProvider.getUserIdFromToken(refreshToken)).thenReturn("test");
		when(authenticationService.authenticateAndGenerateToken("test")).thenReturn(jwToken);

		mockMvc.perform(post("/refresh")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"refreshToken\":\"" + refreshToken + "\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").value(jwToken.getAccessToken()))
				.andExpect(jsonPath("$.refreshToken").value(jwToken.getRefreshToken()));
		verify(jwtProvider, times(1)).validateToken(refreshToken);
		verify(jwtProvider, times(1)).getUserIdFromToken(refreshToken);
		verify(authenticationService, times(1)).authenticateAndGenerateToken("test");
	}

	@Test
	public void testRefreshTokenWithInvalidToken() throws Exception {
		String refreshToken = "invalidToken";

		when(jwtProvider.validateToken(refreshToken)).thenReturn(false);

		mockMvc.perform(post("/refresh")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"refreshToken\":\"" + refreshToken + "\"}"))
				.andExpect(status().isUnauthorized())
				.andExpect(content().string("토큰 인증 실패"));
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

		doNothing().when(signupService).registerMember(user);

		mockMvc.perform(post("/signup")
						.flashAttr("user", user))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/signupSuccess"));

		verify(signupService, times(1)).registerMember(user);
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
				.andExpect(jsonPath("$").value("사용가능한 ID입니다."));

		verify(signupService, times(1)).isDuplicateId(userId);
	}

	@Test
	public void testIdCheckConflict() throws Exception {
		String userId = "test123";

		when(signupService.isDuplicateId(userId)).thenReturn(true);

		mockMvc.perform(post("/idCheck")
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\"}"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$").value("이미 존재하는 아이디입니다."));
		verify(signupService, times(1)).isDuplicateId(userId);
	}

	@Test
	public void testEmailCheck() throws Exception {
		String email = "test@test.com";

		when(signupService.isDuplicateEmail(email)).thenReturn(false);

		mockMvc.perform(post("/emailCheck")
						.contentType("application/json")
						.content("{\"email\":\"" + email + "\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value("사용가능한 이메일입니다."));
		verify(signupService, times(1)).isDuplicateEmail(email);
	}

	@Test
	public void testEmailCheckConflict() throws Exception {
		String email = "testfaild@test.com";

		when(signupService.isDuplicateEmail(email)).thenReturn(true);

		mockMvc.perform(post("/emailCheck")
						.contentType("application/json")
						.content("{\"email\":\"" + email + "\"}"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$").value("해당정보로 가입한 사용자가 이미 있습니다."));
		verify(signupService, times(1)).isDuplicateEmail(email);
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
				.andExpect(jsonPath("$.userId").value(userId));

		verify(findService, times(2)).findId(name, phoneNum);//findId메서드가 왜 두번 호출 되었는지 확인
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
				.andExpect(jsonPath("$").value("해당 사용자 정보가 없습니다"));
		verify(findService, times(1)).findId(name, phoneNum);
	}


	@Test
	public void testFindPwSuccess() throws Exception {
		// 입력값 설정
		User testUser = new User();
		testUser.setUserId("validUser");
		testUser.setName("Valid");
		testUser.setPhoneNum("987654321");

		//validateUser의 반환값 설정.API의 매개변수로 받는 user 객체와 테스트에 사용되는 user 객체가 달라 any()를 사용
		when(findService.validateUser(any(User.class))).thenReturn(true);

		mockMvc.perform(post("/findPassword")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testUser)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(testUser.getUserId()))
				.andExpect(jsonPath("$.name").value(testUser.getName()))
				.andExpect(jsonPath("$.phoneNum").value(testUser.getPhoneNum()))
				.andExpect(cookie().exists("passwordChanger"))
				.andReturn().getResponse();

		verify(findService, times(1)).validateUser(any(User.class));

		verify(findService).validateUser(userCaptor.capture());
		User capturedUser = userCaptor.getValue();

		// 캡처된 User 객체와 테스트에서 사용한 User 객체의 내용이 동일한지 검증
		assertEquals(testUser.getUserId(), capturedUser.getUserId());
		assertEquals(testUser.getName(), capturedUser.getName());
		assertEquals(testUser.getPhoneNum(), capturedUser.getPhoneNum());
	}

	@Test//비밀번호 찾기 실패
	public void testFindPwFailure() throws Exception {
		String userId = "test";
		String name = "testName";
		String phoneNum = "010-1234-5678";
		User testUser = new User(userId, name, null, phoneNum, null, null, null, null);

		// 사용자 정보가 없을 때
		when(findService.validateUser(any(User.class))).thenReturn(false);

		// 사용자 정보가 없을 때의 요청을 수행
		mockMvc.perform(post("/findPassword")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testUser)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").value("해당 사용자 정보가 없습니다"));

		// 메서드가 실행되었는지 확인
		verify(findService, times(1)).validateUser(any(User.class));

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
		when(findService.validateUser(testUser)).thenReturn(true);
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
		verify(findService, times(1)).changePassword(testUser, newPassword);
		assertNull(session.getAttribute("passwordChangeUser123"));//세션에 저장된 사용자 정보가 삭제되었는지 확인

	}


	@Test//쿠키 누락으로 비밀번호 변경 실패
	public void testNewPasswordChangeWithoutCookie() throws Exception {
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
		when(findService.validateUser(testUser)).thenReturn(true);
		when(findService.changePassword(testUser, newPassword)).thenReturn(true);


		//post요청을 보내는 부분
		mockMvc.perform(post("/passwordChange") // 쿠키 없이 보내면?
						.sessionAttr("passwordChangeUser" + passwordChangeUser, testUser)
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}")
						.content("{\"newPassword\":\"" + newPassword + "\"}")
						.flashAttr("passwordChangeUser", testUser))
				.andExpect(status().isBadRequest());

		//메서드가 실행되었는지 확인
		verify(findService, times(0)).changePassword(testUser, newPassword);
		// 쿠키 이름과 값이 적절히 삭제되었는지 확인
	}

	@Test//잘못된 쿠키값으로 비밀번호 변경 실패 잘못된 쿠키값으로 비밀번호 변경 실패
	public void testNewPasswordChangeWornCookie() throws Exception {
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
		when(findService.validateUser(testUser)).thenReturn(true);
		when(findService.changePassword(testUser, newPassword)).thenReturn(true);


		//post요청을 보내는 부분
		mockMvc.perform(post("/passwordChange").cookie(new Cookie("no", "wrongValue"))
						.sessionAttr("passwordChangeUser" + passwordChangeUser, testUser)
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}")
						.content("{\"newPassword\":\"" + newPassword + "\"}")
						.flashAttr("passwordChangeUser", testUser))
				.andExpect(status().isBadRequest());

		//메서드가 실행되었는지 확인
		verify(findService, times(0)).changePassword(testUser, newPassword);

	}


	@Test//잘못된 쿠키값으로 비밀번호 변경 실패 잘못된 쿠키값으로 비밀번호 변경 실패
	public void testNewPasswordChangeWornCookieValue() throws Exception {
		String userId = "test1234";
		String name = "test";
		String phoneNum = "010-1234-5678";
		String newPassword = "test1234";

		//세션에 저장된 사용자 정보
		String passwordChangeUser = UUID.randomUUID().toString();
		String worngValue = "wrong";
		User testUser = new User(userId, name, phoneNum, null, null, null, null, null);

		//when = 해당 메서드가 실행됬을때의 리턴값을 설정

		//세션에 저장된 사용자 정보를 가져오는 메서드
		when(session.getAttribute("passwordChangeUser" + passwordChangeUser)).thenReturn(testUser);

		//validateUser메서드, changePassword메서드가 실행됬을때의 리턴값을 설정

		when(findService.changePassword(testUser, newPassword)).thenReturn(false);


		//post요청을 보내는 부분
		mockMvc.perform(post("/passwordChange").cookie(new Cookie("passwordChanger", worngValue))
						.sessionAttr("passwordChangeUser" + passwordChangeUser, testUser)
						.contentType("application/json")
						.content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}")
						.content("{\"newPassword\":\"" + newPassword + "\"}")
						.flashAttr("passwordChangeUser", testUser))
				.andExpect(status().isNotFound());

		//메서드가 실행되었는지 확인
		verify(findService, times(0)).changePassword(testUser, newPassword);
		// 쿠키 이름과 값이 적절히 삭제되었는지 확인
	}


//	@Test
//	public void testNewPasswordChangeFailure() throws Exception {
//		String userId = "test1234";
//		String name = "test";
//		String phoneNum = "010-1234-5678";
//		String newPassword = "test1234";
//
//		//세션에 저장된 사용자 정보
//		String passwordChangeUser = UUID.randomUUID().toString();
//		String worngValue = "wrong";
//		User testUser = new User(userId, name, phoneNum, null, null, null, null, null);
//
//		//when = 해당 메서드가 실행됬을때의 리턴값을 설정
//
//		//세션에 저장된 사용자 정보를 가져오는 메서드
//		when(session.getAttribute("passwordChangeUser" + passwordChangeUser)).thenReturn(testUser);
//
//		//validateUser메서드, changePassword메서드가 실행됬을때의 리턴값을 설정
//
//		when(findService.changePassword(testUser, newPassword)).thenReturn(true);
//
//		when(cookie.getName()).thenReturn("FaiureCookie");
//
//
//		//post요청을 보내는 부분
//		mockMvc.perform(post("/passwordChange").cookie(new Cookie("passwordChanger", worngValue))
//						.sessionAttr("passwordChangeUser" + passwordChangeUser, testUser)
//						.contentType("application/json")
//						.content("{\"userId\":\"" + userId + "\",\"name\":\"" + name + "\",\"phoneNum\":\"" + phoneNum + "\"}")
//						.content("{\"newPassword\":\"" + newPassword + "\"}")
//						.flashAttr("passwordChangeUser", testUser))
//				.andExpect(status().isNotFound());
//
//		//메서드가 실행되었는지 확인
//		Mockito.verify(findService, Mockito.times(0)).changePassword(testUser, newPassword);
//		// 쿠키 이름과 값이 적절히 삭제되었는지 확인
//	}
//
}

// todo : 빠진부분 없는지 확인하기
