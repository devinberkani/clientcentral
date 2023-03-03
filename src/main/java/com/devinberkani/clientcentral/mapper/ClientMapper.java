package com.devinberkani.clientcentral.mapper;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.entity.Client;

public class ClientMapper {

    public static ClientDto mapToClientDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .createdOn(client.getCreatedOn())
                .updatedOn(client.getUpdatedOn())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .address(client.getAddress())
                .phoneNumber(client.getPhoneNumber())
                .email(client.getEmail())
                .birthday(client.getBirthday())
                .user(client.getUser())
                .notes(client.getNotes())
                .build();
    }

    public static Client mapToClient(ClientDto clientDto) {
        return Client.builder()
                .firstName(clientDto.getFirstName())
                .lastName(clientDto.getLastName())
                .address(clientDto.getAddress())
                .phoneNumber(clientDto.getPhoneNumber())
                .email(clientDto.getEmail())
                .birthday(clientDto.getBirthday())
                .build();
    }

}
