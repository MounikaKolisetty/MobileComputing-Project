package com.rescuer.api.service;

import com.rescuer.api.entity.Attachment;
import com.rescuer.api.entity.Ticket;
import com.rescuer.api.repository.audit.AttachmentRepository;
import com.rescuer.api.repository.rescuer.TicketRepository;
import com.rescuer.api.web.error.ResourceNotFound;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class FileService {

    private static FileService fileServiceInstance;
    private final AttachmentRepository attachmentRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public FileService(AttachmentRepository attachmentRepository, TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        fileServiceInstance = this;
        this.attachmentRepository = attachmentRepository;
    }

    public static FileService getInstance() {
        return fileServiceInstance;
    }

    public Attachment uploadFile(MultipartFile file) throws IOException {
        Attachment attachment = new Attachment();
        attachment.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        attachment.setAttachmentName(file.getOriginalFilename());
        attachment.setAttachmentType(file.getContentType());
        Attachment attachment1 = attachmentRepository.save(attachment);
        return attachment1;
    }

    public Set<Attachment> uploadFiles(List<MultipartFile> files) {
        return files.stream().map(multipartFile -> {
            try {
                return this.uploadFile(multipartFile);
            } catch (IOException e) {
                log.error("unable to upload file with exception" + e.getMessage());
            }
            return null;
        }).collect(Collectors.toSet());
    }

    public String downloadFiles(String ticketId) throws IOException {
        log.info("Request received for getting ticket: {} details", ticketId.toString());
        Optional<Ticket> optionalTicket = ticketRepository.findById(UUID.fromString(ticketId));
        if (optionalTicket.isPresent() && ObjectUtils.isEmpty(optionalTicket.get()
                .getTicketDetails().getVictim().getAttachments())) {
            String guid = UUID.randomUUID().toString();
            String tempPath = System.getProperty("java.io.tmpdir") + File.separator + guid;
            File dir = new File(tempPath);
            dir.mkdir();
            Set<Attachment> attachments = optionalTicket.get()
                    .getTicketDetails().getVictim().getAttachments();
            List<File> fileList = attachments.stream().map(attachment -> {
                ByteArrayResource resource = new ByteArrayResource(attachment.getFile().getData());
                Path path = Paths.get(tempPath + File.separator + attachment.getAttachmentName());
                try {
                    Files.write(path, resource.getByteArray());
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
                return new File(String.valueOf(path));
            }).collect(Collectors.toList());
            String zipFilePath = tempPath + File.separator + "compressed.zip";
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (File file : fileList) {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
            return zipFilePath;
        }
        log.error("Could not find ticket details for id {}ans status :", ticketId.toString());
        throw new ResourceNotFound(String.format("Could not find ticket details for id %s", ticketId.toString()));
    }
}
