package com.argusoft.sewa.android.app.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.LMSFeedbackComponent;
import com.argusoft.sewa.android.app.component.MyAudioPlayer;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.BookmarkDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LmsBookMarkBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.treeview.holder.TreeHolder;
import com.argusoft.sewa.android.app.treeview.model.TreeNode;
import com.argusoft.sewa.android.app.treeview.view.AndroidTreeView;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.MyComparatorUtil;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author alpeshkyada
 */
@EActivity
public class LMSVideoPlayerActivity extends MenuActivity implements
        OnCompletionListener,
        SurfaceHolder.Callback, MediaController.MediaPlayerControl {

    @Bean
    LmsServiceImpl lmsService;
    @Bean
    SewaFhsServiceImpl fhsService;

    private int currentPosition;
    private static final String TAG = "LMSVideoPlayer";

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private PagingListView listView;
    private LinearLayout parentLayout;
    private LinearLayout parentLayout1;
    private LinearLayout linearLayout1;
    private LinearLayout innerLayout;
    private int height;
    private int width;
    private LinearLayout bookMarkButtonLayout;
    private FrameLayout frameLayout;
    private boolean openToggle = false;
    private List<LmsBookMarkBean> bookmarkDataBeans = new LinkedList<>();
    private TextInputLayout note;
    private boolean isAudioRecording = false;
    private MediaRecorder recorder;
    private String fileName;
    private View tabBar;
    private MaterialTextView descView;
    private View openVideoButton;
    private View openPdfButton;
    private View openImageButton;
    private View openAudioButton;

    private Integer courseId;
    private LmsLessonDataBean selectedLesson;
    private LmsLessonDataBean selectedVideo;

    private Date sessionStartDate;

    private LMSFeedbackComponent feedbackAlert;

    public int getAudioSessionId() {
        return 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // keep screen active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        parentLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        frameLayout = new FrameLayout(context);
        getDataFromBundle();
        setContentView(parentLayout);
    }

    private void getDataFromBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            courseId = extras.getInt("courseId");
            setDataSourcesForVideo(new Gson().fromJson(extras.getString("lesson"), LmsLessonDataBean.class));
        }
    }

    private void setDataSourcesForVideo(LmsLessonDataBean media) {
        this.selectedVideo = media;
        setVideoLayout();
    }

    private void setVideoLayout() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        if (playVideoSequentially()) {
            mediaPlayer.setScreenOnWhilePlaying(true);
            surfaceView = new SurfaceView(this);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

            frameLayout.addView(surfaceView);
            frameLayout.addView(createToggle());
            parentLayout.addView(frameLayout);
            sessionStartDate = new Date();
            lmsService.updateStartDateForMedia(sessionStartDate, selectedVideo.getActualId(), selectedVideo.getTopicId(), courseId);
        } else {
            MaterialTextView msg = MyStaticComponents.generateTitleView(this, LabelConstants.NO_VIDEO_TO_PLAY);
            parentLayout.addView(msg);
        }
    }

    private View createToggle() {
        final ImageView imageButton = new ImageView(context);
        imageButton.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(70, 70);
        layoutParams.setMargins(0, 0, 30, 0);
        layoutParams.gravity = Gravity.END | Gravity.CENTER;
        if (openToggle) {
            imageButton.setImageResource(R.drawable.ic_arrow_right);
        } else {
            imageButton.setImageResource(R.drawable.ic_arrow_left);
        }
        imageButton.setLayoutParams(layoutParams);
        tabBar = createTabBar();
        imageButton.setOnClickListener(v -> {
            openToggle = !openToggle;
            if (openToggle) {
                imageButton.setImageResource(R.drawable.ic_arrow_right);
                mediaPlayer.pause();
                currentPosition = mediaPlayer.getCurrentPosition();
                parentLayout.removeAllViews();

                linearLayout1 = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                parentLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(width * 70 / 100, height));
                parentLayout.addView(frameLayout);
                parentLayout1 = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(width * 30 / 100, height, 1f));
                innerLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                listView = generateBookmarkList(selectedVideo.getActualId());
                innerLayout.addView(listView);
                bookMarkButtonLayout = setBookMarkButton();

                parentLayout1.addView(tabBar, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.1f));
                parentLayout1.addView(innerLayout, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.75f));
                parentLayout1.addView(bookMarkButtonLayout, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.15f));

                linearLayout1.addView(parentLayout);
                linearLayout1.addView(parentLayout1);
                setContentView(linearLayout1);
            } else {
                parentLayout.removeAllViews();
                frameLayout.removeAllViews();
                parentLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                setVideoLayout();
                imageButton.setImageResource(R.drawable.ic_arrow_left);
                setContentView(parentLayout);
            }
        });
        return imageButton;
    }

    private View createTabBar() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.bookmark_tabs, null);
        final View bookmarkButton = inflate.findViewById(R.id.bookmark_button);
        bookmarkButton.setSelected(true);
        final View iButton = inflate.findViewById(R.id.interaction_button);

        bookmarkButton.setOnClickListener(v -> {
            bookmarkButton.setSelected(true);
            iButton.setSelected(false);
            parentLayout1.removeAllViews();
            innerLayout.removeAllViews();

            listView = generateBookmarkList(selectedVideo.getActualId());
            innerLayout.addView(listView);

            parentLayout1.addView(tabBar, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.1f));
            parentLayout1.addView(innerLayout, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.75f));
            parentLayout1.addView(bookMarkButtonLayout, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.15f));
        });

        iButton.setOnClickListener(v -> {
            bookmarkButton.setSelected(false);
            iButton.setSelected(true);
            parentLayout1.removeAllViews();
            innerLayout.removeAllViews();

            View inflate1 = LayoutInflater.from(context).inflate(R.layout.video_description_layout, null);
            descView = inflate1.findViewById(R.id.video_desc);

            openVideoButton = inflate1.findViewById(R.id.open_video);
            openVideoButton.setOnClickListener(v14 -> openMediaByMediaType(LmsConstants.MEDIA_TYPE_VIDEO, false));
            openVideoButton.setVisibility(View.GONE);

            openPdfButton = inflate1.findViewById(R.id.open_pdf);
            openPdfButton.setOnClickListener(v13 -> openMediaByMediaType(LmsConstants.MEDIA_TYPE_PDF, !LmsConstants.MEDIA_TYPE_PDF.equals(selectedLesson.getMediaType())));
            openPdfButton.setVisibility(View.GONE);

            openImageButton = inflate1.findViewById(R.id.open_image);
            openImageButton.setOnClickListener(v12 -> openMediaByMediaType(LmsConstants.MEDIA_TYPE_IMAGE, false));
            openImageButton.setVisibility(View.GONE);

            openAudioButton = inflate1.findViewById(R.id.open_audio);
            openAudioButton.setOnClickListener(v1 -> openMediaByMediaType(LmsConstants.MEDIA_TYPE_AUDIO, false));
            openAudioButton.setVisibility(View.GONE);

            View treeView = generateTreeView();
            innerLayout.addView(treeView);

            parentLayout1.addView(tabBar, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.01f));
            parentLayout1.addView(innerLayout, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.06f));
            parentLayout1.addView(inflate1, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 0.03f));
        });

        return inflate;
    }

    @UiThread
    public void startPDFViewerActivity(String fileName, String desc, Boolean isTranscript) {
        String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + fileName;
        Intent intent = new Intent(context, CustomPDFViewerActivity_.class);
        intent.putExtra("path", path);
        intent.putExtra("fileName", desc);
        intent.putExtra("fromLms", true);
        intent.putExtra("courseId", courseId);
        intent.putExtra("transcript", isTranscript);
        intent.putExtra("lesson", new Gson().toJson(selectedLesson));
        startActivityForResult(intent, ActivityConstants.LMS_VIDEO_PLAYER_ACTIVITY_REQUEST_CODE);
    }

    private View generateTreeView() {
        final LmsCourseDataBean course = lmsService.retrieveCourseByCourseId(courseId);
        if (course == null) {
            MaterialTextView textView = MyStaticComponents.generateInstructionView(context, "No Lessons to show");
            textView.setPadding(20, 0, 0, 0);
            return textView;
        }

        //Root
        TreeNode root = TreeNode.root();
        AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);

        //Parent
        descView.setText(course.getCourseDescription());
        TreeHolder.IconTreeItem nodeItem = new TreeHolder.IconTreeItem(R.drawable.ic_arrow_right, course.getCourseName(), false);
        TreeNode parent = new TreeNode(nodeItem).setViewHolder(new TreeHolder(getApplicationContext(), true, TreeHolder.DEFAULT, TreeHolder.DEFAULT));
        root.addChild(parent);
        parent.setExpanded(true);

        parent.setClickListener((node, value) -> {
            descView.setText(course.getCourseDescription());
            openVideoButton.setVisibility(View.GONE);
            openPdfButton.setVisibility(View.GONE);
            openImageButton.setVisibility(View.GONE);
            openAudioButton.setVisibility(View.GONE);
        });

        //Adding Modules in Tree View
        List<LmsTopicDataBean> topics = course.getTopics();
        Collections.sort(topics, MyComparatorUtil.LMS_TOPIC_ORDER_COMPARATOR);
        for (final LmsTopicDataBean topic : topics) {
            TreeHolder.IconTreeItem childItem = new TreeHolder.IconTreeItem(R.drawable.ic_arrow_right, topic.getTopicName(), false);
            TreeNode child = new TreeNode(childItem).setViewHolder(new TreeHolder(getApplicationContext(), true, R.layout.child, 50));
            parent.addChildren(child);

            child.setClickListener((node, value) -> {
                descView.setText(topic.getTopicDescription());
                openVideoButton.setVisibility(View.GONE);
                openPdfButton.setVisibility(View.GONE);
                openImageButton.setVisibility(View.GONE);
                openAudioButton.setVisibility(View.GONE);
            });

            //Adding Lessons in Tree View
            List<LmsLessonDataBean> lessons = topic.getTopicMedias();
            Collections.sort(lessons, MyComparatorUtil.LMS_TOPIC_MEDIA_ORDER_COMPARATOR);
            List<Integer> viewedLessonList = lmsService.getViewedLessonList();
            for (final LmsLessonDataBean lesson : lessons) {
                TreeHolder.IconTreeItem subChildItem = new TreeHolder.IconTreeItem(getIconFromMediaType(lesson), lesson.getTitle(), viewedLessonList.contains(lesson.getActualId()));
                TreeNode subChild = new TreeNode(subChildItem).setViewHolder(new TreeHolder(getApplicationContext(), false, R.layout.child, 70));
                subChild.setClickListener((node, value) -> {
                    selectedLesson = lesson;
                    descView.setText(lesson.getDescription());
                    setOpenIconVisibilityByMediaType(lesson.getMediaType());
                });
                subChild.setLongClickListener((node, value) -> {
                    selectedLesson = lesson;
                    descView.setText(lesson.getDescription());
                    setOpenIconVisibilityByMediaType(lesson.getMediaType());
                    openMediaByMediaType(lesson.getMediaType(), false);
                    return true;
                });

                child.addChild(subChild);
                if (selectedVideo != null && selectedVideo.getActualId().equals(lesson.getActualId())) {
                    child.setExpanded(true);
                    subChild.getClickListener().onClick(child, null);
                    subChild.setSelected(true);
                }

                //Added Lesson Test in TreeView
                addLessonTestInTreeView(lesson, child);
            }

            //Added Module Test in TreeView
            addModuleTestInTreeView(topic, child);
        }

        //Added Course Test in TreeView
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

    private void setOpenIconVisibilityByMediaType(String mediaType) {
        openVideoButton.setVisibility(View.GONE);
        openPdfButton.setVisibility(View.GONE);
        openImageButton.setVisibility(View.GONE);
        openAudioButton.setVisibility(View.GONE);

        switch (mediaType) {
            case LmsConstants.MEDIA_TYPE_VIDEO:
                openVideoButton.setVisibility(View.VISIBLE);
                if (selectedLesson.getTranscriptFileId() != null) {
                    openPdfButton.setVisibility(View.VISIBLE);
                }
                break;
            case LmsConstants.MEDIA_TYPE_PDF:
                openPdfButton.setVisibility(View.VISIBLE);
                break;
            case LmsConstants.MEDIA_TYPE_IMAGE:
                openImageButton.setVisibility(View.VISIBLE);
                break;
            case LmsConstants.MEDIA_TYPE_AUDIO:
                openAudioButton.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    private void openMediaByMediaType(String mediaType, Boolean isTranscript) {
        if (Boolean.FALSE.equals(lmsService.getAllowedMediaMap(courseId).get(selectedLesson.getActualId()))) {
            SewaUtil.generateToast(context, LabelConstants.COMPLETE_PREVIOUS_LESSONS);
            return;
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }

        String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(selectedLesson.getMediaId(), selectedLesson.getMediaFileName());
        switch (mediaType) {
            case LmsConstants.MEDIA_TYPE_VIDEO:
                lmsService.updateLastPausedOnForMedia(currentPosition, selectedVideo.getActualId(), selectedVideo.getTopicId(), courseId);
                lmsService.addSessionsForMedia(sessionStartDate, new Date(), selectedVideo.getActualId(), selectedVideo.getTopicId(), courseId);
                parentLayout.removeAllViews();
                frameLayout.removeAllViews();
                currentPosition = 0;
                setDataSourcesForVideo(selectedLesson);
                break;
            case LmsConstants.MEDIA_TYPE_PDF:
                if (Boolean.TRUE.equals(isTranscript)) {
                    startPDFViewerActivity(UtilBean.getLMSFileName(selectedLesson.getTranscriptFileId(), selectedLesson.getTranscriptFileName()),
                            selectedLesson.getDescription(), true);
                } else {
                    startPDFViewerActivity(UtilBean.getLMSFileName(selectedLesson.getMediaId(), selectedLesson.getMediaFileName()),
                            selectedLesson.getDescription(), false);
                }
                break;
            case LmsConstants.MEDIA_TYPE_IMAGE:
                Intent intent = new Intent(context, CustomImageViewerActivity_.class);
                intent.putExtra("path", path);
                intent.putExtra("fileName", selectedLesson.getTitle());
                intent.putExtra("fromLms", true);
                intent.putExtra("courseId", courseId);
                intent.putExtra("lesson", new Gson().toJson(selectedLesson));
                startActivityForResult(intent, ActivityConstants.LMS_VIDEO_PLAYER_ACTIVITY_REQUEST_CODE);
                break;
            case LmsConstants.MEDIA_TYPE_AUDIO:
                Uri uri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".provider", new File(path));
                MediaPlayer player = MediaPlayer.create(context, uri);
                MyAudioPlayer myAudioPlayer = new MyAudioPlayer(context, selectedLesson.getTitle(), player, true, lmsService, selectedLesson, courseId);
                myAudioPlayer.show();
                myAudioPlayer.setOnCancelListener(dialog -> {
                    innerLayout.removeAllViews();
                    innerLayout.addView(generateTreeView());
                });
                break;
            default:
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
        List<Integer> viewedMediaList = lmsService.getViewedLessonList();
        boolean allLessonsCompleted = true;

        if (course != null) {
            for (LmsTopicDataBean topicDataBean : course.getTopics()) {
                for (LmsLessonDataBean mediaDataBean : topicDataBean.getTopicMedias()) {
                    if (!viewedMediaList.contains(mediaDataBean.getActualId())) {
                        allLessonsCompleted = false;
                        break;
                    }
                }
            }
        } else if (topic != null) {
            for (LmsLessonDataBean mediaDataBean : topic.getTopicMedias()) {
                if (!viewedMediaList.contains(mediaDataBean.getActualId())) {
                    allLessonsCompleted = false;
                    break;
                }
            }
        } else if (media != null) {
            allLessonsCompleted = viewedMediaList.contains(media.getActualId());
        }

        if (allLessonsCompleted) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            Intent intent = new Intent(context, LmsQuestionModuleActivity_.class);
            intent.putExtra("questionSet", new Gson().toJson(questionSet));
            intent.putExtra("testFor", testFor);
            startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
        } else {
            SewaUtil.generateToast(context, LabelConstants.COMPLETE_PREVIOUS_LESSONS);
        }
    }

    private PagingListView generateBookmarkList(Integer lessonId) {
        bookmarkDataBeans = lmsService.retrieveBookmarks(lessonId);
        List<ListItemDataBean> list = new ArrayList<>();
        if (bookmarkDataBeans != null && !bookmarkDataBeans.isEmpty()) {
            for (LmsBookMarkBean b : bookmarkDataBeans) {
                ListItemDataBean listItemDataBean = new ListItemDataBean();
                listItemDataBean.setPosition(b.getPosition());
                listItemDataBean.setCrossRefBookmark(false);

                //for text bookmark
                if (b.getFileName() == null) {
                    listItemDataBean.setAudio(false);
                    listItemDataBean.setBookmarkNote(b.getBookmarkNote());
                } else {
                    String file = b.getFileName();
                    listItemDataBean.setBookmarkText(file);
                    listItemDataBean.setAudio(true);
                    listItemDataBean.setBookmarkNote("Play");
                }
                if (!b.getBookmarkNote().isEmpty()) {
                    StringBuilder name = new StringBuilder();
                    String[] text = b.getBookmarkNote().split(" ");
                    for (int i = 0; i < text.length && i < 3; i++) {
                        name.append(text[i]).append(" ");
                    }
                    listItemDataBean.setBookmarkText(name.toString());
                }
                list.add(listItemDataBean);
            }
        }
        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            if (bookmarkDataBeans != null && position < bookmarkDataBeans.size()) {
                LmsBookMarkBean bookmarkDataBean = bookmarkDataBeans.get(position);
                if (bookmarkDataBean.getFileName() != null) {
                    mediaPlayer.pause();
                    String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_BOOKMARK) + bookmarkDataBean.getFileName();
                    Uri uri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".provider", new File(path));
                    MediaPlayer player = MediaPlayer.create(context, uri);
                    MyAudioPlayer myAudioPlayer = new MyAudioPlayer(context, bookmarkDataBean.getBookmarkText(), player);
                    myAudioPlayer.show();
                } else {
                    mediaPlayer.seekTo(bookmarkDataBean.getPosition());
                    mediaPlayer.start();
                }
            } else {
                mediaPlayer.start();
                mediaPlayer.seekTo(2000);
            }
        };

        listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_bookmark, onItemClickListener, null);

        listView.setPadding(10, 0, 10, 0);
        return listView;
    }

    private LinearLayout setBookMarkButton() {
        LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        l1.setMargins(20, 20, 20, 0);

        LinearLayout bookmarkButtonLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL, l1);
        MaterialButton button = MyStaticComponents.getCustomButton(context, UtilBean.getMyLabel(LabelConstants.ADD_BOOKMARK), -1, l1);

        button.setOnClickListener(v -> {
            mediaPlayer.pause();
            currentPosition = mediaPlayer.getCurrentPosition();
            showPopUpWindow(v);
        });
        bookmarkButtonLayout.addView(button);
        return bookmarkButtonLayout;
    }

    private void showPopUpWindow(View view) {
        final View inflate = LayoutInflater.from(context).inflate(R.layout.text_popup_window, null);
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(inflate, width * 7 / 10, MATCH_PARENT, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button addBookmark = inflate.findViewById(R.id.popup_button);
        note = inflate.findViewById(R.id.popup_note);
        addBookmark.setOnClickListener(v -> {
            if (!Objects.requireNonNull(note.getEditText()).getText().toString().trim().isEmpty() || fileName != null) {
                BookmarkDataBean bookmark = new BookmarkDataBean(currentPosition, null, note.getEditText().getText().toString().trim(), null);
                if (fileName != null) {
                    bookmark.setFileName(fileName);
                }
                lmsService.saveBookmark(selectedVideo.getActualId(), bookmark);
                listView = generateBookmarkList(selectedVideo.getActualId());
                innerLayout.removeAllViews();
                innerLayout.addView(listView);
                fileName = null;
                popupWindow.dismiss();
            } else {
                SewaUtil.generateToast(context, "Please enter bookmark note");
            }
        });

        final View micButton = inflate.findViewById(R.id.mic_button);
        micButton.setBackgroundResource(R.drawable.record_audio);
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
                    micButton.setBackgroundResource(R.drawable.stop_audio);
                    SewaUtil.generateToast(context, "Recording started");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
                micButton.setBackgroundResource(R.drawable.record_audio);
                SewaUtil.generateToast(context, "Audio Recorded successfully");
                isAudioRecording = false;
            }
        });
    }

    private boolean playVideoSequentially() {
        String filePath = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(selectedVideo.getMediaId(), selectedVideo.getMediaFileName());
        currentPosition = lmsService.getLastPausedOn(selectedVideo.getActualId());
        if (UtilBean.isFileExists(filePath)) {
            Log.i(TAG, filePath + " is found and ready to play");
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                if (currentPosition == mediaPlayer.getDuration()) {
                    mediaPlayer.seekTo(0);
                } else if (currentPosition > 0) {
                    mediaPlayer.seekTo(currentPosition);
                }
                return true;
            } catch (Exception e) {
                Log.e(TAG, null, e);
                currentPosition = 0;
                SewaUtil.generateToast(this, LabelConstants.VIDEO_FILE_CORRUPTED);
                mediaPlayer.reset();
                UtilBean.deleteFile(filePath);
            }
        } else {
            Log.i(TAG, "File not found");
            currentPosition = 0;
            SewaUtil.generateToast(this, LabelConstants.VIDEO_FILE_NOT_DOWNLOADED);
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG, "surfaceCreated Called");
        mediaPlayer.setDisplay(holder);
        try {
            MediaController controller = new MediaController(this);
            controller.setMediaPlayer(this);
            controller.setAnchorView(surfaceView);
            controller.bringToFront();
            controller.show();
            surfaceView.setOnTouchListener((new View.OnTouchListener() {
                private MediaController mc;

                public View.OnTouchListener setMediaController(MediaController mc) {
                    this.mc = mc;
                    return this;
                }

                @Override
                public boolean onTouch(View view, MotionEvent me) {
                    mc.show();
                    return true;
                }
            }).setMediaController(controller));
        } catch (Exception e) {
            Log.e(TAG, null, e);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.v(TAG, "surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "surfaceDestroyed Called");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v(TAG, "onCompletion Called");
        currentPosition = 0;
        lmsService.markLessonCompleted(selectedVideo.getActualId(), selectedVideo.getTopicId(), courseId);
        lmsService.updateLastPausedOnForMedia(null, selectedVideo.getActualId(), selectedVideo.getTopicId(), courseId);
        lmsService.updateEndDateForMedia(new Date(), selectedVideo.getActualId(), selectedVideo.getTopicId(), courseId);
        lmsService.addSessionsForMedia(sessionStartDate, new Date(), selectedVideo.getActualId(), selectedVideo.getTopicId(), courseId);
        askForFeedback();
    }

    @Override
    public void onBackPressed() {
        //******DO NOT DELETE THIS FUNCTION*****
        //User wont be able to stop the video in between
        currentPosition = mediaPlayer.getCurrentPosition();
        lmsService.updateLastPausedOnForMedia(currentPosition, selectedVideo.getActualId(), selectedVideo.getTopicId(), courseId);
        lmsService.addSessionsForMedia(sessionStartDate, new Date(), selectedVideo.getActualId(), selectedVideo.getTopicId(), courseId);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        askForFeedback();
    }

    @Override
    public void finish() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = null;
        currentPosition = 0;
        surfaceHolder = null;
        surfaceView = null;
        super.finish();
    }

    @Override
    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    @Override
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int i) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(i);
        }
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 20;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.LMS_VIDEO_PLAYER_ACTIVITY_REQUEST_CODE) {
            View treeView = generateTreeView();
            innerLayout.removeAllViews();
            innerLayout.addView(treeView);
        }
    }

    private void askForFeedback() {
        if (Boolean.TRUE.equals(selectedVideo.getUserFeedbackRequired())) {
            final LmsViewedMediaBean viewedMediaBean = lmsService.getViewedLessonById(selectedVideo.getActualId());
            if (viewedMediaBean == null || !Boolean.TRUE.equals(viewedMediaBean.getCompleted()) || viewedMediaBean.getUserFeedback() != null) {
                finish();
                return;
            }
            feedbackAlert = new LMSFeedbackComponent(context, lmsService, viewedMediaBean);
            feedbackAlert.show();
        } else {
            finish();
        }
    }
}
