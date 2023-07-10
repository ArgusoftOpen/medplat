package com.argusoft.sewa.android.app.lms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.util.UtilBean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LmsOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LmsOverviewFragment extends Fragment {

    private LmsCourseDetailActivity activity;
    private LinearLayout bodyLayoutContainer;

    private LmsCourseDataBean selectedCourse;
    private LmsTopicDataBean selectedModule;
    private LmsLessonDataBean selectedLesson;

    public LmsOverviewFragment() {
        // Required empty public constructor
    }

    public static LmsOverviewFragment newInstance() {
        return new LmsOverviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainLayout = inflater.inflate(R.layout.fragment_lms_overview, container, false);
        bodyLayoutContainer = mainLayout.findViewById(R.id.bodyLayoutContainer);
        activity = (LmsCourseDetailActivity) getActivity();
        initView();
        return mainLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    public void initView() {
        getDataFromActivity();
        setBodyLayout();
    }

    private void getDataFromActivity() {
        if (activity != null) {
            selectedCourse = activity.getSelectedCourse();
            selectedModule = activity.getSelectedModule();
            selectedLesson = activity.getSelectedLesson();
        }
    }

    private void setBodyLayout() {
        bodyLayoutContainer.removeAllViews();
        if (selectedCourse != null) {
            if (selectedCourse.getCourseName() != null && !selectedCourse.getCourseName().isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(getContext(),
                        UtilBean.getMyLabel("Course") + " - " + UtilBean.getMyLabel(selectedCourse.getCourseName())));
            }
            if (selectedCourse.getCourseDescription() != null && !selectedCourse.getCourseDescription().isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(getContext(), selectedCourse.getCourseDescription()));
            }
        }
        if (selectedModule != null) {
            if (selectedModule.getTopicName() != null && !selectedModule.getTopicName().isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(getContext(),
                        UtilBean.getMyLabel("Module") + " - " + UtilBean.getMyLabel(selectedModule.getTopicName())));
            }
            if (selectedModule.getTopicDescription() != null && !selectedModule.getTopicDescription().isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(getContext(), selectedModule.getTopicDescription()));
            }
        }
        if (selectedLesson != null) {
            if (selectedLesson.getTitle() != null && !selectedLesson.getTitle().isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(getContext(),
                        UtilBean.getMyLabel("Lesson") + " - " + UtilBean.getMyLabel(selectedLesson.getTitle())));
            }
            if (selectedLesson.getDescription() != null && !selectedLesson.getTitle().isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(getContext(), selectedLesson.getDescription()));
            }
        }
    }
}