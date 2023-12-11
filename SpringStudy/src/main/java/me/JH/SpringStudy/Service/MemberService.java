package me.JH.SpringStudy.Service;

import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class MemberService {

    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public MemberService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerMember(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));// 비밀번호를 해시화하여 저장
        user.setCreatedDate(new Date());
        user.setUpdateDate(new Date());
        memberDao.save(user); // Member 엔티티를 데이터베이스에 저장
    }
    public boolean duplicateId(String userId){
        return memberDao.existsByUserId(userId);//아이디 중복 검사 하는 메서드
    }

}
