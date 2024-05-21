import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.MySpringBootApplication;
import me.jh.springstudy.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@SpringBootTest(classes = MySpringBootApplication.class)
public class SpringApplicationTest {

	@Autowired
	private UserDao userDao;
	private PasswordEncoder passwordEncoder;

	@Test
	public void testMariaDbConnection() {
		// 테스트할 연결을 테스트하는 코드 작성
		User user = new User();
		user.setUserId("kaby1217");
		user.setName("Lim");
		user.setPassword("1217159");
		user.setBirth(LocalDate.of(1996,11,27));
		user.setPhoneNum("010-9525-6863");
		user.setEmail("kaby1217@gmail.com");
		user.setCreatedDate(LocalDateTime.now());
		user.setUpdateDate(LocalDateTime.now());

		userDao.save(user);
	}

}

