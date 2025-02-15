package me.jh.board.service;


import me.jh.board.dao.BoardDao;
import me.jh.board.dto.board.BoardBasicDTO;
import me.jh.board.dto.board.BoardDTO;
import me.jh.board.dto.board.BoardNoCommentDTO;
import me.jh.board.dto.comment.CommentDTO;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import me.jh.springstudy.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    @PostConstruct
    public void init() {
        log.info("BoardService Bean 등록 성공");
    }

    private final BoardDao boardDao;
    private final UserDao userDao;

    @Autowired
    public BoardService(BoardDao boardDao, UserDao userDao) {
        this.boardDao = boardDao;
        this.userDao = userDao;
    }


    public boolean saveBoard(String userId, Board board) {

        try {//userId로 사용자 정보를 찾아서 게시글 작성자로 설정
            userDao.findById(userId).ifPresentOrElse(user -> {
                board.setTabName(board.getTabName());
                board.setDate(LocalDateTime.now());
                board.setCreator(user);

                boardDao.save(board);
            }, () -> {
                throw new IllegalArgumentException("사용자 정보가 존재하지 않습니다.");
            });
            return true;
        } catch (Exception e) {
            log.error("게시글 작성 중 오류 발생", e);
            return false;
        }
    }

    @Transactional//todo: DTO로 반환하도록 수정하기//todo: related problems Test 코드
    public Page<BoardNoCommentDTO> getBoard(String tabName, Pageable pageable) {
        Page<Board> boards = boardDao.findByTabName(tabName, pageable);
        return boards.map(board -> new BoardNoCommentDTO(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getDate(),
                board.getTabName(),
                board.getCreator().getUserId()
        ));
    }

    public boolean updateBoard(Long id, String userId, Board board) {
        Board oldBoard = boardDao.findById(id).orElse(null);
        if (oldBoard == null || !oldBoard.getCreator().getUserId().equals(userId)) {
            return false;
        }// todo: 게시글 변경 사항에 대해서만 변경하도록 수정하기

        oldBoard.setTitle(board.getTitle());
        oldBoard.setContent(board.getContent());
        oldBoard.setDate(LocalDateTime.now());

        boardDao.save(oldBoard);
        return true;
    }

    public boolean deleteBoard(Long id, String userId) {
        Board board = boardDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!board.getCreator().getUserId().equals(userId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        boardDao.delete(board);
        return true;
    }

    @Transactional
    public BoardDTO getBoardDetail(Long boardId) {
        Optional<Board> board = boardDao.findById(boardId);
        if (board.isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        List<CommentDTO> commentDTOs = board.get().getComments().stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getDate(),
                        comment.getUpdateDate(),
                        comment.getCreator().getUserId() // Lazy Loading 강제 초기화
                ))
                .collect(Collectors.toList());

        return new BoardDTO(
                board.get().getId(),
                board.get().getTitle(),
                board.get().getContent(),
                board.get().getDate(),
                board.get().getTabName(),
                board.get().getCreator().getUserId(),
                commentDTOs
        );
    }

    @Transactional
    public Page<BoardBasicDTO> searchPosts(String query, String type, Pageable pageable, String tabName) {
        if (type == null || type.isEmpty() || !List.of("title", "content", "titleAndContent").contains(type)) {
            type = "title";  // 기본값을 "title"로 설정
        }

        // Board 데이터를 검색하여 DTO로 변환
        Page<Board> searchResult = searchPostsByType(query, type, tabName, pageable);

        return searchResult.map(board -> new BoardBasicDTO(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getDate(),
                board.getTabName()
        ));
    }


    @Transactional
    public BoardBasicDTO findBoard(String userId, Long boardId) {
        return boardDao.findById(boardId)
                .filter(board -> board.getCreator().getUserId().equals(userId))
                .map(board -> new BoardBasicDTO(
                        board.getId(),
                        board.getTitle(),
                        board.getContent(),
                        board.getDate(),
                        board.getTabName()
                ))
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않거나 권한이 없습니다."));
    }


    public boolean isUserAuthorized(BoardDTO board, String userId) {
        boolean isMember = "member".equals(board.getTabName());
        return !(isMember && userId == null);
    }


    private Page<Board> searchPostsByType(String query, String type, String tabName, Pageable pageable) {
        switch (type) {
            case "title":
                return searchByTitle(query, tabName, pageable);
            case "content":
                return searchByContent(query, tabName, pageable);
            default:
                return searchByTitleAndContent(query, tabName, pageable);
        }
    }

    private Page<Board> searchByTitle(String query, String tabName, Pageable pageable) {
        return boardDao.findByTabNameAndTitleContaining(tabName, query, pageable);
    }

    private Page<Board> searchByContent(String query, String tabName, Pageable pageable) {
        return boardDao.findByTabNameAndContentContaining(tabName, query, pageable);
    }

    private Page<Board> searchByTitleAndContent(String query, String tabName, Pageable pageable) {
        return boardDao.findByTabNameAndTitleContainingOrContentContaining(tabName, query, query, pageable);
    }
}

