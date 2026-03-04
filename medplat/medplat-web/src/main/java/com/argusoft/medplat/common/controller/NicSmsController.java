/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.common.service.NicSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *<p>Define rest end point to send sms by National Informatics Centre</p>
 * @author ashish
 * @since 26/08/2020 10:30
 */
@RestController
@RequestMapping("/nicsms")
@Tag(name = "Nic Sms Controller", description = "")
public class NicSmsController {

    @Autowired
    NicSmsService nicSmsService;

    /**
     * Sends sms to given mobile numbers
     * @param a2wackid An acknowledgement id
     * @param a2wstatus An acknowledgement status
     * @param carrierstatus A carrier status
     * @param lastutime A last updated time
     * @param custref A customer reference
     * @param submitdt A date of submit
     * @param mnumber A mobile numbers
     * @param acode An account code
     * @param senderid Sender Id
     * @return A string of response
     */
    @GetMapping(value = "/smsresponse")
    public String smsResponse(
            @RequestParam(value = "a2wackid", required = true) String a2wackid,
            @RequestParam(value = "a2wstatus", required = false) String a2wstatus,
            @RequestParam(value = "carrierstatus", required = false) String carrierstatus,
            @RequestParam(value = "lastutime", required = false) String lastutime,
            @RequestParam(value = "custref", required = false) String custref,
            @RequestParam(value = "submitdt", required = false) String submitdt,
            @RequestParam(value = "mnumber", required = false) String mnumber,
            @RequestParam(value = "acode", required = false) String acode,
            @RequestParam(value = "senderid", required = false) String senderid) {

        nicSmsService.smsResponse(a2wackid, a2wstatus, carrierstatus, lastutime, custref, submitdt, mnumber, acode, senderid);
        return "Ok";
    }
}

