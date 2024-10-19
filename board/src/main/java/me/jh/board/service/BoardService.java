package me.jh.board.service;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import me.jh.board.dao.BoardDao;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public List<Board> getBoard(String tabName) {
		List<Board> boards = boardDao.findByTabName(tabName);
//		boards.forEach(board -> board.getComments().size());//todo: 강제로 comments를 초기화하는 꼼수. fetch= EAGER와 비슷하게 동작하니 고쳐놓기
		boards.forEach(board -> board.setComments(null));
		return boards;
	}

	public Board getBoardByTitle(String title) {
		Optional<Board> board = boardDao.findByTitle(title);
		return board.orElse(null);
	}




	public boolean updateBoard(Long id, Board board) {

		Board oldBoard = boardDao.findById(id).orElse(null);
		if (oldBoard == null) {
			return false;
		}

		oldBoard.setTitle(board.getTitle());
		oldBoard.setContent(board.getContent());
		oldBoard.setDate(board.getDate());
		oldBoard.setComments(board.getComments());

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

	@Transactional
	public Board getBoardDetail(Long boardId) {
		Optional<Board> board = boardDao.findById(boardId);
		if (board.isPresent()) {
		board.get().getComments().size();//todo: 강제로 comments를 초기화하는 꼼수. fetch= EAGER와 비슷하게 동작하니 고쳐놓기
		}
		return board.orElse(null);
	}

	@Transactional
	public List<Board> searchPosts(String query, String type) { //todo: 강제로 comments를 초기화하는 꼼수. fetch= EAGER와 비슷하게 동작하니 고쳐놓기
		List<Board> boards;

		switch (type) {
			case "title":
				boards = boardDao.findByTitleContaining(query);
				boards.forEach(board -> board.setComments(null));
				return	boards;
			case "content":
				boards = boardDao.findByContentContaining(query);
				boards.forEach(board -> board.setComments(null));
				return  boards;
			case "titleAndContent":
				boards = boardDao.findByTitleContainingOrContentContaining(query, query);
				boards.forEach(board -> board.setComments(null));
				return  boards;
			default:
				return new ArrayList<>(); // 잘못된 타입이 들어온 경우 빈 리스트 반환
		}
	}
}
