package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MemberInvestigationDto;
import com.argusoft.medplat.ncd.model.MemberInvestigationDetail;
import com.argusoft.medplat.ncd.service.NcdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ncd")
public class NcdInvestigationController {

    @Autowired
    private NcdService ncdService;

    @PostMapping(value = "/investigation")
    public MemberInvestigationDetail saveInvestigation(@RequestBody MemberInvestigationDto memberInvestigationDto) {
        return ncdService.saveInvestigation(memberInvestigationDto);
    }

    @GetMapping(value = "/retreiveInvestigationDetailByMemberId")
    public List<MemberInvestigationDetail> retreiveInvestigationDetailByMemberId(@RequestParam Integer memberId){
        return ncdService.retreiveInvestigationDetailByMemberId(memberId);
    }
}
