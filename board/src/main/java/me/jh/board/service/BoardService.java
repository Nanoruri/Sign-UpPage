package me.jh.board.service;


import me.jh.board.dao.BoardDao;
import me.jh.board.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

	@PostConstruct
	public void init() {
		System.out.println("BoardService Bean 등록 성공");
	}

	private final BoardDao boardDao;

	@Autowired
	public BoardService(BoardDao boardDao) {
		this.boardDao = boardDao;
	}


	public boolean saveBoard(Board board) {
		board.setTabName(board.getTabName());
		board.setDate(LocalDateTime.now());
		boardDao.save(board);
		return true;
	}

	public List<Board> getAllBoard() {
		return boardDao.findAll();
	}

	public Board getBoardByTitle(String title) {
		Optional<Board> board = boardDao.findByTitle(title);
		return board.orElse(null);
	}




	public boolean updateBoard(Board board) {

		Board oldBoard = boardDao.findByTitle(board.getTitle()).orElse(null);
		if (oldBoard == null) {
			return false;
		}

		oldBoard.setTitle(board.getTitle());
		oldBoard.setContent(board.getContent());
		oldBoard.setDate(board.getDate());

		boardDao.save(oldBoard);
		return true;
	}

	public boolean deleteBoard(Long id) {
		Board board = boardDao.findById(id).orElse(null);
		if (board == null) {
			return false;
		}
		boardDao.delete(board);
		return true;
	}

	public Board getBoardDetail(Long boardId) {
		return boardDao.findById(boardId).orElse(null);
	}

	public List<Board> searchPosts(String query, String type) {
		switch (type) {
			case "title":
				return boardDao.findByTitleContaining(query);
			case "content":
				return boardDao.findByContentContaining(query);
			case "titleAndContent":
				return boardDao.findByTitleContainingOrContentContaining(query, query);
			default:
				return new ArrayList<>(); // 잘못된 타입이 들어온 경우 빈 리스트 반환
		}
	}
}
