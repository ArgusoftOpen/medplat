package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.MOReviewFollowupDto;
import com.argusoft.medplat.ncd.model.MOReviewFollowupDetail;
import com.argusoft.medplat.ncd.service.NcdMOReviewFollowupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ncd")
public class NcdMOReviewFollowupController {

    @Autowired
    private NcdMOReviewFollowupService ncdMOReviewFollowupService;

    @PostMapping(value = "/moreviewfollowup")
    public void saveMOReviewFollowup(@RequestBody MOReviewFollowupDto moReviewFollowupDto){
        ncdMOReviewFollowupService.saveMOReviewFollowup(moReviewFollowupDto);
    }

    @GetMapping(value = "/moreviewfollowupbydate")
    public MOReviewFollowupDetail retrieveMoReviewFollowupByMemberAndDate(@RequestParam Integer memberId, @RequestParam Long screeningDate){
        return ncdMOReviewFollowupService.retrieveMoReviewFollowupByMemberAndDate(memberId,new Date(screeningDate));
    }

    @GetMapping(value = "/getLastMOReviewFollowupComment")
    public MOReviewFollowupDto retrieveLastCommentByMOReviewFollowup(@RequestParam Integer memberId) {
        return ncdMOReviewFollowupService.retrieveLastCommentByMOReviewFollowup(memberId);
    }
}
