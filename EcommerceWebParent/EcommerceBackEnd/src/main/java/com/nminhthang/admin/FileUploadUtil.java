package com.nminhthang.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static final String USER_DIR_NAME = "user-photos/";
    public static final String CATEGORY_DIR_NAME = "category-images/";


    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex){
            throw new IOException("Could not save file: " + fileName, ex);
        }

    }

    public static void cleanDirectory(String directory){
        Path directoryPath = Paths.get(directory);

        try {
            Files.list(directoryPath).forEach(file -> {
                if (!Files.isDirectory(file)){
                    try {
                        Files.delete(file);
                    } catch (IOException exception){
                        LOGGER.error("Could not delete this file: " + file);
                        exception.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Could not list directory: " + directoryPath);
        }
    }

}
