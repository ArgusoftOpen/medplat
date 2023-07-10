/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dto;

/**
 *
 * <p>
 *     Used for hierarchy type master.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class HierarchyTypeMasterDto {
    private Integer id;
    String name;
    String code;

    public HierarchyTypeMasterDto(){
    }
    
    public HierarchyTypeMasterDto(Integer id,String name,String code){
        this.name = name;
        this.id = id;
        this.code = code;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
}
