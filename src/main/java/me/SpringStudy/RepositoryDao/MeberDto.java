package me.SpringStudy.RepositoryDao;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "table_user")
public abstract class MeberDto {
    private int userNo;

    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name ="USER_PW" )
    private String userPassword;
    @Column(name = "")
    private String userPhoneNum;
    @Column(name = "")
    private String userBirth;
    @Column(name = "")
    private String userEmail;

    @Column(name = "APPEND_DATE")
    private String appendDate;
    @Column(name = "UPDATE_DATE")
    private String updateDate;
    //TODO : DB의 컬럼과 맞추기
}

