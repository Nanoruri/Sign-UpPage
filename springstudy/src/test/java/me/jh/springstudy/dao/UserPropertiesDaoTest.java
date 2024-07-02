package me.jh.springstudy.dao;

import me.jh.springstudy.entitiy.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;



/**
 * 동적 쿼리를 생성해 사용자 정보를 찾는 CustomDAO 테스트 클래스.
 */
@ExtendWith(MockitoExtension.class)
public class UserPropertiesDaoTest {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserPropertiesDaoTest.class);

	@Mock
	private EntityManager entityManager;
	@Mock
	private CriteriaBuilder criteriaBuilder;
	@Mock
	private CriteriaQuery<User> criteriaQuery;
	@Mock
	private Root<User> root;
	@Mock
	private TypedQuery<User> typedQuery;
	@Mock
	private Predicate predicate;


	@Test
	public void testFindByProperties() {
		String userId = "test";
		String name = "test";
		String phoneNum = "010-1234-5678";

		User user = new User();
		user.setUserId(userId);
		user.setName(name);
		user.setPhoneNum(phoneNum);

		when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
		when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
		when(criteriaQuery.from(User.class)).thenReturn(root);
		when(criteriaBuilder.conjunction()).thenReturn(predicate);
		when(criteriaBuilder.equal(root.get("userId"), userId)).thenReturn(predicate);
		when(criteriaBuilder.equal(root.get("name"), name)).thenReturn(predicate);
		when(criteriaBuilder.equal(root.get("phoneNum"), phoneNum)).thenReturn(predicate);
		when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
		when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
		when(typedQuery.getResultList()).thenReturn(List.of(user));

//		when(criteriaQuery.where(any(Predicate.class))).thenReturn(criteriaQuery);
//		when(criteriaQuery.from(User.class)).thenReturn(root);

		UserPropertiesDaoImpl customDao = new UserPropertiesDaoImpl(entityManager);
		Optional<User> foundUser = customDao.findByProperties(user.getUserId(), user.getName(), user.getPhoneNum());

		assertTrue(foundUser.isPresent());
		User optionalUser = foundUser.get();
		assertEquals(user, optionalUser);
		assertEquals(userId, optionalUser.getUserId());
		assertEquals(name, optionalUser.getName());
		assertEquals(phoneNum, optionalUser.getPhoneNum());


	}

}



