package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyAudioPlayer;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.treeview.holder.TreeHolder;
import com.argusoft.sewa.android.app.treeview.model.TreeNode;
import com.argusoft.sewa.android.app.treeview.view.AndroidTreeView;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.MyComparatorUtil;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EActivity
public class LMSActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    LmsServiceImpl lmsService;

    @Bean
    SewaFhsServiceImpl fhsService;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout footerLayout;
    private Button nextButton;

    List<LmsCourseDataBean> lmsCourseDataBeans;
    private LmsCourseDataBean selectedCourse;
    private Integer selectedCourseIndex;
    private LmsLessonDataBean selectedMedia;

    private View treeView;

    private String screen;
    private static final String COURSE_SCREEN = "COURSE_SCREEN";
    private static final String MODULE_SCREEN = "MODULE_SCREEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (globalPanel != null) {
            setContentView(globalPanel);
        }
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.LEARNING_MANAGEMENT_SYSTEM_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        //Fetch List of courses
        lmsCourseDataBeans = lmsService.retrieveCourses();
        setCourseScreen();
    }

    @UiThread
    public void setCourseScreen() {
        screen = COURSE_SCREEN;
        selectedCourseIndex = null;
        bodyLayoutContainer.removeAllViews();

        if (lmsCourseDataBeans == null || lmsCourseDataBeans.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_COURSES_AVAILABLE));
            nextButton.setText(GlobalTypes.EVENT_OKAY);
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_COURSES));

            List<ListItemDataBean> items = new ArrayList<>();
            for (LmsCourseDataBean bean : lmsCourseDataBeans) {
                items.add(new ListItemDataBean(bean.getCourseName(), null, bean.getCourseDescription()));
            }
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedCourseIndex = position;

            PagingListView pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, items, R.layout.listview_row_with_info, onItemClickListener, null);
            bodyLayoutContainer.addView(pagingListView);
        }
        hideProcessDialog();
    }

    @UiThread
    public void setModuleScreen() {
        showProcessDialog();
        screen = MODULE_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);

        treeView = generateTreeView(selectedCourse);
        bodyLayoutContainer.addView(treeView);
        hideProcessDialog();
    }

    private View generateTreeView(final LmsCourseDataBean course) {
        if (course == null) {
            MaterialTextView textView = MyStaticComponents.generateInstructionView(context, "No courses to show. Please refresh and try again");
            textView.setPadding(20, 0, 0, 0);
            return textView;
        }

        //Parent
        //Root
        TreeNode root = TreeNode.root();
        AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);

        //For Courses
        TreeHolder.IconTreeItem nodeItem = new TreeHolder.IconTreeItem(R.drawable.ic_arrow_right, course.getCourseName(), false);
        TreeNode parent = new TreeNode(nodeItem).setViewHolder(new TreeHolder(getApplicationContext(), true, TreeHolder.DEFAULT, TreeHolder.DEFAULT));
        root.addChild(parent);

        //For Modules
        List<LmsTopicDataBean> topics = course.getTopics();
        Collections.sort(topics, MyComparatorUtil.LMS_TOPIC_ORDER_COMPARATOR);
        for (final LmsTopicDataBean topic : topics) {
            //Child
            TreeHolder.IconTreeItem childItem = new TreeHolder.IconTreeItem(R.drawable.ic_arrow_right, topic.getTopicName(), false);
            TreeNode child = new TreeNode(childItem).setViewHolder(new TreeHolder(getApplicationContext(), true, R.layout.child, 50));
            parent.addChildren(child);
            parent.setExpanded(true);

            //Sub Child
            //For Lessons
            List<LmsLessonDataBean> medias = topic.getTopicMedias();
            Collections.sort(medias, MyComparatorUtil.LMS_TOPIC_MEDIA_ORDER_COMPARATOR);
            List<Integer> viewedLessonList = lmsService.getViewedLessonList();
            for (final LmsLessonDataBean media : medias) {
                TreeHolder.IconTreeItem subChildItem = new TreeHolder.IconTreeItem(getIconFromMediaType(media), media.getTitle(), viewedLessonList.contains(media.getActualId()));
                TreeNode subChild = new TreeNode(subChildItem).setViewHolder(new TreeHolder(getApplicationContext(), false, R.layout.child, 70));
                child.addChild(subChild);

                subChild.setClickListener((node, value) -> {
                    selectedMedia = media;
                    if (Boolean.TRUE.equals(lmsService.getAllowedMediaMap(course.getCourseId()).get(media.getActualId()))) {
                        openMediaAccordingToType(media);
                    } else {
                        SewaUtil.generateToast(context, LabelConstants.COMPLETE_PREVIOUS_LESSONS);
                    }
                });

                if (selectedMedia != null && selectedMedia.getActualId().equals(media.getActualId())) {
                    child.setExpanded(true);
                    subChild.setSelected(true);
                }

                //Lesson Test Added
                addLessonTestInTreeView(media, child);
            }

            //Module Test Added
            addModuleTestInTreeView(topic, child);
        }

        //Course Test Added
        addCourseTestInTreeView(course, parent);
        return tView.getView();
    }

    private int getIconFromMediaType(LmsLessonDataBean media) {
        switch (media.getMediaType()) {
            case LmsConstants.MEDIA_TYPE_VIDEO:
                return R.drawable.play_video;
            case LmsConstants.MEDIA_TYPE_PDF:
                return R.drawable.pdf;
            case LmsConstants.MEDIA_TYPE_IMAGE:
                return R.drawable.image;
            case LmsConstants.MEDIA_TYPE_AUDIO:
                return R.drawable.play_audio;
            default:
                return R.drawable.pending;
        }
    }

    private void openMediaAccordingToType(LmsLessonDataBean media) {
        String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(media.getMediaId(), media.getMediaFileName());
        if (!UtilBean.isFileExists(path)) {
            SewaUtil.generateToast(context, LabelConstants.FILE_NOT_FOUND);
            return;
        }

        Intent intent;
        switch (media.getMediaType()) {
            case LmsConstants.MEDIA_TYPE_VIDEO:
                intent = new Intent(context, LMSVideoPlayerActivity_.class);
                intent.putExtra("courseId", selectedCourse.getCourseId());
                intent.putExtra("lesson", new Gson().toJson(media));
                startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
                break;
            case LmsConstants.MEDIA_TYPE_PDF:
                intent = new Intent(context, CustomPDFViewerActivity_.class);
                intent.putExtra("path", path);
                intent.putExtra("fileName", media.getDescription());
                intent.putExtra("fromLms", true);
                intent.putExtra("courseId", selectedCourse.getCourseId());
                intent.putExtra("lesson", new Gson().toJson(media));
                startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
                break;
            case LmsConstants.MEDIA_TYPE_IMAGE:
                intent = new Intent(this, CustomImageViewerActivity_.class);
                intent.putExtra("path", path);
                intent.putExtra("fileName", media.getTitle());
                intent.putExtra("fromLms", true);
                intent.putExtra("courseId", selectedCourse.getCourseId());
                intent.putExtra("lesson", new Gson().toJson(media));
                startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
                break;
            case LmsConstants.MEDIA_TYPE_AUDIO:
                Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(path));
                MediaPlayer player = MediaPlayer.create(this, uri);
                MyAudioPlayer myAudioPlayer = new MyAudioPlayer(this, media.getTitle(), player, true, lmsService, media, selectedCourse.getCourseId());
                myAudioPlayer.show();
                myAudioPlayer.setOnCancelListener(dialog -> {
                    bodyLayoutContainer.removeView(treeView);
                    treeView = generateTreeView(selectedCourse);
                    bodyLayoutContainer.addView(treeView);
                });
                break;
            default:
                SewaUtil.generateToast(context, LabelConstants.FILE_IS_NOT_SUPPORTED);
        }
    }

    private void addLessonTestInTreeView(final LmsLessonDataBean media, TreeNode child) {
        if (media.getQuestionSet() != null && !media.getQuestionSet().isEmpty()) {
            for (final LmsQuestionSetDataBean questionSet : media.getQuestionSet()) {
                TreeHolder.IconTreeItem queSubChildItem = new TreeHolder.IconTreeItem(R.drawable.test,
                        UtilBean.getMyLabel("Lesson Test") + " - " + questionSet.getQuestionSetName() + " ("
                                + UtilBean.getMyLabel(fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString())) + ")",
                        false);
                final TreeNode queSubChild = new TreeNode(queSubChildItem).setViewHolder(new TreeHolder(getApplicationContext(), false, R.layout.child, 70));
                child.addChild(queSubChild);

                queSubChild.setClickListener((node, value) -> openLmsTestModule(null, null, media, questionSet, media.getTitle()));
            }
        }
    }

    private void addModuleTestInTreeView(final LmsTopicDataBean topic, TreeNode child) {
        if (topic.getQuestionSet() != null && !topic.getQuestionSet().isEmpty()) {
            for (final LmsQuestionSetDataBean questionSet : topic.getQuestionSet()) {
                TreeHolder.IconTreeItem subChildItem = new TreeHolder.IconTreeItem(R.drawable.test,
                        UtilBean.getMyLabel("Module Test") + " - " + questionSet.getQuestionSetName() + " ("
                                + UtilBean.getMyLabel(fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString())) + ")",
                        false);
                final TreeNode subChild = new TreeNode(subChildItem).setViewHolder(new TreeHolder(getApplicationContext(), false, R.layout.child, 70));
                child.addChild(subChild);

                subChild.setClickListener((node, value) -> openLmsTestModule(null, topic, null, questionSet, topic.getTopicName()));
            }
        }
    }

    private void addCourseTestInTreeView(final LmsCourseDataBean course, TreeNode parent) {
        if (course.getQuestionSet() != null && !course.getQuestionSet().isEmpty()) {
            for (final LmsQuestionSetDataBean questionSet : course.getQuestionSet()) {
                TreeHolder.IconTreeItem subChildItem = new TreeHolder.IconTreeItem(R.drawable.test,
                        UtilBean.getMyLabel("Course Test") + " - " + questionSet.getQuestionSetName() + " ("
                                + UtilBean.getMyLabel(fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString())) + ")",
                        false);
                final TreeNode child = new TreeNode(subChildItem).setViewHolder(new TreeHolder(getApplicationContext(), false, R.layout.child, 70));
                parent.addChild(child);

                child.setClickListener((node, value) -> openLmsTestModule(course, null, null, questionSet, course.getCourseName()));
            }
        }
    }

    private void openLmsTestModule(LmsCourseDataBean course, LmsTopicDataBean topic, LmsLessonDataBean media, LmsQuestionSetDataBean questionSet, String testFor) {
        List<Integer> viewedLessonList = lmsService.getViewedLessonList();
        boolean allLessonsCompleted = true;

        if (course != null) {
            for (LmsTopicDataBean topicDataBean : course.getTopics()) {
                for (LmsLessonDataBean mediaDataBean : topicDataBean.getTopicMedias()) {
                    if (!viewedLessonList.contains(mediaDataBean.getActualId())) {
                        allLessonsCompleted = false;
                        break;
                    }
                }
            }
        } else if (topic != null) {
            for (LmsLessonDataBean mediaDataBean : topic.getTopicMedias()) {
                if (!viewedLessonList.contains(mediaDataBean.getActualId())) {
                    allLessonsCompleted = false;
                    break;
                }
            }
        } else if (media != null) {
            allLessonsCompleted = viewedLessonList.contains(media.getActualId());
        }

        if (allLessonsCompleted) {
            Intent intent;
            if (selectedCourse != null && selectedCourse.getTestConfigJson() != null
                    && Boolean.TRUE.equals(selectedCourse.getTestConfigJson().get(questionSet.getQuestionSetType()).getProvideInteractiveFeedbackAfterEachQuestion())) {
                intent = new Intent(context, LmsInteractiveQuestionModuleActivity_.class);
            } else if (selectedCourse != null && selectedCourse.getTestConfigJson() != null
                    && questionSet.getQuestionSetType() == 2640) { //todo make id value dynamic as per field value name
                intent = new Intent(context, LmsSimulationActivity_.class);
            } else {
                intent = new Intent(context, LmsQuestionModuleActivity_.class);
            }
            intent.putExtra("questionSet", new Gson().toJson(questionSet));
            intent.putExtra("testFor", testFor);
            startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
        } else {
            SewaUtil.generateToast(context, LabelConstants.COMPLETE_PREVIOUS_LESSONS);
        }
    }

    @Override
    public void onClick(View v) {
        if (COURSE_SCREEN.equals(screen)) {
            if (lmsCourseDataBeans.isEmpty()) {
                navigateToHomeScreen(false);
            } else if (selectedCourseIndex != null) {
                selectedCourse = lmsCourseDataBeans.get(selectedCourseIndex);
                setModuleScreen();
            } else {
                SewaUtil.generateToast(context, "Please select a course");
            }
        } else {
            throw new IllegalStateException("Unexpected value: " + screen);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            if (MODULE_SCREEN.equals(screen)) {
                setCourseScreen();
                footerLayout.setVisibility(View.VISIBLE);
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                navigateToHomeScreen(false);
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(this,
                LabelConstants.CLOSE_LMS_ACTIVITY,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.LMS_ACTIVITY_REQUEST_CODE) {
            switch (screen) {
                case COURSE_SCREEN:
                    setCourseScreen();
                    footerLayout.setVisibility(View.VISIBLE);
                    break;
                case MODULE_SCREEN:
                    if (selectedCourse != null) {
                        setModuleScreen();
                    } else {
                        setCourseScreen();
                        footerLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + screen);
            }
        }
    }
}
