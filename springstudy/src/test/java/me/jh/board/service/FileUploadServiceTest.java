package me.jh.board.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileUploadServiceTest {


    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileUploadService fileUploadService;

    @TempDir
    private Path tempDir;


    @BeforeEach
    void setUp() {

    }


    @AfterEach
    void tearDown() {
        // 테스트 후에는 환경 변수 또는 시스템 속성 초기화
        System.clearProperty("UPLOAD_DIR");
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

    @Test
    void testSaveImageSuccessfully() throws IOException {
        String originalFilename = "test.jpg";
        String savePath = "E:/uploadTest";
        File destFile = new File(savePath, UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9.]", "_"));

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(originalFilename);
        doNothing().when(multipartFile).transferTo(any(File.class));

        String result = fileUploadService.saveImage(multipartFile);

        assertNotNull(result);
        assertTrue(result.contains("/study/images/"));
        assertTrue(result.endsWith("_" + originalFilename.replaceAll("[^a-zA-Z0-9.]", "_")));
    }

    @Test
    void testSaveImageFailsDueToEmptyFile() {
        when(multipartFile.isEmpty()).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fileUploadService.saveImage(multipartFile);
        });

        assertEquals("파일이 없습니다.", exception.getMessage());
    }

    @Test
    void testSaveImageFailsDueToIOException() throws IOException {
        String originalFilename = "test.jpg";
        String savePath = tempDir.resolve("image").toString();
        File destFile = new File(savePath, UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9.]", "_"));

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(originalFilename);
        doThrow(new IOException("File transfer error")).when(multipartFile).transferTo(any(File.class));

        IOException exception = assertThrows(IOException.class, () -> {
            fileUploadService.saveImage(multipartFile);
        });

        assertEquals("File transfer error", exception.getMessage());
    }


}
