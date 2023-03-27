package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.FileAttachment;
import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.mapper.ClientMapper;
import com.devinberkani.clientcentral.repository.ClientRepository;
import com.devinberkani.clientcentral.repository.FileAttachmentRepository;
import com.devinberkani.clientcentral.repository.NoteRepository;
import com.devinberkani.clientcentral.repository.UserRepository;
import com.devinberkani.clientcentral.service.FileService;
import com.devinberkani.clientcentral.util.FileUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
import java.util.Objects;
import java.util.Set;

@Service
public class FileServiceImpl implements FileService {

    UserRepository userRepository;
    ClientRepository clientRepository;
    NoteRepository noteRepository;
    FileAttachmentRepository fileAttachmentRepository;

    public FileServiceImpl(UserRepository userRepository, ClientRepository clientRepository, NoteRepository noteRepository, FileAttachmentRepository fileAttachmentRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.noteRepository = noteRepository;
        this.fileAttachmentRepository = fileAttachmentRepository;
    }

    // handle converting ClientDto list from FileUtil class to Client list and saving newly bulk-uploaded list of Client to database
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

    // handle saving new user file to a specific client and note

    @Override
    public void saveNewFile(MultipartFile multipartFile, Long userId, Long clientId, Long noteId) throws IOException {
        Path uploadPath = Paths.get("src/main/resources/static/file-attachments/user-" + userId + "/client-" + clientId + "/note-" + noteId);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setFileReference(fileName);
            fileAttachment.setNote(noteRepository.findNoteById(noteId));
            fileAttachmentRepository.save(fileAttachment);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }
    }

    // handle deleting file from database and filesystem

    @Override
    public void deleteFile(Long fileId, Long noteId, Long clientId, String fileReference) {

        // handle deleting file from the filesystem from edit note view

        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set filepath for any existing files - should get current logged in user
        Path filePath = Paths.get("src/main/resources/static/file-attachments/user-" + 1 + "/client-" + clientId + "/note-" + noteId + "/" + fileReference);
        if (Files.exists(filePath)) {
            try {

                // delete the file
                Files.delete(filePath);

                // check if the specific note directory is empty, if it is, delete it from the filesystem
                // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set filepath for any existing files - should get current logged in user
                File noteDirectory = new File("src/main/resources/static/file-attachments/user-" + 1 + "/client-" + clientId + "/note-" + noteId);
                deleteDirectoryIfEmpty(noteDirectory);

                // check if the specific client directory is empty, if it is, delete it from the filesystem
                // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set filepath for any existing files - should get current logged in user
                File clientDirectory = new File("src/main/resources/static/file-attachments/user-" + 1 + "/client-" + clientId);
                deleteDirectoryIfEmpty(clientDirectory);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileAttachmentRepository.deleteById(fileId);
    }

    // handle deleting directory from filesystem if it is empty

    @Override
    public void deleteDirectoryIfEmpty(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            if (files.length == 0) {
                directory.delete();
            }
        }
    }

    // handle load CSV template file from absolute file path
    @Override
    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = Paths.get("src/main/resources/static/downloads/" + fileName).normalize();
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

    // handle load specific user file from path based on specific client and note that belongs to logged-in user

    @Override
    public Resource loadFileAsResource(Long userId, Long clientId, Long noteId, String fileName) throws FileNotFoundException {
        try {
            Path filePath = Paths.get("src/main/resources/static/file-attachments/user-" + userId + "/client-" + clientId + "/note-" + noteId + "/" + fileName).normalize();
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
