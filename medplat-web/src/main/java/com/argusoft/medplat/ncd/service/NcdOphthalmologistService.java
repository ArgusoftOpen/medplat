package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.ncd.dto.NcdOphthalmologistDto;
import com.argusoft.medplat.ncd.model.NcdOphthalmologistData;

import java.util.Date;

public interface NcdOphthalmologistService {
    void saveOphthalmologistResponse(NcdOphthalmologistDto ncdOphthalmologistDto);

    NcdOphthalmologistData retrieveOphthalmologistReponse(Integer memberId, Date date);
}
