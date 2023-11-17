import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.MySpringBootApplication;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;


@SpringBootTest(classes = MySpringBootApplication.class)
public class SpringApplicationTest {

	@Autowired
	private MemberDao memberDao;
	private PasswordEncoder passwordEncoder;

	@Test
	public void testMariaDbConnection() {
		// 테스트할 연결을 테스트하는 코드 작성
		User user = new User();
		user.setUserId("kaby1217");
		user.setName("Lim");
		user.setPassword("1217159");
		user.setBirth(new Date(19961127));
		user.setPhoneNum("010-9525-6863");
		user.setEmail("kaby1217@gmail.com");
		user.setCreatedDate(new Date());
		user.setUpdateDate(new Date());

		memberDao.save(user);
	}

}

