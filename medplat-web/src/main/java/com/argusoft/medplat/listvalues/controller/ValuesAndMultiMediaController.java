/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.listvalues.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.listvalues.service.UploadMultimediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;

/**
 *
 * <p>
 * Define APIs for list values.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping(value = "/api/values_and_multimedia/")
@Tag(name = "Values And Multi Media Controller", description = "")
public class ValuesAndMultiMediaController {

    @Autowired
    UploadMultimediaService uploadMultiMediaService;

    /**
     * Upload file for list values.
     * @param file File for list values.
     * @return Returns name of file.
     */
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.APPLICATION_JSON)
    public String uploadXls(@RequestParam("file") MultipartFile[] file) {
        return uploadMultiMediaService.uploadFile(file);
    }

    /**
     * Retrieves file by file name.
     * @param fileName  name of file.
     * @param request Request object.
     * @param response Response object.
     * @return Returns file by file name.
     * @throws FileNotFoundException This exception will be thrown  when a file with the specified pathname does not exist.
     */
    @GetMapping(value = "file")
    public FileSystemResource uploadXls(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        return uploadMultiMediaService.getFileById(fileName);
    }
}