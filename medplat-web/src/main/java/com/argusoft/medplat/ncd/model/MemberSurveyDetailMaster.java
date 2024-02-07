package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "member_survey_detail_master")
@Data
public class MemberSurveyDetailMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "opd_id")
    private String opdId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "eligible_for_anemia")
    private Boolean eligibleForAnemia;

    @Column(name = "caste")
    private String caste;

    @Column(name = "religion")
    private String religion;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "is_pregnant")
    private Boolean isPregnantFlag;

    @Column(name = "lmp")
    @Temporal(TemporalType.DATE)
    private Date lmpDate;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "created_from")
    private String createdFrom;

    @Column(name = "mobile_start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileStartDate;

    @Column(name = "mobile_end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileEndDate;
}
