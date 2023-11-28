package me.SpringStudy.Service;

import me.SpringStudy.Entitiy.Member;
import me.SpringStudy.RepositoryDao.MemberDao;
import me.SpringStudy.RepositoryDto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	private final MemberDao memberDao;


	@Autowired
	public LoginService(MemberDao memberDao, PasswordEncoder passwordEncoder){
		this.memberDao = memberDao;
	}

	public boolean loginCheck(String userId, String inputPassword) {
		//todo : 로그인 find로직 구현

		return member != null && inputPassword.equals(member.getUserPassword());

	}
}
