package me.SpringStudy.Service;
import me.SpringStudy.Entitiy.Member;
import me.SpringStudy.RepositoryDao.MemberDao;
import me.SpringStudy.RepositoryDto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class MemberService {

    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public MemberService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerMember(MemberDto memberDto) {
        // 비밀번호를 해시화하여 저장
        String hashedPassword = passwordEncoder.encode(memberDto.getUserPassword());



        Member member = new Member(
                memberDto.getUserNo(),
                memberDto.getUserId(),
                memberDto.getUserName(),
                hashedPassword,
                memberDto.getUserPhoneNum(),
                memberDto.getUserBirth(),
                memberDto.getUserEmail(),
                memberDto.getAppendDate(),
                memberDto.getUpdateDate()
        ); //Dto를 기반으로 Member엔티티 클래스로 저장하게 만듬

        memberDao.save(member); // Member 엔티티를 데이터베이스에 저장
    }
    public boolean duplicateId(String userId){
        return memberDao.existsByUserId(userId);//아이디 중복 검사 하는 메서드
    }

}
