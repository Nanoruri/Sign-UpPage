package me.jh.board.dao;


import me.jh.board.TestSpringContext;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = TestSpringContext.class)
@ActiveProfiles("boardTest")
@ExtendWith({SpringExtension.class})
@DataJpaTest
public class CommentDaoTest {

    @Autowired
    private CommentDao commentDao;

    @Mock
    private Comment comment;
    @Mock
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "Test Tab", "testUser");

        comment = new Comment(0, "Test Comment", LocalDateTime.now() ,board);

    }

    @Test
    public void testCreateComment() {
        Comment savedComment = commentDao.save(comment);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    public void testReadComment() {
        Comment savedComment = commentDao.save(comment);
        Comment foundComment = commentDao.findById(savedComment.getId()).orElse(null);
        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getContent()).isEqualTo(comment.getContent());
    }

    @Test
    public void testUpdateComment() {
        Comment savedComment = commentDao.save(comment);
        savedComment.setContent("Updated Comment");
        Comment updatedComment = commentDao.save(savedComment);
        assertThat(updatedComment.getContent()).isEqualTo("Updated Comment");
    }

    @Test
    public void testDeleteComment() {
        Comment savedComment = commentDao.save(comment);
        Long commentId = savedComment.getId();
        commentDao.deleteById(commentId);
        Comment deletedComment = commentDao.findById(commentId).orElse(null);
        assertThat(deletedComment).isNull();
    }

}
