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
	public LoginService(MemberDao memberDao){
		this.memberDao = memberDao;
	}

	public boolean loginCheck(MemberDto memberDto) {
		Member member =memberDao.findByUserId(memberDto.getUserId());// 아이디 찾기

		return member != null && memberDto.getUserPassword().equals(member.getUserPassword());//비밀번호 대조
	}
}
