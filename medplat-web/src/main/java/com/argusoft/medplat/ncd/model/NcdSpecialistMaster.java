package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ncd_specialist_master")
public class NcdSpecialistMaster extends EntityAuditInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "last_ecg_specialist_id")
    private Integer lastEcgSpecialistId;

    @Column(name = "last_stroke_specialist_id")
    private Integer lastStrokeSpecialistId;

    @Column(name = "last_amputation_specialist_id")
    private Integer lastAmputationSpecialistId;

    @Column(name = "last_renal_specialist_id")
    private Integer lastRenalSpecialistId;

    @Column(name = "last_cardiologist_id")
    private Integer lastCardiologistId;

    @Column(name = "last_opthamologist_id")
    private Integer lastOpthamologistId;

    @Column(name = "last_generic_screening_id")
    private Integer lastGenericScreeningId;

    @Column(name = "last_urine_screening_id")
    private Integer lastUrineScreeningId;

    @Column(name = "last_ecg_screening_id")
    private Integer lastEcgScreeningId;

    @Column(name="last_retinopathy_test_id")
    private Integer lastRetinopathyTestId;

    public static class Fields {
        public static final String MEMBER_ID = "memberId";
    }
}
