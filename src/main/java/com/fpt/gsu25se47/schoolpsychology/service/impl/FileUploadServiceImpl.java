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
    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
        long sizeInBytes = file.getSize();
        double sizeInMB = (double) sizeInBytes / (1024 * 1024);

        Long maxSize = systemConfigService.getValueAs("file.size", Long.class);

        if (sizeInMB > maxSize) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File size exceeded " + maxSize);
        }

        String publicId = UUID.randomUUID().toString();

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                Map.of("public_id", publicId)
        );

        String url = uploadResult.get("url").toString();

        return Map.of(
                "public_id", publicId,
                "url", url
        );
    }

    public void deleteFile(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, Map.of());
            System.out.println("Delete result: " + result);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting file from Cloudinary", e);
        }
    }

}
