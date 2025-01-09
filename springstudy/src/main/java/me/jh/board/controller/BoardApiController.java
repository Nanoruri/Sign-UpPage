package me.jh.board.controller;

import me.jh.board.dto.board.BoardBasicDTO;
import me.jh.board.dto.board.BoardDTO;
import me.jh.board.dto.board.BoardNoCommentDTO;
import me.jh.board.entity.Board;
import me.jh.board.service.AuthService;
import me.jh.board.service.BoardService;
import me.jh.board.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/board/api")// todo: 로직중 인증이 필요한 로직들은 인증용 컨트롤러에 넣기.
public class BoardApiController {

    private final static Logger log = LoggerFactory.getLogger(BoardApiController.class);


    @PostConstruct
    public void init() {
        log.info("BoardApiController Bean 등록 성공");
    }

    private final BoardService boardService;
    private final FileUploadService fileUploadService;
    private final AuthService authService;

    @Autowired
    public BoardApiController(BoardService boardService, FileUploadService fileUploadService, AuthService authService) {
        this.boardService = boardService;
        this.fileUploadService = fileUploadService;

        this.authService = authService;
    }


    //CRUD에 대한 API 작성
    //Create
    // 토큰 내 정보를 이용하여 작성자 정보를 함께 저장하게 하기
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Board> saveBoard(@RequestBody Board board) {

        String userId = authService.getAuthenticatedUserId();
        boardService.saveBoard(userId, board);
        if (userId == null || !boardService.saveBoard(userId, board)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }


    //Read
    @GetMapping("/generalBoard")
    @ResponseBody
    public ResponseEntity<Page<BoardNoCommentDTO>> getGeneralBoard(Pageable pageable) {
        Page<BoardNoCommentDTO> boards = boardService.getBoard("general", pageable);
        return ResponseEntity.ok(boards);//TODO: board.comments필드 JSON 직렬화 이슈 수정 필요
    }

    //Update
    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<Board> updateBoard(@PathVariable Long id, @RequestBody Board board) {
        String userId = authService.getAuthenticatedUserId();
        if (!boardService.updateBoard(id, userId, board)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }


    //Delete
    @DeleteMapping("/delete/{boardId}")
    @ResponseBody
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        String userId = authService.getAuthenticatedUserId();
        try {
            boardService.deleteBoard(boardId, userId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // 게시글 없음
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage()); // 권한 없음
        }
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/detail/{boardId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBoardDetail(@PathVariable Long boardId) {


        String userId = authService.getAuthenticatedUserIdOrNull();

        BoardDTO board = boardService.getBoardDetail(boardId);

        if (!boardService.isUserAuthorized(board, userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> response = board.toObject(userId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<BoardBasicDTO>> searchPosts(@RequestParam("query") String query,
                                                           @RequestParam("type") String type,
                                                           @RequestParam("tabName") String tabName, Pageable pageable) {

        Page<BoardBasicDTO> searchResults = boardService.searchPosts(query, type, pageable, tabName);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/memberBoard")
    @ResponseBody
    public ResponseEntity<Page<BoardNoCommentDTO>> getMemberBoard(Pageable pageable) {
        Page<BoardNoCommentDTO> boards = boardService.getBoard("member", pageable);
        return ResponseEntity.ok(boards);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String imageUrl = fileUploadService.saveImage(file);
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //todo: boardId를 받아서 해당 게시글을 찾아주는 API 구현
    //todo: Token 내 아이디와 게시글 작성자 아이디가 일치하는지 확인 후 게시글을 찾아 반환
    @GetMapping("/getBoardInfo/{boardId}")
    @ResponseBody
    public ResponseEntity<BoardBasicDTO> findBoard(@PathVariable Long boardId) {
        String userId = authService.getAuthenticatedUserId();

        BoardBasicDTO board = boardService.findBoard(userId, boardId);
        if (board == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(board);
    }
}