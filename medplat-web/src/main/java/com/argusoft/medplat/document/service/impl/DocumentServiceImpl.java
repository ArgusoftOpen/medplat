/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.document.service.impl;

import com.argusoft.medplat.common.util.CommonUtils;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.document.dao.DocumentDao;
import com.argusoft.medplat.document.dao.DocumentModuleMasterDao;
import com.argusoft.medplat.document.dto.DocumentDto;
import com.argusoft.medplat.document.dto.DocumentModuleMasterDto;
import com.argusoft.medplat.document.mapper.DocumentMapper;
import com.argusoft.medplat.document.model.DocumentMaster;
import com.argusoft.medplat.document.model.DocumentModuleMaster;
import com.argusoft.medplat.document.service.DocumentService;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.exception.ImtechoUserException;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jay
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    DocumentDao documentDao;

    @Autowired
    DocumentModuleMasterDao documentModuleMasterDao;

    @Override
    public DocumentDto uploadFile(MultipartFile file, String moduleName, boolean isTemporary) {
        List<DocumentModuleMasterDto> retrieveModuleName = documentModuleMasterDao.retrieveModuleName(moduleName);

        if (retrieveModuleName.size() != 1) {
            throw new NullPointerException();
        }

        DocumentModuleMasterDto masterData = retrieveModuleName.get(0);

        DocumentDto fileDto = null;
        String filePath = ConstantUtil.REPOSITORY_PATH + masterData.getBasePath();
        String extention = FilenameUtils.getExtension(file.getOriginalFilename());
        String tempFileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")) + CommonUtils.randomNumber();
        String fileName = tempFileName + "." + extention;
        try {
            File directory = new File(filePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File originalFile = new File(filePath + File.separator + fileName);
            String mimetype = file.getContentType().substring(0, file.getContentType().lastIndexOf("/"));
            String thumbFileName;
            try (FileOutputStream outputStream = new FileOutputStream(originalFile)) {
                outputStream.write(file.getBytes());
                thumbFileName = null;
                if ("video".equals(mimetype)) {
                    thumbFileName = createThumbnailForVideo(originalFile, extention);
                } else if ("image".equals(mimetype)) {
                    thumbFileName = createThumbnailForImage(originalFile);
                }
            }
            if (originalFile.exists()) {
                DocumentMaster fileMaster = DocumentMapper.entityMaker(fileName, thumbFileName, extention,
                        file.getOriginalFilename(), masterData.getId(), isTemporary);
                long id = documentDao.create(fileMaster);
                fileDto = DocumentMapper.entityToDto(documentDao.retrieveById(id));
            }
        } catch (IOException e) {
            throw new ImtechoMobileException("Unable to write a file", e);
        } catch (Exception ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileDto;
    }

    @Override
    public DocumentDto uploadFile(byte[] bytes, String moduleName, boolean isTemporary, String filename, String contentType) {
        List<DocumentModuleMasterDto> retrieveModuleName = documentModuleMasterDao.retrieveModuleName(moduleName);

        if (retrieveModuleName.size() != 1) {
            throw new NullPointerException();
        }

        DocumentModuleMasterDto masterData = retrieveModuleName.get(0);

        DocumentDto fileDto = null;
        String filePath = ConstantUtil.REPOSITORY_PATH + masterData.getBasePath();
        String extention = FilenameUtils.getExtension(filename);
        String tempFileName = filename.substring(0, filename.lastIndexOf(".")) + CommonUtils.randomNumber();
        String fileName = tempFileName + "." + extention;
        try {
            File directory = new File(filePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File originalFile = new File(filePath + File.separator + fileName);
            if(!originalFile.createNewFile()){
                throw new ImtechoUserException("File creation fail",0);
            }
            String mimetype = contentType.substring(0, contentType.lastIndexOf("/"));
            String thumbFileName;
            try (FileOutputStream outputStream = new FileOutputStream(originalFile)) {
                outputStream.write(bytes);
                thumbFileName = null;
                if ("video".equals(mimetype)) {
                    thumbFileName = createThumbnailForVideo(originalFile, extention);
                } else if ("image".equals(mimetype)) {
                    thumbFileName = createThumbnailForImage(originalFile);
                }
            }
            if (originalFile.exists()) {
                DocumentMaster fileMaster = DocumentMapper.entityMaker(fileName, thumbFileName, extention,
                        filename, masterData.getId(), isTemporary);
                long id = documentDao.create(fileMaster);
                fileDto = DocumentMapper.entityToDto(documentDao.retrieveById(id));
            }
        } catch (IOException e) {
            throw new ImtechoMobileException("Unable to write a file", e);
        } catch (Exception ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileDto;
    }

    public static String createThumbnailForVideo(File inputImgFile, String extention) throws FrameGrabber.Exception {
        String tempFileName = inputImgFile.getName().substring(0, inputImgFile.getName().lastIndexOf(".")) + "_." + "jpg";
        String mp4Path = inputImgFile.getParentFile() + File.separator + inputImgFile.getName();
        String imagePath = inputImgFile.getParentFile() + File.separator + tempFileName;
        Java2DFrameConverter converter = new Java2DFrameConverter();
        Frame frame;
        try (FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(mp4Path)) {
            frameGrabber.setFormat(extention);
            frameGrabber.start();

            frameGrabber.setFrameNumber(100);       // 3 sec aprox
            frame = frameGrabber.grabImage();
            BufferedImage bi = converter.convert(frame);
            ImageIO.write(bi, "jpg", new File(imagePath));
            frameGrabber.stop();
            return tempFileName;
        } catch (IOException e) {
            throw new ImtechoMobileException("Unable to wirte a thumbnail", e);
        }
    }

    public static String createThumbnailForImage(File inputImgFile) {
        String tempFileName = inputImgFile.getName().substring(0, inputImgFile.getName().lastIndexOf(".")) + "_." + "jpg";
        try {
            BufferedImage originalBufferedImage = ImageIO.read(inputImgFile);
            int width = (int)(originalBufferedImage.getWidth() * 0.4);
            int height = (int)(originalBufferedImage.getHeight() * 0.4);
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            img.createGraphics().drawImage(ImageIO.read(inputImgFile).getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            String temFileName = inputImgFile.getParentFile() + File.separator + tempFileName;
            ImageIO.write(img, "jpg", new File(temFileName));
            return tempFileName;
        } catch (IOException e) {
            throw new ImtechoMobileException("Unable to wirte a thumbnail", e);
        }
    }

    @Override
    public File getFile(Long id) throws FileNotFoundException {
        DocumentDto fileDto = DocumentMapper.entityToDto(documentDao.retrieveById(id));
        DocumentModuleMaster documentModuleMaster = documentModuleMasterDao.retrieveById(fileDto.getModuleId());
        return new File(ConstantUtil.REPOSITORY_PATH + File.separator + documentModuleMaster.getBasePath() + File.separator + fileDto.getFileName());
    }

    @Override
    public File getThumbnail(Long id) throws FileNotFoundException {
        DocumentDto fileDto = DocumentMapper.entityToDto(documentDao.retrieveById(id));
        DocumentModuleMaster documentModuleMaster = documentModuleMasterDao.retrieveById(fileDto.getModuleId());
        return new File(ConstantUtil.REPOSITORY_PATH + File.separator + documentModuleMaster.getBasePath() + File.separator + fileDto.getFileNameTh());
    }

    @Override
    public void removeDocument(Long id) {
        documentDao.updateTemporaryStatus(id);
    }

    @Override
    public void cronForRemoveTemporaryDocument() {
        List<DocumentDto> documentDtos = documentDao.getTemporaryData();

        if (!documentDtos.isEmpty()) {
            documentDtos.forEach(documentDto -> {
                DocumentModuleMaster documentModuleMaster = documentModuleMasterDao.retrieveById(documentDto.getModuleId());
                try {
                    Files.delete(Path.of(ConstantUtil.REPOSITORY_PATH + File.separator + documentModuleMaster.getBasePath() + File.separator + documentDto.getFileNameTh()));
                    Files.delete(Path.of(ConstantUtil.REPOSITORY_PATH + File.separator + documentModuleMaster.getBasePath() + File.separator + documentDto.getFileName()));
                    documentDao.deleteById(documentDto.getId());
                } catch (Exception e) {
                    Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.INFO, e.getMessage(), e);
                }
            });
        }
    }

    @Override
    public DocumentDto retrieveDocumentById(Long id) {
        return DocumentMapper.entityToDto(documentDao.retrieveById(id));
    } 

}
