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
                hashedPassword,
                memberDto.getUserName(),
                memberDto.getUserPassword(),
                memberDto.getUserBirth(),
                memberDto.getUserEmail(),
                memberDto.getAppendDate(),
                memberDto.getUpdateDate()
        );
                // 해시된 비밀번호를 저장

        memberDao.save(member); // Member 엔티티를 데이터베이스에 저장
    }
}
