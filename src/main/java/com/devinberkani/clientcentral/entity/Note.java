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
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp // automatically populates value of field with created timestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp // automatically populates value of field with updated timestamp
    private LocalDateTime updatedOn;
    @Column(nullable = false)
    private String content;

    /* many notes belong to one client
     * */
    @ManyToOne
    @JoinColumn(name = "owner_client_id", nullable = false)
    private Client client;

    /* one note has many file attachments
     * */
    @OneToMany(mappedBy = "note", cascade = CascadeType.REMOVE)
    private List<FileAttachment> files = new ArrayList<>();
}
