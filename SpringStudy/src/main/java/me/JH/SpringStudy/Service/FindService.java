package me.JH.SpringStudy.Service;

import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FindService {
	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public FindService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
		this.memberDao = memberDao;
		this.passwordEncoder = passwordEncoder;
	}

	public String findId(String name, String email) {
		User user = memberDao.findByNameAndEmail(name, email);
		return (user != null) ? user.getUserId() : null;
	}

	public boolean validateUser(String userId, String name, String email) {//메서드명: 유저 확인 vs 비밀번호 찾기
		User user = memberDao.findByUserIdAndNameAndEmail(userId, name, email);
		return user != null;
	}

	public void resetPassword(String presentPassword, String newPassword) {
		String hashedRawPassword = passwordEncoder.encode(presentPassword);
		User user = memberDao.findByPassword(hashedRawPassword);

		if (user != null) {
			passwordEncoder.matches(presentPassword, user.getPassword());

			// 새로운 비밀번호로 업데이트
			user.setPassword(passwordEncoder.encode(newPassword));

			// 업데이트된 사용자 정보 저장
			memberDao.save(user);
		}
	}
}
