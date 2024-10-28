package me.jh.board.service;

import me.jh.board.dao.BoardDao;
import me.jh.board.entity.Board;
import me.jh.board.entity.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardDao boardDao;
    @InjectMocks
    private BoardService boardService;


    @Test//Create
    public void save() {

        long id = 1L;
        String title = "title";
        String content = "content";
        LocalDateTime date = LocalDateTime.now();
        String user = "testUser";

        Board post = new Board(id, title, content, date, "testTab", user);

        when(boardDao.save(post)).thenReturn(post);

        boolean result = boardService.saveBoard(user, post);

        verify(boardDao).save(post);
    }


    /**
     * 일반 게시글 조회 테스트
     */
    @Test//Read
    public void findAll() {

        LocalDateTime now = LocalDateTime.now();

        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", now, "testTab", "testUser"),
                new Board(2L, "title2", "content2", now, "testTab", "testUser"),
                new Board(3L, "title3", "content3", now, "testTab", "testUser")
        );
        boardList.forEach(board -> board.setComments(new ArrayList<>()));

        when(boardDao.findByTabName("testTab")).thenReturn(boardList);

        List<Board> result = boardService.getBoard("testTab");

        verify(boardDao).findByTabName("testTab");
    }

    /**
     * 특정 제목의 게시글 조회 테스트
     */
    @Test//Read2
    public void findByTitle() {

        LocalDateTime now = LocalDateTime.now();

        // 기존 게시글 리스트
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", now, "testTab", "testUser"),
                new Board(2L, "title2", "content2", now, "testTab", "testUser"),
                new Board(3L, "title3", "content3", now, "testTab", "testUser")
        );

        // 찾아올 게시글의 제목
        String title = "title1";

        // 특정 제목의 게시글을 찾아오기
        when(boardDao.findByTitle(title)).thenReturn(Optional.of(boardList.get(0)));

        // 게시글 찾아오기
        Board result = boardService.getBoardByTitle(title);

        // 검증
        verify(boardDao).findByTitle(title);

        // Optional로 찾아서 만약 여러게시글이 있을 경우(Optional은 하나만을 반환하니 false일 경우) List로 변환해서 반환하도록 하기
    }


    /**
     * 게시글 수정 테스트
     */
    @Test//Update
    public void update() {

        LocalDateTime now = LocalDateTime.now();

        //기존 게시글 리스트
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", now, "testTab", "testUser"),
                new Board(2L, "title2", "content2", now, "testTab", "testUser"),
                new Board(3L, "title3", "content3", now, "testTab", "testUser")
        );

        // 업데이트할 게시글
        Board existingBoard = boardList.get(0);
        Board updatedBoard = new Board(1L, "title1", "updatedContent", now, "testTab", "testUser");

        // 게시글을 찾아오기
        Long id = 1L;
        when(boardDao.findById(1L)).thenReturn(Optional.of(existingBoard));
        when(boardDao.save(existingBoard)).thenReturn(existingBoard);

        // 게시글 수정
        boolean result = boardService.updateBoard(id, "testUser", updatedBoard);

        // 검증
        verify(boardDao).findById(1L);
        verify(boardDao).save(existingBoard);

        assertTrue(result);
        assertEquals("updatedContent", existingBoard.getContent());
    }


    @Test
    void updateBoardWithInvalidUserTest() {
        Long id = 1L;
        String userId = "testUser";
        String invalidUserId = "invalidUser";
        Board existingBoard = new Board(id, "oldTitle", "oldContent", LocalDateTime.now(), "testTab", userId);
        Board updatedBoard = new Board(id, "newTitle", "newContent", LocalDateTime.now(), "testTab", invalidUserId);

        when(boardDao.findById(id)).thenReturn(Optional.of(existingBoard));

        boolean result = boardService.updateBoard(id, invalidUserId, updatedBoard);

        assertFalse(result);

        verify(boardDao).findById(id);
        verify(boardDao, never()).save(existingBoard);
    }


    @Test
    void updateBoardNotFoundTest() {
        Long id = 1L;
        String userId = "testUser";
        Board updatedBoard = new Board(id, "newTitle", "newContent", LocalDateTime.now(), "testTab", userId);

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
                new Board(1L, "title1", "content1", now, "testTab", "testUser"),
                new Board(2L, "title2", "content2", now, "testTab", "testUser"),
                new Board(3L, "title3", "content3", now, "testTab", "testUser")
        );


        // 특정 조건으로 게시글을 찾기 위한 변수
        Long id = 1L;

        when(boardDao.findById(id)).thenReturn(Optional.of(boardList.get(0)));
        doNothing().when(boardDao).delete(boardList.get(0));

        boolean result = boardService.deleteBoard(id);

        verify(boardDao).findById(id);
        verify(boardDao).delete(boardList.get(0));
    }


    @Test
    public void testGetBoarDetailSuccessful() {
        long boardId = 1L;
        Board board = new Board(boardId, "Test Title", "Test Content", LocalDateTime.now(), "testTab", "testUser");
        Comment comment = new Comment(1L, "Test Comment", LocalDateTime.now(), board, "testCommentUser");
        board.setComments(List.of(comment));

        when(boardDao.findById(boardId)).thenReturn(Optional.of(board));

        Board result = boardService.getBoardDetail(boardId);

        assertEquals(board, result);
        assertEquals(1, result.getComments().size());
        assertEquals("Test Comment", result.getComments().get(0).getContent());
    }

    @Test
    public void testGetBoarDetailNotFoundTest() {
        long boardId = 1L;

        when(boardDao.findById(boardId)).thenReturn(Optional.empty());

        Board result = boardService.getBoardDetail(boardId);

        assertNull(result);
    }


    @Test
    public void searchPostsByTitle() {
        String query = "title1";
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", "testUser")
        );

        when(boardDao.findByTitleContaining(query)).thenReturn(boardList);

        List<Board> result = boardService.searchPosts(query, "title");

        assertEquals(boardList, result);
    }

    @Test
    public void searchPostsByContent() {
        String query = "content1";
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", "testUser")
        );

        when(boardDao.findByContentContaining(query)).thenReturn(boardList);

        List<Board> result = boardService.searchPosts(query, "content");

        assertEquals(boardList, result);
    }

    @Test
    public void searchPostsByTitleAndContent() {
        String query = "title1";
        List<Board> boardList = List.of(
                new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", "testUser")
        );

        when(boardDao.findByTitleContainingOrContentContaining(query, query)).thenReturn(boardList);

        List<Board> result = boardService.searchPosts(query, "titleAndContent");

        assertEquals(boardList, result);
    }

    @Test
    public void searchPostsInvalidType() {
        String query = "title1";

        List<Board> result = boardService.searchPosts(query, "invalidType");

        assertEquals(0, result.size());
    }


    @Test
    public void testFindBoard_Success() {
        // Arrange
        String userId = "user123";
        Long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setCreator(userId);

        when(boardDao.findById(boardId)).thenReturn(Optional.of(board));

        // Act
        Board result = boardService.findBoard(userId, boardId);

        // Assert
        assertNotNull(result);
        assertEquals(boardId, result.getId());
        assertEquals(userId, result.getCreator());
    }

    @Test
    public void testFindBoard_BoardNotFound() {
        // Arrange
        String userId = "user123";
        Long boardId = 1L;

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
        Long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setCreator("anotherUser");

        when(boardDao.findById(boardId)).thenReturn(Optional.of(board));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            boardService.findBoard(userId, boardId);
        });

        assertEquals("게시글이 존재하지 않거나 권한이 없습니다.", exception.getMessage());
    }

}