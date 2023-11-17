import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest()
public class ServiceTest {

	@Autowired
	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;


	public ServiceTest(MemberDao memberDao, PasswordEncoder passwordEncoder) {
		this.memberDao = memberDao;
		this.passwordEncoder = passwordEncoder;
	}

	@Test
	public void changePassword(String userId, String name, String email, String newPassword) {//todo :
		User user = memberDao.findByUserIdAndNameAndEmail(userId, name, email);
		if (user != null && passwordEncoder != null){
			// 새로운 비밀번호로 업데이트
			user.setPassword(passwordEncoder.encode(newPassword));
			// 업데이트된 사용자 정보 저장
			memberDao.save(user);
		}

	}
}
