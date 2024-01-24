/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Setter
@Table(name = "ncd_member_ecg_detail")
public class MemberEcgDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "family_id")
    private Integer familyId;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "service_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "symptom")
    private String symptom;

    @Column(name = "other_symptom")
    private String otherSymptom;

    @Column(name = "detection")
    private String detection;

    @Column(name = "ecg_type")
    private String ecgType;

    @Column(name = "recommendation")
    private String recommendation;

    @Column(name = "risk")
    private String risk;

    @Column(name = "anomalies")
    private String anomalies;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "pr")
    private Integer pr;

    @Column(name = "qrs")
    private Integer qrs;

    @Column(name = "qt")
    private Integer qt;

    @Column(name = "qtc", columnDefinition = "numeric", precision = 6, scale = 2)
    private Double qtc;

    @Column(name = "graph_detail_id")
    private Integer graphDetailId;

    @Column(name = "report_pdf_doc_id")
    private Long reportPdfDocId;

    @Column(name = "report_image_doc_id")
    private Long reportImageDocId;

    @Column(name = "report_pdf_doc_uuid")
    private String reportPdfDocUuid;

    @Column(name = "report_image_doc_uuid")
    private String reportImageDocUuid;

    public static class Fields {
        private Fields(){}
        public static final String REPORT_PDF_UUID = "reportPdfDocUuid";
        public static final String REPORT_IMAGE_UUID = "reportImageDocUuid";
    }
}
