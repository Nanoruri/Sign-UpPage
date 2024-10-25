package me.jh.board.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @Column(name = "CREATED_USER")
    private String creator;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comments;

    public Board(long id, String title, String content, LocalDateTime date, String tabName, String creator) {
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
