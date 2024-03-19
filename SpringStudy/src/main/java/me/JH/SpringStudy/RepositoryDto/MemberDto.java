package me.jh.springStudy.repositoryDto;

import java.time.LocalDateTime;
@Deprecated
public class MemberDto {

    private long userNo;
    private String userId;
    private String userName;
    private String userPassword;
    private String userPhoneNum;
    private int userBirth;
    private String userEmail;
    private LocalDateTime appendDate;
    private LocalDateTime updateDate;


    public MemberDto() {
    }

    public MemberDto(long userNo, String userId, String userName, String userPassword, String userPhoneNum, int userBirth, String userEmail, LocalDateTime appendDate, LocalDateTime updateDate) {
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

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
