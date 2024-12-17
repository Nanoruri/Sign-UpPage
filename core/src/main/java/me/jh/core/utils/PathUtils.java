package me.jh.core.utils;

import java.io.File;

public class PathUtils {

    private static final String DEFAULT_UPLOAD_DIR = "uploadTest";

    // 파일 경로를 절대 경로로 변환하는 메서드
    public static String getSavePath() {
        String absolutePath = getUploadDir();
        String pathSeparator = File.separator;
        return absolutePath + pathSeparator + "files" + pathSeparator + "image";
    }

    // 리소스를 서빙하기 위한 경로를 반환
    public static String getResourceLocation() {
        String absolutePath = getUploadDir().replace("\\", "/");
        String pathSeparator = "/";
        return "file:///" + absolutePath + pathSeparator + "files" + pathSeparator + "image" + pathSeparator;
    }

    // 경로가 null이거나 비어 있으면 기본 경로를 사용하여 절대 경로를 반환
    private static String getUploadDir() {
        String uploadDir = System.getProperty("UPLOAD_DIR");

        if (uploadDir == null || uploadDir.isEmpty()) {
            uploadDir = DEFAULT_UPLOAD_DIR;
        }

        return new File(uploadDir).getAbsolutePath();
    }
}