package me.jh.board.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jh.board.TestSpringContext;
import me.jh.board.entity.Board;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = MySpringBootApplication.class)
@ActiveProfiles("test")
@ExtendWith({SpringExtension.class})
@Import(ObjectMapper.class)
@DataJpaTest
public class BoardDaoTest {

    @Autowired
    private BoardDao boardDao;
    @Autowired
    private UserDao userDao;


    @Mock
    private Board board;


    @BeforeEach
    public void setUp() {
        User user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(), "test@testEmail.com", LocalDateTime.now(), LocalDateTime.now(), "USER");
        userDao.save(user);
        board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "Test Tab", user);
        boardDao.save(board);
    }

    @Test
    public void testSaveBoard() {
        // given

        User user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(), "test@testEmail.com", LocalDateTime.now(), LocalDateTime.now(), "USER");
        userDao.save(user);
        board = new Board(2L, "Test Title", "Test Content", LocalDateTime.now(), "Test Tab", user);

        // when
        Board savedBoard = boardDao.save(board);

        // then
        assertThat(savedBoard).isNotNull();
        assertThat(savedBoard.getTitle()).isEqualTo("Test Title");
    }


    @Test
    public void testFindById() {


        Optional<Board> foundBoard = boardDao.findById(board.getId());

        // then
        assertThat(foundBoard).isPresent();
        assertThat(foundBoard.get().getTitle()).isEqualTo("Test Title");
    }

    @Test
    public void testFindByTitle() {


        // when
        Optional<Board> foundBoard = boardDao.findByTitle("Test Title");

        // then
        assertThat(foundBoard).isPresent();
        assertThat(foundBoard.get().getTitle()).isEqualTo("Test Title");
    }

    @Test
    public void testDeleteBoard() {


        // when
        boardDao.deleteById(board.getId());
        Optional<Board> deletedBoard = boardDao.findById(board.getId());

        // then
        assertThat(deletedBoard).isNotPresent();
    }


}


