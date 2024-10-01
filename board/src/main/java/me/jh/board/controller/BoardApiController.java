package me.jh.board.controller;

import me.jh.board.entity.Board;
import me.jh.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/board/api")
public class BoardApiController {

	@PostConstruct
	public void init() {
		System.out.println("BoardApiController Bean 생성");
	}

	private final BoardService boardService;

	@Autowired
	public BoardApiController(BoardService boardService) {
		this.boardService = boardService;
	}


	//CRUD에 대한 API 작성
	//Create
	@PostMapping("/create")
	@ResponseBody
	public ResponseEntity<Board> saveBoard(@RequestBody Board board) {
		boardService.saveBoard(board);
		return ResponseEntity.ok().build();
	}


	//Read
	@GetMapping("/generalBoard")
	@ResponseBody
	public ResponseEntity<List<Board>> getGeneralBoard() {
		List<Board> boards = boardService.getBoard("general");
		return ResponseEntity.ok(boards);
	}

	@GetMapping("/search_content")
	@ResponseBody
	public ResponseEntity<Board> getBoardByContent(@RequestBody Board board) {
		boardService.getBoardByTitle(board.getTitle());
		return ResponseEntity.ok().build();
	}

	//Update
	@PutMapping("/update")
	@ResponseBody
	public ResponseEntity<Board> updateBoard(@RequestBody Board board) {
		boardService.updateBoard(board);
		return ResponseEntity.ok().build();
	}


	//Delete
	@DeleteMapping("/delete/{boardId}")
	@ResponseBody
	public ResponseEntity<Board> deleteBoard(@PathVariable Long boardId) {
		boardService.deleteBoard(boardId);
		return ResponseEntity.noContent().build();
	}


	@GetMapping("/detail/{boardId}")
	@ResponseBody
	public ResponseEntity<Board> getBoardDetail(@PathVariable Long boardId) {
		Board board = boardService.getBoardDetail(boardId);
		return ResponseEntity.ok(board);
	}


	@GetMapping("/search")
	public ResponseEntity<List<Board>> searchPosts(@RequestParam("query") String query,
	                                               @RequestParam("type") String type) {

		List<Board> searchResults = boardService.searchPosts(query, type);
		return ResponseEntity.ok(searchResults);
	}

	@GetMapping("/memberBoard")
	@ResponseBody
	public ResponseEntity<List<Board>> getMemberBoard() {
		List<Board> boards = boardService.getBoard("member");
		return ResponseEntity.ok(boards);
	}
}
