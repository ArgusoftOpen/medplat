package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MemberDiabetesDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberDiabetesDetail;
import com.argusoft.medplat.ncd.service.NcdDiabetesService;
import com.argusoft.medplat.ncd.service.NcdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ncd")
public class NcdDiabetesController {

    @Autowired
    private NcdDiabetesService ncdDiabetesService;

    @PostMapping(value = "/diabetes")
    public MemberDiabetesDetail saveDiabetes(@RequestBody MemberDiabetesDto diabetesDto) {
        return ncdDiabetesService.saveDiabetes(diabetesDto);
    }

    @GetMapping(value = "/diabetesbydate")
    public MemberDiabetesDetail retrieveDiabetesDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate, @RequestParam DoneBy type) {
        return ncdDiabetesService.retrieveDiabetesDetailsByMemberAndDate(memberId, new Date(screeningDate),type);
    }

    @GetMapping(value = "/lastrecordfordiabetes")
    public MemberDiabetesDto retrieveLastRecordForDiabetesByMemberId(@RequestParam Integer memberId) {
        return ncdDiabetesService.retrieveLastRecordForDiabetesByMemberId(memberId);
    }


}
