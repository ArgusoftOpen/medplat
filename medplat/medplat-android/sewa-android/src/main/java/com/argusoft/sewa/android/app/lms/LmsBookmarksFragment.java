package com.argusoft.sewa.android.app.lms;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.databean.BookmarkDataBean;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.model.LmsBookMarkBean;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LmsBookmarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LmsBookmarksFragment extends Fragment {

    private LmsCourseDetailActivity activity;
    private LinearLayout bodyLayoutContainer;
    private FloatingActionButton fab;

    private LmsCourseDataBean selectedCourse;
    private LmsTopicDataBean selectedModule;
    private LmsLessonDataBean selectedLesson;

    private MyVideoView myVideoView;
    private Integer currentPosition;
    private View bookmarksView;
    private MediaRecorder recorder;

    private boolean isAudioRecording = false;

    private String fileName;

    public LmsBookmarksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LmsBookmarksFragment.
     */
    public static LmsBookmarksFragment newInstance() {
        return new LmsBookmarksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainLayout = inflater.inflate(R.layout.fragment_lms_bookmarks, container, false);
        bodyLayoutContainer = mainLayout.findViewById(R.id.bodyLayoutContainer);
        fab = mainLayout.findViewById(R.id.add_bookmark);
        initView();
        return mainLayout;
    }

    public void initView() {
        if (getView() == null) {
            return;
        }
        if (bodyLayoutContainer == null) {
            bodyLayoutContainer = getView().findViewById(R.id.bodyLayoutContainer);
        }
        if (fab == null) {
            fab = getView().findViewById(R.id.add_bookmark);
        }
        activity = (LmsCourseDetailActivity) getActivity();

        bodyLayoutContainer.removeAllViews();
        fab.setVisibility(View.GONE);
        selectedLesson = activity.getSelectedLesson();
        if (selectedLesson != null && selectedLesson.getMediaType().equals("VIDEO")) {
            bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(getContext(), selectedLesson.getTitle()));
            myVideoView = activity.getVideoView();
            bodyLayoutContainer.addView(generateBookmarkList(selectedLesson.getActualId()));
            setBookMarkButton();
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(getContext(), "No bookmarks available for the selected lesson"));
        }
    }

    private View generateBookmarkList(final Integer lessonId) {
        final List<LmsBookMarkBean> bookmarkDataBeans = activity.lmsService.retrieveBookmarks(lessonId);
        if (bookmarkDataBeans != null && !bookmarkDataBeans.isEmpty()) {
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
                LmsBookMarkBean bookmarkDataBean = bookmarkDataBeans.get(position);
                myVideoView.seekTo(bookmarkDataBean.getPosition());
                myVideoView.start();
            };

            LmsBookmarksAdaptor adaptor = new LmsBookmarksAdaptor(getContext(), bookmarkDataBeans, activity.lmsService, myVideoView);

            ListView listView = new ListView(getContext());
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            listView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            listView.setNestedScrollingEnabled(true);
            listView.setAdapter(adaptor);
            listView.setOnItemClickListener(onItemClickListener);

            bookmarksView = listView;
        } else {
            bookmarksView = MyStaticComponents.generateLabelView(getContext(), "No bookmarks has been added yet");
        }
        return bookmarksView;
    }

    private void setBookMarkButton() {
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(v -> {
            currentPosition = activity.pauseMediaPlayerAndGetPosition();
            if (currentPosition != -1) {
                showPopUpWindow(v);
            }
        });
    }

    private void showPopUpWindow(View view) {
        final View inflate = LayoutInflater.from(getContext()).inflate(R.layout.text_popup_window, null);
        boolean focusable = true;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final PopupWindow popupWindow = new PopupWindow(inflate, displayMetrics.widthPixels * 8 / 10, ViewGroup.LayoutParams.MATCH_PARENT, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button addBookmark = inflate.findViewById(R.id.popup_button);
        final TextInputLayout note = inflate.findViewById(R.id.popup_note);
        addBookmark.setOnClickListener(v -> {
            if (!Objects.requireNonNull(note.getEditText()).getText().toString().trim().isEmpty()) {
                BookmarkDataBean bookmark = new BookmarkDataBean(currentPosition, null, note.getEditText().getText().toString().trim(), null);
                if (fileName != null) {
                    bookmark.setFileName(fileName);
                }
                activity.lmsService.saveBookmark(selectedLesson.getActualId(), bookmark);
                bodyLayoutContainer.removeView(bookmarksView);
                bodyLayoutContainer.addView(generateBookmarkList(selectedLesson.getActualId()));
                fileName = null;
                popupWindow.dismiss();
            } else {
                SewaUtil.generateToast(getContext(), "Please enter bookmark note");
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
                    recorder.setOutputFile(SewaConstants.getDirectoryPath(getContext(), SewaConstants.DIR_BOOKMARK) + fileName);
                    recorder.prepare();
                    recorder.start();
                    micButton.setImageResource(R.drawable.stop_audio);
                    SewaUtil.generateToast(getContext(), "Recording started");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
                micButton.setImageResource(R.drawable.record_audio);
                SewaUtil.generateToast(getContext(), "Audio Recorded successfully");
                isAudioRecording = false;
            }
        });
    }
}