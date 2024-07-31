package me.jh.springstudy.entitiy.auth;

import javax.persistence.*;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {


	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Id
	@Column(nullable = false)
	private String token;


	public RefreshToken(String userId, String token) {
		this.userId = userId;
		this.token = token;

	}

	public RefreshToken() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}