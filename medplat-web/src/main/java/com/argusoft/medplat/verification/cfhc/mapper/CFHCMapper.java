
package com.argusoft.medplat.verification.cfhc.mapper;

import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.verification.cfhc.dto.CFHCUpdateDto;
import com.argusoft.medplat.verification.cfhc.dto.CFHCVerificationDto;
import com.argusoft.medplat.verification.cfhc.model.CFHCUpdate;

import java.util.List;

/**
 *<p>
 *     An util class for child malnutrition treatment center to convert dto to modal or modal to dto
 *</p>
 * @author raj
 * @since 09/09/2020 12:30
 */
public class CFHCMapper {
    private CFHCMapper() {

    }

    /**
     * Converts member modal to cfhc verification dto
     * @param member An instance of MemberEntity
     * @return An instance of CFHCVerificationDto
     */
    public static CFHCVerificationDto convertMemberEntityToCfhcVerificationDto(MemberEntity member) {
        CFHCVerificationDto cFHCVerificationDto = new CFHCVerificationDto();
        cFHCVerificationDto.setFamilyid(member.getFamilyId());
        cFHCVerificationDto.setMemberid(member.getId());
        cFHCVerificationDto.setUniqueHealthid(member.getUniqueHealthId());
        cFHCVerificationDto.setContactno(member.getMobileNumber());
        cFHCVerificationDto.setIsMobileNumberVerified(member.getIsMobileNumberVerified());
        Integer married = member.getMaritalStatus();
        String gender = member.getGender();
        if (married != null && married == 629 && gender != null && gender.equals("F")) {
            cFHCVerificationDto.setMarriedWomen(true);
        }
        cFHCVerificationDto.setRelationship(member.getRelationWithHof());
        String memberName = null;
        if (member.getFirstName() != null) {
            memberName = member.getFirstName();
        }
        if (member.getMiddleName() != null) {
            memberName += " " + member.getMiddleName();
        }
        if (member.getLastName() != null) {
            memberName += " " + member.getLastName();
        }
        cFHCVerificationDto.setMembername(memberName);
        cFHCVerificationDto.setChronicDisease(member.getChronicDisease());
        cFHCVerificationDto.setFpMethod(member.getLastMethodOfContraception());
        return cFHCVerificationDto;
    }

    /**
     * Converts cfhc dto to cfhc update modal
     * @param cFHCUpdateDto An instance of CFHCUpdateDto
     * @return An instance of CFHCUpdate
     */
    public static CFHCUpdate convertCFHCUpdateDtotoCFHCUpdate(CFHCUpdateDto cFHCUpdateDto) {
        CFHCUpdate cFHCUpdate = new CFHCUpdate();
        cFHCUpdate.setMemberid(cFHCUpdateDto.getMemberid());
        cFHCUpdate.setFamilyid(cFHCUpdateDto.getFamilyid());
        cFHCUpdate.setLocationid(cFHCUpdateDto.getLocationid());
        cFHCUpdate.setRelationshipStatus(cFHCUpdateDto.getRelationshipStatus());
        cFHCUpdate.setFpMethodStatus(cFHCUpdateDto.getFpMethodStatus());
        cFHCUpdate.setChronicDiseaseStatus(cFHCUpdateDto.getChronicDiseaseStatus());
        cFHCUpdate.setContactnoStatus(cFHCUpdateDto.getContactnoStatus());
        cFHCUpdate.setComment(cFHCUpdateDto.getComment());
        cFHCUpdate.setFamilyVerificationId(cFHCUpdateDto.getFamilyVerificationId());
        cFHCUpdate.setVerificationBody(cFHCUpdateDto.getVerificationBody());
        cFHCUpdate.setFirstNameStatus(cFHCUpdateDto.getFirstNameStatus());
        cFHCUpdate.setMiddleNameStatus(cFHCUpdateDto.getMiddleNameStatus());
        cFHCUpdate.setLastNameStatus(cFHCUpdateDto.getLastNameStatus());
        cFHCUpdate.setDobStatus(cFHCUpdateDto.getDobStatus());
        cFHCUpdate.setPregnancyStatus(cFHCUpdateDto.getPregnancyStatus());
        cFHCUpdate.setDeadStatus(cFHCUpdateDto.getDeadStatus());
        cFHCUpdate.setMigratedStatus(cFHCUpdateDto.getMigratedStatus());

        List<String> notVerified = cFHCUpdateDto.getNotVerified();
        if (notVerified.isEmpty()) {
            cFHCUpdate.setVerificationState("VERIFIED");
        } else {
            cFHCUpdate.setVerificationState("IN_REVERIFICATION");
        }

        return cFHCUpdate;
    }

}
