package me.jh.board.entity;

import me.jh.board.dao.BoardDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("boardTest")
@DataJpaTest
@Transactional
public class BoardTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BoardDao boardDao;

	@Test
	public void testSaveBoard() {
		// given
		Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now());

		// when
		Board savedBoard = entityManager.persistAndFlush(board);

		// then
		assertThat(savedBoard).isNotNull();
		assertThat(savedBoard.getTitle()).isEqualTo("Test Title");
	}

	@Test
	public void testFindById() {
		// given
		Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now());
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
		Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now());
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
		Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now());
		entityManager.persistAndFlush(board);

		// when
		boardDao.deleteById(board.getId());
		Optional<Board> deletedBoard = boardDao.findById(board.getId());

		// then
		assertThat(deletedBoard).isNotPresent();
	}


}
