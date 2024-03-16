package me.JH.SpringStudy.Service.UserService;

import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.Exception.Finds.FindPwException;
import me.JH.SpringStudy.Exception.Finds.FindPwExceptionType;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindService {
	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;
	public final static Logger log = LoggerFactory.getLogger(FindService.class);


	@Autowired
	public FindService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
		this.memberDao = memberDao;
		this.passwordEncoder = passwordEncoder;
	}

	public String findId(String name, String email) {
		User user = memberDao.findByNameAndEmail(name, email);// TODO : 이름과 이메일이 같으면 500에러 발생
		return (user != null) ? user.getUserId() : null;//todo : 여기서 컨트롤러의 예외처리 해도 되나??
	}

	public boolean validateUser(String userId, String name, String email) {//todo : findBy properties, Criteria 사용
		boolean isValid = memberDao.findByProperties(userId, name, email).isPresent();
		log.info(isValid ? "사용자를 찾았습니다" + userId : "사용자를 찾을 수 없습니다.");
		return isValid;
	}

	public boolean changePassword(User changePasswordUser, String newPassword) {
		Optional<User> optionalUser = memberDao.findByProperties(changePasswordUser.getUserId(),//todo : 주성이한테 검사 받고 findByProperties 적용하기
				changePasswordUser.getName(),
				changePasswordUser.getEmail());//todo : 이거 html에서 hidden으로 받아오긴 한데 찾아지는건가..?

		if (optionalUser.isEmpty()) {
			log.info("사용자를 찾을 수 없습니다.");
			throw new FindPwException(FindPwExceptionType.USER_NOT_FOUND);
		}
		User user = optionalUser.get();

		// 새로운 비밀번호로 업데이트
		user.setPassword(passwordEncoder.encode(newPassword));
		// 업데이트된 사용자 정보 저장
		memberDao.save(user);
		return true;
	}
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

