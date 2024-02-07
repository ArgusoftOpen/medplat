package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.dto.DrugInventoryDto;
import com.argusoft.medplat.ncd.dto.DrugInventoryHealthInfraDto;
import com.argusoft.medplat.ncd.dto.DrugInventoryMedicineDto;
import com.argusoft.medplat.ncd.model.DrugInventoryDetail;

import java.util.List;

public interface DrugInventoryDao extends GenericDao<DrugInventoryDetail, Integer> {

    List<DrugInventoryMedicineDto> retrieveAllDrugsList();

    DrugInventoryDto retrieveMedicine(Integer medicineId, Integer helathId);

    List<DrugInventoryHealthInfraDto> retrieveChildHealthInfraByParentId(Integer healthId);

    List<DrugInventoryDto> retrieveAllByDuration(String duration, Integer healthId);

    DrugInventoryDto retrieveAMC(Integer healthId, Integer medicineId);

    public List<DrugInventoryDto> retrieveQI(Integer healthId, String medicine);

    List<DrugInventoryMedicineDto> retrieveDrugReceivedByInfraId(Integer healthId);

    DrugInventoryDto retrieveMedicineByLocationId(Integer medicineId, Integer locationId);
}
