package com.devinberkani.clientcentral.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {
    void saveCsv(MultipartFile file);
    void saveNewFile(MultipartFile multipartFile, Long userId, Long clientId, Long noteId) throws IOException;
    Resource loadFileAsResource(String fileName) throws FileNotFoundException;
}
