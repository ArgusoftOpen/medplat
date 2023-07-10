/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dao;

import com.argusoft.medplat.training.model.Certificate;
import com.argusoft.medplat.database.common.GenericDao;
import java.util.List;

/**
 *
 * <p>
 * Define methods for certificate.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
public interface CertificateDao extends GenericDao<Certificate, Integer> {


    /**
     * Used to get certificates of given course id and type.
     * @param courseId Course id.
     * @param type Certificate type.
     * @return Returns list of certificates.
     */
    List<Certificate> getCertificatesByCourseAndType(Integer courseId,
            Certificate.Type type);

    /**
     * Used to get certificates by training id and user id.
     * @param userId User id.
     * @param trainingId Training id.
     * @return Returns list of certificates.
     */
    List<Certificate> getCertificatesByTrainingAndUser(Integer trainingId,
            Integer userId);

    /**
     * Used to get certificates of given list of training ids,course id and type
     * @param trainingIds List of training ids.
     * @param courseId Course id.
     * @param type Certificate type.
     * @return Returns list of certificates.
     */
    List<Certificate> getCertificatesByTrainingIdsAndCourseAndType(List<Integer> trainingIds,
            Integer courseId,
            Certificate.Type type);
}
