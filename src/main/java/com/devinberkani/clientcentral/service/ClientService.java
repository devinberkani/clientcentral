package com.devinberkani.clientcentral.service;

import com.devinberkani.clientcentral.dto.ClientDto;
import org.springframework.data.domain.Page;

public interface ClientService {
    Page<ClientDto> findMatchingClients(String query, int pageNo, String sortField, String sortDir);
    Long saveNewClient(ClientDto client);
    void updateClient(ClientDto client, Long clientId);
    void deleteClientById(Long clientId);
    ClientDto findClientById(Long clientId);
}
