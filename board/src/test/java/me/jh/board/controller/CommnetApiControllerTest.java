package me.jh.board.controller;


import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import me.jh.board.service.CommentService;
import me.jh.core.utils.auth.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@WebMvcTest(controllers = CommentApiController.class)
@AutoConfigureMockMvc(addFilters = false)// csrf 비활성화
public class CommnetApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;
    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    void setup() throws IOException {
        mockMvc = standaloneSetup(new CommentApiController(commentService, jwtProvider)).build();

    }


    @Test
    public void testSaveCommentReturnsOk() throws Exception {
        Board board = new Board(1L, "Test Title", "Test Content", LocalDateTime.now(), "testTab", "testUser");

        when(commentService.saveComment(eq(board.getId()), any(Comment.class))).thenReturn(true);

        mockMvc.perform(post("/comment/api/create-comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test Comment\",\"board\":{\"id\":1}}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveCommentReturnsNotFound() throws Exception {
        Board board = new Board(1L, "Test Title", "Test Content", LocalDateTime.now(), "testTab", "testUser");

        when(commentService.saveComment(eq(board.getId()), any(Comment.class))).thenReturn(false);

        mockMvc.perform(post("/comment/api/create-comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test Comment\",\"board\":{\"id\":1}}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCommentUpdateSuccessful() throws Exception {
        String token = "token";
        String userId = "testUser";
        long commentId = 1L;

        Board board = new Board(1L, "Test Title", "Test Content", LocalDateTime.now(), "testTab", "testUser");

        when(jwtProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(commentService.updateComment(eq(commentId), any(Comment.class))).thenReturn(true);

        mockMvc.perform(put("/comment/api/update-comment")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"content\":\"Test Comment\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCommentFail() throws Exception {
        String token = "token";
        String userId = "testUser";
        long commentId = 1L;

        Board board = new Board(1L, "Test Title", "Test Content", LocalDateTime.now(), "testTab", "testUser");


        when(jwtProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(commentService.updateComment(eq(commentId), any(Comment.class))).thenReturn(false);

        mockMvc.perform(put("/comment/api/update-comment")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"content\":\"Test Comment\"}"))
                .andExpect(status().isNotFound());
    }


}
