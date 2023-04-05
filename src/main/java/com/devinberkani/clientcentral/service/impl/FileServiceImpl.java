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
import com.devinberkani.clientcentral.service.UserService;
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
    UserService userService;

    public FileServiceImpl(UserRepository userRepository, ClientRepository clientRepository, NoteRepository noteRepository, FileAttachmentRepository fileAttachmentRepository, UserService userService) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.noteRepository = noteRepository;
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.userService = userService;
    }

    // handle converting ClientDto list from FileUtil class to Client list and saving newly bulk-uploaded list of Client to database
    @Override
    public void saveCsv(MultipartFile file) {
        try {
            List<ClientDto> clientDtoList = FileUtil.csvToClientDto(file.getInputStream());
            List<Client> newClients = clientDtoList.stream().map(ClientMapper::mapToClient).toList();
            // get logged in user
            User user = userService.getCurrentUser();
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
    public void saveNewFile(MultipartFile multipartFile, Long clientId, Long noteId) throws IOException {

        // get logged in user id
        Long currentUserId = userService.getCurrentUser().getId();

        Path uploadPath = Paths.get("src/main/resources/static/file-attachments/user-" + currentUserId + "/client-" + clientId + "/note-" + noteId);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setFileReference(fileName);
            fileAttachment.setNote(noteRepository.findNoteByIdAndUser(noteId, currentUserId));
            fileAttachmentRepository.save(fileAttachment);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }
    }

    // handle deleting file from database and filesystem
    @Override
    public int deleteFile(Long fileId, Long noteId, Long clientId, String fileReference) {

        // get logged in user id
        Long currentUserId = userService.getCurrentUser().getId();

        // handle deleting file from the filesystem from edit note view
        Path filePath = Paths.get("src/main/resources/static/file-attachments/user-" + currentUserId + "/client-" + clientId + "/note-" + noteId + "/" + fileReference);
        if (Files.exists(filePath)) {
            try {

                // delete the file
                Files.delete(filePath);

                // check if the specific note directory is empty, if it is, delete it from the filesystem
                File noteDirectory = new File("src/main/resources/static/file-attachments/user-" + currentUserId + "/client-" + clientId + "/note-" + noteId);
                deleteDirectoryIfEmpty(noteDirectory);

                // check if the specific client directory is empty, if it is, delete it from the filesystem
                File clientDirectory = new File("src/main/resources/static/file-attachments/user-" + currentUserId + "/client-" + clientId);
                deleteDirectoryIfEmpty(clientDirectory);

                // check if the specific user directory is empty, if it is, delete it from the filesystem
                File userDirectory = new File("src/main/resources/static/file-attachments/user-" + currentUserId);
                deleteDirectoryIfEmpty(userDirectory);

                // delete the file
                fileAttachmentRepository.deleteById(fileId);

                return 1;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
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
    public Resource loadFileAsResource(Long clientId, Long noteId, String fileName) throws FileNotFoundException {

        // get logged in user id
        Long currentUserId = userService.getCurrentUser().getId();

        try {
            Path filePath = Paths.get("src/main/resources/static/file-attachments/user-" + currentUserId + "/client-" + clientId + "/note-" + noteId + "/" + fileName).normalize();
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
