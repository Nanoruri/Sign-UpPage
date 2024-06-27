import me.jh.springstudy.entitiy.User;
import me.jh.springstudy.MySpringBootApplication;
import me.jh.springstudy.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = MySpringBootApplication.class)
public class SpringApplicationTest {

	@Autowired
	private UserDao userDao;
	private PasswordEncoder passwordEncoder;
	private ApplicationContext context;




	@Test // 애플리케이션 로드 테스트
	public void applicationContextLoadsTest() {
		String[] args = {}; // 테스트를 위한 빈 args 배열을 생성
		ConfigurableApplicationContext context = SpringApplication.run(MySpringBootApplication.class, args);
		assertNotNull(context); // 컨텍스트의 null체크.
		context.close(); // 컨텍스트를 닫아 다음 테스트에 영향을 주지 않도록 함.
	}


	@Test// 메인 메서드 테스트
	public void contextLoads() {
		MySpringBootApplication.main(new String[]{});
	}

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

