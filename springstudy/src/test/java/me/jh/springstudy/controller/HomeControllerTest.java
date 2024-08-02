package me.jh.springstudy.controller;

import me.jh.springstudy.utils.auth.JwtProvider;
import me.jh.springstudy.config.SecurityConfig;
import me.jh.springstudy.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = HomeController.class) // YourController는 실제 컨트롤러 클래스명으로 대체해야 합니다.
@Import(SecurityConfig.class)
public class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@MockBean
	private UserDao userDao;

	@MockBean
	private UserDetailsService userDetailsService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwtProvider jwtProvider;

//	@MockBean
//	private HttpSession session;


	@Test
	public void testIndexForm() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("index"));
	}

	@Test
	public void testIndexFormWhenLogin() throws Exception {


//		when(session.getAttribute("userId")).thenReturn("testUser");
		//UserDetails 객체 생성
		UserDetails userDetails = User.builder()
				.username("test")
				.password(passwordEncoder.encode("test"))
				.roles("USER")
				.build();


		//UserDetailsService에서 loadUserByUsername 메서드를 호출하면 userDetails 객체를 반환하도록 설정
		mockMvc.perform(get("/")
//						.sessionAttr("userId", "testUser"))
						.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))//인증된 사용자 정보를 전달
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attribute("LoggedIn", true));
//				.andExpect(request().sessionAttribute("userId", "testUser"));
	}

	@Test
	public void testIndexFormWhenLogout() throws Exception {

		//로그인하지 않은 사용자의 경우

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attribute("LoggedIn", false))
				.andExpect(request().sessionAttributeDoesNotExist("userId"));
	}

}
