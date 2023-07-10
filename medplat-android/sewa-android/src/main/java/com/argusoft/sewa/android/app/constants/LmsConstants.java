package com.argusoft.sewa.android.app.constants;

import com.argusoft.sewa.android.app.databean.LmsQuestionOptionDataBean;

import java.util.List;

public class LmsConstants {

    public LmsConstants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String MEDIA_TYPE_VIDEO = "VIDEO";
    public static final String MEDIA_TYPE_PDF = "PDF";
    public static final String MEDIA_TYPE_IMAGE = "IMAGE";
    public static final String MEDIA_TYPE_AUDIO = "AUDIO";

    public static final String QUESTION_TYPE_SINGLE_SELECT = "SINGLE_SELECT";
    public static final String QUESTION_TYPE_MULTI_SELECT = "MULTI_SELECT";
    public static final String QUESTION_TYPE_FILL_IN_THE_BLANKS = "FILL_IN_THE_BLANKS";
    public static final String QUESTION_TYPE_MATCH_THE_FOLLOWING = "MATCH_THE_FOLLOWING";

    public static final String LMS_GROUP = "LMS_GROUP";
    public static final String LMS_GROUP_QUIZ = "LMS_GROUP_QUIZ";
    public static final String LMS_COURSE = "LMS_COURSE";
    public static final String COURSE_COMPLETE_ALERT = "We insist you to complete the course before proceeding to archive. Are you sure you want to continue?";
    public static final String COURSE_ARE_YOU_SURE_ALERT = "Are you sure you want to archive the course?";


    //todo remove after testing
    public static final String LMS_CASE_SCENARIO_DESC = "A G2P1L1 at 24 weeks POG presents to you for routine antenatal check up. She missed her last two antenatal visits as she had gone to her mother’s place. Presently, she complained of mild breathlessness on climbing 2-3  flights of stairs, swelling in both her feet and tiredness. On inquiring about IFA, she confessed not taking them for the last 1 and half months as the pills finished while she was out of town. On examination she had pallor and pedal edema, vitals were normal and abdominal examination corresponded to GA. On testing her Hb is 9 g/dl";

    public static boolean isImageAvailableInMatchTheFollowing(List<LmsQuestionOptionDataBean> lhsOptions, List<LmsQuestionOptionDataBean> rhsOptions) {
        for (LmsQuestionOptionDataBean lhs : lhsOptions) {
            if (lhs.getMediaId() != null) {
                return true;
            }
        }
        for (LmsQuestionOptionDataBean rhs : rhsOptions) {
            if (rhs.getMediaId() != null) {
                return true;
            }
        }
        return false;
    }
}
