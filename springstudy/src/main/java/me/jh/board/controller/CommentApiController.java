package me.jh.board.controller;

import me.jh.board.entity.Comment;
import me.jh.board.service.AuthService;
import me.jh.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment/api")
public class CommentApiController {//todo: 댓글 CUD API 구현


    private final CommentService commentService;
    private final AuthService authService;


    @Autowired
    public CommentApiController(CommentService commentService, AuthService authService) {
        this.commentService = commentService;
        this.authService = authService;
    }


    //create
    @PostMapping("/create-comment")
    @ResponseBody
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment) {
        String userId = authService.getAuthenticatedUserId();

        Long boardId = comment.getBoard().getId();
        if (!commentService.saveComment(boardId, comment, userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-comment")
    @ResponseBody//todo: 토큰과 함께 받아 사용자 검증 로직 추가하기
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        String userId = authService.getAuthenticatedUserId();

        Long commentId = comment.getId();
        if (!commentService.updateComment(commentId, comment, userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{commentId}")
    @ResponseBody
    public ResponseEntity<Comment> deleteComment(@PathVariable Long commentId) {
        String userId = authService.getAuthenticatedUserId();

        if (!commentService.deleteComment(commentId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}