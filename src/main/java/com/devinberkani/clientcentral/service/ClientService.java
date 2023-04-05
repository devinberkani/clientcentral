package com.devinberkani.clientcentral.service;

import com.devinberkani.clientcentral.dto.ClientDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ClientService {
    Page<ClientDto> findMatchingClients(String query, int pageNo, String sortField, String sortDir);
    Long saveNewClient(ClientDto client);
    void updateClient(ClientDto client, Long clientId);
    int deleteClientById(Long clientId);
    ClientDto findClientById(Long clientId);
    Page<ClientDto> getTodayBirthdays(int pageNo);
    Page<ClientDto> getUpcomingBirthdays(int pageNo, String sortDir);
}
