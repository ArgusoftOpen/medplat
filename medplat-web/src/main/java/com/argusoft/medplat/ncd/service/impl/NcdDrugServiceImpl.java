package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.web.healthinfra .dao.HealthInfrastructureDetailsDao;
import com.argusoft.medplat.web.healthinfra.model.HealthInfrastructureDetails;
import com.argusoft.medplat.ncd.dao.DrugInventoryDao;
import com.argusoft.medplat.ncd.dao.MemberGeneralDetailDao;
import com.argusoft.medplat.ncd.dto.*;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.DrugInventoryDetail;
import com.argusoft.medplat.ncd.service.NcdDrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NcdDrugServiceImpl implements NcdDrugService {

    @Autowired
    private DrugInventoryDao drugInventoryDao;
    @Autowired
    private MemberGeneralDetailDao memberGeneralDetailDao;
    @Autowired
    private HealthInfrastructureDetailsDao healthInfrastructureDetailsDao;

    @Override
    public List<DrugInventoryMedicineDto> retrieveAllDrugsList() {
        return this.drugInventoryDao.retrieveAllDrugsList();
    }

    @Override
    public List<MedicineDto> retrieveAllGeneralDrugs() {
        return memberGeneralDetailDao.retrieveGeneralDrugs();
    }

    @Override
    public List<DrugInventoryMedicineDto> retrieveDrugReceivedByInfraId(Integer healthId) {
        return drugInventoryDao.retrieveDrugReceivedByInfraId(healthId);
    }

    @Override
    public List<DrugInventoryDto> retrieveAllDrugInventory(String duration, Integer healthId) {
        List<DrugInventoryDto> drugInventoryDtos = new ArrayList<>();
        List<DrugInventoryDto> drugInventoryDetails = drugInventoryDao.retrieveAllByDuration(duration, healthId);
        for (DrugInventoryDto drugInventoryDetail : drugInventoryDetails) {
            drugInventoryDetail.setBalanceInHand(drugInventoryDetail.getQuantityReceived() - drugInventoryDetail.getQuantityIssued());
            DrugInventoryDto amc = drugInventoryDao.retrieveAMC(drugInventoryDetail.getHealthInfraId(), drugInventoryDetail.getMedicineId());
            if(amc!=null){
                drugInventoryDetail.setAppMonthly(amc.getQuantityIssued() / 3);
            }
            drugInventoryDtos.add(drugInventoryDetail);
        }
        return drugInventoryDtos;
    }
    @Override
    public List<DrugInventoryDetailDataBean> retrieveDrugInventoryDetails(Integer userId) {
        if (userId != null) {
            List<HealthInfrastructureDetails> healthInfraDetails = healthInfrastructureDetailsDao.getHealthInfraByUserIdAndType(userId, "SC");
            if (healthInfraDetails != null && !healthInfraDetails.isEmpty()) {
                List<DrugInventoryDetailDataBean> drugInventoryDetailList = new ArrayList<>();
                for (HealthInfrastructureDetails bean : healthInfraDetails) {
                    List<DrugInventoryDto> drugInventoryDtos = retrieveAllDrugInventory("CFY", bean.getId());
                    if (drugInventoryDtos != null && !drugInventoryDtos.isEmpty()) {
                        for (DrugInventoryDto dto : drugInventoryDtos) {
                            DrugInventoryDetailDataBean inventory = new DrugInventoryDetailDataBean();
                            inventory.setLocationId(bean.getLocationId());
                            inventory.setMedicineId(dto.getMedicineId());
                            inventory.setMedicineName(dto.getMedicineName());
                            inventory.setBalanceInHand(dto.getBalanceInHand());
                            drugInventoryDetailList.add(inventory);
                        }
                    }
                }
                return drugInventoryDetailList;
            }
        }
        return null;
    }

    @Override
    public void saveDrugInventorys(DrugInventoryDto drugInventoryDto) {
        DrugInventoryDetail drugInventoryDetail = MemberDetailMapper.dtoToEntityForDrugInventoryDetail(drugInventoryDto);
        DrugInventoryDto lastParentInventoryDto = drugInventoryDao.retrieveMedicine
                (drugInventoryDetail.getMedicineId(), drugInventoryDto.getParentHealthId());
        if (drugInventoryDto.getIsReceived()) {
            if (lastParentInventoryDto != null) {
                drugInventoryDetail.setBalanceInHand(lastParentInventoryDto.getBalanceInHand() + drugInventoryDto.getQuantityReceived());
            } else {
                drugInventoryDetail.setBalanceInHand(drugInventoryDto.getQuantityReceived());
            }
        } else if (drugInventoryDto.getIsIssued()) {
            DrugInventoryDto lastInventoryDto = drugInventoryDao.retrieveMedicine
                    (drugInventoryDetail.getMedicineId(), drugInventoryDto.getHealthInfraId());
            Integer balanceInHand = 0;
            Integer parentBalanceInHand = lastParentInventoryDto.getBalanceInHand();
            if (lastInventoryDto != null) {
                balanceInHand = lastInventoryDto.getBalanceInHand();
            }
            if (drugInventoryDto.getIsReturn()) {
                if (balanceInHand < drugInventoryDetail.getQuantityReceived()) {
                    throw new ImtechoSystemException("Available Medicine Quantity :" + balanceInHand, 500);
                }
                balanceInHand -= drugInventoryDetail.getQuantityReceived();
                drugInventoryDetail.setQuantityIssued(0);
                drugInventoryDetail.setQuantityReceived(-drugInventoryDetail.getQuantityReceived());
                lastParentInventoryDto.setQuantityReceived(0);
                lastParentInventoryDto.setQuantityIssued(-drugInventoryDto.getQuantityReceived());
                lastParentInventoryDto.setIsReturn(true);
                parentBalanceInHand += drugInventoryDto.getQuantityReceived();
            } else {
                if (parentBalanceInHand < drugInventoryDetail.getQuantityReceived()) {
                    throw new ImtechoSystemException("Parent Health Facility Available Medicine Quantity : " + parentBalanceInHand, 500);
                }
                balanceInHand += drugInventoryDetail.getQuantityReceived();
                parentBalanceInHand -= drugInventoryDto.getQuantityReceived();
                lastParentInventoryDto.setQuantityIssued(drugInventoryDto.getQuantityReceived());
                lastParentInventoryDto.setQuantityReceived(0);
                lastParentInventoryDto.setIsReturn(false);
            }
            drugInventoryDetail.setBalanceInHand(balanceInHand);
            lastParentInventoryDto.setBalanceInHand(parentBalanceInHand);
            lastParentInventoryDto.setIssuedDate(drugInventoryDto.getIssuedDate());
            lastParentInventoryDto.setIsReceived(false);
            lastParentInventoryDto.setIsIssued(true);
            DrugInventoryDetail lastDrugInventory = MemberDetailMapper.dtoToEntityForDrugInventoryDetail(lastParentInventoryDto);
            lastDrugInventory.setId(null);
            this.drugInventoryDao.create(lastDrugInventory);
        }
        this.drugInventoryDao.create(drugInventoryDetail);
    }

    @Override
    public List<DrugInventoryHealthInfraDto> retrieveChildHealthInfraByParentId(Integer healthId) {
        List<DrugInventoryHealthInfraDto> drugInventoryHealthInfraDtos = drugInventoryDao.retrieveChildHealthInfraByParentId(healthId);
        return drugInventoryHealthInfraDtos;
    }
}
