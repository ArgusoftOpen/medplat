/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.mapper;

import com.argusoft.medplat.training.dto.CertificateDto;
import com.argusoft.medplat.training.model.Certificate;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * <p>
 *     Mapper for certificate in order to convert dto to model or model to dto.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class CertificateMapper {
    private CertificateMapper(){}

    /**
     * Convert certificate details into entity.
     * @param certificateDto Details of certificate.
     * @param certificate Entity of certificate.
     * @return Returns entity of certificate.
     */
    public static Certificate dtoToEntityCertificate(CertificateDto certificateDto,Certificate certificate) {
        if(certificate == null){
            certificate = new Certificate();
        }

        certificate.setCertificateId(certificateDto.getCertificateId());
        certificate.setCertificateName(certificateDto.getCertificateName());
        certificate.setCertificateDescription(certificateDto.getCertificateDescription());
        certificate.setState(certificateDto.getCertificateState());
        certificate.setCertificateRemarks(certificateDto.getCertificateRemarks());
        certificate.setTrainingId(certificateDto.getTrainingId());
        certificate.setCourseId(certificateDto.getCourseId());
        certificate.setUserId(certificateDto.getUserId());
        certificate.setCertificationDate(certificateDto.getCertificationDate());
        certificate.setGradeType(certificateDto.getGradeType());
        certificate.setType(certificateDto.getCertificateType());

        return certificate;
    }

    /**
     * Convert certificate entity into details.
     * @param certificate Entity of certificate.
     * @return Returns details of certificate.
     */
    public static CertificateDto entityToDtoCertificate(Certificate certificate) {
        CertificateDto certificateDto = new CertificateDto();

        certificateDto.setCertificateId(certificate.getCertificateId());
        certificateDto.setCertificateName(certificate.getCertificateName());
        certificateDto.setCertificateDescription(certificate.getCertificateDescription());
        certificateDto.setCertificateState(certificate.getState());
        certificateDto.setCertificateRemarks(certificate.getCertificateRemarks());
        certificateDto.setTrainingId(certificate.getTrainingId());
        certificateDto.setCourseId(certificate.getCourseId());
        certificateDto.setUserId(certificate.getUserId());
        certificateDto.setCertificationDate(certificate.getCertificationDate());
        certificateDto.setGradeType(certificate.getGradeType());
        certificateDto.setCertificateType(certificate.getType());

        return certificateDto;
    }

    /**
     * Convert list of certificate entities into certificate details.
     * @param certificates List of certificate entities.
     * @return Returns list of certificate details.
     */
    public static List<CertificateDto> entityToDtoCertificateList(List<Certificate> certificates) {
        List<CertificateDto> certificateDtos = new LinkedList<>();
        for (Certificate certificate : certificates) {
            certificateDtos.add(CertificateMapper.entityToDtoCertificate(certificate));
        }

        return certificateDtos;
    }
}
