package me.jh.board.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOARD_NO")
	private long id;

	@Column(name = "BOARD_TITLE")
	private String title;

	@Column(name = "BOARD_CONTENT")
	private String content;

	@Column(name = "BOARD_DATE")
	private LocalDateTime date;

	public Board(long id, String title, String content, LocalDateTime date) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.date = date;
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

}
