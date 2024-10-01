package me.jh.board.controller;

import me.jh.board.dao.BoardDao;
import me.jh.board.entity.Board;
import me.jh.board.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@ContextConfiguration(classes = {BoardApiController.class})
@WebMvcTest(controllers = BoardApiController.class)
@AutoConfigureMockMvc(addFilters = false)// csrf 비활성화
public class BoardApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoardDao boardDao;

	@MockBean
	private BoardService boardService;

	@BeforeEach
	void setup() {
		mockMvc = standaloneSetup(new BoardApiController(boardService)).build();
	}



	@Test
	public void saveBoardTest() throws Exception {
		long id = 1L;
		String title = "title";
		String content = "content";
		LocalDateTime date = LocalDateTime.now();
        String tab = "testTab";

		Board post = new Board(id, title, content, date, tab);

		when(boardService.saveBoard(post)).thenReturn(true);


		mockMvc.perform(post("/board/api/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"id\":1,\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"date\":\"" + date + "\"}"))
				.andExpect(status().isOk());
	}


	@Test
	public void findAllTest() throws Exception {

		Board post = new Board(1L, "title", "content", LocalDateTime.now(), "testTab");

		boardDao.save(post);

		when(boardService.getBoard("testTab")).thenReturn(List.of(post));

		mockMvc.perform(get("/board/api/read"))
				.andExpect(status().isOk());
	}

	@Test
	public void findByTitleTest() throws Exception {
		long id = 1L;
		String title = "title";
		String content = "content";
		LocalDateTime date = LocalDateTime.now();
        String tab = "testTab";

		Board post = new Board(id, title, content, date,tab);

		when(boardService.getBoardByTitle(title)).thenReturn(post);

		mockMvc.perform(get("/board/api/search_content")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"id\":1,\"title\":\"" + title + "\"}"))//title만 으로 검색하였을 때..
				.andExpect(status().isOk());
	}

	@Test
	public void updateBoardTest() throws Exception {
		long id = 1L;
		String title = "title";
		String content = "content";
		LocalDateTime date = LocalDateTime.now();
        String tab = "testTab";

		Board post = new Board(id, title, content, date, tab);

		when(boardService.updateBoard(post)).thenReturn(true);

		mockMvc.perform(put("/board/api/update")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"id\":1,\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"date\":\"" + date + "\"}"))
				.andExpect(status().isOk());
	}

	//Delete
	@Test
	public void deleteBoardTest() throws Exception {
		long id = 1L;
		String title = "title";
		String content = "content";
		LocalDateTime date = LocalDateTime.now();
        String tab = "testTab";

		Board post = new Board(id, title, content, date,tab);

		when(boardService.deleteBoard(post.getId())).thenReturn(true);

		mockMvc.perform(delete("/board/api/delete/{boardId}", post.getId()))
				.andExpect(status().isNoContent());
	}

	//Detail
	@Test
	public void getBoardDetailTest() throws Exception {
		long boardId = 1L;
		Board board = new Board(boardId, "Test Title", "Test Content", LocalDateTime.now(), "testTab");

		when(boardService.getBoardDetail(boardId)).thenReturn(board);

		mockMvc.perform(get("/board/api/detail/{boardId}", boardId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}


	@Test
	public void searchPostsTest() throws Exception {
		String query = "title1";
		String type = "title";
		List<Board> boardList = List.of(
				new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab")
		);

		when(boardService.searchPosts(query, type)).thenReturn(boardList);

		mockMvc.perform(get("/board/api/search")
						.param("query", query)
						.param("type", type)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

}
