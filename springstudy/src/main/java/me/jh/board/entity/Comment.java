package me.jh.board.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import me.jh.springstudy.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_NO")
    private long id;

    @Column(name = "COMMENT_CONTENT")
    private String content;

    @Column(name = "COMMENT_DATE")
    private LocalDateTime date;

    @Column(name = "COMMENT_UPDATE_DATE")
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NO")
    @JsonBackReference("board-comment")
    private Board board;  // 댓글이 속한 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonBackReference("user-comment")
    private User creator;


    public Comment() {
    }

    public Comment(long id, String content, LocalDateTime date, LocalDateTime updateDate, Board board, User creator) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.updateDate = updateDate;
        this.board = board;
        this.creator = creator;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getUpdateDate() {return updateDate;}

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
