/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.argusoft.medplat.rch.dao.VaccineAdverseEffectDao;
import com.argusoft.medplat.rch.model.VaccineAdverseEffectMaster;
import com.argusoft.medplat.rch.service.VaccineAdverseEffectService;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * Define services for vaccine adverse effect.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class VaccineAdverseEffectServiceImpl implements VaccineAdverseEffectService {

    @Autowired
    VaccineAdverseEffectDao vaccineAdverseEffectDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer storeVaccineAdverseEffectForm(ParsedRecordBean parsedRecordBean, Map<String, String> keyAndAnswerMap, UserMaster user) {

        if (keyAndAnswerMap.get("3") != null && keyAndAnswerMap.get("3").equals("NO")) {
            return 1;
        }

        Integer childId = Integer.valueOf(keyAndAnswerMap.get("-4"));
        Integer familyId = Integer.valueOf(keyAndAnswerMap.get("-5"));
        Integer locationId = Integer.valueOf(keyAndAnswerMap.get("-6"));

        VaccineAdverseEffectMaster vaccineAdverseEffectMaster = new VaccineAdverseEffectMaster();
        vaccineAdverseEffectMaster.setMemberId(childId);
        vaccineAdverseEffectMaster.setFamilyId(familyId);
        vaccineAdverseEffectMaster.setLocationId(locationId);
        vaccineAdverseEffectMaster.setLatitude(keyAndAnswerMap.get("-2"));
        vaccineAdverseEffectMaster.setLongitude(keyAndAnswerMap.get("-1"));
        if ((keyAndAnswerMap.get("-8")) != null && !keyAndAnswerMap.get("-8").equals("null")) {
            vaccineAdverseEffectMaster.setMobileStartDate(new Date(Long.parseLong(keyAndAnswerMap.get("-8"))));
        } else {
            vaccineAdverseEffectMaster.setMobileStartDate(new Date(0L));
        }
        if ((keyAndAnswerMap.get("-9")) != null && !keyAndAnswerMap.get("-9").equals("null")) {
            vaccineAdverseEffectMaster.setMobileEndDate(new Date(Long.parseLong(keyAndAnswerMap.get("-9"))));
        } else {
            vaccineAdverseEffectMaster.setMobileEndDate(new Date(0L));
        }
        vaccineAdverseEffectMaster.setNotificationId(Integer.valueOf(parsedRecordBean.getNotificationId()));

        for (Map.Entry<String, String> entry : keyAndAnswerMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            storeAnswersToVaccineAdverseEffectMaster(key, value, vaccineAdverseEffectMaster);
        }

        vaccineAdverseEffectDao.create(vaccineAdverseEffectMaster);
        return 1;
    }

    /**
     * Set answers to vaccine adverse effect details.
     *
     * @param key                        Key.
     * @param answer                     Answer for vaccine adverse effect details.
     * @param vaccineAdverseEffectMaster Vaccine adverse effect details.
     */
    private void storeAnswersToVaccineAdverseEffectMaster(String key, String answer, VaccineAdverseEffectMaster vaccineAdverseEffectMaster) {
        switch (key) {
            case "3":
                vaccineAdverseEffectMaster.setAdverseEffect(answer);
                break;
            case "4":
                vaccineAdverseEffectMaster.setVaccineName(answer);
                break;
            case "5":
                vaccineAdverseEffectMaster.setBatchNumber(answer);
                break;
            case "6":
                vaccineAdverseEffectMaster.setExpiryDate(new Date(Long.parseLong(answer)));
                break;
            case "7":
                vaccineAdverseEffectMaster.setManufacturer(answer);
                break;
            default:
        }
    }
}
