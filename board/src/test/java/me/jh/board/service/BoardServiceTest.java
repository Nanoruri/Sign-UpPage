package me.jh.board.service;

import me.jh.board.dao.BoardDao;
import me.jh.board.entity.Board;
import me.jh.board.service.BoardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

	@Mock
	private BoardDao boardDao;
	@InjectMocks
	private BoardService boardService;


	@Test//Create
	public void save() {

		long id = 1L;
		String title = "title";
		String content = "content";
		LocalDateTime date = LocalDateTime.now();

		Board post = new Board(id, title, content, date);

		when(boardDao.save(post)).thenReturn(post);

		boolean result = boardService.saveBoard(post);

		verify(boardDao).save(post);
	}


	/*
	 * 모든 게시글 조회 테스트
	 */
	@Test//Read
	public void findAll() {

		LocalDateTime now = LocalDateTime.now();

		List<Board> boardList = List.of(
				new Board(1L, "title1", "content1", now),
				new Board(2L, "title2", "content2", now),
				new Board(3L, "title3", "content3", now)
		);

		when(boardDao.findAll()).thenReturn(boardList);

		List<Board> result = boardService.getAllBoard();

		verify(boardDao).findAll();
	}

	/**
	 * 특정 제목의 게시글 조회 테스트
	 */
	@Test//Read2
	public void findByTitle() {

		LocalDateTime now = LocalDateTime.now();

		// 기존 게시글 리스트
		List<Board> boardList = List.of(
				new Board(1L, "title1", "content1", now),
				new Board(2L, "title2", "content2", now),
				new Board(3L, "title3", "content3", now)
		);

		// 찾아올 게시글의 제목
		String title = "title1";

		// 특정 제목의 게시글을 찾아오기
		when(boardDao.findByTitle(title)).thenReturn(Optional.of(boardList.get(0)));

		// 게시글 찾아오기
		Board result = boardService.getBoardByTitle(title);

		// 검증
		verify(boardDao).findByTitle(title);

		// Optional로 찾아서 만약 여러게시글이 있을 경우(Optional은 하나만을 반환하니 false일 경우) List로 변환해서 반환하도록 하기
	}


	/**
	 * 게시글 수정 테스트
	 */
	@Test//Update
	public void update() {

		LocalDateTime now = LocalDateTime.now();

		//기존 게시글 리스트
		List<Board> boardList = List.of(
				new Board(1L, "title1", "content1", now),
				new Board(2L, "title2", "content2", now),
				new Board(3L, "title3", "content3", now)
		);

		// 업데이트할 게시글
		Board existingBoard = boardList.get(0);
		Board updatedBoard = new Board(1L, "title1", "updatedContent", now);

		// 게시글을 찾아오기
		when(boardDao.findByTitle("title1")).thenReturn(Optional.of(existingBoard));
		when(boardDao.save(existingBoard)).thenReturn(existingBoard);

		// 게시글 수정
		boolean result = boardService.updateBoard(updatedBoard);

		// 검증
		verify(boardDao).findByTitle("title1");
		verify(boardDao).save(existingBoard);

		assertTrue(result);
		assertEquals("updatedContent", existingBoard.getContent());
	}

	/**
	 * 특정 게시글 삭제 테스트
	 */
	@Test
	public void delete() {
		LocalDateTime now = LocalDateTime.now();

		//기존 게시글 리스트
		List<Board> boardList = List.of(
				new Board(1L, "title1", "content1", now),
				new Board(2L, "title2", "content2", now),
				new Board(3L, "title3", "content3", now)
		);


		// 특정 조건으로 게시글을 찾기 위한 변수
		String title = "title1";

		// 삭제할 게시글을 찾아오기
		when(boardDao.findByTitle(title)).thenReturn(Optional.of(boardList.get(0)));

		// 가져온 게시글을 삭제
		doNothing().when(boardDao).delete(boardList.get(0));

		// 게시글 삭제
		boolean result = boardService.deleteBoard(title);

		verify(boardDao).findByTitle(title);
	}
}