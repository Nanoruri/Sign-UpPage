package me.jh.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import me.jh.board.entity.Comment;
import me.jh.board.service.AuthService;
import me.jh.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment/api")
public class CommentApiController {//todo: 댓글 CUD API 구현


    private final CommentService commentService;
    private final AuthService authService;


    @Autowired
    public CommentApiController(CommentService commentService, AuthService authService) {
        this.commentService = commentService;
        this.authService = authService;
    }


    @Operation(summary = "댓글 생성", description = "새 댓글을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 생성 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    @PostMapping

    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment) {
        String userId = authService.getAuthenticatedUserId();

        Long boardId = comment.getBoard().getId();
        if (!commentService.saveComment(boardId, comment, userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
    @PutMapping
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        String userId = authService.getAuthenticatedUserId();

        Long commentId = comment.getId();
        if (!commentService.updateComment(commentId, comment, userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "댓글 삭제 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Long commentId) {
        String userId = authService.getAuthenticatedUserId();

        if (!commentService.deleteComment(commentId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}