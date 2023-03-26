package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.mapper.ClientMapper;
import com.devinberkani.clientcentral.repository.ClientRepository;
import com.devinberkani.clientcentral.repository.UserRepository;
import com.devinberkani.clientcentral.service.ClientService;
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

    public ClientServiceImpl(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    // handle find all matching clients (based query parameters) for user dashboard
    @Override
    public Page<ClientDto> findMatchingClients(String query, int pageNo, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, 10, sort);
        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to find list of clients for dashboard - should get current logged in user
        return clientRepository.findMatchingClients((long)1, query, pageable).map(ClientMapper::mapToClientDto);
    }

    // handle create new client
    @Override
    public Long saveNewClient(ClientDto client) {
        Client newClient = ClientMapper.mapToClient(client);
        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set owner user for newly created client - should get current logged in user
        User user = userRepository.findUserById((long)1);
        newClient.setUser(user);
        clientRepository.save(newClient);
        return newClient.getId();
    }

    // handle update client
    @Override
    public void updateClient(ClientDto client, Long clientId) {
        client.setId(clientId);
        Client updatedClient = ClientMapper.mapToClient(client);
        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set owner user for newly created client - should get current logged in user
        User user = userRepository.findUserById((long)1);
        updatedClient.setUser(user);
        clientRepository.save(updatedClient);
    }

    @Override
    public void deleteClientById(Long clientId) {

        // handle deleting corresponding notes and files from the filesystem if a client is deleted

        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set filepath for any existing files - should get current logged in user
        Path deletePath = Paths.get("src/main/resources/static/file-attachments/user-" + 1 + "/client-" + clientId);
        if (Files.exists(deletePath)) {
            try {
                FileSystemUtils.deleteRecursively(deletePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        clientRepository.deleteById(clientId);
    }

    // handle find specific client by id
    @Override
    public ClientDto findClientById(Long clientId) {
        Client client = clientRepository.findClientById(clientId);
        return ClientMapper.mapToClientDto(client);
    }
}
