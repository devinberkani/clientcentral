package com.devinberkani.clientcentral.repository;

import com.devinberkani.clientcentral.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
}
