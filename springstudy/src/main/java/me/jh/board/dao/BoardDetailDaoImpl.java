package me.jh.board.dao;

import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardDetailDaoImpl implements BoardDetailDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Board> getBoardDetail(long boardId) {
        // CriteriaBuilder를 이용한 쿼리 작성
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Board> cq = cb.createQuery(Board.class);
        Root<Board> boardRoot = cq.from(Board.class);

        // Fetch Join을 이용하여 연관된 Comments 엔티티를 조회
        boardRoot.fetch("comments", JoinType.LEFT);
        boardRoot.fetch("creator", JoinType.LEFT);

        // 조건 설정 (boardId와 일치하는 게시글만 조회)
        cq.where(cb.equal(boardRoot.get("id"), boardId));

        // 쿼리 실행
        TypedQuery<Board> query = entityManager.createQuery(cq);
        List<Board> result = query.getResultList();

        // 결과가 있으면 반환
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
