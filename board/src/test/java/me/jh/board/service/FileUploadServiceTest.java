package me.jh.board.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FileUploadServiceTest {


    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileUploadService fileUploadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadImageSuccessfully() throws IOException {
        String originalFilename = "test.jpg";


        when(multipartFile.getOriginalFilename()).thenReturn(originalFilename);
        doNothing().when(multipartFile).transferTo(any(File.class));

        String result = fileUploadService.uploadImage(new MultipartFile[]{multipartFile});

        assertNotNull(result);  // 결과가 null이 아님을 확인
        assertTrue(result.endsWith("_" + originalFilename)); // 결과 파일 이름이 기대대로 형성되었는지 확인
        assertEquals(36 + 1 + originalFilename.length(), result.length());
    }

    @Test
    void testUploadImageFails() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        doThrow(new IOException("File upload error")).when(multipartFile).transferTo(any(File.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileUploadService.uploadImage(new MultipartFile[]{multipartFile});
        });

        assertEquals("파일 업로드에 실패했습니다.", exception.getMessage());
    }

    @Test// 이거 고치기
    void downloadImageSuccessfully() {
        String fileName = "test.jpg";
        File file = new File(fileUploadService.getSavePath(), fileName);


        File result = fileUploadService.downloadImage(fileName);

        assertNotNull(result);
        assertEquals(file.getPath(), result.getPath());
    }

    @Test
    void testDownloadImageFileNotFound() {
        String fileName = "nonexistent.jpg";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileUploadService.downloadImage(fileName);
        });

        assertEquals("파일을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void testGetFileContentSuccessfully() throws IOException {
        // 테스트 파일 생성 및 내용 작성
        File file = new File("test.txt");
        Files.write(file.toPath(), "file content".getBytes());

        // 테스트 수행
        byte[] expectedContent = "file content".getBytes();
        byte[] result = fileUploadService.getFileContent(file);

        assertArrayEquals(expectedContent, result);

        // 테스트 후 파일 삭제
        file.delete();
    }

    @Test
    void testGetFileContentFails() throws IOException {
        // 존재하지 않는 파일 경로
        File nonExistentFile = new File("non_existent_file.txt");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileUploadService.getFileContent(nonExistentFile);
        });


        assertEquals("파일을 읽는 도중 오류가 발생했습니다.", exception.getMessage());
    }


    @Test
    void testGetSavePathWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        System.setProperty("os.name", "Windows 10");

        String expectedPath = System.getProperty("user.dir") + "\\files\\image";

        String result = fileUploadService.getSavePath();

        assertEquals(expectedPath, result);

        System.setProperty("os.name", os);
    }

    @Test
    void testGetSavePathLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        System.setProperty("os.name", "Linux");

        String expectedPath = System.getProperty("user.dir") + "/files/image";

        String result = fileUploadService.getSavePath();

        assertEquals(expectedPath, result);

        System.setProperty("os.name", os);
    }

    @Test
    void testGetSavePathMac() {
        String os = System.getProperty("os.name").toLowerCase();
        System.setProperty("os.name", "Mac OS X");

        String expectedPath = System.getProperty("user.dir") + "/files/image";

        String result = fileUploadService.getSavePath();

        assertEquals(expectedPath, result);

        System.setProperty("os.name", os);
    }


    @Test
    public void testUUIDGeneration() {
        // When
        String uuid = UUID.randomUUID().toString();

        // Then
        assertNotNull(uuid);  // UUID가 null이 아닌지 확인
        assertEquals(36, uuid.length()); // UUID의 길이는 항상 36이어야 함
        assertTrue(uuid.matches("^[a-f0-9\\-]{36}$"));  // UUID 형식이 맞는지 확인
    }
}
