/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

import com.argusoft.medplat.web.location.dto.LocationDetailDto;

import java.util.List;

/**
 * <p>
 * Used for rch village profile.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public class RchVillageProfileDto {

    private LocationDetailDto state;
    private LocationDetailDto district;
    private LocationDetailDto block;
    private LocationDetailDto chc;
    private LocationDetailDto phc;
    private LocationDetailDto subCentre;
    private LocationDetailDto village;
    private Integer totalPopulation;
    private Integer totalEligibleCouple;
    private Integer pregnantWomenCount;
    private Integer infantChildCount;
    private List<WorkerDetailDto> ashaDetail;
    private WorkerDetailDto anmDetail;
    private WorkerDetailDto fhwDetail;
    private WorkerDetailDto mphwDetail;
    private String nearbyHospitalDetail;
    private String fruDetail;
    private String ambulanceNumber;
    private String transportationNumber;
    private String nationalCallCentreNumber;
    private String helplineNumber;

    public LocationDetailDto getState() {
        return state;
    }

    public void setState(LocationDetailDto state) {
        this.state = state;
    }

    public LocationDetailDto getDistrict() {
        return district;
    }

    public void setDistrict(LocationDetailDto district) {
        this.district = district;
    }

    public LocationDetailDto getBlock() {
        return block;
    }

    public void setBlock(LocationDetailDto block) {
        this.block = block;
    }

    public LocationDetailDto getChc() {
        return chc;
    }

    public void setChc(LocationDetailDto chc) {
        this.chc = chc;
    }

    public LocationDetailDto getPhc() {
        return phc;
    }

    public void setPhc(LocationDetailDto phc) {
        this.phc = phc;
    }

    public LocationDetailDto getSubCentre() {
        return subCentre;
    }

    public void setSubCentre(LocationDetailDto subCentre) {
        this.subCentre = subCentre;
    }

    public LocationDetailDto getVillage() {
        return village;
    }

    public void setVillage(LocationDetailDto village) {
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

    public List<WorkerDetailDto> getAshaDetail() {
        return ashaDetail;
    }

    public void setAshaDetail(List<WorkerDetailDto> ashaDetail) {
        this.ashaDetail = ashaDetail;
    }

    public WorkerDetailDto getAnmDetail() {
        return anmDetail;
    }

    public void setAnmDetail(WorkerDetailDto anmDetail) {
        this.anmDetail = anmDetail;
    }

    public WorkerDetailDto getFhwDetail() {
        return fhwDetail;
    }

    public void setFhwDetail(WorkerDetailDto fhwDetail) {
        this.fhwDetail = fhwDetail;
    }

    public WorkerDetailDto getMphwDetail() {
        return mphwDetail;
    }

    public void setMphwDetail(WorkerDetailDto mphwDetail) {
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

}
