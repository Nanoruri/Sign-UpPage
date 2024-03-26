package me.jh.springstudy.entitiy;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

	@Test
	public void testUserEntity() {
		String userId = "test123";
		String name = "John Doe";
		String password = "password123";
		String phoneNum = "010-1234-5678";
		Date birth = new Date(1997, Calendar.FEBRUARY, 1);
		String email = "test@example.com";
		Date createdDate = new Date();
		Date updateDate = new Date();

		User user1 = new User(userId, name, password, phoneNum, birth, email, createdDate, updateDate);

		// 엔티티 속성 값이 예상대로 설정되었는지 확인
		assertEquals(userId, user1.getUserId());
		assertEquals(name, user1.getName());
		assertEquals(password, user1.getPassword());
		assertEquals(phoneNum, user1.getPhoneNum());
//		assertEquals(birth, user.getBirth());
		assertEquals(email, user1.getEmail());
		assertEquals(createdDate, user1.getCreatedDate());
		assertEquals(updateDate, user1.getUpdateDate());

		// 엔티티 동등성 확인
		User user2 = new User(userId, name, password, phoneNum, birth, email, createdDate, updateDate);
		assertThat(user1).usingRecursiveComparison().isEqualTo(user2);

		assertEquals(user1, user2);// 새 인스턴스로 만들어서 assertEquals를 사용하면 참조값을 비교하기 때문에 실패한다.
		// 객체의 참조값을 비교할떄는 assertSame 사용

		// 엔티티의 hashCode() 메서드 확인
		assertEquals(user1.hashCode(), user2.hashCode());

		// 엔티티의 문자열 표현 확인
		assertNotNull(user1.toString());

		// 엔티티의 상태 변경 확인
		String newPassword = "newpassword123";
		user1.setPassword(newPassword);
		assertEquals(newPassword, user1.getPassword());

		// 엔티티의 상태 변경 후 동등성 확인
		assertNotEquals(user1, user2);
	}

}
