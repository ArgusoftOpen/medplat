/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.service;

import com.argusoft.medplat.training.dto.CertificateDto;
import com.argusoft.medplat.training.model.Certificate;
import java.util.List;

/**
 *
 * <p>
 *     Define services for certificate.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public interface CertificateService {
   
    /**
     * Add new certificate details.
     * @param certificateDto Certificate details.
     */
    void createCertificate(CertificateDto certificateDto);
    
    /**
     * Update certificate details.
     * @param certificateDto Certificate details.
     */
    void updateCertificate(CertificateDto certificateDto) ;

    /**
     * Used to get certificates of given courseId and type.
     * @param courseId Course id.
     * @param type Certificate type.
     * @return Returns certificate details.
     */
    List<CertificateDto> getCertificatesByCourseAndType(Integer courseId,
            Certificate.Type type);

    /**
     * Used to get certificates by trainingId and userId.
     * @param userId User id.
     * @param trainingId Training id.
     * @return Returns certificate details.
     */
    List<CertificateDto> getCertificatesByTrainingAndUser(Integer trainingId,
            Integer userId);


    /**
     * Used to get certificates by trainingId,courseId and certificate type .
     * @param trainingIds List of trainings ids.
     * @param courseId Course id.
     * @param type Certificate type.
     * @return Returns certificate details.
     */
    List<CertificateDto> getCertificatesByTrainingIdsAndCourseAndType(List<Integer> trainingIds,
            Integer courseId,
            Certificate.Type type);
}
