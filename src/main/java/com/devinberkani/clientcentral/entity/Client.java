package com.devinberkani.clientcentral.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "clients")
public class Client {

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
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private LocalDate birthday;

    /* many clients belong to one user
     * this column is used to hold the foreign keys for the user entity
     * "owner_id" column in client entity represents the user that has that client
     * */
    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User user;

    /* one client has many notes */
    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE)
    private List<Note> notes = new ArrayList<>();

}

