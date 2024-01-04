import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.MySpringBootApplication;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MySpringBootApplication.class)
public class ServiceTest {

	@Autowired
	private MemberDao memberDao;
	private PasswordEncoder passwordEncoder;


	@Test
	public void changePasswordTest() {//todo :

		String userId = "test";
		String name = "test";
		String email = "test@test.com";
		String newPassword = "12345678";

		Optional<User> optionalUser = memberDao.findByProperties(userId, name, email);

		assertTrue(optionalUser.isPresent(), "사용자를 찾았습니다.");

		User user = optionalUser.get();

		// 새로운 비밀번호로 업데이트
		user.setPassword(newPassword);//변경값을 확인하기 위해 PasswordEncoder 사용 X
		// 업데이트된 사용자 정보 저장
		memberDao.save(user);

		assertTrue(true, "저장에 성공하였습니다.");


	}
}
