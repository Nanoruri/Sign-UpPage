package me.jh.springstudy.controller;

import me.jh.board.dao.BoardDao;
import me.jh.board.dao.BoardDetailDaoImpl;
import me.jh.board.dao.BoardSearchDaoImpl;
import me.jh.board.dao.CommentDao;
import me.jh.springstudy.config.SecurityConfig;
import me.jh.springstudy.dao.UserDao;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.dao.UserPropertiesDaoImpl;
import me.jh.springstudy.dao.auth.RefreshTokenDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

	@MockBean
	private BoardDao boardDao;
	@MockBean
	private BoardSearchDaoImpl boardSearchDao;
	@MockBean
	private UserPropertiesDaoImpl userPropertiesDao;
	@MockBean
	private CommentDao commentDao;
	@MockBean
	private RefreshTokenDao refreshTokenDao;
	@MockBean
	private BoardDetailDaoImpl boardDetailDao;

//	@MockBean
//	private HttpSession session;


	@Test
	public void testIndexForm() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("index"));
	}

}
