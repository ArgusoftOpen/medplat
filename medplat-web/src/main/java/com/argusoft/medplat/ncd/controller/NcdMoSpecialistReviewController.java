package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.NcdAmputationMemberDetailDto;
import com.argusoft.medplat.ncd.dto.NcdEcgMemberDetailDto;
import com.argusoft.medplat.ncd.dto.NcdRenalMemberDetailDto;
import com.argusoft.medplat.ncd.dto.NcdStrokeMemberDetailDto;
import com.argusoft.medplat.ncd.service.NcdMoSpecialistReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ncd")
public class NcdMoSpecialistReviewController {

    @Autowired
    private NcdMoSpecialistReviewService ncdMoSpecialistReviewService;

    @PostMapping(value = "/saveEcgData")
    public void saveEcgData(@RequestBody NcdEcgMemberDetailDto dto){
        ncdMoSpecialistReviewService.saveEcgData(dto);
    }

    @PostMapping(value = "/saveStrokeData")
    public void saveStrokeData(@RequestBody NcdStrokeMemberDetailDto dto){
        ncdMoSpecialistReviewService.saveStrokeData(dto);
    }

    @PostMapping(value = "/saveAmputationData")
    public void saveAmputationData(@RequestBody NcdAmputationMemberDetailDto dto){
        ncdMoSpecialistReviewService.saveAmputationData(dto);
    }

    @PostMapping(value = "/saveRenalData")
    public void saveRenalData(@RequestBody NcdRenalMemberDetailDto dto){
        ncdMoSpecialistReviewService.saveRenalData(dto);
    }




}
