import me.jh.springstudy.MySpringBootApplication;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entitiy.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = MySpringBootApplication.class)
public class SpringApplicationTest {

	@Mock
	private UserDao userDao;

	@Autowired
	private Environment env;


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
		user.setBirth(LocalDate.of(1996, 11, 27));
		user.setPhoneNum("010-9525-6863");
		user.setEmail("kaby1217@gmail.com");
		user.setCreatedDate(LocalDateTime.now());
		user.setUpdateDate(LocalDateTime.now());
		user.setRole("USER");

		userDao.save(user);
	}


	@Test
	void testServerPort() {
		String serverPort = env.getProperty("server.port");
		assertThat(serverPort).isEqualTo("8082");
	}

	@Test
	void testDatasource() {
		String dbUrl = env.getProperty("spring.datasource.url");
		String dbUsername = env.getProperty("spring.datasource.username");
		String dbPassword = env.getProperty("spring.datasource.password");
		String dbDriver = env.getProperty("spring.datasource.driver-class-name");

		assertThat(dbUrl).isEqualTo("jdbc:mariadb://127.0.0.1:44327/USERINFO");
		assertThat(dbUsername).isEqualTo("kaby1217");
		assertThat(dbPassword).isEqualTo("Cubie159!@");
		assertThat(dbDriver).isEqualTo("org.mariadb.jdbc.Driver");
	}

	@Test
	void testJpaProperties() {
		String ddlAuto = env.getProperty("spring.jpa.hibernate.ddl-auto");
		String showSql = env.getProperty("spring.jpa.show-sql");
		String generateDdl = env.getProperty("spring.jpa.generate-ddl");
		String openInView = env.getProperty("spring.jpa.open-in-view");

		assertThat(ddlAuto).isEqualTo("update");
		assertThat(showSql).isEqualTo("true");
		assertThat(generateDdl).isEqualTo("true");
		assertThat(openInView).isEqualTo("false");
	}

	@Test
	void testHibernateProperties() {
		String dialect = env.getProperty("spring.jpa.properties.hibernate.dialect");
		String hibernateShowSql = env.getProperty("spring.jpa.properties.hibernate.show-sql");

		assertThat(dialect).isEqualTo("org.hibernate.dialect.MySQL5Dialect");
		assertThat(hibernateShowSql).isEqualTo("true");
	}

	@Test
	void testSecurityProperties() {
		String userName = env.getProperty("security.user.name");
		String userPassword = env.getProperty("security.user.password");
		String userRoles = env.getProperty("security.user.roles");

		assertThat(userName).isEqualTo("kaby1217");
		assertThat(userPassword).isEqualTo("1217159");
		assertThat(userRoles).isEqualTo("USER,ADMIN");
	}

	@Test
	void testJwtProperties() {
		String secret = env.getProperty("jwt.secret");
		String expiration = env.getProperty("jwt.expiration");

		assertThat(secret).isEqualTo("7L2cIOu4lOugiOydtOuTnCDsubTtg4DsiqTtirjroZztlLw=");
		assertThat(expiration).isEqualTo("8640000");
	}


}

