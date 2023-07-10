package com.argusoft.sewa.android.app.lms;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.activity.CustomImageViewerActivity_;
import com.argusoft.sewa.android.app.activity.CustomPDFViewerActivity_;
import com.argusoft.sewa.android.app.activity.LmsInteractiveQuestionModuleActivity_;
import com.argusoft.sewa.android.app.activity.LmsQuestionModuleActivity_;
import com.argusoft.sewa.android.app.activity.LmsSimulationActivity_;
import com.argusoft.sewa.android.app.activity.LoginActivity_;
import com.argusoft.sewa.android.app.activity.MenuActivity;
import com.argusoft.sewa.android.app.component.LMSFeedbackComponent;
import com.argusoft.sewa.android.app.component.MyAudioComponent;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.core.impl.LmsDownloadServiceImpl;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.BookmarkDataBean;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsCourseImageDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LmsBookMarkBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class LmsCourseDetailActivity extends MenuActivity {

    @Bean
    LmsServiceImpl lmsService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    LmsDownloadServiceImpl lmsDownloadService;

    private static final String TAG = "LmsCourseDetailActivity";

    private Integer courseId;
    private LmsCourseDataBean selectedCourse;
    private LmsTopicDataBean selectedModule;
    private LmsLessonDataBean selectedLesson;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FrameLayout lessonLayout;

    private MyVideoView myVideoView;
    private ImageButton videoRightButton;
    private LinearLayout videoRightLayout;
    private LinearLayout bookmarksListLayout;
    private LmsBookmarksFragment bookmarksFragment;
    private boolean isRightLayoutShown = false;
    private boolean isFullScreen = false;

    private final Context mContext = this;

    private int currentPosition;

    private MediaPlayer audioMediaPlayer;
    private String fileName = null;
    private boolean isAudioRecording = false;
    private MediaRecorder recorder = null;

    private LMSFeedbackComponent feedbackAlert;
    private Date sessionStartDate;

    private List<Timer> caseStudyTimerList = new ArrayList<>();

    private int positionForTranscript = 0;

    private MyAudioComponent myAudioComponent;

    public LmsCourseDataBean getSelectedCourse() {
        return selectedCourse;
    }

    public LmsTopicDataBean getSelectedModule() {
        return selectedModule;
    }

    public LmsLessonDataBean getSelectedLesson() {
        return selectedLesson;
    }

    public void setSelectedModule(LmsTopicDataBean selectedModule) {
        this.selectedModule = selectedModule;
    }

    public void setPositionForTranscript(int position) {
        positionForTranscript = position;
    }

    public void setSelectedLesson(LmsLessonDataBean selectedLesson) {
        if (this.selectedLesson == null || !selectedLesson.getActualId().equals(this.selectedLesson.getActualId())) {
            if (this.selectedLesson != null && this.selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_VIDEO) && myVideoView != null) {
                currentPosition = myVideoView.getCurrentPosition();
                if (currentPosition != 0 && currentPosition != myVideoView.getDuration()) {
                    lmsService.updateLastPausedOnForMedia(currentPosition, this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                    lmsService.addSessionsForMedia(sessionStartDate, new Date(), this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                }
            } else if (this.selectedLesson != null && this.selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_AUDIO) && audioMediaPlayer != null) {
                currentPosition = audioMediaPlayer.getCurrentPosition();
                audioMediaPlayer.pause();
                if (currentPosition != 0 && currentPosition != audioMediaPlayer.getDuration()) {
                    lmsService.updateLastPausedOnForMedia(currentPosition, this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                    lmsService.addSessionsForMedia(sessionStartDate, new Date(), this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                }
            }
            this.selectedLesson = selectedLesson;
            addLessonView();
            startNextLessonDownload();
        }
    }

    @Background
    public void startNextLessonDownload() {
        if (selectedCourse != null && selectedCourse.getTopics() != null && !selectedCourse.getTopics().isEmpty()) {
            boolean bool = false;
            for (LmsTopicDataBean topicDataBean : selectedCourse.getTopics()) {
                if (topicDataBean.getTopicMedias() != null && !topicDataBean.getTopicMedias().isEmpty()) {
                    for (LmsLessonDataBean lesson : topicDataBean.getTopicMedias()) {
                        if (!bool && lesson.getActualId().equals(selectedLesson.getActualId())) {
                            bool = true;
                        } else {
                            startFileDownloadForLesson(lesson);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void startFileDownloadForLesson(LmsLessonDataBean lesson) {
        String lmsFileName = UtilBean.getLMSFileName(lesson.getMediaId(), lesson.getMediaFileName());
        String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + lmsFileName;
        if (!UtilBean.isFileExists(path)) {
            lmsDownloadService.startFileDownloading(context, lesson.getMediaId(), lmsFileName);
        }
    }

    public MyVideoView getVideoView() {
        return myVideoView;
    }

    public Integer pauseMediaPlayerAndGetPosition() {
        if (myVideoView != null) {
            myVideoView.pause();
            return myVideoView.getCurrentPosition();
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_lms_course_detail);
        getDataFromBundle();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }

        if (selectedLesson != null && selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_VIDEO)
                && myVideoView != null) {
            if (positionForTranscript != 0)
                myVideoView.seekTo(positionForTranscript);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_refresh).setVisible(false);
        menu.findItem(R.id.menu_about).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(true);
        return true;
    }

    public void getDataFromBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            courseId = extras.getInt("courseId");
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        fetchSelectedCourse();
    }

    public void fetchSelectedCourse() {
        selectedCourse = lmsService.retrieveCourseByCourseId(courseId);
        lmsService.markCourseDataViewed(courseId, selectedCourse.getTopics(), selectedCourse.getQuestionSet());
        setBodyDetail();
    }

    @UiThread
    public void setBodyDetail() {
        if (selectedCourse == null) {
            SewaUtil.generateToast(mContext, "Some error occurred. Please try again.");
            finish();
            return;
        }
        setTitle(selectedCourse.getCourseName());
        addMainLayout();
    }

    public void addMainLayout() {
        lessonLayout = findViewById(R.id.lesson_view);

        if (selectedCourse != null && selectedCourse.getCourseImage() != null) {
            LmsCourseImageDataBean courseImage = selectedCourse.getCourseImage();
            if (courseImage.getMediaId() != null) {
                String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(courseImage.getMediaId(), courseImage.getMediaName());
                if (UtilBean.isFileExists(path)) {
                    lessonLayout.setBackground(Drawable.createFromPath(path));
                }
            }
        }

        viewPager = findViewById(R.id.lms_view_pager);
        tabLayout = findViewById(R.id.lms_tabs);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        LmsFragmentPagerAdaptor adapter = new LmsFragmentPagerAdaptor(getSupportFragmentManager());
        LmsContentFragment contentFragment = LmsContentFragment.newInstance();
        LmsOverviewFragment overviewFragment = LmsOverviewFragment.newInstance();
        bookmarksFragment = LmsBookmarksFragment.newInstance();
        adapter.addFrag(overviewFragment, UtilBean.getMyLabel("Overview"));
        adapter.addFrag(contentFragment, UtilBean.getMyLabel("Contents"));
        adapter.addFrag(bookmarksFragment, UtilBean.getMyLabel("Bookmarks"));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        TabLayout.Tab contents = tabLayout.getTabAt(0);
        if (contents != null) {
            contents.setIcon(R.drawable.ic_contents);
        }
        TabLayout.Tab overview = tabLayout.getTabAt(1);
        if (overview != null) {
            overview.setIcon(R.drawable.ic_info);
        }
        TabLayout.Tab bookmarks = tabLayout.getTabAt(2);
        if (bookmarks != null) {
            bookmarks.setIcon(R.drawable.ic_bookmarks);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.menu_home) {
            if (this.selectedLesson != null && this.selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_VIDEO) && myVideoView != null) {
                currentPosition = myVideoView.getCurrentPosition();
                if (currentPosition != 0 && currentPosition != myVideoView.getDuration()) {
                    lmsService.updateLastPausedOnForMedia(currentPosition, this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                    lmsService.addSessionsForMedia(sessionStartDate, new Date(), this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                }
            } else if (this.selectedLesson != null && this.selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_AUDIO) && audioMediaPlayer != null) {
                currentPosition = audioMediaPlayer.getCurrentPosition();
                if (currentPosition != 0 && currentPosition != audioMediaPlayer.getDuration()) {
                    lmsService.updateLastPausedOnForMedia(currentPosition, this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                    lmsService.addSessionsForMedia(sessionStartDate, new Date(), this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                }
            }
            navigateToHomeScreen(false);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen && selectedLesson != null && selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_VIDEO) && myVideoView != null) {
            myVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
            if (this.selectedLesson != null && this.selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_VIDEO) && myVideoView != null) {
                currentPosition = myVideoView.getCurrentPosition();
                if (currentPosition != 0 && currentPosition != myVideoView.getDuration()) {
                    lmsService.updateLastPausedOnForMedia(currentPosition, this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                    lmsService.addSessionsForMedia(sessionStartDate, new Date(), this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                }
            } else if (this.selectedLesson != null && this.selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_AUDIO) && audioMediaPlayer != null) {
                currentPosition = audioMediaPlayer.getCurrentPosition();
                if (currentPosition != 0 && currentPosition != audioMediaPlayer.getDuration()) {
                    lmsService.updateLastPausedOnForMedia(currentPosition, this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                    lmsService.addSessionsForMedia(sessionStartDate, new Date(), this.selectedLesson.getActualId(), this.selectedLesson.getTopicId(), courseId);
                }
            }
        }
    }

    public List<LmsBookMarkBean> getBookmarks() {
        if (selectedLesson != null) {
            lmsService.retrieveBookmarks(selectedLesson.getActualId());
        }

        return Collections.emptyList();
    }

    public void addLessonView() {
        lessonLayout.removeAllViews();
        String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(selectedLesson.getMediaId(), selectedLesson.getMediaFileName());
        if (!UtilBean.isFileExists(path)) {
            SewaUtil.generateToast(mContext, UtilBean.getMyLabel("File not found. Please refresh and try again."));
            return;
        }

        if (!this.selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_AUDIO) && audioMediaPlayer != null) {
            currentPosition = audioMediaPlayer.getCurrentPosition();
            audioMediaPlayer.pause();
            if (myAudioComponent != null) {
                myAudioComponent.setPlayIcon();
            }
        }

        switch (selectedLesson.getMediaType()) {
            case LmsConstants.MEDIA_TYPE_VIDEO:
                addVideoLesson(path, selectedLesson);
                break;
            case LmsConstants.MEDIA_TYPE_AUDIO:
                addAudioLesson(path, selectedLesson);
                break;
            case LmsConstants.MEDIA_TYPE_IMAGE:
                addImageLesson(path, selectedLesson);
                break;
            case LmsConstants.MEDIA_TYPE_PDF:
                addPdfLesson(path, selectedLesson);
                break;
            default:
        }
    }

    private void addVideoLesson(final String path, LmsLessonDataBean lesson) {
        File file = new File(path);
        if (file.exists()) {
            if (!UtilBean.videoFileIsCorrupted(mContext, path)) {
                SewaUtil.generateToast(mContext, UtilBean.getMyLabel("Some error occurred. Please refresh and try again."));
                UtilBean.deleteFile(path);
                return;
            }
            FrameLayout layout = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.lms_universal_video_view, null);
            Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
            myVideoView = layout.findViewById(R.id.videoView);
            MyVideoMediaController myVideoMediaController = layout.findViewById(R.id.media_controller);
            videoRightButton = layout.findViewById(R.id.right_menu);
            videoRightLayout = layout.findViewById(R.id.right_menu_layout);

            myVideoView.setMediaController(myVideoMediaController);

            lessonLayout.addView(layout);

            setRightLayoutInVideo();
            videoRightButton.setVisibility(View.GONE);

            myVideoView.setOnErrorListener((mp, what, extra) -> {
                SewaUtil.generateToast(mContext, UtilBean.getMyLabel("Some error occurred. Please refresh and try again."));
                UtilBean.deleteFile(path);
                return false;
            });

            setVideoCallback(myVideoView);
            myVideoMediaController.setTitle(lesson.getTitle());
            myVideoView.setVideoURI(uri);
            myVideoView.seekTo(lmsService.getLastPausedOn(selectedLesson.getActualId()));
            myVideoView.start();

            setCaseStudyQuizInVideo();

            sessionStartDate = new Date();
            lmsService.updateStartDateForMedia(sessionStartDate, selectedLesson.getActualId(), selectedLesson.getTopicId(), selectedCourse.getCourseId());
            myVideoView.setOnCompletionListener(mp -> {
                lmsService.markLessonCompleted(selectedLesson.getActualId(), selectedLesson.getTopicId(), courseId);
                lmsService.updateLastPausedOnForMedia(null, selectedLesson.getActualId(), selectedLesson.getTopicId(), courseId);
                lmsService.updateEndDateForMedia(new Date(), selectedLesson.getActualId(), selectedLesson.getTopicId(), courseId);
                lmsService.addSessionsForMedia(sessionStartDate, new Date(), selectedLesson.getActualId(), selectedLesson.getTopicId(), courseId);
                if (isFullScreen) {
                    myVideoView.setFullscreen(false);
                }
                currentPosition = 0;
                sessionStartDate = null;
                askForFeedback();
            });
        } else {
            SewaUtil.generateToast(mContext, UtilBean.getMyLabel("File not found. Please refresh and try again."));
        }
    }

    private void setCaseStudyQuizInVideo() {
        if (!caseStudyTimerList.isEmpty()) {
            for (Timer timer : caseStudyTimerList) {
                timer.cancel();
            }
            caseStudyTimerList = new ArrayList<>();
        }
        if (selectedLesson.getQuestionSet() != null && !selectedLesson.getQuestionSet().isEmpty()) {
            for (final LmsQuestionSetDataBean questionSet : selectedLesson.getQuestionSet()) {
                if (questionSet.getQuizAtSecond() != null) {
                    final Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (myVideoView != null && myVideoView.isPlaying() && myVideoView.getCurrentPosition() / 1000 == questionSet.getQuizAtSecond()) {
                                myVideoView.pause();
                                positionForTranscript = myVideoView.getCurrentPosition();
                                Intent intent;
                                if (selectedCourse.getTestConfigJson() != null
                                        && Boolean.TRUE.equals(selectedCourse.getTestConfigJson().get(questionSet.getQuestionSetType()).getProvideInteractiveFeedbackAfterEachQuestion())) {
                                    intent = new Intent(mContext, LmsInteractiveQuestionModuleActivity_.class);
                                } else if (selectedCourse != null && selectedCourse.getTestConfigJson() != null
                                        && questionSet.getQuestionSetType() == 2640) { //todo make id value dynamic as per field value name
                                    intent = new Intent(context, LmsSimulationActivity_.class);
                                } else {
                                    intent = new Intent(mContext, LmsQuestionModuleActivity_.class);
                                }
                                intent.putExtra("questionSet", new Gson().toJson(questionSet));
                                intent.putExtra("testFor", selectedLesson.getTitle());
                                startActivityForResult(intent, ActivityConstants.LMS_CASE_STUDY_QUIZ_ACTIVITY_REQUEST_CODE);

                                timer.cancel();
                            }
                        }
                    }, 1000, 1000);
                    caseStudyTimerList.add(timer);
                }
            }
        }
    }


    private void askForFeedback() {
        if (Boolean.TRUE.equals(selectedLesson.getUserFeedbackRequired())) {
            final LmsViewedMediaBean viewedMediaBean = lmsService.getViewedLessonById(selectedLesson.getActualId());
            if (viewedMediaBean == null || !Boolean.TRUE.equals(viewedMediaBean.getCompleted()) || viewedMediaBean.getUserFeedback() != null) {
                return;
            }
            feedbackAlert = new LMSFeedbackComponent(mContext, lmsService, viewedMediaBean);
            feedbackAlert.show();
        }
    }

    private void setRightLayoutInVideo() {
        videoRightButton.setImageResource(R.drawable.ic_arrow_left);
        isRightLayoutShown = false;
        videoRightButton.setVisibility(View.VISIBLE);

        videoRightButton.setOnClickListener(v -> {
            if (isRightLayoutShown) {
                isRightLayoutShown = false;
                videoRightButton.setImageResource(R.drawable.ic_arrow_left);
                videoRightLayout.setVisibility(View.GONE);
                if (!myVideoView.isPlaying()) {
                    myVideoView.start();
                }
            } else {
                isRightLayoutShown = true;
                videoRightButton.setImageResource(R.drawable.ic_arrow_right);
                videoRightLayout.setVisibility(View.VISIBLE);
                if (myVideoView.isPlaying()) {
                    myVideoView.pause();
                }
            }
        });

        bookmarksListLayout = MyStaticComponents.getLinearLayout(mContext, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        bookmarksListLayout.addView(MyStaticComponents.generateLabelView(mContext, "Please select a bookmark"));
        bookmarksListLayout.addView(generateBookmarkList(selectedLesson.getActualId()));

        videoRightLayout.removeAllViews();
        videoRightLayout.addView(bookmarksListLayout, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.85f));
        videoRightLayout.addView(setBookMarkButton(), new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.15f));
    }

    private PagingListView generateBookmarkList(Integer lessonId) {
        final List<LmsBookMarkBean> bookmarkDataBeans = lmsService.retrieveBookmarks(lessonId);
        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            if (bookmarkDataBeans != null && position < bookmarkDataBeans.size()) {
                LmsBookMarkBean bookmarkDataBean = bookmarkDataBeans.get(position);
                myVideoView.seekTo(bookmarkDataBean.getPosition());
            }
            myVideoView.start();
            videoRightButton.callOnClick();
        };
        LmsBookmarksAdaptor adaptor = new LmsBookmarksAdaptor(mContext, bookmarkDataBeans, lmsService, myVideoView);

        PagingListView listView = new PagingListView(mContext);
        listView.setPadding(10, 0, 10, 0);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        listView.setNestedScrollingEnabled(true);
        listView.setAdapter(adaptor);
        listView.setOnItemClickListener(onItemClickListener);

        return listView;
    }

    private LinearLayout setBookMarkButton() {
        LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        l1.setMargins(20, 20, 20, 0);

        LinearLayout bookmarkButtonLayout = MyStaticComponents.getLinearLayout(mContext, -1, LinearLayout.HORIZONTAL, l1);
        MaterialButton button = MyStaticComponents.getCustomButton(mContext, UtilBean.getMyLabel(LabelConstants.ADD_BOOKMARK), -1, l1);

        button.setOnClickListener(v -> {
            myVideoView.pause();
            currentPosition = myVideoView.getCurrentPosition();
            showPopUpWindow(v);
        });
        bookmarkButtonLayout.addView(button);
        return bookmarkButtonLayout;
    }

    private void showPopUpWindow(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final View inflate = LayoutInflater.from(mContext).inflate(R.layout.text_popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(inflate, displayMetrics.widthPixels * 7 / 10, MATCH_PARENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button addBookmark = inflate.findViewById(R.id.popup_button);
        final TextInputLayout note = inflate.findViewById(R.id.popup_note);

        addBookmark.setOnClickListener(v -> {
            if (!Objects.requireNonNull(note.getEditText()).getText().toString().trim().isEmpty() || fileName != null) {
                BookmarkDataBean bookmark = new BookmarkDataBean(currentPosition, null, note.getEditText().getText().toString().trim(), null);
                if (fileName != null) {
                    bookmark.setFileName(fileName);
                }
                lmsService.saveBookmark(selectedLesson.getActualId(), bookmark);
                bookmarksListLayout.removeAllViews();
                bookmarksListLayout.addView(MyStaticComponents.generateLabelView(mContext, "Please select a bookmark"));
                bookmarksListLayout.addView(generateBookmarkList(selectedLesson.getActualId()));
                fileName = null;
                popupWindow.dismiss();
            } else {
                SewaUtil.generateToast(mContext, "Please enter bookmark note");
            }
        });

        final ImageButton micButton = inflate.findViewById(R.id.mic_button);
        micButton.setOnClickListener(v -> {
            if (!isAudioRecording) {
                try {
                    isAudioRecording = true;
                    if (recorder == null) {
                        recorder = new MediaRecorder();
                    }
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    fileName = "Record" + timeStamp + ".amr";
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    recorder.setOutputFile(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_BOOKMARK) + fileName);
                    recorder.prepare();
                    recorder.start();
                    micButton.setImageResource(R.drawable.stop_audio);
                    SewaUtil.generateToast(mContext, "Recording started");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
                micButton.setImageResource(R.drawable.record_audio);
                SewaUtil.generateToast(mContext, "Audio Recorded successfully");
                isAudioRecording = false;
            }
        });
    }


    private void setVideoCallback(MyVideoView myVideoView) {
        myVideoView.setVideoViewCallback(new MyVideoView.VideoViewCallback() {
            final int cachedHeight = lessonLayout.getLayoutParams().height;
            ActionBar actionBar;

            @Override
            public void onScaleChange(boolean isFullscreen) {
                isFullScreen = isFullscreen;
                if (isFullscreen) {
                    ViewGroup.LayoutParams layoutParams = lessonLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    lessonLayout.setLayoutParams(layoutParams);
                    //GONE the unconcerned views to leave room for video and controller
                    viewPager.setVisibility(View.GONE);
                    actionBar = ((LmsCourseDetailActivity) mContext).getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.hide();
                    }
                    //For Right layout in video
                    setRightLayoutInVideo();
                } else {
                    ViewGroup.LayoutParams layoutParams = lessonLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = this.cachedHeight;
                    lessonLayout.setLayoutParams(layoutParams);
                    viewPager.setVisibility(View.VISIBLE);
                    actionBar = ((LmsCourseDetailActivity) mContext).getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.show();
                    }

                    //For Right layout in video
                    videoRightButton.setVisibility(View.GONE);
                    videoRightLayout.setVisibility(View.GONE);
                    bookmarksFragment.initView();
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                Log.d(TAG, "onPause MyVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                Log.d(TAG, "onStart MyVideoView callback");
                if (sessionStartDate == null) {
                    sessionStartDate = new Date();
                }
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d(TAG, "onBufferingStart MyVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d(TAG, "onBufferingEnd MyVideoView callback");
            }

        });
    }

    private void addAudioLesson(String path, LmsLessonDataBean lesson) {
        if (myAudioComponent == null) {
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(path));
            audioMediaPlayer = MediaPlayer.create(this, uri);
            myAudioComponent = new MyAudioComponent(this, lesson.getTitle(), audioMediaPlayer, true, lmsService, lesson, selectedCourse.getCourseId());
        }
        myAudioComponent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myAudioComponent.setBackgroundResource(R.drawable.lms_my_courses);
        lessonLayout.addView(myAudioComponent);
        myAudioComponent.play();
        sessionStartDate = new Date();
    }

    private void addImageLesson(String path, LmsLessonDataBean lesson) {
        Intent intent = new Intent(this, CustomImageViewerActivity_.class);
        intent.putExtra("path", path);
        intent.putExtra("fileName", lesson.getTitle());
        intent.putExtra("fromLms", true);
        intent.putExtra("courseId", selectedCourse.getCourseId());
        intent.putExtra("lesson", new Gson().toJson(lesson));
        startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
    }

    private void addPdfLesson(String path, LmsLessonDataBean lesson) {
        Intent intent = new Intent(mContext, CustomPDFViewerActivity_.class);
        intent.putExtra("path", path);
        intent.putExtra("fileName", lesson.getDescription());
        intent.putExtra("fromLms", true);
        intent.putExtra("courseId", selectedCourse.getCourseId());
        intent.putExtra("lesson", new Gson().toJson(lesson));
        startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.LMS_ACTIVITY_REQUEST_CODE) {
            selectedLesson = null;
        } else if (requestCode == ActivityConstants.LMS_TRANSCRIPT_ACTIVITY_REQUEST_CODE && myVideoView != null && !myVideoView.isPlaying()) {
            myVideoView.seekTo(positionForTranscript);
            myVideoView.start();
        } else if (requestCode == ActivityConstants.LMS_CASE_STUDY_QUIZ_ACTIVITY_REQUEST_CODE && myVideoView != null && !myVideoView.isPlaying()) {
            myVideoView.seekTo(positionForTranscript);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.selectedLesson != null && this.selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_AUDIO)) {
            currentPosition = audioMediaPlayer.getCurrentPosition();
            audioMediaPlayer.pause();
            myAudioComponent.setPlayIcon();
        } else if (this.selectedLesson != null && this.selectedLesson.getMediaType().equals(LmsConstants.MEDIA_TYPE_VIDEO)) {
            positionForTranscript = myVideoView.getCurrentPosition();
            myVideoView.pause();
        }
    }
}
