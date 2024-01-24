package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.NcdCardiologistDto;
import com.argusoft.medplat.ncd.model.NcdCardiologistData;
import com.argusoft.medplat.ncd.service.NcdCardiologistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ncd")
public class NcdCardiologistController {

    @Autowired
    private NcdCardiologistService ncdCardiologistService;

    @PostMapping(value = "/saveCardiologistResponse")
    public void saveCardiologistResponse (@RequestBody NcdCardiologistDto ncdCardiologistDto){
        ncdCardiologistService.saveCardiologistResponse(ncdCardiologistDto);
    }

    @GetMapping(value = "/retrieveCardiologistReponse")
    public NcdCardiologistData retrieveCardiologistReponse(@RequestParam Integer memberId, @RequestParam Long screeningDate){
        return ncdCardiologistService.retrieveCardiologistReponse(memberId,new Date(screeningDate));
    }
}
