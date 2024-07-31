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

	@Column(name = "ip_address", nullable = false)
	private String ipAddress;

	public RefreshToken(String userId, String token, String ipAddress) {
		this.userId = userId;
		this.token = token;
		this.ipAddress = ipAddress;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}