package me.jh.springstudy.service.userservice;

import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entitiy.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

	@Mock
	private UserDao userDao;

	@Mock
	private User user;


	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;


	@BeforeEach
	public void setUp() {// 테스트에 사용할 User 객체 생성
		user = new User("test", "testName", "validPassword", "010-1234-5678",
				LocalDate.of(1990, 11, 21), "test@test.com", LocalDateTime.now(), LocalDateTime.now());
	}

	@Test
	public void testLoadUserByUsername_Success() {

		// userDao.findById() 메서드의 Mock 설정
		when(userDao.findById(user.getUserId())).thenReturn(Optional.of(user));

		// Act
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUserId());

		// Assert
		assertNotNull(userDetails);
		assertEquals(user.getUserId(), userDetails.getUsername());
		assertEquals(user.getPassword(), userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
		verify(userDao,times(1)).findById(user.getUserId());
	}

	@Test
	public void testLoadUserByUsername_Fail() {
		// Arrange

		when(userDao.findById(user.getUserId())).thenReturn(Optional.empty());

		// Act & Assert
		try {
			customUserDetailsService.loadUserByUsername(user.getUserId());
			fail("Expected UsernameNotFoundException was not thrown");
		} catch (UsernameNotFoundException exception) {
			// Assert
			assertEquals("사용자가 없습니다!{}" + user.getUserId(), exception.getMessage());}

		verify(userDao, times(1)).findById(user.getUserId());// userDao.findById() 메서드가 실행되었는지 확인
	}


}
