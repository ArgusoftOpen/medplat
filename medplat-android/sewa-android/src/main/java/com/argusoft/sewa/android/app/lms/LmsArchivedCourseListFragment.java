package com.argusoft.sewa.android.app.lms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.core.LmsDownloadService;
import com.argusoft.sewa.android.app.core.impl.LmsDownloadServiceImpl;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class LmsArchivedCourseListFragment extends Fragment {

    private List<LmsCourseDataBean> archivedCourses;
    private LmsServiceImpl lmsService;
    private LmsDownloadService lmsDownloadService;
    private LmsCourseListAdaptor adapter;
    private RecyclerView listView;
    private List<LmsCourseScreenDataBean> items;
    private FrameLayout mainLayout;

    public LmsArchivedCourseListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PendingCourseListFragment.
     */
    public static LmsArchivedCourseListFragment newInstance(LmsDownloadServiceImpl lmsDownloadService,
                                                            LmsServiceImpl lmsService, List<LmsCourseDataBean> courses) {
        LmsArchivedCourseListFragment fragment = new LmsArchivedCourseListFragment();
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
            archivedCourses = new Gson().fromJson(courses, new TypeToken<List<LmsCourseDataBean>>() {
            }.getType());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainLayout = (FrameLayout) inflater.inflate(R.layout.fragment_pending_course_list, container, false);
        items = new ArrayList<>();
        setItems();
        for (LmsCourseDataBean lmsCourseDataBean : archivedCourses){
            lmsService.markCourseViewed(lmsCourseDataBean.getCourseId());
        }

        return mainLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setItems(){
        if(mainLayout != null) {
            if(items != null)
                items.clear();
            else
                items = new ArrayList<>();
            mainLayout.removeAllViews();
            if (archivedCourses != null && !archivedCourses.isEmpty()) {
                int indexOfCourses = 0;
                int indexOfChapters = 0;
                int totalLessons = 0;
                for (LmsCourseDataBean bean : archivedCourses) {
                    Bitmap image = null;
                    if (bean.getCourseImage() != null) {
                        String path = SewaConstants.getDirectoryPath(getContext(), SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(bean.getCourseImage().getMediaId(), bean.getCourseImage().getMediaName());
                        if (UtilBean.isFileExists(path)) {
                            image = BitmapFactory.decodeFile(path);
                        }
                    }

                    List<LmsTopicDataBean> topicMedias = archivedCourses.get(indexOfCourses).getTopics();
                    for (LmsTopicDataBean lmsTopicDataBean : topicMedias) {
                        totalLessons = totalLessons + lmsTopicDataBean.getTopicMedias().size();
                    }

                    int quizSet = 0;
                    if (archivedCourses != null && !archivedCourses.isEmpty()) {
                        if (archivedCourses.get(indexOfCourses).getQuestionSet() != null && !archivedCourses.get(indexOfCourses).getQuestionSet().isEmpty()) {
                            quizSet = archivedCourses.get(indexOfCourses).getQuestionSet().size();
                        }
                        if (archivedCourses.get(indexOfCourses).getTopics() != null && !archivedCourses.get(indexOfCourses).getTopics().isEmpty()) {
                            for (LmsTopicDataBean lmsTopicDataBean : archivedCourses.get(indexOfCourses).getTopics()) {
                                quizSet = quizSet + lmsTopicDataBean.getQuestionSet().size();
                                if (archivedCourses.get(indexOfCourses).getTopics().get(indexOfChapters).getTopicMedias() != null && !archivedCourses.get(indexOfCourses).getTopics().get(indexOfChapters).getTopicMedias().isEmpty()) {
                                    for (LmsLessonDataBean lmsLessonDataBean : archivedCourses.get(indexOfCourses).getTopics().get(indexOfChapters).getTopicMedias()) {
                                        quizSet = quizSet + lmsLessonDataBean.getQuestionSet().size();
                                    }
                                }
                                indexOfChapters++;
                            }
                        }
                    }

                    items.add(new LmsCourseScreenDataBean(image, bean.getCourseId(), bean.getCourseName(),
                            bean.getCourseDescription(),
                            bean.getCompletionStatus(),
                            quizSet,
                            totalLessons, true,
                            0L)
                    );

                    indexOfCourses++;
                    indexOfChapters = 0;
                    totalLessons = 0;
                }
                adapter = new LmsCourseListAdaptor(getContext(), items, true, true, lmsService) {
                    @Override
                    public void onAdapterItemClick(int position) {
                    }

                    @Override
                    public void onMenuClick(int position, View view) {
                        PopupMenu popupMenu = new PopupMenu(getContext(), view);

                        // Inflating popup menu from popup_menu.xml file
                        popupMenu.getMenuInflater().inflate(R.menu.menu_archive, popupMenu.getMenu());
                        Menu menu = popupMenu.getMenu();
                        menu.findItem(R.id.menu_archive).setVisible(false);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                // Toast message on menu item clicked
                                popupMenu.dismiss();
                                LmsCourseScreenDataBean item = adapter.getData().get(position);
                                lmsService.makeCourseArchive(item.getCourseId(), false);
                                lmsDownloadService.addCourseMediaToDownload(item.getCourseId());
                                adapter.removeItem(position);
                                archivedCourses.remove(position);
                                SewaUtil.generateToast(getContext(), getContext().getResources().
                                        getString(R.string.msg_course_restored));
                                if(archivedCourses == null || archivedCourses.isEmpty()){
                                    mainLayout.removeAllViews();
                                    MaterialTextView textView = MyStaticComponents.generateInstructionView(getContext(), "No course to display at this time. Please check back later");
                                    textView.setPadding(20, 20, 20, 20);
                                    mainLayout.addView(textView);
                                }
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
                        archivedCourses = courses;
                        setItems();
                    }
                }, 1000);
            } else {
                archivedCourses = courses;
                setItems();
            }
        }
    }

}

