package me.jh.board.dao;

import me.jh.board.TestSpringContext;
import me.jh.board.entity.Board;
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

import static org.assertj.core.api.Assertions.assertThat;


@ContextConfiguration(classes = TestSpringContext.class)
@ActiveProfiles("boardTest")
@ExtendWith({SpringExtension.class})
@DataJpaTest
public class BoardSearchDaoImplTest {

    @Autowired
    private BoardSearchDaoImpl boardSearchDaoImpl;

    @Autowired
    private BoardDao boardDao;


    @Mock
    private Board board;


    @Test
    public void testFindByTitleContaining() {
        // given
        String type = "title";
        Board board1 = new Board(1L, "Spring Boot Guide", "Content 1", LocalDateTime.now(), "testTab", "testUser");
        Board board2 = new Board(2L, "Spring Data JPA", "Content 2", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);
        boardDao.save(board2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardDao.searchPosts("Boot", type, pageable, "testTab");

        // then
        assertThat(foundBoards).hasSize(1);
        assertThat(foundBoards.getContent())
                .extracting(Board::getTitle)
                .contains(board1.getTitle());
    }

    @Test
    public void testFindByContentContaining() {
        // given
        String type = "content";
        Board board1 = new Board(1L, "Title 1", "Spring Boot Content", LocalDateTime.now(), "testTab", "testUser");
        Board board2 = new Board(2L, "Title 2", "Spring Data JPA Content", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);
        boardDao.save(board2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardDao.searchPosts("Spring", type, pageable, "testTab");

        // then
        assertThat(foundBoards).hasSize(2);
        assertThat(foundBoards.getContent()).extracting(Board::getContent)
                .contains(board1.getContent())
                .contains(board2.getContent());
    }

    @Test
    public void testFindByTitleContainingOrContentContaining() {
        // given
        String type = "titleAndContent";
        Board board1 = new Board(1L, "Spring Boot Guide", "Content 1", LocalDateTime.now(), "testTab", "testUser");
        Board board2 = new Board(2L, "Title 2", "Spring Data JPA Content", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);
        boardDao.save(board2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardDao.searchPosts("Spring", type, pageable, "testTab");

        // then
        assertThat(foundBoards).hasSize(2);
        assertThat(foundBoards.getContent()).extracting(Board::getContent)
                .contains(board1.getContent())
                .contains(board2.getContent());
    }


    @Test
    public void searchPosts_withNullQuery_returnsAllBoardsInTab() {
        // given
        String type = "title";
        Board board1 = new Board(1L, "Spring Boot Guide", "Content 1", LocalDateTime.now(), "testTab", "testUser");
        Board board2 = new Board(2L, "Spring Data JPA", "Content 2", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);
        boardDao.save(board2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardSearchDaoImpl.searchPosts(null, type, pageable, "testTab");

        // then
    }

    @Test
    public void searchPosts_withEmptyQuery_returnsAllBoardsInTab() {
        // given
        String type = "title";
        Board board1 = new Board(1L, "Spring Boot Guide", "Content 1", LocalDateTime.now(), "testTab", "testUser");
        Board board2 = new Board(2L, "Spring Data JPA", "Content 2", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);
        boardDao.save(board2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardSearchDaoImpl.searchPosts("", type, pageable, "testTab");

        // then
        assertThat(foundBoards.getContent()).extracting(Board::getTitle)
                .contains(board1.getTitle(), board2.getTitle());

    }


    @Test
    public void searchPosts_withNullType_returnsEmptyPage() {
        // given
        Board board1 = new Board(1L, "Spring Boot Guide", "Content 1", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardSearchDaoImpl.searchPosts("Spring", null, pageable, "testTab");

        // then
        assertThat(foundBoards.getContent()).extracting(Board::getTitle)
                .contains(board1.getTitle());
    }

    @Test
    public void searchPosts_withEmptyStringType_returnsEmptyPage() {
        // given
        String type = "";
        Board board1 = new Board(1L, "Spring Boot Guide", "Content 1", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardSearchDaoImpl.searchPosts("Spring", type, pageable, "testTab");

        // then
        assertThat(foundBoards.getContent()).extracting(Board::getTitle)
                .contains(board1.getTitle());

    }

    @Test
    public void searchPosts_withInvalidType_returnsEmptyPage() {
        // given
        String type = "invalidType";
        Board board1 = new Board(1L, "Spring Boot Guide", "Content 1", LocalDateTime.now(), "testTab", "testUser");
        boardDao.save(board1);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> foundBoards = boardSearchDaoImpl.searchPosts("Spring", type, pageable, "testTab");

        // then
        assertThat(foundBoards.getContent()).extracting(Board::getTitle)
                .contains(board1.getTitle());
    }
}
