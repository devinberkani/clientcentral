package com.devinberkani.clientcentral.repository;

import com.devinberkani.clientcentral.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
    @Transactional
    @Modifying
    void deleteFileAttachmentByNoteId(Long noteId);
}
