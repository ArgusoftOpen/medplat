
package com.argusoft.medplat.verification.cfhc.service.impl;

import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.verification.cfhc.dao.CFHCDao;
import com.argusoft.medplat.verification.cfhc.dto.CFHCUpdateDto;
import com.argusoft.medplat.verification.cfhc.dto.CFHCVerificationDto;
import com.argusoft.medplat.verification.cfhc.dto.CFHCVerificationStateDto;
import com.argusoft.medplat.verification.cfhc.mapper.CFHCMapper;
import com.argusoft.medplat.verification.cfhc.model.CFHCUpdate;
import com.argusoft.medplat.verification.cfhc.service.CFHCVerificationService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants.*;

/**
 * <p>
 *     Implements methods of CFHCVerificationService
 * </p>
 * @author raj
 * @since 09/09/2020 12:30
 */
@Service
@Transactional
public class CFHCVerificationServiceImpl implements CFHCVerificationService {

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CFHCDao cFHCDao;

//    @Autowired
//    private CFHCVerificationDto cfhcVerificationDto;

    /**
     * {@inheritDoc}
     */
//    @Override
//    public List<CFHCVerificationDto> getVerificationsFamiliesByStates(CFHCVerificationStateDto cFHCVerificationStateDto, Integer limit, Integer offset) {
//        List<CFHCVerificationDto> cFHCVerificationDtos = null;
//        String fullLocation = cFHCDao.getLocationHierarchy(cFHCVerificationStateDto.getLocationIds().get(0));
////        cFHCVerificationDtos = familyDao.getCFHCFamiliesByStates(cFHCVerificationStateDto.getLocationIds(), limit, offset);
////        cFHCVerificationDtos.forEach(cFHCVerificationDto -> cFHCVerificationDto.setFulllocation(fullLocation));
//        return cFHCVerificationDtos;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CFHCVerificationDto> getFamilyMembers(CFHCVerificationDto cFHCVerificationDto) {
        List<MemberEntity> members = memberDao.retriveMemberByFamilyIdAndStates(cFHCVerificationDto.getFamilyid(), cFHCVerificationDto.getStates());
        List<CFHCVerificationDto> cHFCVerificationDtos = new LinkedList<>();

        members.forEach(member -> {
            String state = member.getState();
            if (!state.equals("com.argusoft.imtecho.member.state.dead.fhw.reverified")
                    && !state.equals("com.argusoft.imtecho.member.state.dead.fhsr.verified")) {
                        CFHCVerificationDto tempDto = CFHCMapper.convertMemberEntityToCfhcVerificationDto(member);
                        tempDto.setLocationid(cFHCVerificationDto.getLocationid());
                        setChronicDisease(tempDto, member);
                        cHFCVerificationDtos.add(tempDto);
                    }
        });
        return cHFCVerificationDtos;
    }

    /**
     * Set chronic disease to cfhc verification dto
     * @param tempDto An instance of CFHCVerificationDto
     * @param member An instance of MemberEntity
     */
    private void setChronicDisease(CFHCVerificationDto tempDto, MemberEntity member){
        if (tempDto.getChronicDisease() != null) {
            String[] split = member.getChronicDisease().split(",");
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (String string : split) {
                String value = cFHCDao.getValueFromListValue(Integer.parseInt(string));
                if (value != null) {
                    if (count != 0) {
                        sb.append(",");
                    }
                    sb.append(value);
                    count++;
                }
            }
            String chronicDisease = sb.toString();
            tempDto.setChronicDisease(chronicDisease);
        }

    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public void updateVerificationsMembers(List<CFHCUpdateDto> cFHCUpdateDtos) {
//        CFHCVerificationDto cFHCVerificationDto = new CFHCVerificationDto();
//        cFHCVerificationDto.setFamilyVerified(true);
//        cFHCVerificationDto.setFamilyid(cFHCUpdateDtos.get(0).getFamilyid());
//        VerificationListDto verificationListDto = verificationListDao.retriveByFamilyId(cFHCUpdateDtos.get(0).getFamilyid());
//        cFHCUpdateDtos.forEach(cFHCUpdateDto -> {
//            CFHCUpdate cFHCUpdate = CFHCMapper.convertCFHCUpdateDtotoCFHCUpdate(cFHCUpdateDto);
////            cFHCUpdate.setFamilyVerificationId(verificationListDto.getId());
//            cFHCUpdate.setVerificationBody(verificationListDto.getVerificationBody());
//            cFHCDao.updateMember(cFHCUpdate);
//            if (cFHCUpdate.getDeadStatus() == null || cFHCUpdate.getDeadStatus().equals("false")) {
//                MemberEntity member = memberDao.retrieveById(cFHCUpdate.getMemberid());
//                if (cFHCUpdate.getVerificationState().equals("IN_REVERIFICATION")) {
//                    member.setState(CFHC_MEMBER_STATE_IN_REVERIFICATION);
//                    cFHCVerificationDto.setFamilyVerified(false);
//                } else {
//                    member.setState(CFHC_MEMBER_STATE_MO_VERIFIED);
//                }
//                memberDao.update(member);
//            } else {
//                MemberEntity member = memberDao.retrieveById(cFHCUpdate.getMemberid());
//                if (cFHCUpdate.getVerificationState().equals("IN_REVERIFICATION")) {
//                    member.setState(CFHC_MEMBER_STATE_IN_REVERIFICATION);
//                    cFHCVerificationDto.setFamilyVerified(false);
//                } else {
//                    member.setState(CFHC_MEMBER_STATE_DEAD);
//                }
//                memberDao.update(member);
//            }
//        });
//
//        FamilyEntity family = familyDao.retrieveFamilyByFamilyId(cFHCVerificationDto.getFamilyid());
//        if (Boolean.TRUE.equals(cFHCVerificationDto.getFamilyVerified())) {
//            family.setState(CFHC_FAMILY_STATE_MO_VERIFIED);
//        } else {
//            family.setState(CFHC_FAMILY_STATE_IN_REVERIFICATION);
//        }
//        familyDao.update(family);
//        verificationListDao.updateFamilyVerificationState(family.getFamilyId(), family.getState());
//
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CFHCVerificationDto> getDeadMembers(CFHCVerificationStateDto cFHCVerificationStateDto) {
        List<MemberEntity> members = memberDao.retriveMemberByFamilyIdAndStates(cFHCVerificationStateDto.getFamilyid(), cFHCVerificationStateDto.getStates());
        List<CFHCVerificationDto> cHFCVerificationDtos = new LinkedList<>();
        members.forEach(member -> {
            CFHCVerificationDto cFHCVerificationDto = CFHCMapper.convertMemberEntityToCfhcVerificationDto(member);
            cFHCVerificationDto.setLocationid((cFHCVerificationStateDto.getLocationIds()).get(0));
            cHFCVerificationDtos.add(cFHCVerificationDto);

        });
        return cHFCVerificationDtos;
    }
}
