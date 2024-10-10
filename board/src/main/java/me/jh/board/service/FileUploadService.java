package me.jh.board.service;


import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileUploadService.class);
    private final String uploadDir = "E:/uploadTest";



    public String getSavePath() {
        String savePath;

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            savePath = System.getProperty("user.dir") + "\\files\\image";
        } else {
            savePath = System.getProperty("user.dir") + "/files/image";
        }

        return savePath;
    }

    
    public String saveImage(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }

        // 파일 이름 생성 (특수문자와 공백을 _로 대체)
        String originalFileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "_");
        String fileName = UUID.randomUUID() + "_" + originalFileName;

        // 파일 경로 설정
        File destFile = new File(uploadDir, fileName);

        // 파일 저장
        file.transferTo(destFile);

        // 저장 경로 확인용 로그 출력
        System.out.println("File saved at: " + destFile.getAbsolutePath());


        // 이미지 URL 리턴
        return "/study/images/" + fileName;
    }


}