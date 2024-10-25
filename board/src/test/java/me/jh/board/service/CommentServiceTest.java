package me.jh.board.service;

import me.jh.board.dao.BoardDao;
import me.jh.board.dao.CommentDao;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {


    @Mock
    private CommentDao commentDao;
    @Mock
    private BoardDao boardDao;

    @InjectMocks
    private CommentService commentService;


    @Test
//게시글에 대해서 댓글을 저장
    void testSaveComment() {
        Long boardId = 1L;
        Board board = new Board(boardId, "title", "content", LocalDateTime.now(), "testTab", "testUser");
        Comment comment = new Comment();
        comment.setContent("testCommentContent");

        when(boardDao.findById(boardId)).thenReturn(Optional.of(board));
        when(commentDao.save(any(Comment.class))).thenReturn(comment);

        boolean result = commentService.saveComment(boardId, comment);

        assertTrue(result);
        verify(boardDao, times(1)).findById(boardId);
        verify(commentDao, times(1)).save(any(Comment.class));
    }
}
