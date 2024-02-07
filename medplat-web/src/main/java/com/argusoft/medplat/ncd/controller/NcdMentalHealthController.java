package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MemberMentalHealthDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberMentalHealthDetails;
import com.argusoft.medplat.ncd.service.NcdMentalHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ncd")
public class NcdMentalHealthController {

    @Autowired
    private NcdMentalHealthService ncdMentalHealthService;

    @PostMapping(value = "/mentalHealth")
    public MemberMentalHealthDetails saveMemberMentalHealth(@RequestBody MemberMentalHealthDto memberMentalHealthDto){
        return ncdMentalHealthService.saveMentalHealth(memberMentalHealthDto);
    }

    @GetMapping(value = "/mentalHealthbydate")
    public MemberMentalHealthDetails retrieveMentalHealthDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate, @RequestParam DoneBy type) {
        return ncdMentalHealthService.retrieveMentalHealthDetailsByMemberAndDate(memberId, new Date(screeningDate), type);
    }

    @GetMapping(value = "/lastrecordforMentalHealth")
    public MemberMentalHealthDetails retrieveLastRecordForMentalHealthByMemberId(@RequestParam Integer memberId) {
        return ncdMentalHealthService.retrieveLastRecordForMentalHealthByMemberId(memberId);
    }
}
