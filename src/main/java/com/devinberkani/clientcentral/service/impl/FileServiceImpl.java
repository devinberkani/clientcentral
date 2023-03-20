package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.mapper.ClientMapper;
import com.devinberkani.clientcentral.repository.ClientRepository;
import com.devinberkani.clientcentral.repository.UserRepository;
import com.devinberkani.clientcentral.service.FileService;
import com.devinberkani.clientcentral.util.FileUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FileServiceImpl implements FileService {

    ClientRepository clientRepository;
    UserRepository userRepository;

    public FileServiceImpl(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    // handle converting ClientDto list from FileUtil class to Client list and saving new list of Client to database
    @Override
    public void saveCsv(MultipartFile file) {
        try {
            List<ClientDto> clientDtoList = FileUtil.csvToClientDto(file.getInputStream());
            List<Client> newClients = clientDtoList.stream().map(ClientMapper::mapToClient).toList();
            // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set owner user for newly created client - should get current logged in user
            User user = userRepository.findUserById((long)1);
            newClients.forEach(clientDto -> clientDto.setUser(user));
            clientRepository.saveAll(newClients);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (ConstraintViolationException constraintViolationException) {
            StringBuilder message = new StringBuilder(); // message stringbuilder
            Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations(); // violation set
            List<String> messages = new ArrayList<>();
            violations.forEach(constraintViolation -> messages.add(constraintViolation.getMessage())); // all custom messages
            // append to message stringbuilder
            for (int i = 0; i < messages.size(); i++) {
                message.append(messages.get(i));
                if (i < messages.size() - 1) message.append(" ");
            }
            // exception thrown if phone number is in wrong format when converting from ClientDto to Client
            throw new ConstraintViolationException(message.toString(), constraintViolationException.getConstraintViolations());
        }
    }

    @Override
    public void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }
    }

    // handle load CSV template file from absolute file path
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
