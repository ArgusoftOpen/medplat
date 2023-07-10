/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.query.dto.QueryDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * Define services for rch register.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 11:00 AM
 */
public interface RchRegisterService {

    /**
     * Download rch register details in pdf.
     *
     * @param queryDto Query code details in order to fetch details.
     * @return Returns pdf for rch register.
     * @throws IOException          Exceptions produced by failed or interrupted I/O operations.
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     * @throws ParseException       Signals that an error has been reached unexpectedly while parsing.
     */
    ByteArrayOutputStream downLoadPdf(QueryDto queryDto) throws IOException, InterruptedException, ParseException;

    /**
     * Download rch register details in excel by service type.
     *
     * @param data                Details of rch register.
     * @param selectedServiceType Selected service type.
     * @return Returns excel sheet for rch register.
     */
    ByteArrayOutputStream downLoadExcelByType(List<LinkedHashMap<String, Object>> data, String selectedServiceType);

    /**
     * Download rch register details in excel.
     *
     * @param queryDto Query code details in order to fetch details.
     * @return Returns excel sheet for rch register.
     */
    ByteArrayOutputStream downloadExcel(QueryDto queryDto);

}
