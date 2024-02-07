package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.model.MbbsMOReviewDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MOReviewDto {

    private Integer id;
    private Integer memberId;
    private Integer locationId;
    private Integer healthInfraId;
    private Date screeningDate;
    private Date followUpDate;
    private String refferralPlace;
    private List<String> followUpDisease;
    private Boolean doesRequiredRef;
    private String  refferralReason;
    private String followupPlace;
    private List<GeneralDetailMedicineDto> medicineDetail;
    private String comment;
    private String commentBy;
    private Boolean isFollowup;
    private String disease;
    private String otherReason;
    private List<GeneralDetailMedicineDto> editedMedicineDetail;
    private List<GeneralDetailMedicineDto> deletedMedicineDetail;
}
