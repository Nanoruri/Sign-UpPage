package me.jh.board.dao;

import me.jh.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearchDao {
    Page<Board> searchPosts(String query, String type, Pageable pageable, String tabName);
}
