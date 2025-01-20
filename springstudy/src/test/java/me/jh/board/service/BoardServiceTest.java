package me.jh.board.service;

import me.jh.board.dao.BoardDao;
import me.jh.board.dto.board.BoardBasicDTO;
import me.jh.board.dto.board.BoardDTO;
import me.jh.board.dto.board.BoardNoCommentDTO;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import me.jh.springstudy.dao.UserDao;
import me.jh.springstudy.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardDao boardDao;

    @Mock
    private UserDao userDao;

    @Mock
    private User user;
    @Mock
    private User anotherUser;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(), "test@testEmail.com", LocalDateTime.now(), LocalDateTime.now(), "USER");
        anotherUser = new User("anotherUser", "anotherName", "anotherPassword", "010-1234-5678", LocalDate.now(), "test123@testEmail.com", LocalDateTime.now(), LocalDateTime.now(), "USER");
    }


    @Test//Create
    public void save() {

        long id = 1L;
        String title = "title";
        String content = "content";
        LocalDateTime date = LocalDateTime.now();


        Board post = new Board(id, title, content, date, "testTab", user);

        when(userDao.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(boardDao.save(post)).thenReturn(post);

        boolean result = boardService.saveBoard(user.getUserId(), post);

        assertTrue(result);
        verify(boardDao).save(post);
    }


    @Test
    public void saveBoard_UserNotFound() {
        String userId = "user123";
        Board post = new Board();
        post.setTabName("testTab");

        when(userDao.findById(userId)).thenReturn(Optional.empty());

        boolean result = boardService.saveBoard(userId, post);

        assertFalse(result);
        verify(boardDao, never()).save(any(Board.class));
    }


    /**
     * 일반 게시글 조회 테스트
     */
    @Test//Read
    public void findAll() {

        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);

        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", now, "testTab", user),
                new Board(2L, "title2", "content2", now, "testTab", user),
                new Board(3L, "title3", "content3", now, "testTab", user)
        );
        Page<Board> boardPage = new PageImpl<>(boardList, pageable, boardList.size());

        when(boardDao.findByTabName("testTab", pageable)).thenReturn(boardPage);

        Page<BoardNoCommentDTO> result = boardService.getBoard("testTab", pageable);

        verify(boardDao).findByTabName("testTab", pageable);
    }


    /**
     * 게시글 수정 테스트
     */
    @Test//Update
    public void update() {

        LocalDateTime now = LocalDateTime.now();

        //기존 게시글 리스트
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", now, "testTab", user),
                new Board(2L, "title2", "content2", now, "testTab", user),
                new Board(3L, "title3", "content3", now, "testTab", user)
        );

        // 업데이트할 게시글
        Board existingBoard = boardList.get(0);
        Board updatedBoard = new Board(1L, "title1", "updatedContent", now, "testTab", user);

        // 게시글을 찾아오기
        Long id = 1L;
        when(boardDao.findById(1L)).thenReturn(Optional.of(existingBoard));
        when(boardDao.save(existingBoard)).thenReturn(existingBoard);

        // 게시글 수정
        boolean result = boardService.updateBoard(id, user.getUserId(), updatedBoard);

        // 검증
        verify(boardDao).findById(1L);
        verify(boardDao).save(existingBoard);

        assertTrue(result);
        assertEquals("updatedContent", existingBoard.getContent());
    }


    @Test
    void updateBoardWithInvalidUserTest() {
        Long id = 1L;
        String invalidUserId = anotherUser.getUserId();
        Board existingBoard = new Board(id, "oldTitle", "oldContent", LocalDateTime.now(), "testTab", user);
        Board updatedBoard = new Board(id, "newTitle", "newContent", LocalDateTime.now(), "testTab", anotherUser);

        when(boardDao.findById(id)).thenReturn(Optional.of(existingBoard));

        boolean result = boardService.updateBoard(id, invalidUserId, updatedBoard);

        assertFalse(result);

        verify(boardDao).findById(id);
        verify(boardDao, never()).save(existingBoard);
    }


    @Test
    void updateBoardNotFoundTest() {
        Long id = 1L;
        String userId = user.getUserId();
        Board updatedBoard = new Board(id, "newTitle", "newContent", LocalDateTime.now(), "testTab", user);

        when(boardDao.findById(id)).thenReturn(Optional.empty());

        boolean result = boardService.updateBoard(id, userId, updatedBoard);

        assertFalse(result);

        verify(boardDao).findById(id);
        verify(boardDao, never()).save(any(Board.class));
    }


    /**
     * 특정 게시글 삭제 테스트
     */
    @Test
    public void delete() {
        LocalDateTime now = LocalDateTime.now();

        //기존 게시글 리스트
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", now, "testTab", user),
                new Board(2L, "title2", "content2", now, "testTab", user),
                new Board(3L, "title3", "content3", now, "testTab", user)
        );


        // 특정 조건으로 게시글을 찾기 위한 변수
        Long id = 1L;

        when(boardDao.findById(id)).thenReturn(Optional.of(boardList.get(0)));
        doNothing().when(boardDao).delete(boardList.get(0));

        boolean result = boardService.deleteBoard(id, user.getUserId());

        assertTrue(result);

        verify(boardDao).findById(id);
        verify(boardDao).delete(boardList.get(0));
    }


    @Test
    public void deleteBoard_BoardNotFound() {
        Long boardId = 1L;
        String userId = user.getUserId();

        // Mocking
        when(boardDao.findById(boardId)).thenReturn(Optional.empty()); // 게시물이 없는 상황 설정

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                boardService.deleteBoard(boardId, userId));

        // Assertion
        verify(boardDao).findById(boardId);
        verify(boardDao, never()).delete(any(Board.class));
    }

    @Test
    public void deleteBoard_UserNotCreator() {
        Long boardId = 1L;
        String userId = user.getUserId();
        Board board = new Board(boardId, "title", "content", LocalDateTime.now(), "testTab", anotherUser);

        when(boardDao.findById(boardId)).thenReturn(Optional.of(board));

        assertThrows(AccessDeniedException.class, () ->
                boardService.deleteBoard(boardId, userId));

        verify(boardDao).findById(boardId);
        verify(boardDao, never()).delete(board);
    }


    @Test
    public void testGetBoarDetailSuccessful() {
        long boardId = 1L;
        Board board = new Board(boardId, "Test Title", "Test Content", LocalDateTime.now(), "testTab", user);
        Comment comment = new Comment(1L, "Test Comment", LocalDateTime.now(), LocalDateTime.now(), board, "testCommentUser");
        board.setComments(List.of(comment));

        when(boardDao.findById(boardId)).thenReturn(Optional.of(board));

        BoardDTO result = boardService.getBoardDetail(boardId);

        assertEquals(1, result.getComments().size());
        assertEquals("Test Comment", result.getComments().get(0).getContent());
    }

    @Test
    public void testGetBoarDetailNotFound() {
        long boardId = 1L;

        when(boardDao.findById(boardId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            boardService.getBoardDetail(boardId);
        });

    }


    @Test
    public void searchPostsByTitle() {
        String query = "title1";
        String type = "title";
        Pageable pageable = PageRequest.of(0, 10);
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", user)
        );
        Page<Board> boardPage = new PageImpl<>(boardList, pageable, boardList.size());

        when(boardDao.findByTabNameAndTitleContaining("testTab", query, pageable)).thenReturn(boardPage);

        Page<BoardBasicDTO> result = boardService.searchPosts(query, type, pageable, "testTab");

        //객체가 아닌 내용물이 같은지 비교
        assertEquals(boardList.get(0).getId(), result.getContent().get(0).getId());
        verify(boardDao).findByTabNameAndTitleContaining("testTab", query, pageable);
    }

    @Test
    public void searchPostsByContent() {
        String query = "content1";
        String type = "content";
        Pageable pageable = PageRequest.of(0, 10);
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", user)
        );
        Page<Board> boardPage = new PageImpl<>(boardList, pageable, boardList.size());

        BoardBasicDTO pagingBoardBasicDTO = new BoardBasicDTO(
                boardList.get(0).getId(), boardList.get(0).getTitle(), boardList.get(0).getContent(), boardList.get(0).getDate(), boardList.get(0).getTabName());

        when(boardDao.findByTabNameAndContentContaining( "testTab", query, pageable)).thenReturn(boardPage);

        Page<BoardBasicDTO> result = boardService.searchPosts(query, type, pageable, "testTab");

        //객체가 아닌 내용물이 같은지 비교
        assertEquals(boardList.get(0).getId(), result.getContent().get(0).getId());
        verify(boardDao).findByTabNameAndContentContaining( "testTab", query, pageable);
    }

    @Test
    public void searchPostsByTitleAndContent() {
        String query = "title1";
        String type = "titleAndContent";
        Pageable pageable = PageRequest.of(0, 10);
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", user)
        );
        Page<Board> boardPage = new PageImpl<>(boardList, pageable, boardList.size());

        when(boardDao.findByTabNameAndTitleContainingOrContentContaining("testTab", query, query, pageable)).thenReturn(boardPage);

        Page<BoardBasicDTO> result = boardService.searchPosts(query, type, pageable, "testTab");

        //객체가 아닌 내용물이 같은지 비교
        assertEquals(boardList.get(0).getId(), result.getContent().get(0).getId());
        verify(boardDao).findByTabNameAndTitleContainingOrContentContaining("testTab", query, query, pageable);
    }

    @Test// 검색 타입이 없을 때 기본값으로 title 검색
    public void searchPostsInvalidType() {
        String query = "title1";
        String type = "invalidType";
        Pageable pageable = PageRequest.of(0, 10);
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", user)
        );
        Page<Board> boardPage = new PageImpl<>(boardList, pageable, boardList.size());

        when(boardDao.findByTabNameAndTitleContaining("testTab", query, pageable)).thenReturn(boardPage);

        Page<BoardBasicDTO> result = boardService.searchPosts(query, type, pageable, "testTab");

        assertNotNull(result);
    }

    @Test
    public void searchPosts_NullType() {
        String query = "title1";
        Pageable pageable = PageRequest.of(0, 10);
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", user)
        );
        Page<Board> boardPage = new PageImpl<>(boardList, pageable, boardList.size());

        // 기본값 "title"로 검색하는 mock 설정
        when(boardDao.findByTabNameAndTitleContaining("testTab", query, pageable)).thenReturn(boardPage);

        // 검색 타입이 null인 경우
        Page<BoardBasicDTO> result = boardService.searchPosts(query, null, pageable, "testTab");

        assertNotNull(result);
        verify(boardDao).findByTabNameAndTitleContaining("testTab", query, pageable);
    }

    @Test
    public void searchPosts_EmptyType() {
        String query = "title1";
        String type = "";//empty
        Pageable pageable = PageRequest.of(0, 10);
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", user)
        );
        Page<Board> boardPage = new PageImpl<>(boardList, pageable, boardList.size());

        // 기본값 "title"로 검색하는 mock 설정
        when(boardDao.findByTabNameAndTitleContaining("testTab", query, pageable)).thenReturn(boardPage);

        // 검색 타입이 빈 문자열인 경우
        Page<BoardBasicDTO> result = boardService.searchPosts(query, type, pageable, "testTab");

        assertNotNull(result);
        verify(boardDao).findByTabNameAndTitleContaining("testTab", query, pageable);
    }


    @Test
    public void testFindBoard_Success() {
        // Arrange
        String userId = user.getUserId();
        Long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setCreator(user);

        when(boardDao.findById(boardId)).thenReturn(Optional.of(board));

        // Act
        BoardBasicDTO result = boardService.findBoard(userId, boardId);

        // Assert
        assertNotNull(result);
        assertEquals(boardId, result.getId());
    }

    @Test
    public void testFindBoard_BoardNotFound() {
        // Arrange
        String userId = "user123";
        long boardId = 1L;

        when(boardDao.findById(boardId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boardService.findBoard(userId, boardId);
        });

        assertEquals("게시글이 존재하지 않거나 권한이 없습니다.", exception.getMessage());
    }

    @Test
    public void testFindBoard_UserMismatch() {
        // Arrange
        String userId = "user123";
        long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setCreator(anotherUser);

        when(boardDao.findById(boardId)).thenReturn(Optional.of(board));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boardService.findBoard(userId, boardId);
        });

        assertEquals("게시글이 존재하지 않거나 권한이 없습니다.", exception.getMessage());
    }

    @Test
    public void testIsUserAuthorized_returnsTrue_whenUserIsCreator() {
        BoardDTO boardDTO = new BoardDTO(1L, "title", "content", LocalDateTime.now(), "general", user.getUserId(), null);
        String userId = user.getUserId();

        boolean result = boardService.isUserAuthorized(boardDTO, userId);

        assertTrue(result);
    }

    @Test
    public void testIsUserAuthorized_returnsFalse_whenUserIsNotCreatorAndMemberTab() {
        BoardDTO boardDTO = new BoardDTO(1L, "title", "content", LocalDateTime.now(), "member", user.getUserId(), null);

        boolean result = boardService.isUserAuthorized(boardDTO, null);

        assertFalse(result);
    }

    @Test
    public void testIsUserAuthorized_returnsTrue_whenUserIsNotCreatorAndGeneralTab() {
        BoardDTO boardDTO = new BoardDTO(1L, "title", "content", LocalDateTime.now(), "general", user.getUserId(), null);

        boolean result = boardService.isUserAuthorized(boardDTO, null);

        assertTrue(result);
    }

    @Test
    public void testIsUserAuthorized_returnsTrue_whenUserIsNotCreatorAndMemberTabWithUserId() {
        BoardDTO boardDTO = new BoardDTO(1L, "title", "content", LocalDateTime.now(), "member", user.getUserId(), null);
        String userId = anotherUser.getUserId();

        boolean result = boardService.isUserAuthorized(boardDTO, userId);

        assertTrue(result);
    }
}