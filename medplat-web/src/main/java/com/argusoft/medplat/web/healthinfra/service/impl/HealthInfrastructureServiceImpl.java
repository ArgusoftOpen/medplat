package com.argusoft.medplat.web.healthinfra.service.impl;

import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureDetailsDao;
import com.argusoft.medplat.web.healthinfra.service.HealthInfrastructureService;
import com.argusoft.medplat.web.location.constants.LocationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Define services for health infra structure.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class HealthInfrastructureServiceImpl implements HealthInfrastructureService {

    private static final Logger logger = LoggerFactory.getLogger(HealthInfrastructureServiceImpl.class);


    @Autowired
    HealthInfrastructureDetailsDao healthInfrastructureDetailsDao;

//    @Autowired
//    private NDHMBridgeService ndhmBridgeService;
//
//    @Autowired
//    @Qualifier(value = "allHealthInfrastructures")
//    private TenantCacheProvider<List<HealthInfrastructureBean>> tenantCacheProviderForAllHealthInfrastructures;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAllHealthInfrastructureForMobile() {
        System.out.println("Updating Health Infrastructures For Mobile");
        LocationConstants.allHealthInfrastructureForMobile = healthInfrastructureDetailsDao.retrieveAllHealthInfrastructureForMobile(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleActive(Integer healthInfraId, String state) {
        healthInfrastructureDetailsDao.toggleActive(healthInfraId, state);
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public List<HealthInfrastructureBean> getHealthInfrastructurePrivateHospital(String query) {
//        return healthInfrastructureDetailsDao.getHealthInfrastructureByType(ConstantUtil.PRIVATE_HOSPITAL_FIELD_ID, query);
//    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public void updateHealthInfraStructure(Integer id, FacilityBasicInformation basicInformation) {
//        HealthInfrastructureDetails details = healthInfrastructureDetailsDao.retrieveById(id);
//        HealthInfraStructureOtherDetails otherDetails;
//        Gson gson = new Gson();
//        if (details.getOtherDetails() != null && !details.getOtherDetails().isEmpty()) {
//            otherDetails = gson.fromJson(details.getOtherDetails(), HealthInfraStructureOtherDetails.class);
//        } else {
//            otherDetails = new HealthInfraStructureOtherDetails();
//        }
//        otherDetails.setTimingsOfFacility(Arrays.asList(basicInformation.getTimingsOfFacility()));
//        if (basicInformation.getFacilityName() != null) {
//            details.setNameInEnglish(basicInformation.getFacilityName());
//        }
//        if (basicInformation.getFacilityAddressDetails().getAddressLine1() != null) {
//            details.setAddress(basicInformation.getFacilityAddressDetails().getAddressLine1());
//        }
//        if (basicInformation.getFacilityAddressDetails().getPincode() != null) {
//            details.setPostalCode(basicInformation.getFacilityAddressDetails().getPincode());
//        }
//        if (basicInformation.getFacilityAddressDetails().getLatitude() != null) {
//            details.setLatitude(basicInformation.getFacilityAddressDetails().getLatitude());
//        }
//        if (basicInformation.getFacilityAddressDetails().getLongitude() != null) {
//            details.setLongitude(basicInformation.getFacilityAddressDetails().getLongitude());
//        }
//        if (basicInformation.getFacilityContactInformation().getFacilityEmailId() != null) {
//            details.setEmail(basicInformation.getFacilityContactInformation().getFacilityEmailId());
//        }
//        if (basicInformation.getFacilityContactInformation().getFacilityLandlineNumber() != null) {
//            details.setLandlineNumber(basicInformation.getFacilityContactInformation().getFacilityLandlineNumber());
//        }
//        if (basicInformation.getFacilityContactInformation().getFacilityContactNumber() != null) {
//            details.setMobileNumber(basicInformation.getFacilityContactInformation().getFacilityContactNumber());
//        }
//        details.setOtherDetails(gson.toJson(otherDetails));
//        details.setSpecialityType(basicInformation.getSpecialityTypeCode());
//        details.setTypeOfServices(basicInformation.getTypeOfServiceCode());
//        details.setOwnershipCode(basicInformation.getOwnershipCode());
//        details.setOwnershipSubTypeCode(basicInformation.getOwnershipSubTypeCode());
//        details.setOwnershipSubTypeCode2(basicInformation.getOwnershipSubTypeCode2());
//        details.setSystemOfMedicineCode(basicInformation.getSystemOfMedicineCode());
//        details.setFacilityTypeCode(basicInformation.getFacilityTypeCode());
//        details.setFacilitySubTypeCode(basicInformation.getFacilitySubType());
//        details.setFacilityRegion(basicInformation.getFacilityAddressDetails().getFacilityRegion());
//        healthInfrastructureDetailsDao.update(details);
//    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public void updateHealthInfraStructure(Integer id, FacilityAdditionalInformationRequest request) {
//        HealthInfrastructureDetails details = healthInfrastructureDetailsDao.retrieveById(id);
//        HealthInfraStructureOtherDetails otherDetails;
//        Gson gson = new Gson();
//        if (details.getOtherDetails() != null && !details.getOtherDetails().isEmpty()) {
//            otherDetails = gson.fromJson(details.getOtherDetails(), HealthInfraStructureOtherDetails.class);
//        } else {
//            otherDetails = new HealthInfraStructureOtherDetails();
//        }
//        otherDetails.setLinkedProgramIds(request.getLinkedProgramIds());
//        otherDetails.setGeneralInformation(request.getGeneralInformation());
//        details.setOtherDetails(gson.toJson(otherDetails));
//        details.setNin(request.getLinkedProgramIds().getNin());
//        healthInfrastructureDetailsDao.update(details);
//    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public void updateHealthInfraStructure(Integer id, FacilityDetailedInformationRequest request) {
//        HealthInfrastructureDetails details = healthInfrastructureDetailsDao.retrieveById(id);
//        HealthInfraStructureOtherDetails otherDetails;
//        Gson gson = new Gson();
//        if (details.getOtherDetails() != null && !details.getOtherDetails().isEmpty()) {
//            otherDetails = gson.fromJson(details.getOtherDetails(), HealthInfraStructureOtherDetails.class);
//        } else {
//            otherDetails = new HealthInfraStructureOtherDetails();
//        }
//        details.setNoOfBeds(request.getMedicalInfrastructure().getTotalNumberOfBeds());
//        otherDetails.setSpecialities(Arrays.asList(request.getSpecialities()));
//        otherDetails.setMedicalInfrastructure(request.getMedicalInfrastructure());
//        otherDetails.setPharmacyDetails(request.getPharmacyDetails());
//        otherDetails.setBloodBankDetails(request.getBloodBankDetails());
//        otherDetails.setImagingServices(Arrays.asList(request.getImagingServices()));
//        otherDetails.setDiagnosticServices(Arrays.asList(request.getDiagnosticServices()));
//        details.setOtherDetails(gson.toJson(otherDetails));
//        healthInfrastructureDetailsDao.update(details);
//    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public HealthInfrastructureHFRDetails getHealthInfrastructureById(Integer id) {
//        HealthInfrastructureHFRDetails healthInfrastructureHFRDetails = new HealthInfrastructureHFRDetails();
//        HealthInfrastructureDetails details = healthInfrastructureDetailsDao.retrieveById(id);
//        healthInfrastructureHFRDetails.setHealthInfrastructureDetails(details);
//
//        HealthInfrastructureLocationName locationName = healthInfrastructureDetailsDao.getLocationName(details.getLocationId());
//        healthInfrastructureHFRDetails.setLocationName(locationName.getLocationname());
//
//        HealthInfraStructureOtherDetails otherDetails;
//        Gson gson = new Gson();
//        if (details.getOtherDetails() != null && !details.getOtherDetails().isEmpty()) {
//            otherDetails = gson.fromJson(details.getOtherDetails(), HealthInfraStructureOtherDetails.class);
//        } else {
//            otherDetails = new HealthInfraStructureOtherDetails();
//        }
//
//        healthInfrastructureHFRDetails.setTimingsOfFacility(otherDetails.getTimingsOfFacility());
//        healthInfrastructureHFRDetails.setGeneralInformation(otherDetails.getGeneralInformation());
//        healthInfrastructureHFRDetails.setLinkedProgramIds(otherDetails.getLinkedProgramIds());
//        healthInfrastructureHFRDetails.setSpecialities(otherDetails.getSpecialities());
//        healthInfrastructureHFRDetails.setMedicalInfrastructure(otherDetails.getMedicalInfrastructure());
//        healthInfrastructureHFRDetails.setPharmacyDetails(otherDetails.getPharmacyDetails());
//        healthInfrastructureHFRDetails.setBloodBankDetails(otherDetails.getBloodBankDetails());
//        healthInfrastructureHFRDetails.setImagingServices(otherDetails.getImagingServices());
//        healthInfrastructureHFRDetails.setDiagnosticServices(otherDetails.getDiagnosticServices());
//        return healthInfrastructureHFRDetails;
//    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public HealthInfrastructureMatchDetail saveAndLinkHFRId(Integer id, String hfrFacilityId) {
//        HealthInfrastructureDetails facilityDetails = healthInfrastructureDetailsDao.retrieveByHFRFacilityId(hfrFacilityId);
//        if (Objects.isNull(facilityDetails)) {
//            HealthInfrastructureDetails details = healthInfrastructureDetailsDao.retrieveById(id);
//            details.setHfrFacilityId(hfrFacilityId);
//            //link facility with bridge id
//            List<AddUpdateServicesRequest> addUpdateServicesRequestList = new ArrayList<>();
//            AddUpdateServicesRequest addUpdateService = new AddUpdateServicesRequest();
//            addUpdateService.setId(hfrFacilityId);
//            if (details.getNameInEnglish() != null && !details.getNameInEnglish().isEmpty()) {
//                addUpdateService.setName(details.getNameInEnglish());
//            } else {
//                addUpdateService.setName(details.getName());
//            }
//            addUpdateService.setActive(true);
//            addUpdateService.setType("HIP");
//            addUpdateServicesRequestList.add(addUpdateService);
//            boolean isLinked = ndhmBridgeService.addUpdateBridge(addUpdateServicesRequestList);
//            details.setLinkToBridgeId(isLinked);
//            healthInfrastructureDetailsDao.update(details);
//            return null;
//        } else {
//            HealthInfrastructureLocationName locationName = healthInfrastructureDetailsDao.getLocationName(facilityDetails.getLocationId());
//            HealthInfrastructureMatchDetail detail = new HealthInfrastructureMatchDetail();
//            detail.setId(facilityDetails.getId());
//            detail.setName(facilityDetails.getName());
//            detail.setNameInEnglish(facilityDetails.getNameInEnglish());
//            detail.setAddress(facilityDetails.getAddress());
//            detail.setHfrFacilityId(facilityDetails.getHfrFacilityId());
//            detail.setType(facilityDetails.getType());
//            detail.setLocationId(facilityDetails.getLocationId());
//            detail.setLocationName(locationName.getLocationname());
//            return detail;
//        }
//    }
}
