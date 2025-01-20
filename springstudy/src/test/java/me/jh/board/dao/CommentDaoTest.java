package me.jh.board.dao;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.jh.board.TestSpringContext;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import me.jh.springstudy.MySpringBootApplication;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = MySpringBootApplication.class)
@ActiveProfiles("test")
@ExtendWith({SpringExtension.class})
@Import(ObjectMapper.class)
@DataJpaTest
public class CommentDaoTest {

    @Autowired
    private CommentDao commentDao;

    @Mock
    private Comment comment;
    @Mock
    private Board board;
    @Mock
    private User user;
    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(), "test@testEmail.com", LocalDateTime.now(), LocalDateTime.now(), "USER");
        userDao.save(user);
        board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "Test Tab", user);

        comment = new Comment(0, "Test Comment", LocalDateTime.now(), LocalDateTime.now(), board, "testCommentUser");

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
