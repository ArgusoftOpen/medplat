package com.argusoft.medplat.systemconstraint.dto;

import java.util.List;

public class SystemConstraintMobileTemplatePage {

    private List<SystemConstraintMobileTemplateField> questions;
    private Integer pageNumber;

    public List<SystemConstraintMobileTemplateField> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SystemConstraintMobileTemplateField> questions) {
        this.questions = questions;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

}
