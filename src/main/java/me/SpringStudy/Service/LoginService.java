package me.SpringStudy.Service;

import me.SpringStudy.Entitiy.Member;
import me.SpringStudy.RepositoryDao.MemberDao;
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

	public boolean loginCheck(MemberDto memberDto) {
		Optional<Member> optionalMember =memberDao.findByUserId(memberDto.getUserId());// 아이디 찾기
		//25번째 줄에서 에러 터짐, 로그 내용 : javax.persistence.NonUniqueResultException: query did not return a unique result: 2
		//Optinal은 유일 결과에 대해서..List는 여려 결과에 대한..
		return optionalMember.isPresent() &&
				passwordEncoder.matches(memberDto.getUserPassword(), optionalMember.get().getUserPassword());//비밀번호 대조
	}
}
