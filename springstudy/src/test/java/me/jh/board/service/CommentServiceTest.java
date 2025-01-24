package me.jh.board.service;

import me.jh.board.dao.BoardDao;
import me.jh.board.dao.CommentDao;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {


    @Mock
    private CommentDao commentDao;
    @Mock
    private BoardDao boardDao;
    @Mock
    private UserDao userDao;

    @InjectMocks
    private CommentService commentService;


    @Test
//게시글에 대해서 댓글을 저장
    void testSaveComment() {
        Long boardId = 1L;
        String userId = "testLoginUser";
        User user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(),"test@testEmail.com", LocalDateTime.now(),LocalDateTime.now(), "USER");
        Board board = new Board(boardId, "title", "content", LocalDateTime.now(), "testTab", user);
        Comment comment = new Comment();
        comment.setContent("testCommentContent");

        when(boardDao.findById(boardId)).thenReturn(Optional.of(board));
        when(commentDao.save(any(Comment.class))).thenReturn(comment);
        when(userDao.findById(userId)).thenReturn(Optional.of(user));

        boolean result = commentService.saveComment(boardId, comment, userId);

        assertTrue(result);
        verify(boardDao, times(1)).findById(boardId);
        verify(commentDao, times(1)).save(any(Comment.class));
    }

    @Test
    void testUpdateComment_SuccessfulUpdate() {
        Long commentId = 1L;
        User user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(),"test@testEmail.com", LocalDateTime.now(),LocalDateTime.now(), "USER");
        Comment newComment = new Comment();
        newComment.setContent("newContent");

        Comment oldComment = new Comment();
        oldComment.setCreator(user);
        oldComment.setContent("oldContent");

        when(commentDao.findById(commentId)).thenReturn(Optional.of(oldComment));
        when(commentDao.save(any(Comment.class))).thenReturn(oldComment);

        boolean result = commentService.updateComment(commentId, newComment, user.getUserId());

        assertTrue(result);
        verify(commentDao, times(1)).findById(commentId);
        verify(commentDao, times(1)).save(any(Comment.class));
    }

    @Test
    void testUpdateComment_CommentNotFound() {
        Long commentId = 1L;
        String userId = "testUser";
        Comment newComment = new Comment();
        newComment.setContent("newContent");

        when(commentDao.findById(commentId)).thenReturn(Optional.empty());

        boolean result = commentService.updateComment(commentId, newComment, userId);

        assertFalse(result);
        verify(commentDao, times(1)).findById(commentId);
        verify(commentDao, times(0)).save(any(Comment.class));
    }

    @Test
    void testUpdateComment_UserNotAuthorized() {
        Long commentId = 1L;
        User user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(),"test@testEmail.com", LocalDateTime.now(),LocalDateTime.now(), "USER");
        String otherUserId = "otherUser";
        Comment newComment = new Comment();
        newComment.setContent("newContent");

        Comment oldComment = new Comment();
        oldComment.setCreator(user);
        oldComment.setContent("oldContent");

        when(commentDao.findById(commentId)).thenReturn(Optional.of(oldComment));

        boolean result = commentService.updateComment(commentId, newComment, otherUserId);

        assertFalse(result);
        verify(commentDao, times(1)).findById(commentId);
        verify(commentDao, times(0)).save(any(Comment.class));
    }


    @Test
    void testDeleteComment_Successful() {
        Long commentId = 1L;
        User user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(),"test@testEmail.com", LocalDateTime.now(),LocalDateTime.now(), "USER");
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setCreator(user);

        when(commentDao.findById(commentId)).thenReturn(Optional.of(comment));

        boolean result = commentService.deleteComment(commentId, user.getUserId());

        assertTrue(result);
        verify(commentDao, times(1)).findById(commentId);
        verify(commentDao, times(1)).delete(comment);
    }

    @Test
    void testDeleteComment_CommentNotFound() {
        Long commentId = 1L;
        String userId = "testUser";

        when(commentDao.findById(commentId)).thenReturn(Optional.empty());

        boolean result = commentService.deleteComment(commentId, userId);

        assertFalse(result);
        verify(commentDao, times(1)).findById(commentId);
        verify(commentDao, times(0)).delete(any(Comment.class));
    }

    @Test
    void testDeleteComment_UserNotMatch() {
        Long commentId = 1L;
        User user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(),"test@testEmail.com", LocalDateTime.now(),LocalDateTime.now(), "USER");
        String otherUserId = "otherUser";
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setCreator(user);

        when(commentDao.findById(commentId)).thenReturn(Optional.of(comment));

        boolean result = commentService.deleteComment(commentId, otherUserId);

        assertFalse(result);
        verify(commentDao, times(1)).findById(commentId);
        verify(commentDao, times(0)).delete(any(Comment.class));
    }
}
