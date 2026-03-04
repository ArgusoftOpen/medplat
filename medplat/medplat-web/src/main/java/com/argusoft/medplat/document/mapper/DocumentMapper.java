/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.document.mapper;

import com.argusoft.medplat.document.dto.DocumentDto;
import com.argusoft.medplat.document.model.DocumentMaster;

/**
 * @author jay
 */
public class DocumentMapper {

    private DocumentMapper() {
        throw new IllegalStateException("Utility Class");
    }

    public static DocumentDto convertToDto(String fileName, String fileNameTh, String extention) {
        DocumentDto fileMasterDto = new DocumentDto();
        fileMasterDto.setExtension(extention);
        fileMasterDto.setFileName(fileName);
        fileMasterDto.setFileNameTh(fileNameTh);

        return fileMasterDto;
    }

    public static DocumentMaster entityMaker(String fileName, String fileNameTh, String extention, String originalName, int masterId, boolean isTemporary) {
        DocumentMaster fileMaster = new DocumentMaster();
        fileMaster.setExtension(extention);
        fileMaster.setFileName(fileName);
        fileMaster.setFileNameTh(fileNameTh);
        fileMaster.setActualFileName(originalName);
        fileMaster.setModuleId(masterId);

        if (isTemporary) {
            fileMaster.setIsTemporary(isTemporary);
        }
        return fileMaster;
    }

    public static DocumentDto entityToDto(DocumentMaster master) {
        DocumentDto fileMasterDto = new DocumentDto();
        fileMasterDto.setExtension(master.getExtension());
        fileMasterDto.setActualFileName(master.getActualFileName());
        fileMasterDto.setFileName(master.getFileName());
        fileMasterDto.setFileNameTh(master.getFileNameTh());
        fileMasterDto.setId(master.getId());
        fileMasterDto.setModuleId(master.getModuleId());
        fileMasterDto.setIsTemporary(master.getIsTemporary());
        return fileMasterDto;
    }
}
