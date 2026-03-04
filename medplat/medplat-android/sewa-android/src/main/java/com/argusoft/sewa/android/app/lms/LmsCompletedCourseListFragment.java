package com.argusoft.sewa.android.app.lms;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.core.impl.LmsDownloadServiceImpl;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LmsCompletedCourseListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LmsCompletedCourseListFragment extends Fragment {

    private List<LmsCourseDataBean> completedCourses;
    private LmsCourseDataBean selectedCourse;
    private LmsServiceImpl lmsService;
    private LmsDownloadServiceImpl lmsDownloadService;
    private LmsCourseListAdaptor adapter;
    private MyAlertDialog alertDialog;
    private RecyclerView listView;
    private List<LmsCourseScreenDataBean> items;
    private FrameLayout mainLayout;

    public LmsCompletedCourseListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CompletedCourseListFragment.
     */
    public static LmsCompletedCourseListFragment newInstance(LmsDownloadServiceImpl lmsDownloadService,
                                                             LmsServiceImpl lmsService,
                                                             List<LmsCourseDataBean> courses) {
        LmsCompletedCourseListFragment fragment = new LmsCompletedCourseListFragment();
        fragment.lmsDownloadService = lmsDownloadService;
        fragment.lmsService = lmsService;
        Bundle args = new Bundle();
        args.putString("courses", new Gson().toJson(courses));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String courses = getArguments().getString("courses");
            completedCourses = new Gson().fromJson(courses, new TypeToken<List<LmsCourseDataBean>>() {
            }.getType());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainLayout = (FrameLayout) inflater.inflate(R.layout.fragment_completed_course_list, container, false);
//        if (completedCourses != null && !completedCourses.isEmpty()) {
//            List<LmsCourseScreenDataBean> items = new ArrayList<>();
//            int indexOfCourses = 0;
//            int indexOfChapters = 0;
//            int totalLessons = 0;
//            for (LmsCourseDataBean bean : completedCourses) {
//                Bitmap image = null;
//                if (bean.getCourseImage() != null) {
//                    String path = SewaConstants.getDirectoryPath(getContext(), SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(bean.getCourseImage().getMediaId(), bean.getCourseImage().getMediaName());
//                    if (UtilBean.isFileExists(path)) {
//                        image = BitmapFactory.decodeFile(path);
//                    }
//                }
//                List<LmsTopicDataBean> topicMedias = completedCourses.get(indexOfCourses).getTopics();
//                for (LmsTopicDataBean lmsTopicDataBean : topicMedias) {
//                    totalLessons = totalLessons + lmsTopicDataBean.getTopicMedias().size();
//                }
//
//                int quizSet = 0;
//                if (!completedCourses.isEmpty()) {
//                    if (completedCourses.get(indexOfCourses).getQuestionSet() != null && !completedCourses.get(indexOfCourses).getQuestionSet().isEmpty()) {
//                        quizSet = completedCourses.get(indexOfCourses).getQuestionSet().size();
//                    }
//                    if (completedCourses.get(indexOfCourses).getTopics() != null && !completedCourses.get(indexOfCourses).getTopics().isEmpty()){
//                        for (LmsTopicDataBean lmsTopicDataBean: completedCourses.get(indexOfCourses).getTopics()){
//                            quizSet = quizSet + lmsTopicDataBean.getQuestionSet().size();
//                            if (completedCourses.get(indexOfCourses).getTopics().get(indexOfChapters).getTopicMedias() !=null && !completedCourses.get(indexOfCourses).getTopics().get(indexOfChapters).getTopicMedias().isEmpty()){
//                                for (LmsLessonDataBean lmsLessonDataBean: completedCourses.get(indexOfCourses).getTopics().get(indexOfChapters).getTopicMedias()){
//                                    quizSet = quizSet + lmsLessonDataBean.getQuestionSet().size();
//                                }
//                            }
//                            indexOfChapters++;
//                        }
//                    }
//                }
//
//                items.add(new LmsCourseScreenDataBean(image,
//                        bean.getCourseName(),
//                        bean.getCourseDescription(),
//                        bean.getCompletionStatus(),
//                        quizSet,
//                        totalLessons)
//                );
//                indexOfCourses++;
//                indexOfChapters = 0;
//                totalLessons = 0;
//            }
//
//            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
//                selectedCourse = completedCourses.get(position);
//                openLessonView();
//            };
//            LmsCourseListAdaptor adapter = new LmsCourseListAdaptor(getContext(), items, true);
//
//            ListView listView = new ListView(getContext());
//            listView.setPadding(30, 30, 30, 30);
//            listView.setDivider(ContextCompat.getDrawable(getContext(), R.color.transparent));
//            listView.setDividerHeight(30);
//            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(onItemClickListener);
//            mainLayout.addView(listView);
//        } else {
//            MaterialTextView textView = MyStaticComponents.generateInstructionView(getContext(), "No course to display at this time. Please check back later");
//            textView.setPadding(20, 20, 20, 20);
//            mainLayout.addView(textView);
//        }
        setItems();
        for (LmsCourseDataBean lmsCourseDataBean : completedCourses){
            lmsService.markCourseViewed(lmsCourseDataBean.getCourseId());
        }
        return mainLayout;
    }

    public void openLessonView() {
        Intent intent = new Intent(getContext(), LmsCourseDetailActivity_.class);
        intent.putExtra("courseId", selectedCourse.getCourseId());
        startActivity(intent);
    }

    private void setItems(){
        if(mainLayout != null) {
            if (items != null)
                items.clear();
            else
                items = new ArrayList<>();
            mainLayout.removeAllViews();
            if (completedCourses != null && !completedCourses.isEmpty()) {
                int indexOfCourses = 0;
                int indexOfChapters = 0;
                int totalLessons = 0;
                for (LmsCourseDataBean bean : completedCourses) {
                    Bitmap image = null;
                    if (bean.getCourseImage() != null) {
                        String path = SewaConstants.getDirectoryPath(getContext(), SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(bean.getCourseImage().getMediaId(), bean.getCourseImage().getMediaName());
                        if (UtilBean.isFileExists(path)) {
                            image = BitmapFactory.decodeFile(path);
                        }
                    }

                    List<LmsTopicDataBean> topicMedias = completedCourses.get(indexOfCourses).getTopics();
                    for (LmsTopicDataBean lmsTopicDataBean : topicMedias) {
                        totalLessons = totalLessons + lmsTopicDataBean.getTopicMedias().size();
                    }

                    int quizSet = 0;
                    if (completedCourses != null && !completedCourses.isEmpty()) {
                        if (completedCourses.get(indexOfCourses).getQuestionSet() != null && !completedCourses.get(indexOfCourses).getQuestionSet().isEmpty()) {
                            quizSet = completedCourses.get(indexOfCourses).getQuestionSet().size();
                        }
                        if (completedCourses.get(indexOfCourses).getTopics() != null && !completedCourses.get(indexOfCourses).getTopics().isEmpty()) {
                            for (LmsTopicDataBean lmsTopicDataBean : completedCourses.get(indexOfCourses).getTopics()) {
                                quizSet = quizSet + lmsTopicDataBean.getQuestionSet().size();
                                if (completedCourses.get(indexOfCourses).getTopics().get(indexOfChapters).getTopicMedias() != null && !completedCourses.get(indexOfCourses).getTopics().get(indexOfChapters).getTopicMedias().isEmpty()) {
                                    for (LmsLessonDataBean lmsLessonDataBean : completedCourses.get(indexOfCourses).getTopics().get(indexOfChapters).getTopicMedias()) {
                                        quizSet = quizSet + lmsLessonDataBean.getQuestionSet().size();
                                    }
                                }
                                indexOfChapters++;
                            }
                        }
                    }

                    items.add(new LmsCourseScreenDataBean(image,
                            bean.getCourseId(),
                            bean.getCourseName(),
                            bean.getCourseDescription(),
                            bean.getCompletionStatus(),
                            quizSet,
                            totalLessons,
                            bean.getMediaDownloaded(),
                            lmsDownloadService.getCourseSize(bean))
                    );
                    indexOfCourses++;
                    indexOfChapters = 0;
                    totalLessons = 0;
                }
                adapter = new LmsCourseListAdaptor(getContext(), items, true, true, lmsService) {
                    @Override
                    public void onAdapterItemClick(int position) {
                        selectedCourse = completedCourses.get(position);
                        openLessonView();
                    }

                    @Override
                    public void onMenuClick(int position, View view) {
                        PopupMenu popupMenu = new PopupMenu(getContext(), view);

                        // Inflating popup menu from popup_menu.xml file
                        popupMenu.getMenuInflater().inflate(R.menu.menu_archive, popupMenu.getMenu());
                        Menu menu = popupMenu.getMenu();
                        menu.findItem(R.id.menu_restore).setVisible(false);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                // Toast message on menu item clicked
                                popupMenu.dismiss();
                                LmsCourseScreenDataBean item = adapter.getData().get(position);
                                View.OnClickListener listener = v -> {
                                    alertDialog.dismiss();
                                    if (v.getId() == BUTTON_POSITIVE) {
                                        lmsService.makeCourseArchive(item.getCourseId(), true);
                                        lmsDownloadService.deleteCourseMedia(item.getCourseId());
                                        adapter.removeItem(position);
                                        completedCourses.remove(position);
                                        SewaUtil.generateToast(getContext(), getContext().getResources().
                                                getString(R.string.msg_course_archived));
                                        if(completedCourses == null || completedCourses.isEmpty()){
                                            mainLayout.removeAllViews();
                                            MaterialTextView textView = MyStaticComponents.generateInstructionView(getContext(), "No course to display at this time. Please check back later");
                                            textView.setPadding(20, 20, 20, 20);
                                            mainLayout.addView(textView);
                                        }
                                    }
                                };
                                alertDialog = new MyAlertDialog(getContext(), false,
                                        UtilBean.getMyLabel(LmsConstants.COURSE_ARE_YOU_SURE_ALERT),
                                        listener, DynamicUtils.BUTTON_YES_NO);
                                alertDialog.show();
                                return true;
                            }
                        });
                        // Showing the popup menu
                        popupMenu.show();
                    }
                };

                listView = new RecyclerView(getContext());
                listView.setLayoutManager(new LinearLayoutManager(getContext()));
                listView.setPadding(30, 30, 30, 30);
                listView.setAdapter(adapter);
                mainLayout.addView(listView);
            } else {
                MaterialTextView textView = MyStaticComponents.generateInstructionView(getContext(), "No course to display at this time. Please check back later");
                textView.setPadding(20, 20, 20, 20);
                mainLayout.addView(textView);
            }
        }
    }

    public void resetList(List<LmsCourseDataBean> courses){
        if(courses != null){
            if(mainLayout == null) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        completedCourses = courses;
                        setItems();
                    }
                }, 2000);
            } else {
                completedCourses = courses;
                setItems();
            }
        }
    }

}