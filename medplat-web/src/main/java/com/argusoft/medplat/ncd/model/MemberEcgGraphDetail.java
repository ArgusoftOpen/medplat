/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "ncd_ecg_graph_detail")
public class MemberEcgGraphDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "avf_data")
    private String avfData;

    @Column(name = "avl_data")
    private String avlData;

    @Column(name = "avr_data")
    private String avrData;

    @Column(name = "lead1_data")
    private String lead1Data;

    @Column(name = "lead2_Data")
    private String lead2Data;

    @Column(name = "lead3_data")
    private String lead3Data;

    @Column(name = "v1_data")
    private String v1Data;

    @Column(name = "v2_data")
    private String v2Data;

    @Column(name = "v3_data")
    private String v3Data;

    @Column(name = "v4_data")
    private String v4Data;

    @Column(name = "v5_data")
    private String v5Data;

    @Column(name = "v6_data")
    private String v6Data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String arrayToString(List<Double> doubleList){
        return StringUtils.join(doubleList,",");
    }

    private List<Double> StringToArray(String listString){
        List<Double> listVals = new ArrayList<Double>();
        for(String field : listString.split(",")) {
            try {
                listVals.add(Double.parseDouble(field));
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return listVals;
    }

    public List<Double> getAvfData() {
        return StringToArray(avfData);
    }

    public void setAvfData(List<Double> avfData) {
        this.avfData = arrayToString(avfData);
    }

    public List<Double> getAvlData() {
        return StringToArray(avlData);
    }

    public void setAvlData(List<Double> avlData) {
        this.avlData = arrayToString(avlData);
    }

    public List<Double> getAvrData() {
        return StringToArray(avrData);
    }

    public void setAvrData(List<Double> avrData) {
        this.avrData = arrayToString(avrData);
    }

    public List<Double> getLead1Data() {
        return StringToArray(lead1Data);
    }

    public void setLead1Data(List<Double> lead1Data) {
        this.lead1Data = arrayToString(lead1Data);
    }

    public List<Double> getLead2Data() {
        return StringToArray(lead2Data);
    }

    public void setLead2Data(List<Double> lead2Data) {
        this.lead2Data = arrayToString(lead2Data);
    }

    public List<Double> getLead3Data() {
        return StringToArray(lead3Data);
    }

    public void setLead3Data(List<Double> lead3Data) {
        this.lead3Data = arrayToString(lead3Data);
    }

    public List<Double> getV1Data() {
        return StringToArray(v1Data);
    }

    public void setV1Data(List<Double> v1Data) {
        this.v1Data = arrayToString(v1Data);
    }

    public List<Double> getV2Data() {
        return StringToArray(v2Data);
    }

    public void setV2Data(List<Double> v2Data) {
        this.v2Data = arrayToString(v2Data);
    }

    public List<Double> getV3Data() {
        return StringToArray(v3Data);
    }

    public void setV3Data(List<Double> v3Data) {
        this.v3Data = arrayToString(v3Data);
    }

    public List<Double> getV4Data() {
        return StringToArray(v4Data);
    }

    public void setV4Data(List<Double> v4Data) {
        this.v4Data = arrayToString(v4Data);
    }

    public List<Double> getV5Data() {
        return StringToArray(v5Data);
    }

    public void setV5Data(List<Double> v5Data) {
        this.v5Data = arrayToString(v5Data);
    }

    public List<Double> getV6Data() {
        return StringToArray(v6Data);
    }

    public void setV6Data(List<Double> v6Data) {
        this.v6Data = arrayToString(v6Data);
    }
}
