package com.argusoft.medplat.listvalues.service.impl;

import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.listvalues.service.UploadMultimediaService;
import com.argusoft.medplat.mobile.dao.MobileLibraryDao;
import com.argusoft.medplat.mobile.model.MobileLibraryMaster;
import com.argusoft.medplat.query.service.QueryMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * <p>
 * Define all services for upload media.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class UploadMultimediaServiceImpl implements UploadMultimediaService {

    public static final String USER_HOME = "user.home";
    private static final Logger log = LoggerFactory.getLogger(UploadMultimediaServiceImpl.class);
    @Autowired
    QueryMasterService queryMasterService;
    @Autowired
    private MobileLibraryDao mobileLibraryDao;
    @Autowired
    private SystemConfigurationService systemConfigurationService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String uploadFile(MultipartFile[] file) {

        String fileName = file[0].getOriginalFilename();
        String filePath = Objects.requireNonNullElseGet(ConstantUtil.REPOSITORY_PATH, () -> System.getProperty(USER_HOME) + "/Repository/");

        try (FileOutputStream outputStream = new FileOutputStream(new File(filePath  + fileName))) {
            outputStream.write(file[0].getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return fileName;


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileSystemResource getFileById(String fileName) throws FileNotFoundException {
        String rootPath;

        rootPath = Objects.requireNonNullElseGet(ConstantUtil.REPOSITORY_PATH, () -> System.getProperty(USER_HOME) + "/Repository/");

        if (fileName.contains("Announcement")) {
            rootPath = rootPath + "Announcement/";
        }

        File file = new File(rootPath, fileName);
        if (!file.exists()) {
            log.error("File not found with name : {} at path: {} ", fileName, rootPath);
            return null;
        }
        return new FileSystemResource(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getLibraryFileById(Integer id) throws FileNotFoundException {
        MobileLibraryMaster mobileLibraryMaster = mobileLibraryDao.retrieveById(id);
        if (mobileLibraryMaster == null) {
            return null;
        }
        return getLibraryFileByName(mobileLibraryMaster.getFileName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getLibraryFileByName(String fileName) throws FileNotFoundException {
        String rootPath;

        rootPath = Objects.requireNonNullElseGet(ConstantUtil.REPOSITORY_PATH, () -> System.getProperty(USER_HOME) + "/Repository/");

        rootPath = rootPath + "Library/";
        File file = new File(rootPath, fileName);
        if (!file.exists()) {
            log.error("File not found with name : {} at path: {} ", fileName, rootPath);
            return null;
        } else {
            return file;
        }
    }
}
