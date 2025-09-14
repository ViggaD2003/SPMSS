package com.fpt.gsu25se47.schoolpsychology.service.inter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface FileUploadService {

    Map<String, String> uploadFile(MultipartFile file) throws IOException;

    void deleteFile(String publicId);
}
