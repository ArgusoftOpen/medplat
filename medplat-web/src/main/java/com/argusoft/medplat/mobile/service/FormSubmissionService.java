/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.service;

import com.argusoft.medplat.mobile.dto.RecordStatusBean;

/**
 *
 * @author prateek
 */
public interface FormSubmissionService {

    RecordStatusBean[] recordsEntryFromMobileToDBServer(String token, String[] records);
}
