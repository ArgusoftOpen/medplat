
package com.argusoft.medplat.verification.cfhc.service;

import com.argusoft.medplat.verification.cfhc.dto.CFHCUpdateDto;
import com.argusoft.medplat.verification.cfhc.dto.CFHCVerificationDto;
import com.argusoft.medplat.verification.cfhc.dto.CFHCVerificationStateDto;
import java.util.List;

/**
 * <p>
 *     Defines methods for child malnutrition treatment center
 * </p>
 * @author raj
 * @since 09/09/2020 12:30
 */
public interface CFHCVerificationService {
    /**
     * Returns a list of CFHCVerificationDto based on given state in CFHCVerificationStateDto
     * @param cFHCVerificationStateDto An instance of CFHCVerificationStateDto
     * @param limit A limit value
     * @param offset A offset value
     * @return A list of CFHCVerificationDto
     */
//    List<CFHCVerificationDto> getVerificationsFamiliesByStates(CFHCVerificationStateDto cFHCVerificationStateDto, Integer limit, Integer offset);

    /**
     * Returns a list of family members of given family id in CFHCVerificationDto
     * @param cFHCVerificationDto An instance of CFHCVerificationDto
     * @return A list of CFHCVerificationDto
     */
    List<CFHCVerificationDto> getFamilyMembers(CFHCVerificationDto cFHCVerificationDto);

    /**
     * Updates verification member based on parameter given in CFHCUpdateDto
     * @param cFHCUpdateDto A list of CFHCUpdateDto
     */
//    void updateVerificationsMembers(List<CFHCUpdateDto> cFHCUpdateDto);

    /**
     * Returns a list of dead member
     * @param cFHCVerificationStateDto An instance of CFHCVerificationStateDto
     * @return A list of CFHCVerificationDto
     */
    List<CFHCVerificationDto> getDeadMembers(CFHCVerificationStateDto cFHCVerificationStateDto);
    
}
