package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MemberInitialAssessmentDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;
import com.argusoft.medplat.ncd.service.NcdInitialAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ncd")
public class NcdInitialAssessmentController {

    @Autowired
    private NcdInitialAssessmentService ncdInitialAssessmentService;

    @PostMapping(value = "/initialAssessment")
    public MemberInitialAssessmentDetail saveMemberInitialAssessment(@RequestBody MemberInitialAssessmentDto memberInitialAssessmentDto){
        return ncdInitialAssessmentService.saveInitialAssessment(memberInitialAssessmentDto);
    }

    @GetMapping(value = "/initialAssessmentbydate")
    public MemberInitialAssessmentDetail retrieveInitialAssessmentDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate, @RequestParam DoneBy type) {
        return ncdInitialAssessmentService.retrieveInitialAssessmentDetailsByMemberAndDate(memberId, new Date(screeningDate),type);
    }

    @GetMapping(value = "/lastrecordforinitialAssessment")
    public MemberInitialAssessmentDetail retrieveLastRecordForInitialAssessmentByMemberId(@RequestParam Integer memberId) {
        return ncdInitialAssessmentService.retrieveLastRecordForInitialAssessmentByMemberId(memberId);
    }
}
