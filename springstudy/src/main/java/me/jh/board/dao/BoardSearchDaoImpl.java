package me.jh.board.dao;


import me.jh.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BoardSearchDaoImpl implements BoardSearchDao {

    private final EntityManager entityManager;

    public BoardSearchDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<Board> searchPosts(String query, String type, Pageable pageable, String tabName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Board> cq = cb.createQuery(Board.class);
        Root<Board> board = cq.from(Board.class);

        board.fetch("comments", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(cb, board, query, type, tabName);

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        List<Board> boards = executeQuery(cq, pageable);

        Long totalCount = getTotalCount(cb, predicates);

        return new PageImpl<>(boards, pageable, totalCount);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Board> board, String query, String type, String tabName) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(board.get("tabName"), tabName));

        if (type == null) {
            type = "title";  // 기본값을 "title"로 설정
        }

        if (query != null && !query.isEmpty()) {
            switch (type) {
                case "title":
                    predicates.add(cb.like(board.get("title"), "%" + query + "%"));
                    break;
                case "content":
                    predicates.add(cb.like(board.get("content"), "%" + query + "%"));
                    break;
                case "titleAndContent":
                    Predicate titleOrContentPredicate = cb.or(
                            cb.like(board.get("title"), "%" + query + "%"),
                            cb.like(board.get("content"), "%" + query + "%")
                    );
                    predicates.add(titleOrContentPredicate);
                    break;
                default:
            }
        }
        return predicates;
    }

    private List<Board> executeQuery(CriteriaQuery<Board> cq, Pageable pageable) {
        TypedQuery<Board> queryResult = entityManager.createQuery(cq);
        queryResult.setFirstResult((int) pageable.getOffset());
        queryResult.setMaxResults(pageable.getPageSize());
        return queryResult.getResultList();
    }

    private Long getTotalCount(CriteriaBuilder cb, List<Predicate> predicates) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Board.class)))
                .where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
