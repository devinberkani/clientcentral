package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.mapper.ClientMapper;
import com.devinberkani.clientcentral.repository.ClientRepository;
import com.devinberkani.clientcentral.repository.UserRepository;
import com.devinberkani.clientcentral.service.FileService;
import com.devinberkani.clientcentral.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    ClientRepository clientRepository;
    UserRepository userRepository;

    public FileServiceImpl(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(MultipartFile file) {
        try {
            List<ClientDto> clientDtoList = FileUtil.csvToClientDto(file.getInputStream());
            List<Client> newClients = clientDtoList.stream().map(ClientMapper::mapToClient).toList();
            // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set owner user for newly created client - should get current logged in user
            User user = userRepository.findUserById((long)1);
            newClients.forEach(clientDto -> clientDto.setUser(user));
            clientRepository.saveAll(newClients);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = Paths.get("src/main/resources/static/downloads").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (FileNotFoundException | MalformedURLException fileNotFoundException) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }

}
