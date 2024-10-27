package me.jh.board.controller;

import me.jh.board.entity.Comment;
import me.jh.board.service.CommentService;
import me.jh.core.utils.auth.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment/api")
public class CommentApiController {//todo: 댓글 CUD API 구현


    private final CommentService commentService;
    private final JwtProvider jwtProvider;


    @Autowired
    public CommentApiController(CommentService commentService, JwtProvider jwtProvider) {
        this.commentService = commentService;
        this.jwtProvider = jwtProvider;
    }


    //create
    @PostMapping("/create-comment")
    @ResponseBody
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment) {
        Long boardId = comment.getBoard().getId();
        if (!commentService.saveComment(boardId, comment)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-comment")
    @ResponseBody//todo: 토큰과 함께 받아 사용자 검증 로직 추가하기
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {

        Long commentId = comment.getId();
        if (!commentService.updateComment(commentId, comment)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}