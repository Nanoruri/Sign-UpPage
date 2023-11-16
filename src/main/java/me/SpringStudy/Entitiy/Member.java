package me.SpringStudy.Entitiy;

import javax.persistence.*;

@Entity
@Table(name = "table_user")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userNo;

    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name ="USER_PW" )
    private String userPassword;
    @Column(name = "USER_PHONE")
    private int userPhoneNum;
    @Column(name = "USER_BIRTH")
    private int userBirth;
    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "APPEND_DATE")
    private String appendDate;
    @Column(name = "UPDATE_DATE")
    private String updateDate;


    //기본 생성자(NoArgs Constructor), 셍략가능
    public Member(String userId, String userName, int userPhoneNum, int userBirth, String userEmail, String appendDate, String updateDate, String hashedPassword) {
    }


    //AllArgs Constructor
    public Member(long userNo, String userId, String userName, String userPassword, int userPhoneNum, int userBirth, String userEmail, String appendDate, String updateDate) {
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

    public int getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(int  userPhoneNum) {
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

    public String getAppendDate() {
        return appendDate;
    }

    public void setAppendDate(String appendDate) {
        this.appendDate = appendDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}

