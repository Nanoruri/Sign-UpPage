package me.jh.springstudy.service.userservice;

import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entitiy.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
//todo : UserDetailsService를 상속받아서 loadUserByUsername 메서드를 구현하도록 하여 사용자 정보를 가져오도록 함.


	private final UserDao userDao;
	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	public CustomUserDetailsService(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userDao.findById(userId)
				.orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다!{}" + userId));//사용자가 없을 경우 예외를 던지도록 함.

		log.info("success to load user : {}", user.getUserId());//로그인 성공 시 로그를 찍도록 함.

		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUserId())
				.password(user.getPassword())
				.roles("USER")// 사용자의 권한을 설정함. 이렇게 설정하면 "ROLE_USER"로 설정.
				.build();
	}
}
