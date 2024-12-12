package me.jh.board.dao;

import me.jh.board.TestSpringContext;
import me.jh.board.entity.Board;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = TestSpringContext.class)
@ActiveProfiles("boardTest")
@ExtendWith({SpringExtension.class})
@DataJpaTest
public class BoardDaoTest {

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private BoardSearchDaoImpl boardSearchDaoImpl;

    @Mock
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "Test Tab", "testUser");
        boardDao.save(board);
    }

    @Test
    public void testSaveBoard() {
        // given
        board = new Board(2L, "Test Title", "Test Content", LocalDateTime.now(), "Test Tab", "testUser");

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


