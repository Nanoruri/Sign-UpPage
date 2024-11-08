package me.jh.board.dao;

import me.jh.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardDao extends JpaRepository<Board, Long>, BoardWtihCommentDao, BoardSearchDao {
    Optional<Board> findByTitle(String title);

    Page<Board> findByTitleContaining(String query, Pageable pageable);

    Page<Board> findByContentContaining(String query, Pageable pageable);

    Page<Board> findByTitleContainingOrContentContaining(String query, String query1, Pageable pageable);

    Page<Board> findByTabName(String tabName, Pageable pageable);

}
