package com.argusoft.sewa.android.app.lms;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.activity.LmsInteractiveQuestionModuleActivity_;
import com.argusoft.sewa.android.app.activity.LmsQuestionModuleActivity_;
import com.argusoft.sewa.android.app.activity.LmsSimulationActivity_;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsExpandableListDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LmsContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LmsContentFragment extends Fragment {

    private LmsCourseDetailActivity activity;
    private LinearLayout bodyLayoutContainer;

    private LmsCourseDataBean selectedCourse;

    private ExpandableListView expandableListView;
    private LmsExpandableListAdaptor expandableListAdaptor;

    private int lastExpandedGroupPosition = -1;
    private int selectedLessonChildIndex = -1;
    private int selectedLessonGroupIndex = -1;

    public LmsContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LmsContentFragment.
     */
    public static LmsContentFragment newInstance() {
        return new LmsContentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainLayout = inflater.inflate(R.layout.fragment_lms_content, container, false);
        bodyLayoutContainer = mainLayout.findViewById(R.id.bodyLayoutContainer);
        activity = (LmsCourseDetailActivity) getActivity();
        initView();
        return mainLayout;
    }

    public LmsExpandableListAdaptor getExpandableListAdaptor() {
        return expandableListAdaptor;
    }

    public void initView() {
        if (selectedCourse == null) {
            selectedCourse = activity.getSelectedCourse();
        }
        if (selectedCourse == null) {
            return;
        }

        List<LmsExpandableListDataBean> listDataBeans = new ArrayList<>();
        LmsExpandableListDataBean lmsExpandableListDataBean;
        for (LmsTopicDataBean topicDataBean : selectedCourse.getTopics()) {
            lmsExpandableListDataBean = new LmsExpandableListDataBean();
            lmsExpandableListDataBean.setGroupType(LmsConstants.LMS_GROUP);
            lmsExpandableListDataBean.setModule(topicDataBean);
            listDataBeans.add(lmsExpandableListDataBean);
            if (topicDataBean.getQuestionSet() != null && !topicDataBean.getQuestionSet().isEmpty()) {
                lmsExpandableListDataBean = new LmsExpandableListDataBean();
                lmsExpandableListDataBean.setGroupType(LmsConstants.LMS_GROUP_QUIZ);
                lmsExpandableListDataBean.setModule(topicDataBean);
                listDataBeans.add(lmsExpandableListDataBean);
            }
        }
        if (selectedCourse.getQuestionSet() != null) {
            for (LmsQuestionSetDataBean lmsQuestionSetDataBean : selectedCourse.getQuestionSet()) {
                lmsExpandableListDataBean = new LmsExpandableListDataBean();
                lmsExpandableListDataBean.setGroupType(LmsConstants.LMS_COURSE);
                lmsExpandableListDataBean.setQuestionSet(lmsQuestionSetDataBean);
                listDataBeans.add(lmsExpandableListDataBean);
            }
        }

        selectedCourse.setListDataBeans(listDataBeans);

        expandableListView = bodyLayoutContainer.findViewById(R.id.expandable_list_view);
        expandableListAdaptor = new LmsExpandableListAdaptor(
                getContext(),
                selectedCourse,
                activity.lmsService.getViewedLessonList(),
                activity.fhsService,
                activity.lmsService,
                listDataBeans);
        expandableListView.setAdapter(expandableListAdaptor);
        expandableListView.setOnChildClickListener(getChildClickListener());
        expandableListView.setOnGroupExpandListener(getOnExpandGroupListener());
        expandableListView.setOnGroupCollapseListener(getOnCollapseGroupListener());
    }

    private void openLmsTestModule(LmsQuestionSetDataBean questionSet, String testFor) {
        boolean allLessonsCompleted = true;

        for (LmsTopicDataBean topicDataBean : selectedCourse.getTopics()) {
            for (LmsLessonDataBean mediaDataBean : topicDataBean.getTopicMedias()) {
                if (!activity.lmsService.getViewedLessonList().contains(mediaDataBean.getActualId())) {
                    allLessonsCompleted = false;
                    break;
                }
            }
        }

        if (allLessonsCompleted) {
            Intent intent;
            if (selectedCourse.getTestConfigJson() != null
                    && Boolean.TRUE.equals(selectedCourse.getTestConfigJson().get(questionSet.getQuestionSetType()).getProvideInteractiveFeedbackAfterEachQuestion())) {
                intent = new Intent(getContext(), LmsInteractiveQuestionModuleActivity_.class);
            } else if (selectedCourse != null && selectedCourse.getTestConfigJson() != null
                    && questionSet.getQuestionSetType() == 2640) { //todo make id value dynamic as per field value name
                intent = new Intent(getContext(), LmsSimulationActivity_.class);
            } else {
                intent = new Intent(getContext(), LmsQuestionModuleActivity_.class);
            }
            intent.putExtra("questionSet", new Gson().toJson(questionSet));
            intent.putExtra("testFor", testFor);
            activity.startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
        } else {
            SewaUtil.generateToast(getContext(), LabelConstants.COMPLETE_PREVIOUS_LESSONS);
        }
    }

    private ExpandableListView.OnChildClickListener getChildClickListener() {
        return new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (!selectedCourse.getListDataBeans().get(groupPosition).getGroupType().equals(LmsConstants.LMS_COURSE)) {
                    LmsTopicDataBean selectedModule = selectedCourse.getListDataBeans().get(groupPosition).getModule();
                    LmsLessonDataBean selectedLesson = selectedCourse.getListDataBeans().get(groupPosition).getModule().getTopicMedias().get(childPosition);
                    String path = SewaConstants.getDirectoryPath(getContext(), SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(selectedLesson.getMediaId(), selectedLesson.getMediaFileName());
                    if (UtilBean.isFileExists(path)) {
                        if (activity.getSelectedLesson() == null || !selectedLesson.getActualId().equals(activity.getSelectedLesson().getActualId())) {
                            if (Boolean.TRUE.equals(selectedCourse.getAllowedToSkipLessons()) || Boolean.TRUE.equals(activity.lmsService.getAllowedMediaMap(selectedCourse.getCourseId()).get(selectedLesson.getActualId()))) {
                                if (Boolean.TRUE.equals(activity.lmsService.isQuizMinimumMarks(selectedModule.getCourseId(),selectedModule.getTopicId(),selectedLesson.getActualId()))) {
                                    expandableListAdaptor.setViewedLesson(activity.lmsService.getViewedLessonList());
                                    activity.setSelectedModule(selectedModule);
                                    activity.setSelectedLesson(selectedLesson);
                                    selectedLessonGroupIndex = groupPosition;
                                    selectedLessonChildIndex = childPosition;
                                    int idx = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                                    parent.setItemChecked(idx, true);
                                } else {
                                    SewaUtil.generateToast(getContext(), LabelConstants.COMPLETE_PREVIOUS_QUIZ);
                                }
                            } else {
                                SewaUtil.generateToast(getContext(), LabelConstants.COMPLETE_PREVIOUS_LESSONS);
                            }
                        }
                    } else {
                        //Don't do anything if file is not downloaded
                    }
                }
                return false;
            }
        };
    }

    private ExpandableListView.OnGroupExpandListener getOnExpandGroupListener() {
        return new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (selectedCourse.getListDataBeans().get(groupPosition).getGroupType().equals(LmsConstants.LMS_COURSE)) {
                    openLmsTestModule(selectedCourse.getListDataBeans().get(groupPosition).getQuestionSet(), selectedCourse.getCourseName());
                } else {
                    if (lastExpandedGroupPosition != -1 && groupPosition != lastExpandedGroupPosition) {
                        expandableListView.collapseGroup(lastExpandedGroupPosition);
                    }
                    lastExpandedGroupPosition = groupPosition;

                    if (selectedLessonChildIndex != -1 && selectedLessonGroupIndex != -1 && groupPosition == selectedLessonGroupIndex) {
                        int idx = expandableListView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, selectedLessonChildIndex));
                        expandableListView.setItemChecked(idx, true);
                    }
                }
            }
        };
    }

    private ExpandableListView.OnGroupCollapseListener getOnCollapseGroupListener() {
        return new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                expandableListView.clearChoices();
            }
        };
    }

}