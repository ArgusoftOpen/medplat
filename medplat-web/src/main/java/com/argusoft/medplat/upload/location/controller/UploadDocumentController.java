/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.upload.location.controller;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.upload.location.dto.MobileLibraryDto;
import com.argusoft.medplat.upload.location.service.UploadDocumentService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <p>
 * Define APIs for upload document.
 * </p>
 *
 * @author harsh
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/upload/document")
public class UploadDocumentController {

    @Autowired
    UploadDocumentService uploadDocumentService;

    /**
     * Upload document.
     * @param file Contains file.
     * @return Returns file name.
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.APPLICATION_JSON)
    public String uploadDocument(@RequestParam("file") MultipartFile[] file
    ) {
        return uploadDocumentService.uploadDocument(file);
    }

    /**
     * Upload document.
     * @param request Instance of request.
     * @return Returns success message.
     * @throws FileUploadException Occurred when underlying socket was closed or reset.
     */
    @GetMapping(value = "/upload")
    public String handleUpload(HttpServletRequest request) throws FileUploadException {
        String rootPath;
        rootPath = Objects.requireNonNullElseGet(ConstantUtil.REPOSITORY_PATH, () -> System.getProperty("user.home") + "/Repository/Library_temps");

        rootPath = rootPath + "Library_temp/";

        File oldDir = new File(rootPath);
        boolean isOldDirExists = oldDir.exists();

        if (!isOldDirExists) {
            new File(rootPath).mkdirs();
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(
                new File(rootPath));
        factory.setSizeThreshold(
                DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        factory.setFileCleaningTracker(null);

        ServletFileUpload upload = new ServletFileUpload(factory);

        List<FileItem> fileItems = upload.parseRequest(request);

        for (FileItem item : fileItems) {
            if (!item.isFormField()) {
                try (
                        InputStream uploadedStream = item.getInputStream();
                        OutputStream out = new FileOutputStream("file.mov")) {

                    IOUtils.copy(uploadedStream, out);
                } catch (Exception e) {
                    Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return "success!";
    }

    /**
     * Remove document.
     * @param file File name need to remove.
     */
    @DeleteMapping(value = "/delete")
    public void removeDocument(@QueryParam("file") String file) {
        uploadDocumentService.removeDocument(file);
    }

    /**
     * Update library master state by file name.
     * @param mobileLibraryDto Mobile library details.
     */
    @PutMapping(value = "/updateStateStatus")
    public void updateStateStatus(@RequestBody MobileLibraryDto mobileLibraryDto) {
        uploadDocumentService.updateStateStatus(mobileLibraryDto);
    }

    /**
     * Add document details in mobile library master.
     * @param mobileLibraryDto Mobile library master details.
     */
    @PostMapping(value = "/createmobilelibrarydocument")
    public void createMobileLibraryDocument(@RequestBody MobileLibraryDto mobileLibraryDto) {
        uploadDocumentService.createMobileLibraryDocument(mobileLibraryDto);
    }

    /**
     * Upload document using file name.
     * @param fileName File name.
     */
    @GetMapping(value = "/uploadDocument")
    public void createUploadDocument(@RequestParam(value = "fileName") String fileName) {
        uploadDocumentService.createUploadDocument(fileName);
    }

    /**
     * Retrieves all mobile library master's document.
     * @return Returns list of mobile library master's document.
     */
    @GetMapping(value = "/retreiveAll")
    public List<MobileLibraryDto> retreiveAll() {
        return uploadDocumentService.retrieveAll();
    }

    /**
     * Update mobile library master document's details.
     * @param mobileLibraryDto Mobile library master document details.
     */
    @PutMapping(value = "/update")
    public void updateMobileLibrary(@RequestBody MobileLibraryDto mobileLibraryDto) {
        uploadDocumentService.updateMobileLibrary(mobileLibraryDto);
    }

}
