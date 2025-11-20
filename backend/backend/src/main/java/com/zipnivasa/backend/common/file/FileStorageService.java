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

/**
 * Handles saving uploaded files to disk.
 * Similar to your multer config that saves into "uploads/pgs".
 */
@Service
public class FileStorageService {

    // Base directory where uploads will be stored (configurable)
    // e.g. "uploads" → actual path "uploads/pgs"
    @Value("${file.upload-base-dir:uploads}")
    private String uploadBaseDir;

    /**
     * Save a file under a subfolder (like "pgs", "messes")
     * Returns the relative path used by frontend, e.g. /uploads/pgs/<filename>
     */
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
