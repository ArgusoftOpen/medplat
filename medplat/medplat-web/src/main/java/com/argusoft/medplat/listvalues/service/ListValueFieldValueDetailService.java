package com.argusoft.medplat.listvalues.service;

import com.argusoft.medplat.mobile.dto.OptionTagDto;

import java.util.List;

public interface ListValueFieldValueDetailService {

    public String getListValueNameFormId(Integer id);

    List<OptionTagDto> getListValuesFromFieldKey(String fieldKey);
}
