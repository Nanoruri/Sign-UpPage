package me.jh.board.dao;

import me.jh.board.TestSpringContext;
import me.jh.board.entity.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = TestSpringContext.class)
@ActiveProfiles("boardTest")
@ExtendWith({SpringExtension.class})
@DataJpaTest
public class BoardDaoTest {

    @Autowired
    private BoardDao boardDao;

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


    @Test
    public void testFindByTitleContaining() {
        // given
        Board board1 = new Board(1L, "Spring Boot Guide", "Content 1", LocalDateTime.now(), "testTab", "testUser");
        Board board2 = new Board(2L, "Spring Data JPA", "Content 2", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);
        boardDao.save(board2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardDao.findByTitleContaining("Spring",pageable);

        // then
        assertThat(foundBoards).hasSize(2);
    }

    @Test
    public void testFindByContentContaining() {
        // given
        Board board1 = new Board(1L, "Title 1", "Spring Boot Content", LocalDateTime.now(), "testTab", "testUser");
        Board board2 = new Board(2L, "Title 2", "Spring Data JPA Content", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);
        boardDao.save(board2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardDao.findByContentContaining("Spring",pageable);

        // then
        assertThat(foundBoards).hasSize(2);
    }

    @Test
    public void testFindByTitleContainingOrContentContaining() {
        // given
        Board board1 = new Board(1L, "Spring Boot Guide", "Content 1", LocalDateTime.now(), "testTab", "testUser");
        Board board2 = new Board(2L, "Title 2", "Spring Data JPA Content", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);
        boardDao.save(board2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardDao.findByTitleContainingOrContentContaining("Spring", "Spring",pageable);

        // then
        assertThat(foundBoards).hasSize(2);
    }

}


