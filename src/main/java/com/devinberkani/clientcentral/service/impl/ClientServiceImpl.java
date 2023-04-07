package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.mapper.ClientMapper;
import com.devinberkani.clientcentral.repository.ClientRepository;
import com.devinberkani.clientcentral.repository.UserRepository;
import com.devinberkani.clientcentral.service.ClientService;
import com.devinberkani.clientcentral.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ClientServiceImpl implements ClientService {

    ClientRepository clientRepository;
    UserRepository userRepository;
    UserService userService;

    public ClientServiceImpl(ClientRepository clientRepository, UserRepository userRepository, UserService userService) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // handle find all matching clients (based query parameters) for user dashboard
    @Override
    public Page<ClientDto> findMatchingClients(String query, int pageNo, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, 10, sort);
        // get logged in user id
        Long currentUserId = userService.getCurrentUser().getId();
        return clientRepository.findMatchingClients(currentUserId, query, pageable).map(ClientMapper::mapToClientDto);
    }

    // handle create new client
    @Override
    public Long saveNewClient(ClientDto client) {
        Client newClient = ClientMapper.mapToClient(client);
        // get logged in user
        User user = userService.getCurrentUser();
        newClient.setUser(user);
        clientRepository.save(newClient);
        return newClient.getId();
    }

    // handle update client
    @Override
    public void updateClient(ClientDto client, Long clientId) {
        client.setId(clientId);
        Client updatedClient = ClientMapper.mapToClient(client);
        // get logged in user
        User user = userService.getCurrentUser();
        updatedClient.setUser(user);
        clientRepository.save(updatedClient);
    }

    // handle delete client
    @Override
    public int deleteClientById(Long clientId) {

        // get logged in user
        User user = userService.getCurrentUser();

        // handle deleting corresponding notes and files from the filesystem if a client is deleted
        Path deletePath = Paths.get("file-attachments/user-" + user.getId() + "/client-" + clientId);
        if (Files.exists(deletePath)) {
            try {
                FileSystemUtils.deleteRecursively(deletePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return clientRepository.deleteClientByIdAndUser(clientId, user);
    }

    // handle find specific client by id
    @Override
    public ClientDto findClientById(Long clientId) {
        // get logged in user
        User user = userService.getCurrentUser();
        Client client = clientRepository.findClientByIdAndUser(clientId, user);
        return client == null ? null : ClientMapper.mapToClientDto(client);
    }

    // handle get today's birthdays
    @Override
    public Page<ClientDto> getTodayBirthdays(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        // get logged in user
        User user = userService.getCurrentUser();
        return clientRepository.getTodayBirthdays(user, pageable).map(ClientMapper::mapToClientDto);
    }

    // handle get upcoming birthdays
    @Override
    public Page<ClientDto> getUpcomingBirthdays(int pageNo, String sortDir) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        // get logged in user
        User user = userService.getCurrentUser();
        Page<Client> page = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? clientRepository.getUpcomingBirthdaysAsc(user, pageable) : clientRepository.getUpcomingBirthdaysDesc(user, pageable);
        return page.map(ClientMapper::mapToClientDto);
    }
}
