package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MbbsMOReviewDto;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.model.MbbsMOReviewDetail;
import com.argusoft.medplat.ncd.service.NcdMBBSReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ncd")
public class NcdMBBSReviewController {

    @Autowired
    private NcdMBBSReviewService ncdMBBSReviewService;

    @GetMapping(value = "/getmbbsmoreview")
    public MbbsMOReviewDetail retrieveAllMbbsMO(@RequestParam Integer memberId) {
        return ncdMBBSReviewService.retrieveMbbsMOReviewDetail(memberId);
    }

    @PostMapping(value = "/mbbsmoreview")
    public void saveMbbsMOReview(@RequestBody MbbsMOReviewDto mbbsMOReviewDto){
        ncdMBBSReviewService.saveMbbsMOReview(mbbsMOReviewDto);
    }

    @GetMapping(value = "/mrsmembers")
    public List<MemberDetailDto> retrieveMembersForMbbsMOReview(@RequestParam(required = false) Integer limit,
                                                                @RequestParam(required = false) Integer offset,
                                                                @RequestParam(required = false) String healthInfrastructureType,
                                                                @RequestParam(required = false) String searchString,
                                                                @RequestParam(required = false) String searchBy,
                                                                @RequestParam(required = false) String searchStatus) {
        return ncdMBBSReviewService.retrieveMembersForMbbsMOReview(limit, offset, healthInfrastructureType, searchBy, searchString,searchStatus);
    }

    @GetMapping(value = "/getLastMBBSComment")
    public MbbsMOReviewDto retrieveLastCommentByMBBS(@RequestParam Integer memberId) {
        return ncdMBBSReviewService.retrieveLastCommentByMBBS(memberId);
    }
}
