package me.SpringStudy.RepositoryDto;

public class MemberDto {

    private long userNo;
    private String userId;
    private String userName;
    private String userPassword;
    private String userPhoneNum;
    private String userBirth;
    private String userEmail;
    private String appendDate;
    private String updateDate;

    public MemberDto() {
    }

    public MemberDto(long userNo, String userId, String userName, String userPassword, String userPhoneNum, String userBirth, String userEmail, String appendDate, String updateDate) {
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

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
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
