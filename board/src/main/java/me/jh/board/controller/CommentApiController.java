package me.jh.board.controller;

import me.jh.board.entity.Comment;
import me.jh.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment/api")
public class CommentApiController {//todo: 댓글 CUD API 구현


    private final CommentService commentService;


    @Autowired
    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
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
