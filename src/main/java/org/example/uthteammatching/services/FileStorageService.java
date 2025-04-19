package org.example.uthteammatching.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileStorageService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_EXTENSIONS = {"pdf", "doc", "docx", "xls", "xlsx"};

    @Value("${file.upload-dir:/uploads}")
    private String UPLOAD_DIR;

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Tệp không được để trống.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("Tên tệp không hợp lệ.");
        }

        // Làm sạch tên tệp
        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Tệp " + fileName + " vượt quá kích thước 10MB.");
        }

        String extension = getFileExtension(fileName).toLowerCase();
        boolean isAllowed = false;
        for (String ext : ALLOWED_EXTENSIONS) {
            if (ext.equals(extension)) {
                isAllowed = true;
                break;
            }
        }
        if (!isAllowed) {
            throw new IllegalArgumentException("Tệp " + fileName + " không được hỗ trợ. Chỉ hỗ trợ: pdf, doc, docx, xls, xlsx.");
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        String filePath = "/uploads/" + uniqueFileName;

        // Đảm bảo thư mục uploads tồn tại
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            System.out.println("Creating upload directory: " + uploadDir.getAbsolutePath());
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("Không thể tạo thư mục uploads: " + uploadDir.getAbsolutePath());
            }
        }

        if (!uploadDir.canWrite()) {
            throw new IOException("Không có quyền ghi vào thư mục: " + uploadDir.getAbsolutePath());
        }

        System.out.println("Saving file: " + uniqueFileName);
        File dest = new File(uploadDir, uniqueFileName);
        try {
            file.transferTo(dest);
            System.out.println("File saved successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save file: " + uniqueFileName + ", Error: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Không thể lưu tệp " + uniqueFileName + ": " + e.getMessage(), e);
        }

        return filePath;
    }

    public String determineFileType(String fileName) {
        if (fileName == null) {
            return "Khác";
        }
        String extension = getFileExtension(fileName).toLowerCase();
        switch (extension) {
            case "pdf":
                return "PDF";
            case "doc":
            case "docx":
                return "Word";
            case "xls":
            case "xlsx":
                return "Excel";
            default:
                return "Khác";
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return lastIndex >= 0 ? fileName.substring(lastIndex + 1) : "";
    }
}