package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created by prateek on 23/6/18.
 */

public class RchVillageProfileDataBean {

    private LocationDetailDataBean state;
    private LocationDetailDataBean district;
    private LocationDetailDataBean block;
    private LocationDetailDataBean chc;
    private LocationDetailDataBean phc;
    private LocationDetailDataBean subCentre;
    private LocationDetailDataBean village;
    private Integer totalPopulation;
    private Integer totalEligibleCouple;
    private Integer pregnantWomenCount;
    private Integer infantChildCount;
    private List<WorkerDetailDataBean> ashaDetail;
    private WorkerDetailDataBean anmDetail;
    private WorkerDetailDataBean fhwDetail;
    private WorkerDetailDataBean mphwDetail;
    private String nearbyHospitalDetail;
    private String fruDetail;
    private String ambulanceNumber;
    private String transportationNumber;
    private String nationalCallCentreNumber;
    private String helplineNumber;

    public LocationDetailDataBean getState() {
        return state;
    }

    public void setState(LocationDetailDataBean state) {
        this.state = state;
    }

    public LocationDetailDataBean getDistrict() {
        return district;
    }

    public void setDistrict(LocationDetailDataBean district) {
        this.district = district;
    }

    public LocationDetailDataBean getBlock() {
        return block;
    }

    public void setBlock(LocationDetailDataBean block) {
        this.block = block;
    }

    public LocationDetailDataBean getChc() {
        return chc;
    }

    public void setChc(LocationDetailDataBean chc) {
        this.chc = chc;
    }

    public LocationDetailDataBean getPhc() {
        return phc;
    }

    public void setPhc(LocationDetailDataBean phc) {
        this.phc = phc;
    }

    public LocationDetailDataBean getSubCentre() {
        return subCentre;
    }

    public void setSubCentre(LocationDetailDataBean subCentre) {
        this.subCentre = subCentre;
    }

    public LocationDetailDataBean getVillage() {
        return village;
    }

    public void setVillage(LocationDetailDataBean village) {
        this.village = village;
    }

    public Integer getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(Integer totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public Integer getTotalEligibleCouple() {
        return totalEligibleCouple;
    }

    public void setTotalEligibleCouple(Integer totalEligibleCouple) {
        this.totalEligibleCouple = totalEligibleCouple;
    }

    public Integer getPregnantWomenCount() {
        return pregnantWomenCount;
    }

    public void setPregnantWomenCount(Integer pregnantWomenCount) {
        this.pregnantWomenCount = pregnantWomenCount;
    }

    public Integer getInfantChildCount() {
        return infantChildCount;
    }

    public void setInfantChildCount(Integer infantChildCount) {
        this.infantChildCount = infantChildCount;
    }

    public List<WorkerDetailDataBean> getAshaDetail() {
        return ashaDetail;
    }

    public void setAshaDetail(List<WorkerDetailDataBean> ashaDetail) {
        this.ashaDetail = ashaDetail;
    }

    public WorkerDetailDataBean getAnmDetail() {
        return anmDetail;
    }

    public void setAnmDetail(WorkerDetailDataBean anmDetail) {
        this.anmDetail = anmDetail;
    }

    public WorkerDetailDataBean getFhwDetail() {
        return fhwDetail;
    }

    public void setFhwDetail(WorkerDetailDataBean fhwDetail) {
        this.fhwDetail = fhwDetail;
    }

    public WorkerDetailDataBean getMphwDetail() {
        return mphwDetail;
    }

    public void setMphwDetail(WorkerDetailDataBean mphwDetail) {
        this.mphwDetail = mphwDetail;
    }

    public String getNearbyHospitalDetail() {
        return nearbyHospitalDetail;
    }

    public void setNearbyHospitalDetail(String nearbyHospitalDetail) {
        this.nearbyHospitalDetail = nearbyHospitalDetail;
    }

    public String getFruDetail() {
        return fruDetail;
    }

    public void setFruDetail(String fruDetail) {
        this.fruDetail = fruDetail;
    }

    public String getAmbulanceNumber() {
        return ambulanceNumber;
    }

    public void setAmbulanceNumber(String ambulanceNumber) {
        this.ambulanceNumber = ambulanceNumber;
    }

    public String getTransportationNumber() {
        return transportationNumber;
    }

    public void setTransportationNumber(String transportationNumber) {
        this.transportationNumber = transportationNumber;
    }

    public String getNationalCallCentreNumber() {
        return nationalCallCentreNumber;
    }

    public void setNationalCallCentreNumber(String nationalCallCentreNumber) {
        this.nationalCallCentreNumber = nationalCallCentreNumber;
    }

    public String getHelplineNumber() {
        return helplineNumber;
    }

    public void setHelplineNumber(String helplineNumber) {
        this.helplineNumber = helplineNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "RchVillageProfileDataBean{" +
                ", \nstate=" + state +
                ", \ndistrict=" + district +
                ", \nblock=" + block +
                ", \nchc=" + chc +
                ", \nphc=" + phc +
                ", \nsubCentre=" + subCentre +
                ", \nvillage=" + village +
                ", \ntotalPopulation=" + totalPopulation +
                ", \ntotalEligibleCouple=" + totalEligibleCouple +
                ", \npregnantWomenCount=" + pregnantWomenCount +
                ", \ninfantChildCount=" + infantChildCount +
                ", \nashaDetail=" + ashaDetail +
                ", \nanmDetail=" + anmDetail +
                ", \nfhwDetail=" + fhwDetail +
                ", \nmphwDetail=" + mphwDetail +
                ", \nnearbyHospitalDetail='" + nearbyHospitalDetail + '\'' +
                ", \nfruDetail='" + fruDetail + '\'' +
                ", \nambulanceNumber='" + ambulanceNumber + '\'' +
                ", \ntransportationNumber='" + transportationNumber + '\'' +
                ", \nnationalCallCentreNumber='" + nationalCallCentreNumber + '\'' +
                ", \nhelplineNumber='" + helplineNumber + '\'' +
                '}';
    }
}
