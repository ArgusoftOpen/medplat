package com.argusoft.medplat.ncd.dto;

import java.util.Date;

public class MemberEcgDto {
    private Integer memberId;
    private Integer locationId;
    private Long serviceDate;
    private String detection;
    private String ecgType;
    private String recommendation;
    private String risk;
    private String anomalies;
    private Integer heartRate;
    private Integer pr;
    private Integer qrs;
    private Integer qt;
    private Double qtc;
    private String selectedSymptoms;
    private String otherSymptoms;
    private EcgGraphDataBean ecgGraphData;
    private String reportPdfDocId;
    private String reportImageDocId;
    private String reportPdfDocUuid;
    private String reportImageDocUuid;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Long getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Long serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getDetection() {
        return detection;
    }

    public void setDetection(String detection) {
        this.detection = detection;
    }

    public String getEcgType() {
        return ecgType;
    }

    public void setEcgType(String ecgType) {
        this.ecgType = ecgType;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getAnomalies() {
        return anomalies;
    }

    public void setAnomalies(String anomalies) {
        this.anomalies = anomalies;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getPr() {
        return pr;
    }

    public void setPr(Integer pr) {
        this.pr = pr;
    }

    public Integer getQrs() {
        return qrs;
    }

    public void setQrs(Integer qrs) {
        this.qrs = qrs;
    }

    public Integer getQt() {
        return qt;
    }

    public void setQt(Integer qt) {
        this.qt = qt;
    }

    public Double getQtc() {
        return qtc;
    }

    public void setQtc(Double qtc) {
        this.qtc = qtc;
    }

    public String getSelectedSymptoms() {
        return selectedSymptoms;
    }

    public void setSelectedSymptoms(String selectedSymptoms) {
        this.selectedSymptoms = selectedSymptoms;
    }

    public String getOtherSymptoms() {
        return otherSymptoms;
    }

    public void setOtherSymptoms(String otherSymptoms) {
        this.otherSymptoms = otherSymptoms;
    }

    public EcgGraphDataBean getEcgGraphData() {
        return ecgGraphData;
    }

    public void setEcgGraphData(EcgGraphDataBean ecgGraphData) {
        this.ecgGraphData = ecgGraphData;
    }

    public String getReportPdfDocId() {
        return reportPdfDocId;
    }

    public void setReportPdfDocId(String reportPdfDocId) {
        this.reportPdfDocId = reportPdfDocId;
    }

    public String getReportImageDocId() {
        return reportImageDocId;
    }

    public void setReportImageDocId(String reportImageDocId) {
        this.reportImageDocId = reportImageDocId;
    }

    public String getReportPdfDocUuid() {
        return reportPdfDocUuid;
    }

    public void setReportPdfDocUuid(String reportPdfDocUuid) {
        this.reportPdfDocUuid = reportPdfDocUuid;
    }

    public String getReportImageDocUuid() {
        return reportImageDocUuid;
    }

    public void setReportImageDocUuid(String reportImageDocUuid) {
        this.reportImageDocUuid = reportImageDocUuid;
    }
}
