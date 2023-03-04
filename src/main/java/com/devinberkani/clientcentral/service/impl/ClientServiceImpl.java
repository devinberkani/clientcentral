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
    public void saveNewClient(ClientDto client) {
        Client newClient = ClientMapper.mapToClient(client);
        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set owner user for newly created client - should get current logged in user
        User user = userRepository.findUserById((long)1);
        newClient.setUser(user);
        clientRepository.save(newClient);
    }

    // handle update client
    @Override
    public void updateClient(ClientDto client, Long clientId) {
        Client updatedClient = ClientMapper.mapToClient(client);
        updatedClient.setId(clientId);
        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set owner user for newly created client - should get current logged in user
        User user = userRepository.findUserById((long)1);
        updatedClient.setUser(user);
        clientRepository.save(updatedClient);
    }

    // handle find specific client by id
    @Override
    public ClientDto findClientById(Long id) {
        Client client = clientRepository.findClientById(id);
        return ClientMapper.mapToClientDto(client);
    }
}
