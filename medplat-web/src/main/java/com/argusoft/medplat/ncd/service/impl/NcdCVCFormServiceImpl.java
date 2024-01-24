package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.ncd.dao.NcdCVCFormDao;
import com.argusoft.medplat.ncd.dao.NcdMemberDao;
import com.argusoft.medplat.ncd.dto.GeneralDetailMedicineDto;
import com.argusoft.medplat.ncd.dto.NcdCVCFormDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.MemberDiseaseDiagnosis;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;
import com.argusoft.medplat.ncd.model.NcdCVCForm;
import com.argusoft.medplat.ncd.model.NcdMemberEntity;
import com.argusoft.medplat.ncd.service.NcdCVCFormService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NcdCVCFormServiceImpl implements NcdCVCFormService {

    @Autowired
    private NcdCVCFormDao ncdCVCFormDao;

    @Autowired
    private NcdService ncdService;
    @Autowired
    private NcdMemberDao ncdMemberDao;

    @Override
    public NcdCVCForm saveCVCForm(NcdCVCFormDto ncdCVCFormDto) {
        NcdCVCForm ncdCVCForm = MemberDetailMapper.cvcFormDtoToEntity(ncdCVCFormDto);
        if (ncdCVCFormDto.getTakeMedicine()) {
            ncdCVCForm.setTreatementStatus("CPHC");
        } else {
            ncdCVCForm.setTreatementStatus("OUTSIDE");
        }
        Integer id = ncdCVCFormDao.create(ncdCVCForm);
        ncdService.createMedicineDetails(ncdCVCFormDto.getMedicineDetail(), ncdCVCFormDto.getMemberId(), ncdCVCFormDto.getScreeningDate(),  id, MemberDiseaseDiagnosis.DiseaseCode.CVC, ncdCVCFormDto.getHealthInfraId());
        createOrUpdateNcdMemberDetailsForWeb(ncdCVCFormDto);
        return  ncdCVCFormDao.retrieveById(id);
    }

    private void createOrUpdateNcdMemberDetailsForWeb(NcdCVCFormDto ncdCVCFormDto) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(ncdCVCFormDto.getMemberId());
        if(ncdMember != null) {
            ncdMember.setLastServiceDate(ncdCVCFormDto.getScreeningDate());
            ncdMember.setLastMoVisit(ncdCVCFormDto.getScreeningDate());
            ncdMember.setCvcTreatementStatus(ncdCVCFormDto.getTreatementStatus());

            List<JSONObject> list = new ArrayList<>();
            for(int i=0; i<ncdCVCFormDto.getMedicineDetail().size(); i++){
                GeneralDetailMedicineDto generalDetailMedicineDto = ncdCVCFormDto.getMedicineDetail().get(i);
                generalDetailMedicineDto.setIssuedDate(ncdCVCFormDto.getScreeningDate());
                JSONObject medicineJson = new JSONObject();
                medicineJson.put("medicineId", generalDetailMedicineDto.getMedicineId());
                medicineJson.put("medicineName", generalDetailMedicineDto.getMedicineName());
                medicineJson.put("frequency", generalDetailMedicineDto.getFrequency());
                medicineJson.put("quantity", generalDetailMedicineDto.getQuantity());
                medicineJson.put("duration", generalDetailMedicineDto.getDuration());
                medicineJson.put("specialInstruction", generalDetailMedicineDto.getSpecialInstruction());
                medicineJson.put("expiryDate", generalDetailMedicineDto.getExpiryDate().getTime()); // Replace with your long value
                medicineJson.put("id", generalDetailMedicineDto.getId());
                medicineJson.put("issuedDate", generalDetailMedicineDto.getIssuedDate().getTime()); // Replace with your long value
                medicineJson.put("startDate", generalDetailMedicineDto.getStartDate().getTime()); // Replace with your long value
                medicineJson.put("isReturn", generalDetailMedicineDto.getReturn());
                String jsonString = medicineJson.toString();
                list.add(medicineJson);
            }

            ncdMember.setMedicineDetails(list.toString());
            ncdMemberDao.createOrUpdate(ncdMember);
        }
    }

    @Override
    public NcdCVCForm retrieveCVCDetailsByMemberAndDate(Integer memberId, Date date, DoneBy type) {
        return ncdCVCFormDao.retrieveCVCDetailsByMemberAndDate(memberId, date,type);
    }
}
