/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.datastructure;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.databean.FormulaTagBean;
import com.argusoft.sewa.android.app.databean.OptionTagBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

/**
 * @author alpeshkyada
 */
public class QueFormBean {

    private int loopCounter;
    private int id;
    private boolean ignoreLoop;
    private boolean ignoreNextQueLoop;
    private String title;
    private TextView titleView;
    private String subtitle;
    private TextView subTitleView;
    private String instruction;
    private TextView instructionsView;
    private String question;
    private TextView questionView;
    private String type;
    private Object questionTypeView;
    private String ismandatory;
    private String mandatorymessage;
    private int length;
    private String datamap;
    private List<OptionTagBean> options;
    private Object answer;
    private List<ValidationTagBean> validations;
    private List<FormulaTagBean> formulas;
    private String next;
    private String event;
    private String page;
    private String binding;
    private String subform;
    private String ishidden;
    private String relatedpropertyname;
    private Object questionUIFrame;
    private boolean isNextNull;
    // 
    private int extraBinding;
    private String hint;
    private String helpvideofield;

    public int getExtraBinding() {
        return extraBinding;
    }

    public void setExtraBinding(int extraBinding) {
        this.extraBinding = extraBinding;
    }

    public boolean isIsNextNull() {
        return isNextNull;
    }

    public void setIsNextNull(boolean isNextNull) {
        this.isNextNull = isNextNull;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoopCounter() {
        return loopCounter;
    }

    public void setLoopCounter(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    public boolean isIgnoreLoop() {
        return ignoreLoop;
    }

    public void setIgnoreLoop(boolean ignoreLoop) {
        this.ignoreLoop = ignoreLoop;
    }

    public boolean isIgnoreNextQueLoop() {
        return ignoreNextQueLoop;
    }

    public void setIgnoreNextQueLoop(boolean ignoreNextQueLoop) {
        this.ignoreNextQueLoop = ignoreNextQueLoop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public void setTitleView(MaterialTextView titleView) {
        this.titleView = titleView;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public TextView getSubTitleView() {
        return subTitleView;
    }

    public void setSubTitleView(TextView subTitleView) {
        this.subTitleView = subTitleView;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public TextView getInstructionsView() {
        return instructionsView;
    }

    public void setInstructionsView(TextView instructionsView) {
        this.instructionsView = instructionsView;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public TextView getQuestionView() {
        return questionView;
    }

    public void setQuestionView(TextView questionView) {
        this.questionView = questionView;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getQuestionTypeView() {
        return questionTypeView;
    }

    public void setQuestionTypeView(Object questionTypeView) {
        this.questionTypeView = questionTypeView;
    }

    public String getIsmandatory() {
        return ismandatory;
    }

    public void setIsmandatory(String ismandatory) {
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

    public List<OptionTagBean> getOptions() {
        return options;
    }

    public void setOptions(List<OptionTagBean> options) {
        this.options = options;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
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

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
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

    public String getIshidden() {
        return ishidden;
    }

    public void setIshidden(String ishidden) {
        this.ishidden = ishidden;
    }

    public String getRelatedpropertyname() {
        return relatedpropertyname;
    }

    public void setRelatedpropertyname(String relatedpropertyname) {
        this.relatedpropertyname = relatedpropertyname;
    }

    public Object getQuestionUIFrame() {
        return questionUIFrame;
    }

    public void setQuestionUIFrame(Object questionUIFrame) {
        this.questionUIFrame = questionUIFrame;
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

    public QueFormBean clone(boolean ignoreLoop, boolean ignoreNextQueLoop, Context context) {
        QueFormBean queFormBean = new QueFormBean();

        queFormBean.setLoopCounter(SharedStructureData.loopBakCounter);
        queFormBean.setAnswer(null);
        queFormBean.setBinding(this.getBinding());
        queFormBean.setDatamap(this.getDatamap());
        queFormBean.setEvent(this.getEvent());
        queFormBean.setFormulas(this.getFormulas());
        queFormBean.setId(this.getId());
        queFormBean.setInstruction(this.getInstruction());
        queFormBean.setInstructionsView(null);
        queFormBean.setIshidden(this.getIshidden());
        queFormBean.setIsmandatory(this.getIsmandatory());
        queFormBean.setLength(this.getLength());
        queFormBean.setMandatorymessage(this.getMandatorymessage());
        queFormBean.setIsNextNull(this.isNextNull);
        if (this.isIsNextNull()) {
            queFormBean.setNext(null);
        } else {
            queFormBean.setNext(this.getNext());
        }

        queFormBean.setOptions(this.getOptions());
        queFormBean.setPage(this.getPage());
        queFormBean.setQuestion(this.getQuestion());
        queFormBean.setQuestionTypeView(null);
        queFormBean.setQuestionUIFrame(null);
        queFormBean.setQuestionView(null);
        queFormBean.setRelatedpropertyname(this.getRelatedpropertyname());
        queFormBean.setSubTitleView(null);
        queFormBean.setSubform(this.getSubform());
        queFormBean.setSubtitle(this.getSubtitle());
        queFormBean.setTitle(this.getTitle());
        queFormBean.setTitleView(null);
        queFormBean.setType(this.getType());
        queFormBean.setValidations(this.getValidations());
        queFormBean.setIgnoreLoop(ignoreLoop);
        queFormBean.setIgnoreNextQueLoop(ignoreNextQueLoop);
        queFormBean.setExtraBinding(SharedStructureData.newBindingForMorbidity);
        queFormBean.setHint(this.getHint());
        queFormBean.setHelpvideofield(this.getHelpvideofield());
        // add in to map 
        SharedStructureData.mapIndexQuestion.put(DynamicUtils.getLoopId(queFormBean.getId(), queFormBean.getLoopCounter()), queFormBean);
        FormGenerator.generateQuestion(queFormBean, context);
        return queFormBean;
    }

    @NonNull
    @Override
    public String toString() {
        return "QueFormBean{" +
                "loopCounter=" + loopCounter +
                ", id=" + id +
                ", ignoreLoop=" + ignoreLoop +
                ", ignoreNextQueLoop=" + ignoreNextQueLoop +
                ", title='" + title + '\'' +
                ", titleView=" + titleView +
                ", subtitle='" + subtitle + '\'' +
                ", subTitleView=" + subTitleView +
                ", instruction='" + instruction + '\'' +
                ", instructionsView=" + instructionsView +
                ", question='" + question + '\'' +
                ", questionView=" + questionView +
                ", type='" + type + '\'' +
                ", questionTypeView=" + questionTypeView +
                ", ismandatory='" + ismandatory + '\'' +
                ", mandatorymessage='" + mandatorymessage + '\'' +
                ", length=" + length +
                ", datamap='" + datamap + '\'' +
                ", options=" + options +
                ", answer=" + answer +
                ", validations=" + validations +
                ", formulas=" + formulas +
                ", next='" + next + '\'' +
                ", event='" + event + '\'' +
                ", page='" + page + '\'' +
                ", binding='" + binding + '\'' +
                ", subform='" + subform + '\'' +
                ", ishidden='" + ishidden + '\'' +
                ", relatedpropertyname='" + relatedpropertyname + '\'' +
                ", questionUIFrame=" + questionUIFrame +
                ", isNextNull=" + isNextNull +
                ", extraBinding=" + extraBinding +
                ", hint='" + hint + '\'' +
                ", helpvideofield='" + helpvideofield + '\'' +
                '}';
    }
}
