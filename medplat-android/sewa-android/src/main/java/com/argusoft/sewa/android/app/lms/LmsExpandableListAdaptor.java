package com.argusoft.sewa.android.app.lms;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.activity.CustomPDFViewerActivity_;
import com.argusoft.sewa.android.app.activity.LmsSimulationActivity_;
import com.argusoft.sewa.android.app.activity.LmsInteractiveQuestionModuleActivity_;
import com.argusoft.sewa.android.app.activity.LmsQuestionModuleActivity_;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.core.LmsService;
import com.argusoft.sewa.android.app.core.SewaFhsService;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsExpandableListDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LmsExpandableListAdaptor extends BaseExpandableListAdapter {

    private final Context context;
    private final LmsCourseDataBean course;
    private final SewaFhsService fhsService;
    private final LmsService lmsService;
    private List<Integer> viewedLessons;
    private List<LmsExpandableListDataBean> listDataBeans;
    private PopupWindow popupWindow;

    public LmsExpandableListAdaptor(Context context, LmsCourseDataBean course,
                                    List<Integer> viewedLessons,
                                    SewaFhsService fhsService, LmsService lmsService,
                                    List<LmsExpandableListDataBean> listDataBeans) {
        this.context = context;
        this.course = course;
        this.viewedLessons = viewedLessons;
        this.fhsService = fhsService;
        this.lmsService = lmsService;
        this.listDataBeans = listDataBeans;

    }

    public void setViewedLesson(List<Integer> viewedLessons) {
        this.viewedLessons = viewedLessons;
    }

    @Override
    public LmsLessonDataBean getChild(int listPosition, int expandedListPosition) {
        if (listDataBeans.get(listPosition).getModule() != null) {
            return this.listDataBeans.get(listPosition).getModule().getTopicMedias().get(expandedListPosition);
        } else {
            return null;
        }

    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lms_expandable_list_child_view, null);
        }

        final LmsLessonDataBean child = getChild(listPosition, expandedListPosition);

        TextView textView = convertView.findViewById(R.id.child_text);
        textView.setText(child.getTitle());

        ImageView imageView = convertView.findViewById(R.id.lesson_image);
        TextView lessonType = convertView.findViewById(R.id.lesson_type);

        switch (child.getMediaType()) {
            case LmsConstants.MEDIA_TYPE_VIDEO:
                imageView.setImageResource(R.drawable.ic_video);
                lessonType.setText(UtilBean.getMyLabel("Video"));
                break;
            case LmsConstants.MEDIA_TYPE_AUDIO:
                imageView.setImageResource(R.drawable.ic_audio);
                lessonType.setText(UtilBean.getMyLabel("Audio"));
                break;
            case LmsConstants.MEDIA_TYPE_IMAGE:
                imageView.setImageResource(R.drawable.ic_image);
                lessonType.setText(UtilBean.getMyLabel("Image"));
                break;
            case LmsConstants.MEDIA_TYPE_PDF:
                imageView.setImageResource(R.drawable.ic_pdf);
                lessonType.setText(UtilBean.getMyLabel("PDF"));
                break;
            default:
                imageView.setImageResource(R.drawable.ic_unsupported_file);
                lessonType.setText(UtilBean.getMyLabel("Unsupported"));
                break;
        }

        TextView watched = convertView.findViewById(R.id.lesson_watched);
        LmsViewedMediaBean viewedMediaBean = lmsService.getViewedLessonById(child.getActualId());
        if (viewedMediaBean != null && (viewedMediaBean.getCompleted() != null && viewedMediaBean.getCompleted())) {
            String watchedText = UtilBean.getMyLabel("Watched");
            watched.setText(watchedText);
            watched.setTextColor(ContextCompat.getColor(context, R.color.listTextColor));
            watched.setVisibility(View.VISIBLE);
        } else {
            watched.setVisibility(View.GONE);
        }

        ImageButton transcriptButton = convertView.findViewById(R.id.transcript_button);
        transcriptButton.setFocusable(false);

        if (child.getTranscriptFileId() != null) {
            final String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(child.getTranscriptFileId(), child.getTranscriptFileName());
            if (UtilBean.isFileExists(path)) {
                transcriptButton.setVisibility(View.VISIBLE);
                transcriptButton.setOnClickListener(v -> {
                    LmsLessonDataBean selectedLesson = ((LmsCourseDetailActivity) context).getSelectedLesson();
                    if (selectedLesson != null && selectedLesson.getActualId().equals(child.getActualId())) {
                        Intent intent = new Intent(context, CustomPDFViewerActivity_.class);
                        intent.putExtra("path", path);
                        intent.putExtra("fileName", child.getDescription());
                        intent.putExtra("fromLms", true);
                        intent.putExtra("courseId", course.getCourseId());
                        intent.putExtra("transcript", true);
                        intent.putExtra("lesson", new Gson().toJson(child));
                        LmsCourseDetailActivity activity = (LmsCourseDetailActivity) LmsExpandableListAdaptor.this.context;
                        MyVideoView videoView = activity.getVideoView();
                        if (videoView != null && videoView.isPlaying()) {
                            videoView.pause();
                            activity.setPositionForTranscript(videoView.getCurrentPosition());
                        }
                        activity.startActivityForResult(intent, ActivityConstants.LMS_TRANSCRIPT_ACTIVITY_REQUEST_CODE);
                    } else {
                        SewaUtil.generateToast(context, "Please select the lesson first");
                    }
                });
            } else {
                transcriptButton.setVisibility(View.GONE);
            }
        } else {
            transcriptButton.setVisibility(View.GONE);
        }

        String pathMedia = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(child.getMediaId(), child.getMediaFileName());
        if (!UtilBean.isFileExists(pathMedia)) {
            watched.setText("Media not available");
            watched.setTextColor(ContextCompat.getColor(context, R.color.pregnantWomenTextColor));
            watched.setVisibility(View.VISIBLE);
        }

        LinearLayout llQuiz = convertView.findViewById(R.id.llQuiz);
        ImageButton quizButton = convertView.findViewById(R.id.test_button);
        quizButton.setFocusable(false);

        if (child.getQuestionSet() != null && !child.getQuestionSet().isEmpty()) {

           /* if (lmsService.isQuizCompleted(course.getCourseId(), child.getTopicId(), child.getActualId())) {
                quizButton.setImageResource(R.drawable.test_completed);
            } else {
                quizButton.setImageResource(R.drawable.test);
            }*/
            llQuiz.setVisibility(View.VISIBLE);
            final View finalConvertView = convertView;
            quizButton.setOnClickListener(v -> openLmsTestModulePopup(finalConvertView, null, child, child.getQuestionSet(), child.getTitle()));
        } else {
            llQuiz.setVisibility(View.GONE);
        }

        addQuizListViews(llQuiz, null, child, child.getQuestionSet(), child.getTitle());

        View groupDivider = convertView.findViewById(R.id.group_divider);

        if (getChildrenCount(listPosition) - 1 == expandedListPosition && getGroupCount() - 1 != listPosition) {
            groupDivider.setVisibility(View.VISIBLE);
        } else {
            groupDivider.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void addQuizListViews(LinearLayout llQuiz, final LmsTopicDataBean topic, final LmsLessonDataBean media, final List<LmsQuestionSetDataBean> questionSets, final String testFor) {

        Map<Integer, List<LmsQuestionSetDataBean>> questionSetMap = new HashMap<>();

        for (LmsQuestionSetDataBean questionSetDataBean : questionSets) {
            if (questionSetMap.containsKey(questionSetDataBean.getQuestionSetType())) {
                List<LmsQuestionSetDataBean> lmsQuestionSetDataBeans = questionSetMap.get(questionSetDataBean.getQuestionSetType());
                if (lmsQuestionSetDataBeans == null || lmsQuestionSetDataBeans.isEmpty()) {
                    lmsQuestionSetDataBeans = new ArrayList<>();
                }
                lmsQuestionSetDataBeans.add(questionSetDataBean);
                questionSetMap.put(questionSetDataBean.getQuestionSetType(), lmsQuestionSetDataBeans);
            } else {
                List<LmsQuestionSetDataBean> list = new ArrayList<>();
                list.add(questionSetDataBean);
                questionSetMap.put(questionSetDataBean.getQuestionSetType(), list);
            }
        }

        if (questionSets.size() > 0) {
            List<LmsQuestionSetDataBean> finalListOfQuestionSets = new ArrayList<>();
            for (Map.Entry<Integer, List<LmsQuestionSetDataBean>> entry : questionSetMap.entrySet()) {
                if (Boolean.TRUE.equals(course.getTestConfigJson().get(entry.getKey()).getIsCaseStudyQuestionSetType())) {
                    finalListOfQuestionSets.addAll(entry.getValue());
                } else {
                    List<LmsQuestionSetDataBean> value = entry.getValue();
                    Collections.shuffle(value);
                    finalListOfQuestionSets.add(value.get(0));
                }
            }
            LinearLayout mainLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
                    new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.CENTER));

            for (final LmsQuestionSetDataBean questionSet : finalListOfQuestionSets) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.lms_expandable_list_child_view, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                inflate.setLayoutParams(params);
                inflate.setPadding(0, 0, 0, 0);

                ImageView imageButton = inflate.findViewById(R.id.circle_view);
                if (topic != null) {
                    if (lmsService.isQuizMinimumMarks(course.getCourseId(), topic.getTopicId(), null, questionSet.getQuestionSetType())) {
                        imageButton.setImageResource(R.drawable.test_completed);
                    } else {
                        imageButton.setImageResource(R.drawable.test);
                    }
                } else if (media != null) {
                    if (lmsService.isQuizMinimumMarks(course.getCourseId(), media.getTopicId(), media.getActualId(), questionSet.getQuestionSetType())) {
                        imageButton.setImageResource(R.drawable.test_completed);
                    } else {
                        imageButton.setImageResource(R.drawable.test);
                    }
                }

                imageButton.setVisibility(View.VISIBLE);
                imageButton.setPadding(0, 0, 0, 0);
                imageButton.setImageTintList(null);

                TextView textView1 = inflate.findViewById(R.id.child_text);

                String text1;
                if (UtilBean.getMyLabel(fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString())) != null) {
                    text1 = questionSet.getQuestionSetName()
                            + " ("
                            + UtilBean.getMyLabel(fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString()))
                            + ")";
                } else {
                    text1 = questionSet.getQuestionSetName();
                }

                textView1.setText(text1);

                ImageView imageView1 = inflate.findViewById(R.id.lesson_image);
                imageView1.setImageResource(R.drawable.ic_quiz);

                String quizLabel = UtilBean.getMyLabel("Quiz");
                if (questionSet.getQuizAtSecond() != null) {
                    quizLabel = quizLabel + " (" + questionSet.getQuizAtSecond() + " seconds)";
                }
                TextView lessonType1 = inflate.findViewById(R.id.lesson_type);
                lessonType1.setText(quizLabel);

                ImageButton button = inflate.findViewById(R.id.test_button);
                button.setFocusable(false);
                button.setVisibility(View.GONE);

                View divider = inflate.findViewById(R.id.group_divider);
                divider.setVisibility(View.GONE);

                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openLmsTestModule(topic, media, questionSet, testFor);
                    }
                });

                mainLayout.addView(inflate);
            }
            llQuiz.removeAllViews();
            llQuiz.addView(mainLayout);
        }
    }

    private void openLmsTestModulePopup(View view, final LmsTopicDataBean topic, final LmsLessonDataBean media, final List<LmsQuestionSetDataBean> questionSets, final String testFor) {
        Map<Integer, List<LmsQuestionSetDataBean>> questionSetMap = new HashMap<>();

        for (LmsQuestionSetDataBean questionSetDataBean : questionSets) {
            if (questionSetMap.containsKey(questionSetDataBean.getQuestionSetType())) {
                List<LmsQuestionSetDataBean> lmsQuestionSetDataBeans = questionSetMap.get(questionSetDataBean.getQuestionSetType());
                if (lmsQuestionSetDataBeans == null || lmsQuestionSetDataBeans.isEmpty()) {
                    lmsQuestionSetDataBeans = new ArrayList<>();
                }
                lmsQuestionSetDataBeans.add(questionSetDataBean);
                questionSetMap.put(questionSetDataBean.getQuestionSetType(), lmsQuestionSetDataBeans);
            } else {
                List<LmsQuestionSetDataBean> list = new ArrayList<>();
                list.add(questionSetDataBean);
                questionSetMap.put(questionSetDataBean.getQuestionSetType(), list);
            }
        }

        List<LmsQuestionSetDataBean> finalListOfQuestionSets = new ArrayList<>();
        for (Map.Entry<Integer, List<LmsQuestionSetDataBean>> entry : questionSetMap.entrySet()) {
            if (Boolean.TRUE.equals(course.getTestConfigJson().get(entry.getKey()).getIsCaseStudyQuestionSetType())) {
                finalListOfQuestionSets.addAll(entry.getValue());
            } else {
                List<LmsQuestionSetDataBean> value = entry.getValue();
                Collections.shuffle(value);
                finalListOfQuestionSets.add(value.get(0));
            }
        }

        if (questionSets.size() > 1) {
            LinearLayout mainLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
                    new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.CENTER));

            mainLayout.setBackgroundResource(R.drawable.listview_button_selector);
            mainLayout.setPadding(20, 20, 20, 20);
            MaterialTextView testLabel = MyStaticComponents.generateLabelView(context, "Please select a quiz");
            testLabel.setPadding(20, 20, 20, 20);
            mainLayout.addView(testLabel);

            for (final LmsQuestionSetDataBean questionSet : finalListOfQuestionSets) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.lms_expandable_list_child_view, null);
                inflate.setBackgroundResource(R.drawable.unselected_listview_button);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                params.setMargins(15, 15, 15, 15);
                inflate.setLayoutParams(params);
                inflate.setPadding(0, 0, 0, 0);

                TextView textView = inflate.findViewById(R.id.child_text);

                String text;
                if (UtilBean.getMyLabel(fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString())) != null) {
                    text = questionSet.getQuestionSetName()
                            + " ("
                            + UtilBean.getMyLabel(fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString()))
                            + ")";
                } else {
                    text = questionSet.getQuestionSetName();
                }

                textView.setText(text);

                ImageView imageView = inflate.findViewById(R.id.lesson_image);
                imageView.setImageResource(R.drawable.ic_quiz);

                String quizLabel = UtilBean.getMyLabel("Quiz");
                if (questionSet.getQuizAtSecond() != null) {
                    quizLabel = quizLabel + " (" + questionSet.getQuizAtSecond() + " seconds)";
                }
                TextView lessonType = inflate.findViewById(R.id.lesson_type);
                lessonType.setText(quizLabel);

                ImageButton button = inflate.findViewById(R.id.test_button);
                button.setFocusable(false);
                button.setVisibility(View.GONE);

                View divider = inflate.findViewById(R.id.group_divider);
                divider.setVisibility(View.GONE);

                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openLmsTestModule(topic, media, questionSet, testFor);
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });

                mainLayout.addView(inflate);
            }

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((LmsCourseDetailActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            popupWindow = new PopupWindow(mainLayout, displayMetrics.widthPixels * 8 / 10, WRAP_CONTENT, true);
            popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
        } else {
            openLmsTestModule(topic, media, questionSets.get(0), testFor);
        }
    }

    private void openLmsTestModule(LmsTopicDataBean topic, LmsLessonDataBean media, LmsQuestionSetDataBean questionSet, String testFor) {
        boolean allLessonsCompleted = true;

        if (topic != null) {
            for (LmsLessonDataBean mediaDataBean : topic.getTopicMedias()) {
                if (!viewedLessons.contains(mediaDataBean.getActualId())) {
                    allLessonsCompleted = false;
                    break;
                }
            }
        } else if (media != null) {
            allLessonsCompleted = viewedLessons.contains(media.getActualId());
        }

        if (allLessonsCompleted) {
            Intent intent;
            if (course != null && course.getTestConfigJson() != null
                    && Boolean.TRUE.equals(course.getTestConfigJson().get(questionSet.getQuestionSetType()).getProvideInteractiveFeedbackAfterEachQuestion())) {
                intent = new Intent(context, LmsInteractiveQuestionModuleActivity_.class);
            } else if (course != null && course.getTestConfigJson() != null
                    && questionSet.getQuestionSetType() == 2640) { //todo make id value dynamic as per field value name
                intent = new Intent(context, LmsSimulationActivity_.class);
            } else {
                intent = new Intent(context, LmsQuestionModuleActivity_.class);
            }
            intent.putExtra("questionSet", new Gson().toJson(questionSet));
            intent.putExtra("testFor", testFor);
            ((AppCompatActivity) context).startActivityForResult(intent, ActivityConstants.LMS_ACTIVITY_REQUEST_CODE);
        } else {
            SewaUtil.generateToast(context, LabelConstants.COMPLETE_PREVIOUS_LESSONS);
        }
    }

    @Override
    public int getChildrenCount(int listPosition) {
        if (listDataBeans.get(listPosition).getGroupType() == LmsConstants.LMS_COURSE) {
            return 0;
        } else if (listDataBeans.get(listPosition).getGroupType() == LmsConstants.LMS_GROUP_QUIZ) {
            return listDataBeans.get(listPosition).getModule().getQuestionSet().size();
        } else {
            return this.listDataBeans.get(listPosition).getModule().getTopicMedias().size();
        }
    }

    @Override
    public LmsTopicDataBean getGroup(int listPosition) {
        return this.listDataBeans.get(listPosition).getModule();
    }

    @Override
    public int getGroupCount() {
        return this.listDataBeans.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (listDataBeans.get(listPosition).getGroupType() == LmsConstants.LMS_COURSE) {
            return getGroupViewOfQuiz(listDataBeans.get(listPosition).getQuestionSet());
        } else {
            return getGroupViewOfTopic(listDataBeans.get(listPosition).getModule(), listPosition, isExpanded);
        }
    }

    private View getGroupViewOfTopic(final LmsTopicDataBean group, int listPosition, boolean isExpanded) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.lms_expandable_list_group_view, null);

        LinearLayout llData = convertView.findViewById(R.id.llData);
        LinearLayout llQuiz = convertView.findViewById(R.id.llQuiz);
        ImageButton button = convertView.findViewById(R.id.group_test_button);
        button.setFocusable(false);

        if (listDataBeans.get(listPosition).getGroupType() == LmsConstants.LMS_GROUP_QUIZ) {
            /*if (lmsService.isQuizCompleted(course.getCourseId(), group.getTopicId(), null)) {
                button.setImageResource(R.drawable.test_completed);
            } else {
                button.setImageResource(R.drawable.test);
            }*/
            llQuiz.setVisibility(View.VISIBLE);
            llData.setVisibility(View.GONE);
            final View finalConvertView = convertView;
            button.setOnClickListener(v -> openLmsTestModulePopup(finalConvertView, group, null, group.getQuestionSet(), group.getTopicName()));
        } else {
            llQuiz.setVisibility(View.GONE);
            llData.setVisibility(View.VISIBLE);
            TextView listTitleTextView = convertView.findViewById(R.id.group_text);
            String text = (group.getTopicOrder()) + ". " + UtilBean.getMyLabel(group.getTopicName());
            listTitleTextView.setText(text);

            ImageView imageView = convertView.findViewById(R.id.group_image);
            if (isExpanded) {
                imageView.setImageResource(R.drawable.ic_arrow_up);
            } else {
                imageView.setImageResource(R.drawable.ic_arrow_down);
            }
        }
        addQuizListViews(llQuiz, group, null, group.getQuestionSet(), group.getTopicName());
        return convertView;
    }

    private View getGroupViewOfQuiz(LmsQuestionSetDataBean questionSet) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.lms_expandable_list_child_view, null);

        ImageView img = convertView.findViewById(R.id.circle_view);
        if (lmsService.isQuizMinimumMarks(course.getCourseId(), null, null, questionSet.getQuestionSetType())) {
            img.setImageResource(R.drawable.test_completed);
        } else {
            img.setImageResource(R.drawable.test);
        }
        img.setPadding(0, 0, 0, 0);
        img.setImageTintList(null);

        TextView textView = convertView.findViewById(R.id.child_text);
        String text;
        if (UtilBean.getMyLabel(fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString())) != null) {
            text = questionSet.getQuestionSetName()
                    + " ("
                    + UtilBean.getMyLabel(fhsService.getValueOfListValuesById(questionSet.getQuestionSetType().toString()))
                    + ")";
        } else {
            text = questionSet.getQuestionSetName();
        }
        textView.setText(text);

        ImageView imageView = convertView.findViewById(R.id.lesson_image);
        imageView.setImageResource(R.drawable.ic_quiz);

        TextView lessonType = convertView.findViewById(R.id.lesson_type);
        lessonType.setText(UtilBean.getMyLabel("Quiz"));

        ImageButton button = convertView.findViewById(R.id.test_button);
        button.setFocusable(false);
        button.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}
