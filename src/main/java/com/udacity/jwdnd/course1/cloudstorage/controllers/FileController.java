package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload")MultipartFile file,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes
    ) {
        int rowsEffected = fileService.uploadFile(file, authentication.getName());
        redirectAttributes.addFlashAttribute("isSuccess", rowsEffected > 0);
        return "redirect:/home";
    }

    @GetMapping("/remove/{fileId}")
    public String removeNote(@PathVariable("fileId") int fileId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        int rowsEffected = fileService.removeFile(fileId, authentication.getName());
        redirectAttributes.addFlashAttribute("isSuccess", rowsEffected > 0);
        return "redirect:/home";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") int fileId,
                                          Authentication authentication) {
        if (fileService.isFileOwner(fileId, authentication.getName())) {
            File file = fileService.getFileById(fileId);
            InputStream inputStream = new ByteArrayInputStream(file.getFileData());
            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFileName())
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not file owner");
    }
}
