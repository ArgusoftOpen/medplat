/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import java.util.List;

/**
 * @author kelvin
 */
public class ComponentTagBean {

    private int id;
    private String title;
    private String subtitle;
    private String instruction;
    private String question;
    private String type;
    private String ismandatory;
    private String mandatorymessage;
    private int length;
    private String datamap;
    private List<OptionTagBean> options;
    private List<ValidationTagBean> validations;
    private List<FormulaTagBean> formulas;
    private String next;
    private String event;
    private String page;
    private String binding;
    private String subform;
    private String ishidden;
    private String relatedpropertyname;
    private String hint;
    private String helpvideofield;

    public String getRelatedpropertyname() {
        return relatedpropertyname;
    }

    public void setRelatedpropertyname(String relatedpropertyname) {
        this.relatedpropertyname = relatedpropertyname;
    }

    public String getIshidden() {
        return ishidden;
    }

    public void setIshidden(String ishidden) {
        this.ishidden = ishidden;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatamap() {
        return datamap;
    }

    public void setDatamap(String datamap) {
        this.datamap = datamap;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getIsmandatory() {
        return ismandatory;
    }

    public void setIsmandatory(String ismandatory) {
        this.ismandatory = ismandatory;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getMandatorymessage() {
        return mandatorymessage;
    }

    public void setMandatorymessage(String mandatorymessage) {
        this.mandatorymessage = mandatorymessage;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<OptionTagBean> getOptions() {
        return options;
    }

    public void setOptions(List<OptionTagBean> options) {
        this.options = options;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ValidationTagBean> getValidations() {
        return validations;
    }

    public void setValidations(List<ValidationTagBean> validations) {
        this.validations = validations;
    }

    public List<FormulaTagBean> getFormulas() {
        return formulas;
    }

    public void setFormulas(List<FormulaTagBean> formulas) {
        this.formulas = formulas;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getSubform() {
        return subform;
    }

    public void setSubform(String subform) {
        this.subform = subform;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHelpvideofield() {
        return helpvideofield;
    }

    public void setHelpvideofield(String helpvideofield) {
        this.helpvideofield = helpvideofield;
    }
}
