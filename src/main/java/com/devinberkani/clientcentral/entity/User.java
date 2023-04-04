package com.devinberkani.clientcentral.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
     * This is not added to the database, but is rather used to specify the inverse side of the relationship between the User and Client entities. The mappedBy attribute specifies the name of the field in the Client entity that maps to the User entity's clients field. This allows JPA to correctly manage bidirectional relationships between entities and ensure that changes made to one side of the relationship are propagated to the other side.
     * */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Client> clients = new ArrayList<>();

}

