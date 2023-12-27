package me.JH.SpringStudy.Service;

import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindService {
	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;
	public  final static Logger log =LoggerFactory.getLogger(FindService.class);


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
		if(user != null){
			log.info("사용자를 찾았습니다."+ user);
			return true;
		}else {
			log.info("사용자를 찾을 수 없습니다.");
			return false;
		}
	}

	public boolean changePassword(User changePasswordUser, String newPassword) {
		User user =memberDao.findByUserIdAndNameAndEmail(changePasswordUser.getUserId(),
				changePasswordUser.getName(),
				changePasswordUser.getEmail());//todo : 이거 html에서 hidden으로 받아오긴 한데 찾아지는건가..?

		if (user != null && passwordEncoder != null){
			// 새로운 비밀번호로 업데이트
			user.setPassword(passwordEncoder.encode(newPassword));
			// 업데이트된 사용자 정보 저장
			memberDao.save(user);
		return true;
	}else {
			if (user == null) {
				log.info("사용자를 찾을 수 없습니다.");
			}
			if (passwordEncoder == null){
				log.info("passwordEncoder가 주입되지않았습니다.");
			}
		}
		return false;
	}

//	public void resetPassword(String presentPassword, String newPassword) {//todo : 이거 없는 비밀번호 넣어도 로직이 정상작동됨;;
//		String hashedRawPassword = passwordEncoder.encode(presentPassword);
//		User user = memberDao.findByPassword(presentPassword);
//
//		if (user != null && passwordEncoder.matches(presentPassword, user.getPassword())) {
//
//			// 새로운 비밀번호로 업데이트
//			user.setPassword(passwordEncoder.encode(newPassword));
//
//			// 업데이트된 사용자 정보 저장
//			memberDao.save(user);
//		}
//	}
}
