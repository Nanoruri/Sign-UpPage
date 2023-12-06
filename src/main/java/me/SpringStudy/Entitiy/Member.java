package me.SpringStudy.Entitiy;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "table_user")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userNo;

    @Column(name = "USER_ID",unique = true)
    private String userId;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name ="USER_PW" )
    private String userPassword;
    @Column(name = "USER_PHONE")
    private String userPhoneNum;
    @Column(name = "USER_BIRTH")
    private int userBirth;
    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "APPEND_DATE")
    private LocalDateTime appendDate;
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;



    public Member(String userId, String userName,String userPassword, String userPhoneNum, int userBirth, String userEmail, LocalDateTime appendDate, LocalDateTime updateDate){
    }

    public Member(){}//기본 생성자(NoArgs Constructor), 생략가능


    //AllArgs Constructor
    public Member(long userNo, String userId, String userName, String userPassword, String userPhoneNum, int userBirth, String userEmail, LocalDateTime appendDate, LocalDateTime updateDate) {
        this.userNo = userNo;
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhoneNum = userPhoneNum;
        this.userBirth = userBirth;
        this.userEmail = userEmail;
        this.appendDate = appendDate;
        this.updateDate = updateDate;
    }

    //Getter,Setter 메서드
    public long getUserNo()
    {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public int getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(int userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getAppendDate() {
        return appendDate;
    }

    public void setAppendDate(LocalDateTime appendDate) {
        this.appendDate = appendDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }


}