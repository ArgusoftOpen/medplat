/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.upload.location.service;

import com.argusoft.medplat.upload.location.dto.MobileLibraryDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 * <p>
 *     Define services for upload document.
 * </p>
 * @author harsh
 * @since 26/08/20 11:00 AM
 *
 */
public interface UploadDocumentService {
    /**
     * Upload document.
     * @param file Contains file.
     * @return Returns file name.
     */
    String uploadDocument(MultipartFile[] file);

    /**
     * Remove document.
     * @param file File name need to remove.
     */
    void removeDocument(String file);

    /**
     * Update library master state by file name.
     * @param mobileLibraryDto Mobile library details.
     */
    void updateStateStatus(MobileLibraryDto mobileLibraryDto);

    /**
     * Add document details in mobile library master.
     * @param mobileLibraryDto Mobile library master details.
     */
    void createMobileLibraryDocument(MobileLibraryDto mobileLibraryDto);

    /**
     * Move temp library document.
     * @param fileName File name.
     */
    void moveTempLibraryDocument(String fileName);

    /**
     * Retrieves all mobile library master's document.
     * @return Returns list of mobile library master's document.
     */
    List<MobileLibraryDto> retrieveAll();

    /**
     * Update mobile library master document's details.
     * @param mobileLibraryDto Mobile library master document details.
     */
    void updateMobileLibrary(MobileLibraryDto mobileLibraryDto);

    /**
     * Upload document using file name.
     * @param fileName File name.
     */
    void createUploadDocument(String fileName);
    
}
