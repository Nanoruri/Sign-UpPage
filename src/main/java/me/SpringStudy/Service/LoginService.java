package me.SpringStudy.Service;

import me.SpringStudy.Entitiy.Member;
import me.SpringStudy.RepositoryDao.MemberDao;
import me.SpringStudy.RepositoryDto.LoginDto;
import me.SpringStudy.RepositoryDto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public LoginService(MemberDao memberDao, PasswordEncoder passwordEncoder){
		this.memberDao = memberDao;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean loginCheck(LoginDto loginDto) {
		Optional<Member> optionalMember =memberDao.findByUserId(loginDto.getLoginId());// 아이디 찾기
		//25번째 줄에서 에러 터짐, 로그 내용 : javax.persistence.NonUniqueResultException: query did not return a unique result: 2
		// 아이디 중복 검사로 해결
		return optionalMember.isPresent() &&
				passwordEncoder.matches(loginDto.getLoginPassword(), optionalMember.get().getUserPassword());//비밀번호 대조
	}
}
