package me.jh.board.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jh.board.entity.Board;
import me.jh.springstudy.MySpringBootApplication;
import me.jh.springstudy.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = MySpringBootApplication.class)
@ActiveProfiles("test")
@ExtendWith({SpringExtension.class})
@Import(ObjectMapper.class)
@DataJpaTest
public class BoardDetailDaoImplTest {


    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Board> criteriaQuery;

    @Mock
    private Root<Board> root;

    @Mock
    private TypedQuery<Board> typedQuery;

    @InjectMocks
    private BoardDetailDaoImpl boardDetailDao;


    @Test
    void getBoardDetail_returnsBoard_whenBoardExists() {
        Board board = new Board();
        board.setId(1L);
        board.setTitle("Test Title");
        board.setContent("Test Content");
        board.setDate(LocalDateTime.now());
        board.setTabName("Test Tab");
        board.setCreator(new User("testUser", "testName", "testPassword", "010-1234-5678", LocalDate.now(), "test@testEmail.com", LocalDateTime.now(), LocalDateTime.now(), "USER"));

        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(board));
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Board.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Board.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        Optional<Board> result = boardDetailDao.getBoardDetail(1L);

        assertTrue(result.isPresent());
        assertEquals(board, result.get());
    }

    @Test
    void getBoardDetail_returnsEmpty_whenBoardDoesNotExist() {
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Board.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Board.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        Optional<Board> result = boardDetailDao.getBoardDetail(1L);

        assertTrue(result.isEmpty());
    }
}
