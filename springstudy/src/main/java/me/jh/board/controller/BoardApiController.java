package me.jh.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "게시글 생성", description = "생성할 게시글의 정보를 받아 게시글을 생성")
    @ApiResponse(responseCode = "200", description = "게시글 생성 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음")
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


    @Operation(summary = "일반 게시판 조회", description = "표시할 페이징 정보를 받아 일반 게시판의 게시글 목록을 조회")
    @ApiResponse(responseCode = "200", description = "일반 게시판 게시글을 페이징처리하여 반환")
    @GetMapping("/generalBoard")
    @ResponseBody
    public ResponseEntity<Page<BoardNoCommentDTO>> getGeneralBoard(Pageable pageable) {
        Page<BoardNoCommentDTO> boards = boardService.getBoard("general", pageable);
        return ResponseEntity.ok(boards);//TODO: board.comments필드 JSON 직렬화 이슈 수정 필요
    }

    @Operation(summary = "게시글 수정", description = "게시글ID와 수정할 게시글 정보를 받아 게시글을 수정")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음")
    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<Board> updateBoard(@PathVariable Long id, @RequestBody Board board) {
        String userId = authService.getAuthenticatedUserId();
        if (!boardService.updateBoard(id, userId, board)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "게시글 삭제", description = "주어진 게시글ID에 기반하여 게시글을 삭제")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @ApiResponse(responseCode = "404", description = "게시글 없음")
    @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    @DeleteMapping("/delete/{boardId}")
    @ResponseBody
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        String userId = authService.getAuthenticatedUserId();
        try {
            boardService.deleteBoard(boardId, userId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보를 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공 및 게시글과 댓글 정보 반환")
    @ApiResponse(responseCode = "401", description = "인증되지 않음")
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

    @Operation(summary = "게시글 검색", description = "쿼리와 필터 파라미터를 사용하여 게시글을 검색")
    @ApiResponse(responseCode = "200", description = "게시글 결과 반환")
    @GetMapping("/search")
    public ResponseEntity<Page<BoardBasicDTO>> searchPosts(@RequestParam("query") String query,
                                                           @RequestParam("type") String type,
                                                           @RequestParam("tabName") String tabName, Pageable pageable) {

        Page<BoardBasicDTO> searchResults = boardService.searchPosts(query, type, pageable, tabName);
        return ResponseEntity.ok(searchResults);
    }

    @Operation(summary = "회원 전용 게시판 조회", description = "표시할 페이징 정보를 받아 회원용 게시판의 게시글 목록을 조회")
    @ApiResponse(responseCode = "200", description = "회원 전용 게시판 게시글을 페이징 처리하여 반환")
    @GetMapping("/memberBoard")
    @ResponseBody
    public ResponseEntity<Page<BoardNoCommentDTO>> getMemberBoard(Pageable pageable) {
        Page<BoardNoCommentDTO> boards = boardService.getBoard("member", pageable);
        return ResponseEntity.ok(boards);
    }

    @Operation(summary = "이미지 업로드", description = "이미지 파일을 업로드하고 해당 이미지 URL을 반환")
    @ApiResponse(responseCode = "200", description = "이미지 URL 반환")
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

    @Operation(summary = "게시글 수정에 사용할 게시글 정보 조회", description = "주어진 게시글 ID에 대한 기본 정보를 조회")
    @ApiResponse(responseCode = "200", description = " 게시글 정보 반환")
    @ApiResponse(responseCode = "403", description = "접근 권한 없음")
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