package me.SpringStudy.RepositoryDto;

public class MemberDto {

    private long userNo;
    private String userId;
    private String userPassword;
    private String userName;
    private int userPhoneNum;
    private int userBirth;
    private String userEmail;
    private String appendDate;
    private String updateDate;

    public MemberDto() {
    }

    public MemberDto(long userNo, String userId, String userName, String userPassword, int userPhoneNum, int userBirth, String userEmail, String appendDate, String updateDate) {
        this.userNo = userNo;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
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

    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(int userPhoneNum) {
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
