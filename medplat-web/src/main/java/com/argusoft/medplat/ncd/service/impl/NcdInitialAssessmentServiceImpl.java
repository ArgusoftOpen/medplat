package com.argusoft.medplat.ncd.service.impl;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.ncd.dao.MemberInitialAssessmentDao;
import com.argusoft.medplat.ncd.dao.MemberReferralDao;
import com.argusoft.medplat.ncd.dao.NcdMemberDao;
import com.argusoft.medplat.ncd.dto.MemberInitialAssessmentDto;
import com.argusoft.medplat.ncd.dto.NcdHypertensionDetailDataBean;
import com.argusoft.medplat.ncd.enums.DiseaseCode;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.enums.State;
import com.argusoft.medplat.ncd.enums.SubStatus;
import com.argusoft.medplat.ncd.mapper.MemberDetailMapper;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;
import com.argusoft.medplat.ncd.model.NcdMaster;
import com.argusoft.medplat.ncd.model.NcdMemberEntity;
import com.argusoft.medplat.ncd.service.NcdInitialAssessmentService;
import com.argusoft.medplat.ncd.service.NcdService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Transactional
public class NcdInitialAssessmentServiceImpl implements NcdInitialAssessmentService {

    @Autowired
    private MemberReferralDao memberReferralDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private NcdService ncdService;
    @Autowired
    private MemberInitialAssessmentDao memberInitialAssessmentDao;
    @Autowired
    private NcdMemberDao ncdMemberDao;

    @Override
    public MemberInitialAssessmentDetail saveInitialAssessment(MemberInitialAssessmentDto memberInitialAssessmentDto) {

        MemberEntity memberEntity=memberDao.retrieveMemberById(memberInitialAssessmentDto.getMemberId());
        FamilyEntity familyEntity=familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());

        MemberInitialAssessmentDetail memberInitialAssessmentDetail = MemberDetailMapper.dtotoEntityForInitialDetail(memberInitialAssessmentDto);
        memberInitialAssessmentDetail.setLocationId(familyEntity.getAreaId() != null ? familyEntity.getAreaId() : familyEntity.getLocationId());
        memberInitialAssessmentDetail.setFamilyId(familyEntity.getId());

        //create or update master record
        if(memberInitialAssessmentDto.getDoneBy().equals(DoneBy.MO)){
            Integer masterId = ncdService.createMasterRecord(memberInitialAssessmentDto.getMemberId(),memberInitialAssessmentDto.getHealthInfraId(), DiseaseCode.IA, null, memberInitialAssessmentDto.getScreeningDate(),false);
            memberInitialAssessmentDetail.setMasterId(masterId);
        }
        if(memberInitialAssessmentDto.getDoneBy().equals(DoneBy.CONSULTANT)){
            Integer masterId = ncdService.createMasterRecord(memberInitialAssessmentDto.getMemberId(),memberInitialAssessmentDto.getHealthInfraId(), DiseaseCode.IA, null, memberInitialAssessmentDto.getScreeningDate(),false);
            memberInitialAssessmentDetail.setMasterId(masterId);
            //ncdService.updateNcdMasterSubStatus(memberInitialAssessmentDto.getMemberId(), DiseaseCode.IA.toString() , SubStatus.REFERRED_BACK, true, memberInitialAssessmentDto.getScreeningDate());
        }
        if(memberInitialAssessmentDto.getSelectedHistoryDisease() != null){
            memberInitialAssessmentDetail.setHistoryDisease(memberInitialAssessmentDto.getSelectedHistoryDisease());
        }

        Integer initialAssessmentId = memberInitialAssessmentDao.create(memberInitialAssessmentDetail);

        //create visit history
        String reading = "Height : "+ memberInitialAssessmentDetail.getHeight() + ", Weight : " + memberInitialAssessmentDetail.getWeight() + ", BMI : " + memberInitialAssessmentDetail.getBmi();
        ncdService.createVisitHistory(memberInitialAssessmentDetail.getMasterId(),memberInitialAssessmentDetail.getMemberId(),memberInitialAssessmentDetail.getScreeningDate(),memberInitialAssessmentDetail.getDoneBy(),initialAssessmentId,null,DiseaseCode.IA,null,reading);

