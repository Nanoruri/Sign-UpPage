package me.jh.board.service;


import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileUploadService.class);


    public String uploadImage(MultipartFile[] uploadFile) {
        String savePath = getSavePath();
        File uploadPath = new File(savePath);

        // 파일 저장 경로가 없으면 신규 생성
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {
            String uploadFileName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();

            // 파일명 저장
            uploadFileName = uuid + "_" + uploadFileName;
            File saveFile = new File(uploadPath, uploadFileName);

            try {
                multipartFile.transferTo(saveFile);
                return uploadFileName;
            } catch (Exception e) {
                throw new RuntimeException("파일 업로드에 실패했습니다.", e);
            }
        }
        throw new RuntimeException("파일 업로드에 실패했습니다.");
    }


    public File downloadImage(String fileName) {
        String savePath = getSavePath();

        File file = new File(savePath, fileName);

        // 파일이 존재하지 않을 경우 RuntimeException을 발생시킵니다.
        if (!file.exists()) {
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }

        return file;
    }


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

    public byte[] getFileContent(File file) {
        try {
            return FileCopyUtils.copyToByteArray(file);
        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
            throw new RuntimeException("파일을 읽는 도중 오류가 발생했습니다.");
        }
    }


}