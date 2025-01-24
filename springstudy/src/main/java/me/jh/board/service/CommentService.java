package me.jh.board.service;

import me.jh.board.dao.BoardDao;
import me.jh.board.dao.CommentDao;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class CommentService {


    private final CommentDao commentDao;
    private final BoardDao boardDao;
    private final UserDao userDao;

    @Autowired
    public CommentService(CommentDao commentDao, BoardDao boardDao, UserDao userDao) {
        this.commentDao = commentDao;
        this.boardDao = boardDao;
        this.userDao = userDao;
    }

    @Transactional
    public boolean saveComment(Long boardId, Comment comment, String userId) {
        Board board = boardDao.findById(boardId).orElse(null);
        User user = userDao.findById(userId).orElse(null);

        Comment saveComment = new Comment();
        saveComment.setContent(comment.getContent());
        saveComment.setDate(LocalDateTime.now());
        saveComment.setUpdateDate(LocalDateTime.now());
        saveComment.setCreator(user);
        saveComment.setBoard(board);

        commentDao.save(saveComment);
        return true;
    }

    @Transactional
    public boolean updateComment(Long commentId, Comment comment, String userId) {
        Comment oldcomment = commentDao.findById(commentId).orElse(null);
        if (oldcomment == null || !oldcomment.getCreator().getUserId().equals(userId)) {
            return false;
        }
        oldcomment.setContent(comment.getContent());
        oldcomment.setUpdateDate(LocalDateTime.now());

        commentDao.save(oldcomment);
        return true;
    }

    @Transactional
    public boolean deleteComment(Long commentId, String userId) {
        Comment comment = commentDao.findById(commentId).orElse(null);

        if (comment == null || !comment.getCreator().getUserId().equals(userId)) {
            return false;
        }

        commentDao.delete(comment);
        return true;
    }
}
