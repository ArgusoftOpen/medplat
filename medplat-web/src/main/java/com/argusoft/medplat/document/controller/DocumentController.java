/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.document.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.document.dto.DocumentDto;
import com.argusoft.medplat.document.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jay
 */
@RestController
@RequestMapping("/api/document/")
@Tag(name = "Document Controller", description = "")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping(value = "uploaddocument/{moduleName}/{isTemporary}", consumes = javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA)
    public DocumentDto uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable() String moduleName,
            @PathVariable() boolean isTemporary) {
        return documentService.uploadFile(file, moduleName, isTemporary);
    }

    @GetMapping(value = "getfile/{id}")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable() Long id) throws FileNotFoundException {
        File file = documentService.getFile(id);
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }catch (Exception e){
            Logger.getLogger(DocumentController.class.getName()).log(Level.INFO, e.getMessage(), e);
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping(value = "getthumbnail/{id}")
    public ResponseEntity<InputStreamResource> getThumbnail(@PathVariable() Long id) throws FileNotFoundException {
        File file = documentService.getThumbnail(id);
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }catch (Exception e){
            Logger.getLogger(DocumentController.class.getName()).log(Level.INFO, e.getMessage(), e);
        }
        return ResponseEntity.status(404).build();
    }
    
    @PutMapping(value = "removedocument/{id}")
    public void removeDocument(@PathVariable() Long id) {
        documentService.removeDocument(id);
    }
    @GetMapping(value = "getdocumentdetail/{id}")
    public DocumentDto getDocumentDetail(@PathVariable() Long id) {
        return documentService.retrieveDocumentById(id);
    }
}