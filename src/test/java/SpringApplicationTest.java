import me.SpringStudy.Entitiy.Member;
import me.SpringStudy.MySpringBootApplication;
import me.SpringStudy.RepositoryDao.MemberDao;
import me.SpringStudy.RepositoryDto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(classes = MySpringBootApplication.class)
public class SpringApplicationTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void testMariaDbConnection() {
        // 테스트할 연결을 테스트하는 코드 작성
        MemberDto memberDto = new MemberDto();
        memberDto.setUserNo(1);
        memberDto.setUserId("kaby1217");
        memberDto.setUserName("lim");
        memberDto.setUserPassword("123123");
        memberDto.setUserPhoneNum("01012345678");
        memberDto.setUserBirth(961127);
        memberDto.setUserEmail("kaby1217@gmail.com");
        memberDto.setAppendDate(LocalDateTime.now());
        memberDto.setUpdateDate(LocalDateTime.now());

        Member test1 = new Member(
                memberDto.getUserNo(),
                memberDto.getUserId(),
                memberDto.getUserName(),
                memberDto.getUserPassword(),
                memberDto.getUserPhoneNum(),
                memberDto.getUserBirth(),
                memberDto.getUserEmail(),
                memberDto.getAppendDate(),
                memberDto.getUpdateDate()
        );

        memberDao.save(test1);
    }
}
