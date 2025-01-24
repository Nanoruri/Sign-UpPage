package me.jh.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import me.jh.springstudy.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_NO")
    private long id;

    @Column(name = "BOARD_TITLE")
    private String title;

    @Column(name = "BOARD_CONTENT", columnDefinition = "TEXT")
    private String content;

    @Column(name = "BOARD_DATE")
    private LocalDateTime date;

    @Column(name = "BOARD_TAB")
    private String tabName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonBackReference("user-board")
    private User creator;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    @JsonManagedReference("board-comment")
    private List<Comment> comments;

    public Board(long id, String title, String content, LocalDateTime date, String tabName, User creator) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.tabName = tabName;
        this.creator = creator;
    }

    public Board() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tab) {
        this.tabName = tab;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


}
