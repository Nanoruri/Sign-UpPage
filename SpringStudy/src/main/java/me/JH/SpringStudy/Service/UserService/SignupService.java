package me.JH.SpringStudy.Service.UserService;

import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.Exception.Signup.SignupException;
import me.JH.SpringStudy.Exception.Signup.SignupExceptionType;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class SignupService {

	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public SignupService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
		this.memberDao = memberDao;
		this.passwordEncoder = passwordEncoder;
	}

	public void registerMember(User user) {//로그인 할때에도 한번더 중복 검사 하게 끔하고 예외가 나오면 Catch하도록
		if (isDuplicateId(user.getUserId())) {//중복검사 한번 더 하는 로직
			throw new SignupException(SignupExceptionType.ID_ALREADY_EXIST);
		}
		else if (memberDao.findById(user.getUserId()).isPresent()) {
			throw new SignupException(SignupExceptionType.USER_ALREADY_EXIST);
		}//todo : 이렇게 처리 해도 되나??

		user.setPassword(passwordEncoder.encode(user.getPassword()));// 비밀번호를 해시화하여 저장
		user.setCreatedDate(new Date());
		user.setUpdateDate(new Date());
		memberDao.save(user);// Member 엔티티를 데이터베이스에 저장
	}

	public boolean isDuplicateId(String userId) {
		return memberDao.existsById(userId);//아이디 중복 검사 하는 메서드
	}
}
