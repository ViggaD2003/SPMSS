package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.cloudinary.Cloudinary;
import com.fpt.gsu25se47.schoolpsychology.service.inter.FileUploadService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final Cloudinary cloudinary;
    private final SystemConfigService systemConfigService;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {

        long sizeInBytes = file.getSize();

        double sizeInMB = (double) sizeInBytes / (1024 * 1024);

        Long maxSize = systemConfigService.getValueAs("file.size", Long.class);

        if (sizeInMB > maxSize) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File size exceeded " + maxSize);
        }

        return cloudinary.uploader()
                .upload(file.getBytes(),
                        Map.of("public_id",
                                UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }
}
