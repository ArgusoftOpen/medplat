/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author kelvin
 */
@DatabaseTable
public class QuestionBean extends BaseEntity {

    @DatabaseField
    private int queId;
    @DatabaseField
    private String subtitle;
    @DatabaseField
    private String instruction;
    @DatabaseField
    private String question;
    @DatabaseField
    private String type;
    @DatabaseField
    private Boolean ismandatory;
    @DatabaseField
    private String mandatorymessage;
    @DatabaseField
    private int length;
    @DatabaseField
    private String datamap;
    @DatabaseField
    private int next;
    @DatabaseField
    private String event;
    @DatabaseField
    private int page;
    @DatabaseField
    private String subform;
    @DatabaseField
    private String binding;
    @DatabaseField
    private String title;
    @DatabaseField
    private Boolean ishidden;
    @DatabaseField
    private String relatedpropertyname;
    @DatabaseField
    private String validationmethod;
    @DatabaseField
    private String validationmessage;
    @DatabaseField
    private String formulass;
    @DatabaseField
    private String entity;
    @DatabaseField
    private String hint;
    @DatabaseField
    private String helpvideofield;

    public int getQueId() {
        return queId;
    }

    public void setQueId(int queId) {
        this.queId = queId;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsmandatory() {
        return ismandatory;
    }

    public void setIsmandatory(Boolean ismandatory) {
        this.ismandatory = ismandatory;
    }

    public String getMandatorymessage() {
        return mandatorymessage;
    }

    public void setMandatorymessage(String mandatorymessage) {
        this.mandatorymessage = mandatorymessage;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDatamap() {
        return datamap;
    }

    public void setDatamap(String datamap) {
        this.datamap = datamap;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSubform() {
        return subform;
    }

    public void setSubform(String subform) {
        this.subform = subform;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIshidden() {
        return ishidden;
    }

    public void setIshidden(Boolean ishidden) {
        this.ishidden = ishidden;
    }

    public String getRelatedpropertyname() {
        return relatedpropertyname;
    }

    public void setRelatedpropertyname(String relatedpropertyname) {
        this.relatedpropertyname = relatedpropertyname;
    }

    public String getValidationmethod() {
        return validationmethod;
    }

    public void setValidationmethod(String validationmethod) {
        this.validationmethod = validationmethod;
    }

    public String getValidationmessage() {
        return validationmessage;
    }

    public void setValidationmessage(String validationmessage) {
        this.validationmessage = validationmessage;
    }

    public String getFormulass() {
        return formulass;
    }

    public void setFormulass(String formulass) {
        this.formulass = formulass;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @NonNull
    @Override
    public String toString() {
        return "QuestionBean{" + "queId=" + queId + ", subtitle=" + subtitle + ", instruction=" + instruction + ", question=" + question + ", type=" + type + ", ismandatory=" + ismandatory + ", mandatorymessage=" + mandatorymessage + ", length=" + length + ", datamap=" + datamap + ", next=" + next + ", event=" + event + ", page=" + page + ", subform=" + subform + ", binding=" + binding + ", title=" + title + ", ishidden=" + ishidden + ", relatedpropertyname=" + relatedpropertyname + ", validationmethod=" + validationmethod + ", validationmessage=" + validationmessage + ", formulass=" + formulass + ", entity=" + entity + '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
