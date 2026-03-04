/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

import java.util.List;

/**
 *
 * @author satyam
 */
public class FormTagDto {
    
    private String name;
    private List<ComponentTagDto> components;

    public List<ComponentTagDto> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentTagDto> components) {
        this.components = components;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
