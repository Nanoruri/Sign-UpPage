package me.jh.board.service;

import me.jh.board.dao.BoardDao;
import me.jh.board.dao.CommentDao;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class CommentService {//TODO: 댓글 D 구현


    private final CommentDao commentDao;
    private final BoardDao boardDao;

    @Autowired
    public CommentService(CommentDao commentDao, BoardDao boardDao) {
        this.commentDao = commentDao;
        this.boardDao = boardDao;
    }

    @Transactional
    public boolean saveComment(Long boardId, Comment comment, String userId) {
        Board board = boardDao.findById(boardId).orElse(null);

        Comment saveComment = new Comment();
        saveComment.setContent(comment.getContent());
        saveComment.setDate(LocalDateTime.now());
        saveComment.setCreator(userId);
        saveComment.setBoard(board);

        commentDao.save(saveComment);
        return true;
    }

    @Transactional
    public boolean updateComment(Long commentId, Comment comment, String userId) {
        Comment oldcomment = commentDao.findById(commentId).orElse(null);
        if (oldcomment == null || !oldcomment.getCreator().equals(userId)) {
            return false;
        }
        oldcomment.setContent("(수정됨)" + comment.getContent());
        oldcomment.setDate(LocalDateTime.now());

        commentDao.save(oldcomment);
        return true;
    }
}
