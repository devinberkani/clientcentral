package com.devinberkani.clientcentral.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotEmpty(message = "First name should not be empty.")
    @Column(nullable = false)
    private String firstName;
    @NotEmpty(message = "Last name should not be empty.")
    @Column(nullable = false)
    private String lastName;
    private String address;
    @Pattern(regexp = "^$|\\d{3}-\\d{3}-\\d{4}", message = "Phone number must be empty or in the format 555-555-5555.")
    private String phoneNumber;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

