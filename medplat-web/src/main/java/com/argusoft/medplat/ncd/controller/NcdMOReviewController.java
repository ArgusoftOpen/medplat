package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MOReviewDto;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.model.MOReviewDetail;
import com.argusoft.medplat.ncd.service.NcdMOReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/ncd")
public class NcdMOReviewController {

    @Autowired
    private NcdMOReviewService ncdMOReviewService;

    @PostMapping(value = "/moreview")
    public void saveMOReview(@RequestBody MOReviewDto moReviewDto){
        ncdMOReviewService.saveMOReview(moReviewDto);
    }

    @GetMapping(value = "/moreviewbydate")
    public MOReviewDetail retrieveMoReviewByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate){
        return ncdMOReviewService.retrieveMoReviewByMemberAndDate(memberId,new Date(screeningDate));
    }

    @GetMapping(value = "/retreiveSearchedMembersMOReview")
    public List<MemberDetailDto> retreiveSearchedMembersMOReview(@RequestParam(required = false) Integer limit, @RequestParam Integer offset, @RequestParam String searchBy, @RequestParam String searchString, @RequestParam(required = false) Boolean flag){
        return ncdMOReviewService.retreiveSearchedMembersMOReview(limit,offset,searchBy,searchString,flag);
    }

    @GetMapping(value = "/getLastMOReviewComment")
    public MOReviewDto retrieveLastCommentByMOReview(@RequestParam Integer memberId) {
        return ncdMOReviewService.retrieveLastCommentByMOReview(memberId);
    }

}
