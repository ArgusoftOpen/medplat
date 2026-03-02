package com.argusoft.medplat.rch.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.rch.service.RchRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;

/**
 * <p>
 * Define APIs for rch register.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/rchregister")
@Tag(name = "Rch Register Controller", description = "")
public class RchRegisterController {

    @Autowired
    ImtechoSecurityUser user;

    @Autowired
    RchRegisterService rchRegisterService;

    /**
     * Download rch register details in pdf.
     *
     * @param queryDto Query code details in order to fetch details.
     * @return Returns pdf for rch register.
     * @throws IOException          Exceptions produced by failed or interrupted I/O operations.
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     * @throws ParseException       Signals that an error has been reached unexpectedly while parsing.
     */
    @PostMapping(value = "/downloadpdf")
    public ResponseEntity<byte[]> downLoadPdf(@RequestBody QueryDto queryDto) throws IOException, InterruptedException, ParseException {
        ByteArrayOutputStream pdfOPStream = rchRegisterService.downLoadPdf(queryDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        byte[] data = pdfOPStream.toByteArray();
        headers.setContentDispositionFormData("attachment", "test.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    /**
     * Download rch register details in excel.
     *
     * @param queryDto Query code details in order to fetch details.
     * @param response Instance of HttpServletResponse.
     * @return Returns excel sheet for rch register.
     */
    @PostMapping(value = "/downloadexcel", produces = {"application/vnd.ms-excel"})
    public ResponseEntity<byte[]> downloadExcel(@RequestBody QueryDto queryDto, HttpServletResponse response) {

        ByteArrayOutputStream pdfOPStream = rchRegisterService.downloadExcel(queryDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        byte[] data = pdfOPStream.toByteArray();
        headers.setContentDispositionFormData("attachment", "test.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

}