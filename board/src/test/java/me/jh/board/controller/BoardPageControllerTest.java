package me.jh.board.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ContextConfiguration(classes = {BoardPageController.class})
@WebMvcTest(controllers = BoardPageController.class)
@AutoConfigureMockMvc(addFilters = false)// csrf 비활성화
public class BoardPageControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void boardIndexTest() throws Exception {
        mockMvc.perform(get("/board/page/boardIndex"))
                .andExpect(status().isOk());
    }

    @Test
    public void boardCreatePageTest() throws Exception {
        mockMvc.perform(get("/board/page/postWrite"))
                .andExpect(status().isOk());
    }

    @Test
    public void boardDetailPageTest() throws Exception {
        mockMvc.perform(get("/board/page/detail? postId= 1"))
                .andExpect(view().name("postDetail"))
                .andExpect(status().isOk());
    }
}
