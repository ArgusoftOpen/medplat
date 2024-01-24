package com.argusoft.medplat.ncd.service;

import com.argusoft.medplat.ncd.dto.*;

import java.util.List;

public interface NcdDrugService {

    List<DrugInventoryMedicineDto> retrieveAllDrugsList();
    List<MedicineDto> retrieveAllGeneralDrugs();
    List<DrugInventoryMedicineDto> retrieveDrugReceivedByInfraId(Integer healthId);
    List<DrugInventoryDto> retrieveAllDrugInventory(String duration, Integer healthId);
    List<DrugInventoryDetailDataBean> retrieveDrugInventoryDetails(Integer userId);
    void saveDrugInventorys(DrugInventoryDto drugInventoryDto);
    List<DrugInventoryHealthInfraDto> retrieveChildHealthInfraByParentId(Integer healthId);
}
