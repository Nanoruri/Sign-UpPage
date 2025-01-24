package me.jh.board.dto;

import me.jh.board.dto.board.BoardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class BoardDTOTest {


    private BoardDTO boardDTO;

    @BeforeEach
    public void setUp() {
        // 게시글 객체 생성
        boardDTO = new BoardDTO(1L, "Test Title", "Test Content", LocalDateTime.now(), "General", "user123", null);
    }

    @Test
    public void testToObject_whenUserIsCreator() {
        // userId가 게시글 작성자와 동일한 경우
        String userId = "user123";

        Map<String, Object> result = boardDTO.toObject(userId);

        assertTrue(result.containsKey("board"));
        assertTrue(result.containsKey("isCreator"));
        assertTrue(result.containsKey("currentUserId"));
        assertTrue(result.containsKey("creator"));

        // isCreator가 true이어야 함
        assertTrue((Boolean) result.get("isCreator"));

        // creator와 currentUserId가 일치해야 함
        assertEquals(userId, result.get("creator"));
        assertEquals(userId, result.get("currentUserId"));
    }

    @Test
    public void testToObject_whenUserIsNotCreator() {
        // userId가 게시글 작성자와 다른 경우
        String userId = "user456";

        Map<String, Object> result = boardDTO.toObject(userId);

        assertTrue(result.containsKey("board"));
        assertTrue(result.containsKey("isCreator"));
        assertTrue(result.containsKey("currentUserId"));
        assertTrue(result.containsKey("creator"));

        // isCreator가 false이어야 함
        assertFalse((Boolean) result.get("isCreator"));

        // creator와 currentUserId가 다르더라도, creator는 작성자 id여야 함
        assertEquals("user123", result.get("creator"));
        assertEquals(userId, result.get("currentUserId"));
    }

}
