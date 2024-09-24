package me.jh.board.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {BoardPageController.class})
@WebMvcTest(controllers = BoardPageController.class)
@AutoConfigureMockMvc(addFilters = false)// csrf 비활성화
public class BoardPageControllerTest {

	@Autowired
	private MockMvc mockMvc;


	@Test
	public void board() throws Exception {
		mockMvc.perform(get("/board/page/boardIndex"))
				.andExpect(status().isOk());
	}

	@Test
	public void board2() throws Exception {
		mockMvc.perform(get("/board/page/postWrite"))
				.andExpect(status().isOk());
	}

}
