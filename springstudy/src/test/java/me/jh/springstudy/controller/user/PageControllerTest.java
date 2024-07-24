package me.jh.springstudy.controller.user;

import me.jh.springstudy.auth.JwtProvider;
import me.jh.springstudy.config.SecurityConfig;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entitiy.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = PageController.class) // YourController는 실제 컨트롤러 클래스명으로 대체해야 합니다.
@Import(SecurityConfig.class)
public class PageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private HttpSession session;
	@MockBean
	private UserDao userDao;
	@MockBean
	private UserDetailsService userDetailsService;
	@MockBean
	private JwtProvider jwtProvider;
	@MockBean
	private AuthenticationManager authenticationManager;


	@Test
	public void testLoginForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/login"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("login/loginPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("signin"))
				.andExpect(MockMvcResultMatchers.model().attribute("signin", Matchers.instanceOf(User.class)));
	}

	@Test
	public void testSignupForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/signup"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("signup/signupPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("user"))
				.andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.instanceOf(User.class)));
	}

	@Test//아이디 찾기 폼
	public void testFindIdForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/findId"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/findIdPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("findUserId"))
				.andExpect(MockMvcResultMatchers.model().attribute("findUserId", Matchers.instanceOf(User.class)));
	}

	@Test// 비밀번호 찾기 폼
	public void testFindPasswordForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/findPassword"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/findPasswordPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("findUserPassword"))
				.andExpect(MockMvcResultMatchers.model().attribute("findUserPassword", Matchers.instanceOf(User.class)));
	}

	@Test//비밀번호 변경 폼 로드 성공
	public void testPasswordChangeFormSuccess() throws Exception {
		//세션을 가지고 요청을 보내어 성공적으로 비밀번호 변경 페이지로 이동하는지 확인

		// 세션에 저장된 사용자 정보가 있을 때
		String userId = "test1234";
		String name = "test";
		String phoneNum = "010-1234-5678";

		//세션에 저장된 사용자 정보
		String passwordChanger = UUID.randomUUID().toString();
		User testUser = new User(userId, name, phoneNum, null, null, null, null, null);

		//세션에 저장된 사용자 정보를 가져오는 메서드
		when(session.getAttribute("passwordChangeUser" + passwordChanger)).thenReturn(testUser);

		//세션에 저장된 사용자 정보가 있을때의 요청을 수행
		mockMvc.perform(MockMvcRequestBuilders.get("/passwordChange")
						.cookie(new Cookie("passwordChanger", passwordChanger))
						.sessionAttr("passwordChangeUser" + passwordChanger, testUser))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/newPasswordPage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("passwordChangeUser"))
				.andExpect(MockMvcResultMatchers.model().attribute("passwordChangeUser", Matchers.instanceOf(User.class)));
	}

	@Test//비밀번호 변경 폼 리다이렉트
	public void testPasswordChangeFormRedirect() throws Exception {
		//세션을 가지지 않고 요청을 보내어 비밀번호 찾기 페이지로 리다이렉트 되는지 확인

		// 세션에 저장된 사용자 정보가 없을 때
		when(session.getAttribute("passwordChangeUser")).thenReturn(null);


		// 세션 없이 요청을 수행
		mockMvc.perform(MockMvcRequestBuilders.get("/passwordChange")
						.cookie(new Cookie("passwordChanger", "test")))
				.andExpect(status().is3xxRedirection()) // 3xx 리다이렉션 상태 코드 확인
				.andExpect(redirectedUrl("/findPassword")); // 리다이렉트된 URL 확인
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

	@Test
	public void testErrorPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/error"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("errors/error400"));
	}

}
