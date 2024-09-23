package me.jh.board.dao;

import me.jh.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardDao extends JpaRepository<Board,Long> {
	Optional<Board> findByTitle(String title);
}
