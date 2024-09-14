package me.jh.springstudy.dao;

import me.jh.springstudy.entitiy.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserDaoTest {


	@Mock
	private UserDao userDao;
	@Mock
	private User user;

	/**
	 * 사용자 정보 조회 테스트
	 * findByNameAndPhoneNum 메서드를 호출하여 사용자 정보를 조회하고, 조회한 사용자 정보가 예상한 것과 일치하는지 확인한다.
	 */
	@Test
	public void testFindByNameAndPhoneNum() {
		// UserDao의 구현체를 목 객체로 생성

		// 테스트용 사용자 정보 생성
		user = new User();
		user.setName("test");
		user.setPhoneNum("010-1234-5678");

		// UserDao의 findByNameAndPhoneNum 메서드 호출 시 테스트용 사용자 정보 반환하도록 설정
		when(userDao.findByNameAndPhoneNum("test", "010-1234-5678")).thenReturn(user);

		// UserDao의 findByNameAndPhoneNum 메서드를 호출하여 사용자 정보를 조회
		User foundUser = userDao.findByNameAndPhoneNum("test", "010-1234-5678");

		// 조회한 사용자 정보가 예상한 것과 일치하는지 확인
		assertEquals("test", foundUser.getName());
		assertEquals("010-1234-5678", foundUser.getPhoneNum());

		// UserDao의 findByNameAndPhoneNum 메서드가 정확히 한 번 호출되었는지 확인
		verify(userDao, times(1)).findByNameAndPhoneNum("test", "010-1234-5678");
	}

	/**
	 * 사용자 정보 저장 테스트
	 */
	@Test
	public void testSave() {
		// UserDao의 구현체를 목 객체로 생성

		// 테스트용 사용자 정보 생성
		user = new User();
		user.setUserId("test");
		user.setName("test");
		user.setPassword("test");
		user.setEmail("test@test.com");
		user.setPhoneNum("010-1234-5678");

		// UserDao의 save 메서드 호출 시 테스트용 사용자 정보 저장하도록 설정
		when(userDao.save(user)).thenReturn(user);

		// UserDao의 save 메서드를 호출하여 사용자 정보 저장
		User savedUser = userDao.save(user);

		// 사용자 정보 저장이 성공적으로 이루어졌는지 확인
		assertEquals("test", savedUser.getUserId());
		assertEquals("test", savedUser.getName());
		assertEquals("test", savedUser.getPassword());
		assertEquals("test@test.com", savedUser.getEmail());
		assertEquals("010-1234-5678", savedUser.getPhoneNum());

		verify(userDao, times(1)).save(user);
	}

	/**
	 * 사용자 정보 조회 테스트
	 * findById 메서드를 호출하여 사용자 정보를 조회하고, 조회한 사용자 정보가 예상한 것과 일치하는지 확인한다.
	 */
	@Test
	public void testFindById() {
		// UserDao의 구현체를 목 객체로 생성

		// 테스트용 사용자 정보 생성
		user = new User();
		user.setUserId("test");
		user.setName("test");
		user.setPassword("test");
		user.setEmail("test@test.com");
		user.setPhoneNum("010-1234-5678");


// UserDao의 findById 메서드 호출 시 테스트용 사용자 정보 반환하도록 설정
		when(userDao.findById("test")).thenReturn(Optional.of(user));

		// UserDao의 findById 메서드를 호출하여 사용자 정보를 조회
		Optional<User> foundUserOptinal = userDao.findById("test");

		// 조회한 사용자 정보가 예상한 것과 일치하는지 확인
		assertTrue(foundUserOptinal.isPresent());

		User foundUser = foundUserOptinal.get();

		assertEquals("test", foundUser.getUserId());
		assertEquals("test", foundUser.getName());
		assertEquals("test", foundUser.getPassword());
		assertEquals("test@test.com", foundUser.getEmail());
		assertEquals("010-1234-5678", foundUser.getPhoneNum());

		verify(userDao, times(1)).findById("test");
	}
}
