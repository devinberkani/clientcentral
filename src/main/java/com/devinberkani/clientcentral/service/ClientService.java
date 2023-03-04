package com.devinberkani.clientcentral.service;

import com.devinberkani.clientcentral.dto.ClientDto;
import org.springframework.data.domain.Page;

public interface ClientService {
    Page<ClientDto> findMatchingClients(String query, int pageNo, String sortField, String sortDir);
    void saveNewClient(ClientDto client);
    void updateClient(ClientDto client, Long clientId);
    ClientDto findClientById(Long id);
}
