package me.jh.board.service;


import me.jh.board.dao.BoardDao;
import me.jh.board.entity.Board;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BoardService {

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    @PostConstruct
    public void init() {
        log.info("BoardService Bean 등록 성공");
    }

    private final BoardDao boardDao;

    @Autowired
    public BoardService(BoardDao boardDao) {
        this.boardDao = boardDao;
    }


    public boolean saveBoard(String userId, Board board) {
        board.setTabName(board.getTabName());
        board.setDate(LocalDateTime.now());
        board.setCreator(userId);

        boardDao.save(board);
        return true;
    }

    @Transactional
    public Page<Board> getBoard(String tabName, Pageable pageable) {
        Page<Board> boards = boardDao.findByTabName(tabName, pageable);
//		boards.forEach(board -> board.getComments().size());//todo: 강제로 comments를 초기화하는 꼼수. fetch= EAGER와 비슷하게 동작하니 고쳐놓기
        boards.forEach(board -> board.setComments(null));
        return boards;
    }

    public boolean updateBoard(Long id, String userId, Board board) {
        Board oldBoard = boardDao.findById(id).orElse(null);
        if (oldBoard == null || !oldBoard.getCreator().equals(userId)) {
            return false;
        }// todo: 게시글 변경 사항에 대해서만 변경하도록 수정하기

        oldBoard.setTitle(board.getTitle());
        oldBoard.setContent(board.getContent());
        oldBoard.setDate(LocalDateTime.now());

        boardDao.save(oldBoard);
        return true;
    }

    public boolean deleteBoard(Long id, String userId) {
        Board board = boardDao.findById(id).orElse(null);
        if (board == null || !board.getCreator().equals(userId)) {
            return false;
        }

        boardDao.delete(board);
        return true;
    }

    @Transactional
    public Board getBoardDetail(Long boardId) {
        Optional<Board> board = boardDao.findById(boardId);
        if (board.isPresent()) {
            board.get().getComments().size();//todo: 강제로 comments를 초기화하는 꼼수. fetch= EAGER와 비슷하게 동작하니 고쳐놓기
        }
        return board.orElse(null);
    }

    @Transactional
    public Page<Board> searchPosts(String query, String type, Pageable pageable, String tabName) {
        return boardDao.searchPosts(query, type, pageable, tabName);
    }

    //todo: 토큰의 사용자 ID와 게시판 아이디를 받아  DB 내 작성자 ID와 비교하여 일치하면 게시글 반환
    @Transactional
    public Board findBoard(String userId, Long boardId) {
        Optional<Board> board = boardDao.findById(boardId);

        if (board.isPresent() && board.get().getCreator().equals(userId)) {
            board.get().setComments(null);// todo: 임시 comment 강제초기화
            return board.get();
        }
        throw new IllegalArgumentException("게시글이 존재하지 않거나 권한이 없습니다.");
    }


    public boolean isUserAuthorized(Board board, String userId) {
        boolean isMember = "member".equals(board.getTabName());
        return !(isMember && userId == null);
    }
}
