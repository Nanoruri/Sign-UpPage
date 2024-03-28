package me.jh.springstudy.repositorydao;

import me.jh.springstudy.entitiy.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CustomDaoTest {


	@Mock
	private EntityManager entityManager;
	@Mock
	private User user;
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
	private CustomDaoImpl customDao;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	/**
	 * 사용자 정보 조회 테스트
	 * findByProperties 메서드를 호출하여 사용자 정보를 조회하고, 조회한 사용자 정보가 예상한 것과 일치하는지 확인한다.
	 */

	@Test
	public void testFindByProperties() {

		String userId = "test";
		String name = "test";
		String phoneNum = "010-1234-5678";


		user = new User();
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
		when(criteriaQuery.where(predicate)).thenReturn(criteriaQuery);

		doReturn(criteriaQuery).when(criteriaQuery).where(predicate);


		when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
		when(typedQuery.getSingleResult()).thenReturn(user);

		Optional<User> foundUser = customDao.findByProperties(user.getUserId(), user.getName(), user.getPhoneNum());
		//todo: NullPointException 발생함. 원인 찾기

		assertTrue(foundUser.isPresent());
		User optionalUser = foundUser.get();
		assertEquals(user, optionalUser);
		assertEquals(userId, optionalUser.getUserId());
		assertEquals(name, optionalUser.getName());
		assertEquals(phoneNum, optionalUser.getPhoneNum());





	}


}
