package com.devinberkani.clientcentral.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {
    void saveCsv(MultipartFile file);
    void saveNewFile(MultipartFile multipartFile, Long clientId, Long noteId) throws IOException;
    int deleteFile(Long fileId, Long noteId, Long clientId, String fileReference);
    void deleteDirectoryIfEmpty(File directory);
    Resource loadFileAsResource(String fileName) throws FileNotFoundException;
    Resource loadFileAsResource(Long clientId, Long noteId, String fileName) throws FileNotFoundException;
}
