package me.jh.springstudy.controller.user;

import me.jh.board.dao.BoardDao;
import me.jh.board.dao.BoardDetailDaoImpl;
import me.jh.board.dao.BoardSearchDaoImpl;
import me.jh.board.dao.CommentDao;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.config.SecurityConfig;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.dao.auth.RefreshTokenDao;
import me.jh.springstudy.entity.User;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = PageController.class) // YourController는 실제 컨트롤러 클래스명으로 대체해야 합니다.
@Import(SecurityConfig.class)
@ActiveProfiles("test")
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
	@MockBean
	private BoardDao boardDao;
	@MockBean
	private BoardSearchDaoImpl boardSearchDao;
	@MockBean
	private CommentDao commentDao;
	@MockBean
	private RefreshTokenDao refreshTokenDao;
	@MockBean
	private BoardDetailDaoImpl boardDetailDao;


	@Test
	public void testLoginForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/login"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("login/loginPage"));
	}

	@Test
	public void testSignupForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/signup"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("signup/signupPage"));
	}

	@Test//아이디 찾기 폼
	public void testFindIdForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/findId"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/findIdPage"));
	}

	@Test// 비밀번호 찾기 폼
	public void testFindPasswordForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/findPassword"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/findPasswordPage"));
	}

	@Test//비밀번호 변경 폼 로드 성공
	public void testPasswordChangeFormSuccess() throws Exception {
		//세션에 저장된 사용자 정보가 있을때의 요청을 수행
		mockMvc.perform(MockMvcRequestBuilders.get("/passwordChange"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("finds/newPasswordPage"));
	}

//	@Test//비밀번호 변경 폼 리다이렉트
//	public void testPasswordChangeFormRedirect() throws Exception {
//		//세션을 가지지 않고 요청을 보내어 비밀번호 찾기 페이지로 리다이렉트 되는지 확인
//
//		// 세션에 저장된 사용자 정보가 없을 때
//		when(session.getAttribute("passwordChangeUser")).thenReturn(null);
//
//
//		// 세션 없이 요청을 수행
//		mockMvc.perform(MockMvcRequestBuilders.get("/passwordChange")
//						.cookie(new Cookie("passwordChanger", "test")))
//				.andExpect(status().is3xxRedirection()) // 3xx 리다이렉션 상태 코드 확인
//				.andExpect(redirectedUrl("/findPassword")); // 리다이렉트된 URL 확인
//	}

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
