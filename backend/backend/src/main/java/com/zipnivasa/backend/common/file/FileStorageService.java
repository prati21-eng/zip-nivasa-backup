package com.zipnivasa.backend.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Service
public class FileStorageService {

    @Value("${file.upload-base-dir:uploads}")
    private String uploadBaseDir;

    public String saveFile(String subFolder, MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            originalName = "file";
        }

        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String safeName = timestamp + "-" + originalName.replaceAll("\\s+", "_");

        String root = System.getProperty("user.dir");   // project root
        Path folderPath = Paths.get(root, uploadBaseDir, subFolder);
        Files.createDirectories(folderPath);

        Path filePath = folderPath.resolve(safeName);
        file.transferTo(filePath.toFile());

        return "/uploads/" + subFolder + "/" + safeName;

    }
}
