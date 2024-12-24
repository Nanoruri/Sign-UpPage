package me.jh.board.dao;

import me.jh.board.entity.Board;

import java.util.Optional;

public interface BoardDetailDao {
    Optional<Board> getBoardDetail(long boardNo);
}
