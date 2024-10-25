package me.jh.board.controller;

import me.jh.board.dao.BoardDao;
import me.jh.board.entity.Board;
import me.jh.board.service.BoardService;
import me.jh.board.service.FileUploadService;
import me.jh.core.utils.auth.JwtProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileTypeDetector;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

	@MockBean
	private FileUploadService fileUploadService;

	@MockBean
	private JwtProvider jwtProvider;

	@Mock
	private MockMultipartFile mockMultipartFile;



	@BeforeEach
	void setup() throws IOException {
		mockMvc = standaloneSetup(new BoardApiController(boardService, fileUploadService , jwtProvider)).build();

	}



	@Test
	public void saveBoardTest() throws Exception {
		long id = 1L;
		String title = "title";
		String content = "content";
		LocalDateTime date = LocalDateTime.now();
        String tab = "testTab";
		String userId = "testUser";

		String token = "Bearer MockToken";

		Board post = new Board(id, title, content, date, tab, userId);

		when(jwtProvider.getUserIdFromToken("mockJwtToken")).thenReturn(userId);
		when(boardService.saveBoard(userId, post)).thenReturn(true);

		mockMvc.perform(post("/board/api/create")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", token)
						.content("{\"id\":1,\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"date\":\"" + date + "\"}"))
				.andExpect(status().isOk());
	}


	@Test
	public void findGeneralBoardTest() throws Exception {

		Board post = new Board(1L, "title", "content", LocalDateTime.now(), "general", "testUser");

		when(boardService.getBoard("general")).thenReturn(List.of(post));

		mockMvc.perform(get("/board/api/generalBoard")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[{\"id\":1,\"title\":\"title\",\"content\":\"content\",\"tabName\":\"general\"}]"));

	}

	@Test
	public void findByTitleTest() throws Exception {
		long id = 1L;
		String title = "title";
		String content = "content";
		LocalDateTime date = LocalDateTime.now();
        String tab = "testTab";
		String user = "testUser";

		Board post = new Board(id, title, content, date, tab, user);

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
		String user = "testUser";
		String token = "Bearer testToken"; // JWT 토큰 형식 맞추기


		// 'testToken'에 대한 Mocking 설정
		when(jwtProvider.getUserIdFromToken("testToken")).thenReturn(user);
		when(boardService.updateBoard(eq(id), eq(user), any(Board.class))).thenReturn(true);

		mockMvc.perform(put("/board/api/update/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", token) // JWT 토큰을 헤더에 추가
						.content("{\"id\":1,\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"date\":\"" + date + "\"}"))
				.andExpect(status().isOk());
	}


	@Test
	public void updateBoard_FailTest() throws Exception {
		long id = 1L;
		String title = "title";
		String content = "content";
		LocalDateTime date = LocalDateTime.now();
		String user = "testUser";
		String token = "testToken";


		when(jwtProvider.getUserIdFromToken("mockJwtToken")).thenReturn(user);
		when(boardService.updateBoard(eq(id), eq(user), any(Board.class))).thenReturn(false);

		mockMvc.perform(put("/board/api/update/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", token)
						.content("{\"id\":1,\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"date\":\"" + date + "\"}"))
				.andExpect(status().isForbidden());
	}

	//Delete
	@Test
	public void deleteBoardTest() throws Exception {
		long id = 1L;
		String title = "title";
		String content = "content";
		LocalDateTime date = LocalDateTime.now();
        String tab = "testTab";
		String user = "testUser";

		Board post = new Board(id, title, content, date,tab, user);

		when(boardService.deleteBoard(post.getId())).thenReturn(true);

		mockMvc.perform(delete("/board/api/delete/{boardId}", post.getId()))
				.andExpect(status().isNoContent());
	}

	//Detail
	@Test
	public void getBoardDetailTest() throws Exception {
		long boardId = 1L;
		String token = "Bearer validToken";
		String userId = "testUser";
		Board board = new Board(boardId, "Test Title", "Test Content", LocalDateTime.now(), "testTab", userId);

		when(jwtProvider.getUserIdFromToken("validToken")).thenReturn(userId);
		when(boardService.getBoardDetail(boardId)).thenReturn(board);

		mockMvc.perform(get("/board/api/detail/{boardId}", boardId)
						.header("Authorization", token)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.board.id").value(boardId))
				.andExpect(jsonPath("$.board.title").value("Test Title"))
				.andExpect(jsonPath("$.board.content").value("Test Content"))
				.andExpect(jsonPath("$.isLogin").value(true));
	}

	@Test
	public void getBoardDetailNoTokenTest() throws Exception {
		long boardId = 1L;
		Board board = new Board(boardId, "Test Title", "Test Content", LocalDateTime.now(), "testTab", "testUser");

		when(boardService.getBoardDetail(boardId)).thenReturn(board);

		mockMvc.perform(get("/board/api/detail/{boardId}", boardId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.board.id").value(boardId))
				.andExpect(jsonPath("$.board.title").value("Test Title"))
				.andExpect(jsonPath("$.board.content").value("Test Content"))
				.andExpect(jsonPath("$.isLogin").value(false));
	}

	@Test
	public void searchPostsTest() throws Exception {
		String query = "title1";
		String type = "title";
		List<Board> boardList = List.of(
				new Board(1L, "title1", "content1", LocalDateTime.now(), "testTab", "testUser")
		);

		when(boardService.searchPosts(query, type)).thenReturn(boardList);

		mockMvc.perform(get("/board/api/search")
						.param("query", query)
						.param("type", type)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}


	@Test
	public void getMemberBoardTest() throws Exception {
		Board post = new Board(1L, "title", "content", LocalDateTime.now(), "member", "testUser");

		when(boardService.getBoard("member")).thenReturn(List.of(post));

		mockMvc.perform(get("/board/api/memberBoard")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[{\"id\":1,\"title\":\"title\",\"content\":\"content\",\"tabName\":\"member\"}]"));
	}

	@Test
	void uploadImageSuccessfully() throws Exception {
		String imageUrl = "http://localhost/images/test.jpg";
		mockMultipartFile = new MockMultipartFile( "image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());


		when(fileUploadService.saveImage(mockMultipartFile)).thenReturn(imageUrl);

		mockMvc.perform(multipart("/board/api/upload-image")
						.file(mockMultipartFile))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.imageUrl").value(imageUrl));
	}

	@Test
	void uploadImageFailsDueToException() throws Exception {
		mockMultipartFile = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

		when(fileUploadService.saveImage(mockMultipartFile)).thenThrow(new IOException("File upload error"));

		mockMvc.perform(multipart("/board/api/upload-image")
						.file(mockMultipartFile))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void findBoardSuccessTest() throws Exception {
		long boardId = 1L;
		String token = "Bearer validToken";
		String userId = "testUser";
		Board board = new Board(boardId, "Test Title", "Test Content", LocalDateTime.now(), "testTab", userId);

		when(jwtProvider.getUserIdFromToken("validToken")).thenReturn(userId);
		when(boardService.findBoard(userId, boardId)).thenReturn(board);

		mockMvc.perform(get("/board/api/getBoardInfo/{boardId}", boardId)
						.header("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(boardId))
				.andExpect(jsonPath("$.title").value("Test Title"))
				.andExpect(jsonPath("$.content").value("Test Content"))
				.andExpect(jsonPath("$.tabName").value("testTab"))
				.andExpect(jsonPath("$.creator").value(userId));
	}

	@Test
	public void findBoardFailTest() throws Exception {
		long boardId = 1L;
		String token = "Bearer validToken";
		String userId = "testUser";

		when(jwtProvider.getUserIdFromToken("validToken")).thenReturn(userId);
		when(boardService.findBoard(userId, boardId)).thenReturn(null);

		mockMvc.perform(get("/board/api/getBoardInfo/{boardId}", boardId)
						.header("Authorization", token))
				.andExpect(status().isForbidden());
	}
}
