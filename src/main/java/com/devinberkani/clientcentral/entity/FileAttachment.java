package com.devinberkani.clientcentral.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "files")
public class FileAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp // automatically populates value of field with created timestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp // automatically populates value of field with updated timestamp
    private LocalDateTime updatedOn;
    @Column(nullable = false)
    private String fileReference;

    /* many file attachments belong to one note
     * */
    @ManyToOne
    @JoinColumn(name = "owner_note_id", nullable = false)
    private Note note;
}
