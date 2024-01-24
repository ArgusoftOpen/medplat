package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.ncd.dto.NcdCardiologistDto;
import com.argusoft.medplat.ncd.model.NcdCardiologistData;

import java.util.Date;

public interface NcdCardiologistService {
    void saveCardiologistResponse(NcdCardiologistDto ncdCardiologistDto);

    NcdCardiologistData retrieveCardiologistReponse(Integer memberId, Date date);
}
