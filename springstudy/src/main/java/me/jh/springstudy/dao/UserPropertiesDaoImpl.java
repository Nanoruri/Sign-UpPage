package me.jh.springstudy.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.jh.springstudy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;
import java.util.Optional;


/**
 * 사용자 정보를 찾는 CustomDAO 구현 클래스.//
 */

@Repository
public class UserPropertiesDaoImpl implements UserPropertiesDao {
	@PersistenceContext
	private EntityManager entityManager;

	private final ObjectMapper objectMapper;


	@Autowired
	public UserPropertiesDaoImpl(EntityManager entityManager, ObjectMapper objectMapper) {
		this.entityManager = entityManager;
		this.objectMapper = objectMapper;
		this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}


	/**
	 * User 객체를 Map<String, Object> 형식의 검색 조건 맵으로 변환하여 findByDynamicProperties 메서드를 호출합니다.
	 *
	 * @param user 사용자 정보 객체
	 * @return Optional<User> 사용자 객체의 Optional
	 */
	@Override
	public Optional<User> findByProperties(User user) {

		if (user == null) {
			return Optional.empty();
		}
		Map<String, Object> properties = mapUserToProperties(user);
		return findByDynamicProperties(properties);
	}


	/**
	 * User 객체를 Map<String, Object> 형식의 검색 조건 맵으로 변환하는 유틸리티 메서드.
	 * 모든 필드를 자동으로 순회하면서 null 값을 체크하고 Map에 추가합니다.
	 *
	 * @param user 사용자 정보 객체
	 * @return 검색 조건을 담은 맵
	 */
	private Map<String, Object> mapUserToProperties(User user) {
		// ObjectMapper를 사용하여 Entity를 Map으로 변환
		Map<String, Object> properties = objectMapper.convertValue(user, new TypeReference<>() {
		});

		// null 값 제거
		properties.entrySet().removeIf(entry -> entry.getValue() == null);
		properties.remove("seq");//seq는 사용자의 가입 순서이므로 검색 조건으로 사용하지 않음

		return properties;
	}


	/**
	 * 동적으로 생성된 검색 조건을 받아 사용자 정보를 조회하는 메서드.
	 * Criteria API를 사용하여 동적 검색 조건을 적용합니다.
	 *
	 * @param properties 동적 검색 조건을 담은 맵
	 * @return Optional<User> 사용자 객체의 Optional
	 */

	public Optional<User> findByDynamicProperties(Map<String, Object> properties) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
		Root<User> root = query.from(User.class);

		// 동적으로 생성된 검색 조건 추가
		Predicate predicate = criteriaBuilder.conjunction(); // AND 조건을 위한 빈 Predicate 생성
		//Predicate는 동적으로 생성되는 검색 조건들을 모아두는 컨테이너이고, criteriaBuilder.conjunction()는 초기에 빈 Predicate를 생성하여 이를 시작점으로 활용

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value != null) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(key), value));
			}
		}

		query.select(root).where(predicate);


		User user = entityManager
				.createQuery(query)
				.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
		//todo :메서드 체이닝,  스트림 공부, getSingleResult()공부하기


		return Optional.ofNullable(user);
	}


}
