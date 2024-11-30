package me.jh.core.utils.auth;

import me.jh.core.utils.PathUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathUtilsTest {


    private static final String TEST_ENV_VAR = "UPLOAD_DIR";

    @BeforeEach
    public void setUp() {
        // 환경 변수를 비우고 테스트 시작 전에 필요한 상태를 초기화
        System.clearProperty(TEST_ENV_VAR);
    }

    @Test
    void getSavePathReturnsDefaultWhenEnvVarNotSet() {
        System.clearProperty("UPLOAD_DIR");

        String expectedPath = new File("uploadTest").getAbsolutePath() + File.separator + "files" + File.separator + "image";
        String actualPath = PathUtils.getSavePath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getSavePathReturnsDefaultWhenEnvVarIsEmpty() {
        System.setProperty("UPLOAD_DIR", "");

        String expectedPath = new File("uploadTest").getAbsolutePath() + File.separator + "files" + File.separator + "image";
        String actualPath = PathUtils.getSavePath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getSavePathReturnsEnvVarWhenSet() {
        System.setProperty("UPLOAD_DIR", "C:\\CustomUploadDir");

        String expectedPath = new File("C:\\CustomUploadDir").getAbsolutePath() + File.separator + "files" + File.separator + "image";
        String actualPath = PathUtils.getSavePath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getResourceLocationReturnsDefaultWhenEnvVarNotSet() {
        System.clearProperty("UPLOAD_DIR");

        String expectedPath = "file:///" + new File("uploadTest").getAbsolutePath() + File.separator;
        String actualPath = PathUtils.getResourceLocation();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getResourceLocationReturnsDefaultWhenEnvVarIsEmpty() {
        System.setProperty("UPLOAD_DIR", "");

        String expectedPath = "file:///" + new File("uploadTest").getAbsolutePath() + File.separator;
        String actualPath = PathUtils.getResourceLocation();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getResourceLocationReturnsEnvVarWhenSet() {
        System.setProperty("UPLOAD_DIR", "C:\\CustomUploadDir");

        String expectedPath = "file:///" + new File("C:\\CustomUploadDir").getAbsolutePath() + File.separator;
        String actualPath = PathUtils.getResourceLocation();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getSavePathHandlesWindowsPathSeparator() {
        System.setProperty("os.name", "Windows 10");
        System.setProperty("UPLOAD_DIR", "C:\\CustomUploadDir");

        String expectedPath = new File("C:\\CustomUploadDir").getAbsolutePath() + File.separator + "files" + File.separator + "image";
        String actualPath = PathUtils.getSavePath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getSavePathHandlesUnixPathSeparator() {
        System.setProperty("os.name", "Linux");
        System.setProperty("UPLOAD_DIR", "/custom/upload/dir");

        String expectedPath = new File("/custom/upload/dir").getAbsolutePath() + File.separator + "files" + File.separator + "image";
        String actualPath = PathUtils.getSavePath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getResourceLocationHandlesWindowsPathSeparator() {
        System.setProperty("os.name", "Windows 10");
        System.setProperty("UPLOAD_DIR", "C:\\CustomUploadDir");

        String expectedPath = "file:///" + new File("C:\\CustomUploadDir").getAbsolutePath() + File.separator;
        String actualPath = PathUtils.getResourceLocation();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getResourceLocationHandlesUnixPathSeparator() {
        System.setProperty("os.name", "Linux");
        System.setProperty("UPLOAD_DIR", "/custom/upload/dir");

        String expectedPath = "file:///" + new File("/custom/upload/dir").getAbsolutePath() + File.separator;
        String actualPath = PathUtils.getResourceLocation();

        assertEquals(expectedPath, actualPath);
    }
}
