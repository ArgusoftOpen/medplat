package com.argusoft.sewa.android.app.util;

import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsFIBQuestionAnswersDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;

import java.util.Comparator;

public class MyComparatorUtil {

    public static final Comparator<LmsLessonDataBean> LMS_TOPIC_MEDIA_ORDER_COMPARATOR = (o1, o2) -> o1.getMediaOrder() - o2.getMediaOrder();

    public static final Comparator<LmsTopicDataBean> LMS_TOPIC_ORDER_COMPARATOR = (o1, o2) -> Integer.parseInt(o1.getTopicOrder()) - Integer.parseInt(o2.getTopicOrder());

    public static final Comparator<LmsFIBQuestionAnswersDataBean> LMS_FIB_BLANKS_ORDER_COMPARATOR = (o1, o2) -> o1.getBlankNumber() - o2.getBlankNumber();

    public static final Comparator<LmsCourseDataBean> LMS_COURSE_COMPLETION_COMPARATOR = (o1, o2) -> {
        if (o1.getCompletionStatus() == 100 && o2.getCompletionStatus() == 100) {
            return 0;
        } else if (o1.getCompletionStatus() == 100) {
            return 1;
        } else if (o2.getCompletionStatus() == 100) {
            return -1;
        } else {
            return o2.getCompletionStatus() - o1.getCompletionStatus();
        }
    };
}
