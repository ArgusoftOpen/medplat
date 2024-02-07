package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MemberHyperTensionDto;
import com.argusoft.medplat.ncd.dto.NcdCVCFormDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;
import com.argusoft.medplat.ncd.model.NcdCVCForm;
import com.argusoft.medplat.ncd.service.NcdCVCFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ncd")
public class NcdCVCFormController {

    @Autowired
    private NcdCVCFormService ncdCVCFormService;

    @PostMapping(value = "/cvcform")
    public NcdCVCForm saveCVCForm(@RequestBody NcdCVCFormDto ncdCVCFormDto) {
        return ncdCVCFormService.saveCVCForm(ncdCVCFormDto);
    }

    @GetMapping(value = "/cvcByDate")
    public NcdCVCForm retrieveCVCDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate, @RequestParam DoneBy type) {
        return ncdCVCFormService.retrieveCVCDetailsByMemberAndDate(memberId, new Date(screeningDate),type);
    }
}
