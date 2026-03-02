/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.upload.location.service.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.mobile.dao.MobileLibraryDao;
import com.argusoft.medplat.mobile.model.MobileLibraryMaster;
import com.argusoft.medplat.upload.location.dto.MobileLibraryDto;
import com.argusoft.medplat.upload.location.mapper.MobileLibraryDocumentMapper;
import com.argusoft.medplat.upload.location.service.UploadDocumentService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <p>
 *     Define services for upload document.
 * </p>
 * @author harsh
 * @since 26/08/20 11:00 AM
 *
 */
@Service
@Transactional
public class UploadDocumentServiceImpl implements UploadDocumentService {

    public static final String USER_HOME = "user.home";
    public static final String REPOSITORY_PATH = File.separator + "Repository" + File.separator;
    public static final String LIBRARY_TEMP_FOLDER = "Library_temp";
    public static final String FILE_RENAME_FAIL_MESSAGE = "File not able to rename";


    @Autowired
    ImtechoSecurityUser user;

    @Autowired
    MobileLibraryDao mobileLibraryDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public String uploadDocument(MultipartFile[] file) {
        String fileName = file[0].getOriginalFilename();

        String rootPath;
        rootPath = Objects.requireNonNullElseGet(ConstantUtil.REPOSITORY_PATH, () -> System.getProperty(USER_HOME) + REPOSITORY_PATH);

        rootPath = rootPath + "Library_temp/";

        File oldDir = new File(rootPath);
        boolean isOldDirExists = oldDir.exists();

        if (!isOldDirExists) {
            new File(rootPath).mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(new File(rootPath, fileName))) {
            outputStream.write(file[0].getBytes());
        } catch (IOException e) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDocument(String file) {
        String rootPath;
        rootPath = Objects.requireNonNullElseGet(ConstantUtil.REPOSITORY_PATH, () -> System.getProperty(USER_HOME) + REPOSITORY_PATH);

        rootPath = rootPath + LIBRARY_TEMP_FOLDER + File.separator;

        try {
            Files.delete(Path.of(rootPath + File.separator + file));
        } catch (Exception e) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMobileLibraryDocument(MobileLibraryDto mobileLibraryDto) {
        String rootPath;
        rootPath = Objects.requireNonNullElseGet(ConstantUtil.REPOSITORY_PATH, () -> System.getProperty(USER_HOME) + REPOSITORY_PATH);

        String oldFilePath = rootPath + LIBRARY_TEMP_FOLDER;
        String newFilePath = rootPath + "Library";
        String fileName = mobileLibraryDto.getFileName();

        File newDir = new File(newFilePath);
        boolean isNewDirExists = newDir.exists();

        if (!isNewDirExists) {
            new File(newFilePath).mkdirs();
        }

        File f = new File(newFilePath + File.separator + fileName);
        if (f.exists() && !f.isDirectory()) {
            this.clearTempFolder(oldFilePath);
            throw new ImtechoUserException("This filename is already exist. So try with other file or change the filename.", 0);
        }

        File newFile = new File(oldFilePath + File.separator + fileName);
        if (!newFile.renameTo(new File(newFilePath + File.separator + fileName))) {
            throw new ImtechoUserException(FILE_RENAME_FAIL_MESSAGE + newFile.getPath(), 0);
        }

        this.clearTempFolder(oldFilePath);
        mobileLibraryDao.create(MobileLibraryDocumentMapper.convertMobileLibraryDocumentToMobileLibraryMaster(mobileLibraryDto));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveTempLibraryDocument(String fileName) {
        String rootPath;
        rootPath = Objects.requireNonNullElseGet(ConstantUtil.REPOSITORY_PATH, () -> System.getProperty(USER_HOME) + REPOSITORY_PATH);

        String oldFilePath = rootPath + LIBRARY_TEMP_FOLDER;
        String newFilePath = rootPath + "Library";

        File newDir = new File(newFilePath);
        boolean isNewDirExists = newDir.exists();

        if (!isNewDirExists) {
            new File(newFilePath).mkdirs();
        }

        File f = new File(newFilePath + File.separator + fileName);
        if (f.exists() && !f.isDirectory()) {
            this.clearTempFolder(oldFilePath);
        }

        File newFile = new File(oldFilePath + File.separator + fileName);
        if (!newFile.renameTo(new File(newFilePath + File.separator + fileName))) {
            throw new ImtechoUserException(FILE_RENAME_FAIL_MESSAGE + newFile.getPath(), 0);
        }
        this.clearTempFolder(oldFilePath);
    }

    /**
     * Clear temp folder.
     * @param repositoryPath Temp repository path.
     */
    public void clearTempFolder(String repositoryPath) {
        File file = new File(repositoryPath);
        if (file.isDirectory()) {
            try {
                FileUtils.cleanDirectory(file);
            } catch (IOException e) {
                Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateStateStatus(MobileLibraryDto mobileLibraryDto) {
        mobileLibraryDao.updateStateStatus(mobileLibraryDto.getFileName(), mobileLibraryDto.getState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MobileLibraryDto> retrieveAll() {
        return MobileLibraryDocumentMapper.convertMasterListToDtoList(mobileLibraryDao.retrieveAll());
    }

    /**
     * {@inheritDoc}
     */
    public void updateMobileLibrary(MobileLibraryDto mobileLibraryDto) {
        MobileLibraryMaster mobileLibraryMaster = mobileLibraryDao.retrieveById(mobileLibraryDto.getId());
        mobileLibraryMaster.setDescription(mobileLibraryDto.getDescription());
        mobileLibraryMaster.setTag(mobileLibraryDto.getTag());
        mobileLibraryDao.update(mobileLibraryMaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUploadDocument(String fileName) {
        String rootPath;
        rootPath = Objects.requireNonNullElseGet(ConstantUtil.REPOSITORY_PATH, () -> System.getProperty(USER_HOME) + REPOSITORY_PATH);

        String oldFilePath = rootPath + LIBRARY_TEMP_FOLDER;
        String newFilePath = System.getProperty(USER_HOME) + "uploadedFiles";


        File newDir = new File(newFilePath);
        boolean isNewDirExists = newDir.exists();

        if (!isNewDirExists) {
            new File(newFilePath).mkdirs();
        }

        File f = new File(newFilePath + File.separator + fileName);
        if (f.exists() && !f.isDirectory()) {
            this.clearTempFolder(oldFilePath);
            throw new ImtechoUserException("This filename is already exist. So try with other file or change the filename.", 0);
        }

        File newFile = new File(oldFilePath + File.separator + fileName);
        if (!newFile.renameTo(new File(newFilePath + File.separator + fileName))) {
            throw new ImtechoUserException(FILE_RENAME_FAIL_MESSAGE + newFile.getPath(), 0);
        }
        this.clearTempFolder(oldFilePath);

    }

}
