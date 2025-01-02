package me.jh.board.controller;


import me.jh.board.dao.BoardDao;
import me.jh.board.dao.BoardSearchDaoImpl;
import me.jh.board.dao.CommentDao;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import me.jh.board.service.CommentService;
import me.jh.core.utils.auth.JwtProvider;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.dao.auth.RefreshTokenDao;
import me.jh.springstudy.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@WebMvcTest(controllers = CommentApiController.class)
@AutoConfigureMockMvc(addFilters = false)// csrf 비활성화
public class CommentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private UserDao userDao;
    @MockBean
    private BoardDao boardDao;
    @MockBean
    private BoardSearchDaoImpl boardSearchDao;

    @MockBean
    private CommentDao commentDao;
    @MockBean
    private RefreshTokenDao refreshTokenDao;
    @Mock
    private User user;

    @BeforeEach
    void setup() throws IOException {
        mockMvc = standaloneSetup(new CommentApiController(commentService, jwtProvider)).build();

        user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(), "test@testEmail.com", LocalDateTime.now(), LocalDateTime.now(), "USER");

    }


    @Test
    public void testSaveCommentReturnsOk() throws Exception {
        String token = "Bearer token";
        String userId = user.getUserId();
        Board board = new Board(1L, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        Comment comment = new Comment(1L, "Test Comment", LocalDateTime.now(), board, userId);

        when(jwtProvider.getUserIdFromToken(token.substring(7))).thenReturn(userId);
        when(commentService.saveComment(eq(board.getId()), any(Comment.class), eq(userId))).thenReturn(true);

        mockMvc.perform(post("/comment/api/create-comment")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test Comment\",\"board\":{\"id\":1}}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveCommentReturnsNotFound() throws Exception {
        String token = "Bearer token";
        String userId = user.getUserId();
        Board board = new Board(1L, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        Comment comment = new Comment(1L, "Test Comment", LocalDateTime.now(), board, userId);

        when(jwtProvider.getUserIdFromToken(token.substring(7))).thenReturn(userId);
        when(commentService.saveComment(eq(board.getId()), any(Comment.class), eq(userId))).thenReturn(false);

        mockMvc.perform(post("/comment/api/create-comment")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test Comment\",\"board\":{\"id\":1}}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCommentUpdateSuccessful() throws Exception {
        String token = "token";
        String userId = user.getUserId();
        long commentId = 1L;

        Board board = new Board(1L, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);

        when(jwtProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(commentService.updateComment(eq(commentId), any(Comment.class), eq(userId))).thenReturn(true);

        mockMvc.perform(put("/comment/api/update-comment")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"content\":\"Test Comment\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCommentFail() throws Exception {
        String token = "token";
        String userId = user.getUserId();
        long commentId = 1L;
        String commentUserId = "testCommentUser";

        Board board = new Board(1L, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);


        when(jwtProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(commentService.updateComment(eq(commentId), any(Comment.class), eq(commentUserId))).thenReturn(false);

        mockMvc.perform(put("/comment/api/update-comment")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"content\":\"Test Comment\"}"))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteCommentReturnsSuccess() throws Exception {
        String token = "Bearer token";
        String userId = user.getUserId();
        Long commentId = 1L;

        when(jwtProvider.getUserIdFromToken(token.substring(7))).thenReturn(userId);
        when(commentService.deleteComment(commentId, userId)).thenReturn(true);

        mockMvc.perform(delete("/comment/api/delete/{commentId}", commentId)
                        .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCommentReturnsForbidden() throws Exception {
        String token = "Bearer token";
        String userId = user.getUserId();
        Long commentId = 1L;

        when(jwtProvider.getUserIdFromToken(token.substring(7))).thenReturn(userId);
        when(commentService.deleteComment(commentId, userId)).thenReturn(false);

        mockMvc.perform(delete("/comment/api/delete/{commentId}", commentId)
                        .header("Authorization", token))
                .andExpect(status().isForbidden());
    }


}
