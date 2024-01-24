
package com.argusoft.medplat.verification.cfhc.controller;

import com.argusoft.medplat.verification.cfhc.dto.CFHCUpdateDto;
import com.argusoft.medplat.verification.cfhc.dto.CFHCVerificationDto;
import com.argusoft.medplat.verification.cfhc.dto.CFHCVerificationStateDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.argusoft.medplat.verification.cfhc.service.CFHCVerificationService;

/**
 * <p>
 *     Defines rest end points for child malnutrition treatment center
 * </p>
 * @author raj
 * @since 09/09/2020 12:30
 */
@RestController
@RequestMapping("/api/cfhcVerification")
public class CFHCVerificationController {

    @Autowired
    CFHCVerificationService cFHCVerificationService;

    /**
     * Returns a list of CFHCVerificationDto based on given state in CFHCVerificationStateDto
     * @param cFHCVerificationStateDto An instance of CFHCVerificationStateDto
     * @param limit A limit value
     * @param offset A offset value
     * @return A list of CFHCVerificationDto
     */
//    @PostMapping(path = "/family/{limit}/{offset}")
//    public List<CFHCVerificationDto> getVerificationsFamiliesByStates(@RequestBody CFHCVerificationStateDto cFHCVerificationStateDto,
//            @PathVariable("limit") Integer limit,
//            @PathVariable("offset") Integer offset) {
//        return cFHCVerificationService.getVerificationsFamiliesByStates(cFHCVerificationStateDto, limit, offset);
//    }

    /**
     * Returns a list of family members of given family id in CFHCVerificationDto
     * @param cFHCVerificationDto An instance of CFHCVerificationDto
     * @return A list of CFHCVerificationDto
     */
    @PostMapping(path = "/familymembers")
    public List<CFHCVerificationDto> getFamilyMembers(@RequestBody CFHCVerificationDto cFHCVerificationDto) {
        return cFHCVerificationService.getFamilyMembers(cFHCVerificationDto);
    }

    /**
     * Updates verification member based on parameter given in CFHCUpdateDto
     * @param cFHCUpdateDto A list of CFHCUpdateDto
     */
//    @PutMapping(path = "/family")
//    public void updateVerificationsMembers(@RequestBody List<CFHCUpdateDto> cFHCUpdateDto) {
//        cFHCVerificationService.updateVerificationsMembers(cFHCUpdateDto);
//    }

    /**
     * Returns a list of dead member
     * @param cFHCVerificationStateDto An instance of CFHCVerificationStateDto
     * @return A list of CFHCVerificationDto
     */
    @PostMapping(path = "/deadmembers")
    public List<CFHCVerificationDto> getDeadMembers(@RequestBody CFHCVerificationStateDto cFHCVerificationStateDto) {
        return cFHCVerificationService.getDeadMembers(cFHCVerificationStateDto);
    }

}
