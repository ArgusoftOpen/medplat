package com.argusoft.medplat.course.mapper;

import com.argusoft.medplat.course.model.QuestionBankConfiguration;
import com.argusoft.medplat.course.model.QuestionSetConfiguration;
import com.argusoft.medplat.mobile.dto.QuestionBankBean;
import com.argusoft.medplat.mobile.dto.QuestionSetBean;

import java.util.ArrayList;
import java.util.List;

public class QuestionSetMapper {

    public static QuestionSetBean questionSetEntityToDataBean(QuestionSetConfiguration questionSet, List<QuestionBankConfiguration> questionBanks) {
        QuestionSetBean questionSetBean = new QuestionSetBean();
        questionSetBean.setActualId(questionSet.getId());
        questionSetBean.setRefId(questionSet.getRefId());
        questionSetBean.setRefType(questionSet.getRefType());
        questionSetBean.setQuestionSetName(questionSet.getQuestionSetName());
        questionSetBean.setMinimumMarks(questionSet.getMinimumMarks());
        questionSetBean.setStatus(questionSet.getStatus());
        questionSetBean.setCourseId(questionSet.getCourseId());
        questionSetBean.setQuestionSetType(questionSet.getQuestionSetType());
        questionSetBean.setQuizAtSecond(questionSet.getQuizAtSecond());
        questionSetBean.setQuestionBank(questionBankEntityListToDataBeanList(questionBanks));
        return questionSetBean;
    }

    public static QuestionBankBean questionBankEntityToDataBean(QuestionBankConfiguration questionBank) {
        QuestionBankBean questionBankBean = new QuestionBankBean();
        questionBankBean.setActualId(questionBank.getId());
        questionBankBean.setQuestionSetId(questionBank.getQuestionSetId());
        questionBankBean.setConfigJson(questionBank.getConfigJson());
        return questionBankBean;
    }

    public static List<QuestionBankBean> questionBankEntityListToDataBeanList(List<QuestionBankConfiguration> questionBanks) {
        List<QuestionBankBean> questionBankBeans = new ArrayList<>();
        for (QuestionBankConfiguration questionBankConfiguration : questionBanks) {
            questionBankBeans.add(questionBankEntityToDataBean(questionBankConfiguration));
        }
        return questionBankBeans;
    }
}
