package me.JH.SpringStudy.Service.UserService;

import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;


	@Autowired//의존성 쭈와왑
	public LoginService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
		this.memberDao = memberDao;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean loginCheck(String userId, String password) {//true와 false만 반환하면 되니 boolean타입으로
		Optional<User> user = memberDao.findById(userId);// 아이디 찾기 및 대조
		//25번째 줄에서 에러 터짐, 로그 내용 : javax.persistence.NonUniqueResultException: query did not return a unique result: 2
		// 아이디 중복 검사로 해결
		return user.isPresent() && //해당 아이디가 값이 존재하는 지 검사
				passwordEncoder.matches(password, user.get().getPassword());// 해시된 비밀번호 대조
	}
}
