package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.NcdOphthalmologistDto;
import com.argusoft.medplat.ncd.model.NcdCardiologistData;
import com.argusoft.medplat.ncd.model.NcdOphthalmologistData;
import com.argusoft.medplat.ncd.service.NcdOphthalmologistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ncd")
public class NcdOphthalmologistController {

    @Autowired
    private NcdOphthalmologistService ncdOphthalmologistService;

    @PostMapping(value = "/saveOphthalmologistResponse")
    public void saveOphthalmologistResponse (@RequestBody NcdOphthalmologistDto ncdOphthalmologistDto){
        ncdOphthalmologistService.saveOphthalmologistResponse(ncdOphthalmologistDto);
    }

    @GetMapping(value = "/retrieveOphthalmologistReponse")
    public NcdOphthalmologistData retrieveOphthalmologistReponse(@RequestParam Integer memberId, @RequestParam Long screeningDate){
        return ncdOphthalmologistService.retrieveOphthalmologistReponse(memberId,new Date(screeningDate));
    }
}
