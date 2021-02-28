package com.rescuer.api.web.controllers;

import com.rescuer.api.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Autowired
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        log.info("uploading files");
        fileService.uploadFiles(files);
        return ResponseEntity.status(HttpStatus.OK).body("saved");
    }

    @GetMapping("/download")
    public Object downloadFile(@RequestParam String ticketId, HttpServletResponse response) throws IOException {
        log.info("downloading files");
        try {
            String file = fileService.downloadFiles(ticketId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader("Content-Disposition", "attachment; filename=compressed.zip");
            return FileUtils.readFileToByteArray(new File(file));
        } finally {
            log.info("downloaded files");
        }

    }
}
