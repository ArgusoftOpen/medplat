package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MemberGeneralDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberGeneralDetail;
import com.argusoft.medplat.ncd.service.NcdGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ncd")
public class NcdGeneralController {

    @Autowired
    private NcdGeneralService ncdGeneralService;

    @PostMapping(value = "/general")
    public MemberGeneralDetail saveMemberGeneral(@RequestBody MemberGeneralDto memberGeneralDto){
        return ncdGeneralService.saveGeneral(memberGeneralDto);
    }

    @GetMapping(value = "/generalbydate")
    public MemberGeneralDto retrieveGeneralDetailsByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate, @RequestParam DoneBy type) {
        return ncdGeneralService.retrieveGeneralDetailsByMemberAndDate(memberId, new Date(screeningDate), type);
    }

    @GetMapping(value = "/lastrecordforgeneral")
    public MemberGeneralDetail retrieveLastRecordForGeneralByMemberId(@RequestParam Integer memberId) {
        return ncdGeneralService.retrieveLastRecordForGeneralByMemberId(memberId);
    }

    @GetMapping(value = "/lastcommentforgeneralbytype")
    public MemberGeneralDto retrieveLastCommentForGeneralByMemberIdAndType(@RequestParam Integer memberId, @RequestParam DoneBy type) {
        return ncdGeneralService.retrieveLastCommentForGeneralByMemberIdAndType(memberId,type);
    }
}
