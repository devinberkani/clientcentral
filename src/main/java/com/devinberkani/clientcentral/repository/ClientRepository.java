package com.devinberkani.clientcentral.repository;

import com.devinberkani.clientcentral.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE c.user.id = :userId AND (CONCAT(LOWER(c.firstName),' ',LOWER(c.lastName)) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Client> findMatchingClients(@Param("userId") Long userId, @Param("query") String query, Pageable pageable);
    Client findClientById(Long id);
}
