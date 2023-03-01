package com.devinberkani.clientcentral.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp // automatically populates value of field with created timestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp // automatically populates value of field with updated timestamp
    private LocalDateTime updatedOn;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    /* The "mappedBy = user" attribute indicates that it is the user column from Clients entity that maps to this post entity.
     * The "cascade = CascadeType.REMOVE" attribute specifies that when a User entity is removed, all related Client entities should also be removed (cascaded deletion).
     * This is not added to the database, but is rather used to
     * */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Client> clients = new ArrayList<>();

}

