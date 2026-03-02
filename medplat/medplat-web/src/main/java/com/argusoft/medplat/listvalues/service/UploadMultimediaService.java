    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.listvalues.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * <p>
 *     Define all services for upload media.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public interface UploadMultimediaService {

    /**
     * Upload list values file.
     * @param file File for list values.
     * @return Returns name of file.
     */
    String uploadFile(MultipartFile[] file) ;

    /**
     * Retrieves file by file name.
     * @param fileName name of file.
     * @return Returns file by file name.
     * @throws FileNotFoundException This exception will be thrown  when a file with the specified pathname does not exist.
     */
    FileSystemResource getFileById(String fileName) throws FileNotFoundException;

    /**
     * Retrieve library file by id.
     * @param id Id of library file.
     * @return Returns file.
     * @throws FileNotFoundException This exception will be thrown  when a file with the specified pathname does not exist.
     */
    File getLibraryFileById(Integer id) throws FileNotFoundException;

    /**
     * Retrieve library file by name.
     * @param fileName Name of library file.
     * @return Returns file.
     * @throws FileNotFoundException This exception will be thrown  when a file with the specified pathname does not exist.
     */
    File getLibraryFileByName(String fileName) throws FileNotFoundException;
}
