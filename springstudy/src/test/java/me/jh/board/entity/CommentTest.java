package me.jh.board.entity;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.jh.board.dao.CommentDao;
import me.jh.springstudy.MySpringBootApplication;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = MySpringBootApplication.class)
@ActiveProfiles("test")
@DataJpaTest
@Import(ObjectMapper.class)
public class CommentTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(), "test@testEmail.com", LocalDateTime.now(), LocalDateTime.now(), "USER");
        userDao.save(user);
        }

    @Test
    public void testSaveComment() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        entityManager.persist(board);

        Comment comment = new Comment(0, "Test Comment", LocalDateTime.now(), LocalDateTime.now(), board, "testCommentUser");

        // when
        Comment savedComment = entityManager.persistAndFlush(comment);

        // then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("Test Comment");
        assertThat(savedComment.getBoard()).isEqualTo(board);
    }

    @Test
    public void testFindById() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        entityManager.persistAndFlush(board);

        Comment comment = new Comment(0, "Test Comment", LocalDateTime.now(), LocalDateTime.now(), board, "testCommentUser");
        entityManager.persistAndFlush(comment);

        // when
        Optional<Comment> foundComment = commentDao.findById(comment.getId());

        // then
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("Test Comment");
    }

    @Test
    public void testDeleteComment() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        entityManager.persistAndFlush(board);

        Comment comment = new Comment(0, "Test Comment", LocalDateTime.now(), LocalDateTime.now(), board, "testCommentUser"); // Ensure ID is null
        entityManager.persistAndFlush(comment);

        // when
        commentDao.deleteById(comment.getId());
        Optional<Comment> deletedComment = commentDao.findById(comment.getId());

        // then
        assertThat(deletedComment).isNotPresent();
    }
}
