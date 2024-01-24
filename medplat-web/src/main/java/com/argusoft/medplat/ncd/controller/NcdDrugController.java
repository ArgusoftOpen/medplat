package com.argusoft.medplat.ncd.controller;

import com.argusoft.medplat.ncd.dto.DrugInventoryDto;
import com.argusoft.medplat.ncd.dto.DrugInventoryHealthInfraDto;
import com.argusoft.medplat.ncd.dto.DrugInventoryMedicineDto;
import com.argusoft.medplat.ncd.dto.MedicineDto;
import com.argusoft.medplat.ncd.service.NcdDrugService;
import com.argusoft.medplat.ncd.service.NcdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ncd")
public class NcdDrugController {

    @Autowired
    private NcdDrugService ncdDrugService;

    @GetMapping(value = "/druglist")
    public List<DrugInventoryMedicineDto> retrieveAllDrugsList() {
        return ncdDrugService.retrieveAllDrugsList();
    }

    @GetMapping(value = "/generaldrug")
    public List<MedicineDto> retrieveAllGeneralDrugs() {
        return ncdDrugService.retrieveAllGeneralDrugs();
    }

    @GetMapping(value = "/drugReceivedByInfraId/{healthId}")
    public List<DrugInventoryMedicineDto> retrieveDrugReceivedByInfraId(@PathVariable Integer healthId) {
        return ncdDrugService.retrieveDrugReceivedByInfraId(healthId);
    }

    @GetMapping(value = "/drugInventory/{healthId}")
    public List<DrugInventoryDto> retrieveAllDrugInventory(@PathVariable Integer healthId, @RequestParam String duration) {
        return ncdDrugService.retrieveAllDrugInventory(duration, healthId);
    }

    @PostMapping(value = "/drugIssued")
    public void saveDrugInventory(@RequestBody DrugInventoryDto drugInventoryDto){
        ncdDrugService.saveDrugInventorys(drugInventoryDto);
    }

    @GetMapping(value = "/retrieveHealthInfraByParentId/{healthId}")
    public List<DrugInventoryHealthInfraDto> retrieveChildHealthInfraByParentId(@PathVariable Integer healthId) {
        return ncdDrugService.retrieveChildHealthInfraByParentId(healthId);
    }
}
