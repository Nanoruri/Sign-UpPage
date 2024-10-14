package me.jh.board.entity;


import me.jh.board.dao.CommentDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("boardTest")
@DataJpaTest
public class CommentTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentDao commentDao;

    @Test
    public void testSaveComment() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab");
        entityManager.persist(board);

        Comment comment = new Comment(0, "Test Comment", LocalDateTime.now(), board);

        // when
        Comment savedComment = entityManager.persistAndFlush(comment);

        // then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("Test Comment");
        assertThat(savedComment.getBoard()).isEqualTo(board);
    }

    @Test
    public void testFindById() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab");
        entityManager.persistAndFlush(board);

        Comment comment = new Comment(0, "Test Comment", LocalDateTime.now(), board);
        entityManager.persistAndFlush(comment);

        // when
        Optional<Comment> foundComment = commentDao.findById(comment.getId());

        // then
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("Test Comment");
    }

    @Test
    public void testDeleteComment() {
        // given
        Board board = new Board(0, "Test Title", "Test Content", LocalDateTime.now(), "testTab");
        entityManager.persistAndFlush(board);

        Comment comment = new Comment(0, "Test Comment", LocalDateTime.now(), board); // Ensure ID is null
        entityManager.persistAndFlush(comment);

        // when
        commentDao.deleteById(comment.getId());
        Optional<Comment> deletedComment = commentDao.findById(comment.getId());

        // then
        assertThat(deletedComment).isNotPresent();
    }
}
