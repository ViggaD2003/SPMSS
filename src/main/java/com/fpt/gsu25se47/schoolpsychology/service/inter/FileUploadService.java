package com.fpt.gsu25se47.schoolpsychology.service.inter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {

    String uploadFile(MultipartFile file) throws IOException;
}
