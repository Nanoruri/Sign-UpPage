package me.jh.springstudy.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.jh.springstudy.entitiy.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * 동적 쿼리를 생성해 사용자 정보를 찾는 CustomDAO 테스트 클래스.
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserPropertiesDaoTest {

	@Mock
	private EntityManager entityManager;
	@Mock
	private ObjectMapper objectMapper;
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

	@InjectMocks
	private UserPropertiesDaoImpl userPropertiesDao;

	@BeforeEach
	public void setUp() {
		userPropertiesDao = new UserPropertiesDaoImpl(entityManager, objectMapper);
	}


	@Test
	public void testFindByProperties() {
		String userId = "test";
		String name = "test";
		String phoneNum = "010-1234-5678";

		User user = new User();
		user.setUserId(userId);
		user.setName(name);
		user.setPhoneNum(phoneNum);

		Map<String, Object> properties = new HashMap<>();
		properties.put("userId", userId);
		properties.put("name", name);
		properties.put("phoneNum", phoneNum);

		when(objectMapper.convertValue(any(User.class), ArgumentMatchers.<TypeReference<Map<String, Object>>>any()))
				.thenReturn(properties);
		when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
		when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
		when(criteriaQuery.from(User.class)).thenReturn(root);
		when(criteriaBuilder.conjunction()).thenReturn(predicate);

		when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
		when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
		when(typedQuery.getResultList()).thenReturn(List.of(user));

		Optional<User> foundUser = userPropertiesDao.findByProperties(user);

		assertTrue(foundUser.isPresent());
		User optionalUser = foundUser.get();
		assertEquals(user, optionalUser);
		assertEquals(userId, optionalUser.getUserId());
		assertEquals(name, optionalUser.getName());
		assertEquals(phoneNum, optionalUser.getPhoneNum());

	}

	@Test
	public void testFindByPropertiesWithNullUserReturnsEmptyOptional() {
		Optional<User> result = userPropertiesDao.findByProperties(null);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testFindByDynamicPropertiesWithEmptyProperties() {
		Map<String, Object> properties = new HashMap<>();

		when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
		when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
		when(criteriaQuery.from(User.class)).thenReturn(root);
		when(criteriaBuilder.conjunction()).thenReturn(predicate);
		when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
		when(criteriaQuery.where(predicate)).thenReturn(criteriaQuery);
		when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
		when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

		Optional<User> foundUser = userPropertiesDao.findByDynamicProperties(properties);

		assertTrue(foundUser.isEmpty());
	}

	@Test //findByDynamicProperties if (value != null) 조건문의 테스트
	public void testFindByDynamicPropertiesWithNullValues() {
		String userId = "test";
		String name = null;
		String phoneNum = "010-1234-5678";

		User user = new User();
		user.setUserId(userId);
		user.setPhoneNum(phoneNum);

		Map<String, Object> properties = new HashMap<>();
		properties.put("userId", userId);
		properties.put("name", name);
		properties.put("phoneNum", phoneNum);


		when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
		when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
		when(criteriaQuery.from(User.class)).thenReturn(root);
		when(criteriaBuilder.conjunction()).thenReturn(predicate);

		when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
		when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
		when(typedQuery.getResultList()).thenReturn(List.of(user));

		Optional<User> foundUser = userPropertiesDao.findByDynamicProperties(properties);

		assertTrue(foundUser.isPresent());
		User optionalUser = foundUser.get();
		assertEquals(user, optionalUser);
		assertEquals(userId, optionalUser.getUserId());
		assertNull(optionalUser.getName()); // Name is null
		assertEquals(phoneNum, optionalUser.getPhoneNum());
	}


	@Test// mapUserToProperties에서 properties가 null일 때의 테스트
	public void testFindByPropertiesWithPropertiesNull() {
		String userId = "test";
		String name = "test";
		String phoneNum = "010-1234-5678";

		User user = new User();
		user.setUserId(userId);
		user.setName(name);
		user.setPhoneNum(phoneNum);

		Map<String, Object> properties = new HashMap<>();
		properties.put("userId", null);
		properties.put("name", null);
		properties.put("phoneNum", null);

		when(objectMapper.convertValue(any(User.class), ArgumentMatchers.<TypeReference<Map<String, Object>>>any()))
				.thenReturn(properties);
		when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
		when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
		when(criteriaQuery.from(User.class)).thenReturn(root);
		when(criteriaBuilder.conjunction()).thenReturn(predicate);

		when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
		when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
		when(typedQuery.getResultList()).thenReturn(List.of(user));


		Optional<User> foundUser = userPropertiesDao.findByProperties(user);

		assertTrue(foundUser.isPresent());
		User optionalUser = foundUser.get();
		assertEquals(user, optionalUser);
		assertEquals(userId, optionalUser.getUserId());
		assertEquals(name, optionalUser.getName());
		assertEquals(phoneNum, optionalUser.getPhoneNum());

	}
}



