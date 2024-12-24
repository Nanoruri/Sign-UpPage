package me.jh.board.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jh.board.dao.BoardDao;
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

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = MySpringBootApplication.class)
@ActiveProfiles("test")
@DataJpaTest
@Transactional
@Import(ObjectMapper.class)
public class BoardTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoardDao boardDao;
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
    public void testSaveBoard() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);

        // when
        Board savedBoard = entityManager.persistAndFlush(board);

        // then
        assertThat(savedBoard).isNotNull();
        assertThat(savedBoard.getTitle()).isEqualTo("Test Title");
    }

    @Test
    public void testFindById() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        entityManager.persistAndFlush(board);

        // when
        Optional<Board> foundBoard = boardDao.findById(board.getId());

        // then
        assertThat(foundBoard).isPresent();
        assertThat(foundBoard.get().getTitle()).isEqualTo("Test Title");
    }

    @Test
    public void testFindByTitle() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        entityManager.persistAndFlush(board);

        // when
        Optional<Board> foundBoard = boardDao.findByTitle("Test Title");

        // then
        assertThat(foundBoard).isPresent();
        assertThat(foundBoard.get().getTitle()).isEqualTo("Test Title");
    }

    @Test
    public void testDeleteBoard() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        entityManager.persistAndFlush(board);

        // when
        boardDao.deleteById(board.getId());
        Optional<Board> deletedBoard = boardDao.findById(board.getId());

        // then
        assertThat(deletedBoard).isNotPresent();
    }


    @Test
    void toObject_returnsCorrectMap_whenUserIsCreator() {
        Board board = new Board(1, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        String userId = user.getUserId();

        Map<String, Object> result = board.toObject(userId);

        assertEquals(board, result.get("board"));
        assertTrue((Boolean) result.get("isCreator"));
        assertEquals(userId, result.get("currentUserId"));
    }

    @Test
    void toObject_returnsCorrectMap_whenUserIsNotCreator() {
        Board board = new Board(1, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        String userId = "anotherUser";

        Map<String, Object> result = board.toObject(userId);

        assertEquals(board, result.get("board"));
        assertFalse((Boolean) result.get("isCreator"));
        assertEquals(userId, result.get("currentUserId"));
    }

    @Test
    void toObject_returnsCorrectMap_whenUserIdIsNull() {
        Board board = new Board(1, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);

        Map<String, Object> result = board.toObject(null);

        assertEquals(board, result.get("board"));
        assertFalse((Boolean) result.get("isCreator"));
        assertNull(result.get("currentUserId"));
    }


}
