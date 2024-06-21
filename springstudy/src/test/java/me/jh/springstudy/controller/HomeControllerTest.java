package me.jh.springstudy.controller;

import me.jh.springstudy.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = HomeController.class) // YourController는 실제 컨트롤러 클래스명으로 대체해야 합니다.
@Import(SecurityConfig.class)
public class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private HttpSession session;


	@Test
	public void testIndexForm() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("index"));
	}

	@Test
	public void testIndexFormWhenLogin() throws Exception {

		when(session.getAttribute("userId")).thenReturn("testUser");

		mockMvc.perform(get("/")
						.sessionAttr("userId", "testUser"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attribute("LoggedIn", true))
				.andExpect(request().sessionAttribute("userId", "testUser"));
	}

	@Test
	public void testIndexFormWhenLogout() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attribute("LoggedIn", false))
				.andExpect(request().sessionAttributeDoesNotExist("userId"));
	}

}
