package me.jh.board.dao;

import me.jh.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // todo: DTO사용하면BoardSearchDao, BoardDetailDao 필요 없음
public interface BoardDao extends JpaRepository<Board, Long>, BoardSearchDao {
    Optional<Board> findByTitle(String title);

    Page<Board> findByTabName(String tabName, Pageable pageable);

    // 제목으로 검색
    Page<Board> findByTabNameAndTitleContaining(String tabName, String title, Pageable pageable);

    // 내용으로 검색
    Page<Board> findByTabNameAndContentContaining(String tabName, String content, Pageable pageable);

    // 제목과 내용으로 검색
    Page<Board> findByTabNameAndTitleContainingOrContentContaining(String tabName, String title, String content, Pageable pageable);
}
