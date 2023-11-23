package me.SpringStudy.Service;

import me.SpringStudy.Entitiy.Member;
import me.SpringStudy.RepositoryDao.MemberDao;
import me.SpringStudy.RepositoryDto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	private final MemberDao memberDao;


	@Autowired
	public LoginService(MemberDao memberDao){
		this.memberDao = memberDao;
	}

	public void loginCheck(MemberDto memberDto){



		String hasedpassword = null;

		Member member = new Member(
				memberDto.getUserNo(),
				memberDto.getUserId(),
				hasedpassword,
				memberDto.getUserName(),
				memberDto.getUserPassword(),
				memberDto.getUserBirth(),
				memberDto.getUserEmail(),
				memberDto.getAppendDate(),
				memberDto.getUpdateDate()
		);
		memberDao.findByUserId(member.getUserId());//유저 검색~


	}
}
