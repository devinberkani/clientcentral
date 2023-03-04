package com.devinberkani.clientcentral.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface FileService {
    void save(MultipartFile file);
    Resource loadFileAsResource(String fileName) throws FileNotFoundException;
}
