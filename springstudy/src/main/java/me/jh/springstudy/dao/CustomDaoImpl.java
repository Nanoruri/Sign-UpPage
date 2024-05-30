package me.jh.springstudy.dao;

import me.jh.springstudy.entitiy.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;


/**
 * 사용자 정보를 찾는 CustomDAO 구현 클래스.//
 */
public class CustomDaoImpl implements CustomDao { //todo : 이 위치가..맞나..? DAO 인터페이스 있는데... Repository를 또 등록 해주는게 맞나...
	//todo : 클래스명 한번 더 생각해서 짜기
	@PersistenceContext
	private final EntityManager entityManager;

	public CustomDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Optional<User> findByProperties(String userId, String name, String phoneNum) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
		Root<User> root = query.from(User.class);

		// 동적으로 생성된 검색 조건 추가
		Predicate predicate = criteriaBuilder.conjunction(); // AND 조건을 위한 빈 Predicate 생성
		//Predicate는 동적으로 생성되는 검색 조건들을 모아두는 컨테이너이고, criteriaBuilder.conjunction()는 초기에 빈 Predicate를 생성하여 이를 시작점으로 활용

		//검색 조건 추가하는 코드
		if (userId != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("userId"), userId));
		}
		if (name != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("name"), name));
		}
		if (phoneNum != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("phoneNum"), phoneNum));
		}

		query.select(root).where(predicate);
		//CriteriaQuery에 검색 조건 설정

		//쿼리 실행 및 결과 반환
		User user = entityManager.createQuery(query).getResultList().stream().findFirst().orElse(null);
		//todo :메서드 체이닝,  스트림 공부, getSingleResult()공부하기

		// 동적으로 생성된 검색 조건에 맞는 사용자 반환
		return Optional.ofNullable(user);


//		Session session = entityManager.unwrap(Session.class);
//		Criteria criteria = session.createCriteria(User.class);//todo : createCriteria에서 JPA criteria로 변경
//		// 동적으로 생성된 검색 조건 추가
//		if (userId != null) {
//			criteria.add(Restrictions.eq("userId", userId));
//		}
//		if (name != null) {
//			criteria.add(Restrictions.eq("name", name));
//		}
//		if (email != null) {
//			criteria.add(Restrictions.eq("email", email));
//		}
//		User user = (User) criteria.uniqueResult();
//
//		// 동적으로 생성된 검색 조건에 맞는 모든 사용자 리스트 반환
//		return Optional.ofNullable(user);
	}
}