        updateMemberAdditionalInfoFromInitialAssessment(memberEntity, memberInitialAssessmentDetail,true);
        createOrUpdateNcdMemberDetailsForWeb(memberInitialAssessmentDetail,memberEntity,familyEntity);
        ncdService.updateFamilyAdditionalInfo(familyEntity, memberInitialAssessmentDetail.getScreeningDate());
        memberDao.update(memberEntity);
        familyDao.update(familyEntity);
        return memberInitialAssessmentDao.retrieveById(initialAssessmentId);
    }

    private void createOrUpdateNcdMemberDetailsForWeb(MemberInitialAssessmentDetail memberInitialAssessmentDetail, MemberEntity memberEntity, FamilyEntity familyEntity) {
        NcdMemberEntity ncdMember = ncdMemberDao.retrieveNcdMemberByMemberId(memberEntity.getId());
        if(ncdMember == null){
            ncdMember = new NcdMemberEntity();
            ncdMember.setMemberId(memberEntity.getId());
            if (familyEntity.getAreaId() != null) {
                ncdMember.setLocationId(familyEntity.getAreaId());
            } else {
                ncdMember.setLocationId(familyEntity.getLocationId());
            }
        }
        ncdMember.setLastMoVisit(memberInitialAssessmentDetail.getScreeningDate());
        ncdMember.setLastServiceDate(memberInitialAssessmentDetail.getScreeningDate());
        if(memberInitialAssessmentDetail.getHistoryDisease()!=null){
            ncdMember.setDiseaseHistory(memberInitialAssessmentDetail.getHistoryDisease());
            if(memberInitialAssessmentDetail.getHistoryDisease().contains("OTHER")){
                ncdMember.setOtherDiseaseHistory(memberInitialAssessmentDetail.getOtherDisease());
            }
        }
        else{
            ncdMember.setDiseaseHistory(null);
            ncdMember.setOtherDiseaseHistory(null);
        }
        ncdMemberDao.createOrUpdate(ncdMember);
    }

    @Override
    public void updateMemberAdditionalInfoFromInitialAssessment(MemberEntity member, MemberInitialAssessmentDetail memberInitialAssessmentDetail, boolean isInitialAssessment) {
        MemberAdditionalInfo memberAdditionalInfo;
        if (member.getAdditionalInfo() != null && !member.getAdditionalInfo().isEmpty()) {
            memberAdditionalInfo = new Gson().fromJson(member.getAdditionalInfo(), MemberAdditionalInfo.class);
        } else {
            memberAdditionalInfo = new MemberAdditionalInfo();
        }
        if (memberInitialAssessmentDetail.getHeight() != null) {
            memberAdditionalInfo.setHeight(memberInitialAssessmentDetail.getHeight());
        }
        if (memberInitialAssessmentDetail.getWeight() != null) {
            memberAdditionalInfo.setWeight(memberInitialAssessmentDetail.getWeight());
        }
        if (Objects.nonNull(isInitialAssessment)) {
            memberAdditionalInfo.setNcdConfFor(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getNcdConfFor(), DiseaseCode.IA.toString()));
        }

        memberAdditionalInfo.setIaYear(ImtechoUtil.addCommaSeparatedStringIfNotExists(memberAdditionalInfo.getIaYear(), ImtechoUtil.getFinancialYearFromDate(memberInitialAssessmentDetail.getScreeningDate())));
        member.setAdditionalInfo(new Gson().toJson(memberAdditionalInfo));
    }

    @Override
    public MemberInitialAssessmentDetail retrieveInitialAssessmentDetailsByMemberAndDate(Integer memberId, Date screeningDate, DoneBy type) {
        return memberInitialAssessmentDao.retrieveByMemberIdAndScreeningDate(memberId,screeningDate,type);
    }

    @Override
    public MemberInitialAssessmentDetail retrieveLastRecordForInitialAssessmentByMemberId(Integer memberId) {
        MemberInitialAssessmentDetail memberInitialAssessmentDetail= memberInitialAssessmentDao.retrieveLastRecordByMemberId(memberId);
        return memberInitialAssessmentDetail;
    }
}
