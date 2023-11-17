package me.JH.SpringStudy.Service.UserService;

import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class SignuprService {

	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public SignuprService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
		this.memberDao = memberDao;
		this.passwordEncoder = passwordEncoder;
	}

	public void registerMember(User user) {//로그인 할때에도 한번더 중복 검사 하게 끔하고 예외가 나오면 Catch하도록
//        if (isDuplicateId(user.getUserId())){//todo : 예외 처리 방법 고민/공부
//            throw new DuplcateIdException("중복된 ID");
//        };//중복검사 한번 더 하는 로직
		user.setPassword(passwordEncoder.encode(user.getPassword()));// 비밀번호를 해시화하여 저장
		user.setCreatedDate(new Date());
		user.setUpdateDate(new Date());
		memberDao.save(user); // Member 엔티티를 데이터베이스에 저장
	}

	public boolean isDuplicateId(String userId) {
		return memberDao.existsById(userId);//아이디 중복 검사 하는 메서드
	}
}
