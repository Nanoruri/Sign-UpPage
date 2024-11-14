package me.jh.board.controller;

import me.jh.board.entity.Board;
import me.jh.board.service.AuthService;
import me.jh.board.service.BoardService;
import me.jh.board.service.FileUploadService;
import me.jh.core.utils.auth.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/board/api")// todo: 로직중 인증이 필요한 로직들은 인증용 컨트롤러에 넣기.
public class BoardApiController {


    @PostConstruct
    public void init() {
        System.out.println("BoardApiController Bean 생성");
    }

    private final BoardService boardService;
    private final FileUploadService fileUploadService;
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @Autowired
    public BoardApiController(BoardService boardService, FileUploadService fileUploadService, JwtProvider jwtProvider, AuthService authService) {
        this.boardService = boardService;
        this.fileUploadService = fileUploadService;
        this.jwtProvider = jwtProvider;
        this.authService = authService;
    }


    //CRUD에 대한 API 작성
    //Create
    // 토큰 내 정보를 이용하여 작성자 정보를 함께 저장하게 하기
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Board> saveBoard(@RequestBody Board board) {

        String userId = authService.getAuthenticatedUserId();
        if (userId == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boardService.saveBoard(userId,board);
        return ResponseEntity.ok().build();
    }


    //Read
    @GetMapping("/generalBoard")
    @ResponseBody
    public ResponseEntity<Page<Board>> getGeneralBoard(Pageable pageable) {
        Page<Board> boards = boardService.getBoard("general", pageable);
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
    public ResponseEntity<Board> deleteBoard(@PathVariable Long boardId) {
        String userId = authService.getAuthenticatedUserId();
        if (!boardService.deleteBoard(boardId,userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/detail/{boardId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBoardDetail(@PathVariable Long boardId,
                                                              @RequestHeader(value = "Authorization", required = false) String token) {

        String userId = null;

        if (token != null) {
            String substringToken = token.substring(7); // "Bearer " 이후의 토큰 부분만 추출
            userId = jwtProvider.getUserIdFromToken(substringToken);
        }
        Board board = boardService.getBoardDetail(boardId);

        boolean isCreator = board.getCreator().equals(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("board", board);
        response.put("isCreator", isCreator);
        response.put("currentUserId", userId);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<Board>> searchPosts(@RequestParam("query") String query,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("tabName") String tabName, Pageable pageable) {

        Page<Board> searchResults = boardService.searchPosts(query, type, pageable, tabName);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/memberBoard")
    @ResponseBody
    public ResponseEntity<Page<Board>> getMemberBoard(Pageable pageable) {
        Page<Board> boards = boardService.getBoard("member", pageable);
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
    public ResponseEntity<Board> findBoard(@PathVariable Long boardId) {
        String userId = authService.getAuthenticatedUserId();

        Board board = boardService.findBoard(userId, boardId);
        if (board == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(board);
    }
}