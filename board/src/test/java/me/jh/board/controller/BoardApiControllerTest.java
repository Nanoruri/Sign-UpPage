package me.jh.board.controller;

import me.jh.board.dao.BoardDao;
import me.jh.board.entity.Board;
import me.jh.board.service.AuthService;
import me.jh.board.service.BoardService;
import me.jh.board.service.FileUploadService;
import me.jh.core.utils.auth.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
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

    @MockBean
    private AuthService authService;

    @Mock
    private MockMultipartFile mockMultipartFile;


    @BeforeEach
    void setup() throws IOException {
        mockMvc = standaloneSetup(new BoardApiController(boardService, fileUploadService, jwtProvider, authService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

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

        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(boardService.saveBoard(userId, post)).thenReturn(true);

        mockMvc.perform(post("/board/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                     .content("{\"id\":1,\"title\":\"" + title + "\",\"content\":\"" + content + "\",\"date\":\"" + date + "\",\"tab\":\"" + tab + "\"}"))
                .andExpect(status().isOk());
    }


    @Test
    public void getGeneralBoard_returnsPagedResults() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> boardPage = new PageImpl<>(List.of(new Board(1L, "title", "content", LocalDateTime.now(), "general", "testUser")));

        when(boardService.getBoard("general", pageable)).thenReturn(boardPage);

        mockMvc.perform(get("/board/api/generalBoard")
                        .param("page", "0")  // 페이지 번호
                        .param("size", "10")  // 페이지 크기
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].title").value("title"));
    }

    @Test
    public void updateBoardTest() throws Exception {
        long id = 1L;
        String title = "title";
        String content = "content";
        LocalDateTime date = LocalDateTime.now();
        String user = "testUser";


        // 'testToken'에 대한 Mocking 설정
        when(authService.getAuthenticatedUserId()).thenReturn(user);
        when(boardService.updateBoard(eq(id), eq(user), any(Board.class))).thenReturn(true);

        mockMvc.perform(put("/board/api/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
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


        when(authService.getAuthenticatedUserId()).thenReturn(user);
        when(boardService.updateBoard(eq(id), eq(user), any(Board.class))).thenReturn(false);

        mockMvc.perform(put("/board/api/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
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

        Board post = new Board(id, title, content, date, tab, user);

        when(authService.getAuthenticatedUserId()).thenReturn(user);
        when(boardService.deleteBoard(post.getId(),user)).thenReturn(true);

        mockMvc.perform(delete("/board/api/delete/{boardId}", post.getId())
                        )
                .andExpect(status().isNoContent());
    }


    @Test
    public void deleteBoard_UserNotCreator() throws Exception {
        long boardId = 1L;
        String token = "Bearer validToken";
        String userId = "testUser";
        String anotherUserId = "anotherUser";
        Board board = new Board(boardId, "Test Title", "Test Content", LocalDateTime.now(), "testTab", anotherUserId);

        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(boardService.deleteBoard(boardId, userId)).thenReturn(false);

        mockMvc.perform(delete("/board/api/delete/{boardId}", board.getId()))
                .andExpect(status().isForbidden());
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
                .andExpect(jsonPath("$.isCreator").value(true));
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
                .andExpect(jsonPath("$.isCreator").value(false));
    }

    @Test
    public void searchPosts_returnsPagedResults() throws Exception {
        String thisTab = "general";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> boardPage = new PageImpl<>(List.of(new Board(1L, "title", "content", LocalDateTime.now(), "general", "testUser")));

        when(boardService.searchPosts("title", "title", pageable, thisTab)).thenReturn(boardPage);

        mockMvc.perform(get("/board/api/search")
                        .param("query", "title")
                        .param("type", "title")
                        .param("page", "0")
                        .param("size", "10")
                        .param("tabName", thisTab)// qurry문으로 날려주는 부분
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].title").value("title"));
    }


    @Test
    public void getMemberBoard_returnsPagedResults() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> boardPage = new PageImpl<>(List.of(new Board(1L, "title", "content", LocalDateTime.now(), "member", "testUser")));

        when(boardService.getBoard("member", pageable)).thenReturn(boardPage);

        mockMvc.perform(get("/board/api/memberBoard")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].title").value("title"));
    }

    @Test
    void uploadImageSuccessfully() throws Exception {
        String imageUrl = "http://localhost/images/test.jpg";
        mockMultipartFile = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());


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


        when(authService.getAuthenticatedUserId()).thenReturn(userId);
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

        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(boardService.findBoard(userId, boardId)).thenReturn(null);

        mockMvc.perform(get("/board/api/getBoardInfo/{boardId}", boardId)
                        .header("Authorization", token))
                .andExpect(status().isForbidden());
    }
}
