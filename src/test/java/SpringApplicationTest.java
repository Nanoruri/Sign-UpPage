import static org.junit.jupiter.api.Assertions.assertEquals;

import me.SpringStudy.Entitiy.Member;
import me.SpringStudy.MySpringBootApplication;
import me.SpringStudy.RepositoryDao.MemberDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

@SpringBootTest(classes = MySpringBootApplication.class)

public class SpringApplicationTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void testMariaDbConnection() {
        // 테스트할 연결을 테스트하는 코드 작성
        Member test1 = new Member();
        test1.setUserName("lim");
        test1.setUserBirth(961127);
        test1.setUserEmail("kaby1217@gmail.com");
        test1.setUserPhoneNum(Long.parseLong("0105256863"));
        test1.setUserId("kaby1217");
        test1.setAppendDate(String.valueOf(LocalDateTime.now()));
        test1.setUpdateDate(String.valueOf(LocalDateTime.now()));
        this.memberDao.save(test1);
        // 원하는 테스트를 작성하고 결과를 검증



    }

}
