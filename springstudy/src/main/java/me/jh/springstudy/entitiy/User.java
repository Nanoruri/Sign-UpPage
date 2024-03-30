package me.jh.springstudy.entitiy;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 사용자 정보를 담는 엔티티 클래스.
 */

@Entity//TODO : 싱글톤 적용해서 컨트롤러에서 new로 인스턴스 생성하는거 막을 수 있지 않나.
@Table(name = "user_Info" , uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "USER_PHONE"}))
public class User {//Entity 하나 더 만들어서 userId를 forigen key 설정하고  seq와 연결
	@Id
	@Column(name = "USER_ID"/*,unique = true*/)
	private String userId;//ID

	@GeneratedValue(strategy = GenerationType.IDENTITY)// seq를 사용하면 db에서는 pk로 자동 설정됨
	@Column(name = "USER_NO")
	private long seq;

	@Column(name = "USER_NAME")
	private String name;//name
	@Column(name = "USER_PW")
	private String password;

	@NotBlank(message = "전화번호를 입력해 주세요")//todo : @NotBlank와 @Pattern이 작동하지 않음...
	@Pattern(regexp = "[0-9]{3}-[0-9]{4}-[0-9]{4}", message = "전화번호는 XXX-XXXX-XXXX 형식이어야 합니다.")
	@Column(name = "USER_PHONE")// todo: 여기에다가 unique 설정하는 방법도 존재 함.
	private String phoneNum;//010-0000-0000

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "USER_BIRTH")
	private LocalDate birth;//YYYY-MM-DD

	@Column(name = "USER_EMAIL")
	private String email;//XXX@XXXX.XXX

	@Column(name = "CREATE_DATE")
	private LocalDateTime createdDate;//YYYY/MM/DD/HH/MM/SS

	@Column(name = "UPDATE_DATE")
	private LocalDateTime updateDate;//YYYY/MM/DD/HH/MM/SS

	public User() {
	}//기본 생성자

	public User(String userId, String name, String password, String phoneNum, LocalDate birth, String email, LocalDateTime createdDate, LocalDateTime updateDate) {
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.phoneNum = phoneNum;
		this.email = email;
		this.createdDate = createdDate;
		this.updateDate = updateDate;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

}