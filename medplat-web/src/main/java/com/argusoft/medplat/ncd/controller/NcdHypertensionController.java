package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MemberHyperTensionDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;
import com.argusoft.medplat.ncd.service.NcdHypertensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/ncd")
public class NcdHypertensionController {

    @Autowired
    private NcdHypertensionService ncdHypertensionService;

    @PostMapping(value = "/hypertension")
    public MemberHypertensionDetail saveMemberHypertension(@RequestBody MemberHyperTensionDto hyperTensionDto) {
        return ncdHypertensionService.saveHypertension(hyperTensionDto);
    }

    @GetMapping(value = "/hypertensionbydate")
    public MemberHypertensionDetail retrieveHypertensionDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate, @RequestParam DoneBy type) {
        return ncdHypertensionService.retrieveHypertensionDetailsByMemberAndDate(memberId, new Date(screeningDate),type);
    }

    @GetMapping(value = "/lastrecordforhypertension")
    public List<MemberHyperTensionDto> retrieveLastRecordForHypertensionByMemberId(@RequestParam Integer memberId) {
        return ncdHypertensionService.retrieveLastRecordForHypertensionByMemberId(memberId);
    }
}
