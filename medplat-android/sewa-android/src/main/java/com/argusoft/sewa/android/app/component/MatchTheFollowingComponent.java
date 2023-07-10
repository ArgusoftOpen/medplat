package com.argusoft.sewa.android.app.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LmsConstants;
import com.argusoft.sewa.android.app.databean.LmsQuestionOptionDataBean;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MatchTheFollowingComponent extends LinearLayout {

    private final Context context;
    private final NestedScrollView scrollView;
    private LinearLayout mainLayout;

    private final List<LmsQuestionOptionDataBean> lhsOptions;
    private final List<LmsQuestionOptionDataBean> rhsOptions;

    private final List<View> lhs = new LinkedList<>();
    private final List<View> rhs = new LinkedList<>();

    private boolean imageAvailable;

    private LinearLayout selectionLayout;

    private final Map<String, View> questionTagViewMap = new HashMap<>();
    private final Map<String, View> answerTagViewMap = new HashMap<>();
    private final Map<String, String> queAnsMap = new HashMap<>();
    private final Map<String, String> ansQueMap = new HashMap<>();

    private int lastDragPosition = 0;

    public MatchTheFollowingComponent(Context context, List<LmsQuestionOptionDataBean> lhs, List<LmsQuestionOptionDataBean> rhs, NestedScrollView scrollView) {
        super(context);
        this.context = context;
        this.lhsOptions = lhs;
        this.rhsOptions = rhs;
        this.scrollView = scrollView;
        this.mainLayout = getDragDropView();
        this.addView(this.mainLayout);
    }

    public boolean isImageAvailable() {
        return imageAvailable;
    }

    public List<View> getLhs() {
        return lhs;
    }

    public List<View> getRhs() {
        return rhs;
    }

    public Map<String, View> getQuestionTagViewMap() {
        return questionTagViewMap;
    }

    public Map<String, View> getAnswerTagViewMap() {
        return answerTagViewMap;
    }

    public Map<String, String> getQuestionAnswersMap() {
        return queAnsMap;
    }

    public LinearLayout getDragDropView() {
        Collections.shuffle(rhsOptions);
        imageAvailable = LmsConstants.isImageAvailableInMatchTheFollowing(lhsOptions, rhsOptions);

        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        LinearLayout mainLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, layoutParams);
        mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.lms_answer_layout));
        mainLayout.setPadding(20, 20, 20, 20);

        selectionLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, layoutParams);

        int rhsCount = 0;
        LinearLayout layerLayout = null;

        for (LmsQuestionOptionDataBean option : rhsOptions) {
            final View answer = getRhsViewForMatchTheFollowing(context, option, true);
            rhs.add(answer);
            answerTagViewMap.put(answer.getTag().toString(), answer);

            answer.setOnTouchListener((v, event) -> {
                lastDragPosition = 0;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    DragShadowBuilder shadowBuilder = new DragShadowBuilder(answer);

                    answer.startDrag(data, shadowBuilder, answer, 0);
                    answer.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    return false;
                }
            });

//            selectionLayout.addView(answer);

            if (imageAvailable) {
                if (rhsCount % 2 == 0) {
                    layerLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lms_mf_image_answer_selection_layout, null);
                    selectionLayout.addView(layerLayout);
                }
                ((LinearLayout) layerLayout.getChildAt(rhsCount % 2)).addView(answer);
            } else {
                selectionLayout.addView(answer);
            }

            rhsCount++;
        }

        //Added Answer Options
        mainLayout.addView(selectionLayout);

        LinearLayout matchTheFollowingLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, layoutParams);
        int lhsCount = 0;
        for (LmsQuestionOptionDataBean option : lhsOptions) {
            lhsCount++;
            final View question = getLhsViewForMatchTheFollowing(context, option, lhsCount);
            lhs.add(question);
            questionTagViewMap.put(question.getTag().toString(), question);

            LinearLayout answerLayout = question.findViewById(R.id.lms_mf_ans_layout);
            answerLayout.addView(getInstructionText());

            answerLayout.setOnDragListener((v, event) -> {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_LOCATION:
                        handleScroll(event);
                        break;

                    case DragEvent.ACTION_DROP:
                        View ansView = (View) event.getLocalState();
                        LinearLayout ansLayout = (LinearLayout) v;
                        ViewGroup queView = (ViewGroup) ansLayout.getParent();
                        String queTag = queView.getTag().toString();
                        String ansTag = ansView.getTag().toString();

                        View previousAnsView = null;
                        View previousQueView = null;

                        LinearLayout parent = (LinearLayout) ansView.getParent();
                        parent.removeView(ansView);
                        ansLayout.removeAllViews();

                        if (queAnsMap.containsKey(queTag)) {
                            String previousAns = queAnsMap.get(queTag);
                            previousAnsView = answerTagViewMap.get(previousAns);
                            ansQueMap.remove(previousAns);
                        }
                        if (ansQueMap.containsKey(ansTag)) {
                            String previousQue = ansQueMap.get(ansTag);
                            previousQueView = questionTagViewMap.get(previousQue);
                            queAnsMap.remove(previousQue);
                        }

                        queAnsMap.put(queTag, ansTag);
                        ansQueMap.put(ansTag, queTag);

                        if (previousAnsView != null) {
                            queView.removeView(previousAnsView);
                            if (imageAvailable) {
                                addRhsViewInSelectionLayout();
                            } else {
                                selectionLayout.addView(previousAnsView);
                            }
                        }
                        if (previousQueView != null) {
                            LinearLayout previousAnsLayout = previousQueView.findViewById(R.id.lms_mf_ans_layout);
                            previousAnsLayout.removeView(ansView);
                            previousAnsLayout.addView(getInstructionText());
                            queView.removeView(previousAnsView);
                        }

                        ansLayout.addView(ansView);
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        ((View) event.getLocalState()).setVisibility(View.VISIBLE);
                        break;

                    default:
                }
                return true;
            });

            matchTheFollowingLayout.addView(question);
        }

        mainLayout.addView(matchTheFollowingLayout);
        return mainLayout;
    }

    private void handleScroll(DragEvent event) {
        View lastView = mainLayout.getChildAt(mainLayout.getChildCount() - 1);
        View view = (View) event.getLocalState();

        if (Math.round(event.getY()) > lastDragPosition && (Math.round(event.getY()) + Math.round(view.getY()) < Math.round(lastView.getY()))) {
            scrollView.smoothScrollBy(0, 30);
        }

        lastDragPosition = Math.round(event.getY());
    }

    private void addRhsViewInSelectionLayout() {
        selectionLayout.removeAllViews();

        int rhsCount = 0;
        LinearLayout layerLayout = null;
        for (View view : rhs) {
            if (!ansQueMap.containsKey(view.getTag().toString())) {
                LinearLayout parent = (LinearLayout) view.getParent();
                if (parent != null) {
                    parent.removeView(view);
                }

                if (rhsCount % 2 == 0) {
                    layerLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lms_mf_image_answer_selection_layout, null);
                    selectionLayout.addView(layerLayout);
                }
                ((LinearLayout) layerLayout.getChildAt(rhsCount % 2)).addView(view);
                rhsCount++;
            }
        }
    }

    private TextView getInstructionText() {
        MaterialTextView textView = MyStaticComponents.generateAnswerView(context, "Drag answer here");
        textView.setBackgroundResource(R.drawable.lms_match_text_que_option_background);
        textView.setTextColor(ContextCompat.getColor(context, R.color.lms_instruction_text));
        textView.setPadding(40, 20, 40, 20);
        textView.setTypeface(null, Typeface.ITALIC);
        if (imageAvailable) {
            textView.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
            textView.setBackgroundResource(R.drawable.lms_match_que_image_option_background);
        }
        return textView;
    }

    public static Drawable getImageDrawableForMatch(Context context, Long mediaId, String filename) {
        if (mediaId != null) {
            String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(mediaId, filename);
            if (UtilBean.isFileExists(path)) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                return new BitmapDrawable(context.getResources(),
                        Bitmap.createScaledBitmap(bitmap, displayMetrics.widthPixels / 3, displayMetrics.widthPixels / 4, true));
            }
        }
        return null;
    }

    public View getLhsViewForMatchTheFollowing(Context context, LmsQuestionOptionDataBean lhsOption, int lhsCount) {
        LinearLayout mainLayout;
        if (imageAvailable) {
            mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lms_match_the_following_image_layout, null);
        } else {
            mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.lms_match_the_following_text_layout, null);
        }

        LinearLayout questionLayout = mainLayout.findViewById(R.id.lms_mf_que_layout);
        TextView tv = MyStaticComponents.generateAnswerView(context, lhsOption.getOptionTitle());
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText(lhsCount + ". " + lhsOption.getOptionTitle());

        Drawable image = getImageDrawableForMatch(context, lhsOption.getMediaId(), lhsOption.getMediaName());
        if (image != null) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, image, null, null);
            tv.setCompoundDrawablePadding(10);
        }

        if (imageAvailable) {
            tv.setBackgroundResource(R.drawable.lms_match_image_que_option_background);
            tv.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
            tv.setPadding(40, 30, 40, 30);
        }

        questionLayout.addView(tv);
        mainLayout.setTag(lhsOption.getOptionValue());
        return mainLayout;
    }

    public View getRhsViewForMatchTheFollowing(Context context, LmsQuestionOptionDataBean rhsOption, boolean showMoveIcon) {
        TextView tv = MyStaticComponents.generateQuestionView(null, null, context, rhsOption.getOptionTitle());
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        tv.setTag(rhsOption.getOptionValue());

        Drawable moveIcon;

        Drawable image = getImageDrawableForMatch(context, rhsOption.getMediaId(), rhsOption.getMediaName());
        if (showMoveIcon) {
            if (image != null) {
                moveIcon = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(
                        ((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.move_icon_circle)).getBitmap(),
                        tv.getLineHeight() * 2, tv.getLineHeight() * 2, true));

                Drawable[] layers = new Drawable[2];
                layers[0] = image;
                layers[1] = moveIcon;
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    layerDrawable.setLayerGravity(1, Gravity.TOP);
                    layerDrawable.setLayerInsetTop(1, 10);
                    layerDrawable.setLayerInsetStart(1, 10);
                    layerDrawable.setLayerSize(1, tv.getLineHeight(), tv.getLineHeight());
                }
                tv.setCompoundDrawablesWithIntrinsicBounds(null, layerDrawable, null, null);
                tv.setPadding(40, 20, 40, 20);
            } else {
                moveIcon = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(
                        ((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.move_icon)).getBitmap(),
                        tv.getLineHeight(), tv.getLineHeight(), true));
                tv.setCompoundDrawablesWithIntrinsicBounds(moveIcon, null, null, null);
                tv.setPadding(40, 30, 40, 30);
            }
        } else {
            if (image != null) {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, image, null, null);
                tv.setPadding(40, 20, 40, 20);
            } else {
                tv.setPadding(40, 30, 40, 30);
            }
        }

        if (imageAvailable) {
            tv.setBackgroundResource(R.drawable.lms_match_image_ans_option_background);
            tv.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        } else {
            tv.setBackgroundResource(R.drawable.lms_match_text_ans_option_background);
        }

        tv.setCompoundDrawablePadding(10);

        return tv;
    }

    public LinearLayout getCorrectAnswerLayout(Map<String, String> answerPairs) {
        LinearLayout matchTheFollowingLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        Map<String, View> queTagViewMap = new HashMap<>();
        int lhsCount = 0;
        for (LmsQuestionOptionDataBean option : lhsOptions) {
            lhsCount++;
            View question = getLhsViewForMatchTheFollowing(context, option, lhsCount);
            queTagViewMap.put(option.getOptionValue(), question);
        }
        Map<String, View> ansTagViewMap = new HashMap<>();
        for (LmsQuestionOptionDataBean option : rhsOptions) {
            View ans = getRhsViewForMatchTheFollowing(context, option, false);
            ansTagViewMap.put(option.getOptionValue(), ans);
        }

        for (Map.Entry<String, String> entry : answerPairs.entrySet()) {
            LinearLayout que = (LinearLayout) queTagViewMap.get(entry.getKey());
            LinearLayout ansLayout = que.findViewById(R.id.lms_mf_ans_layout);
            ansLayout.addView(ansTagViewMap.get(entry.getValue()));
            matchTheFollowingLayout.addView(que);
        }
        return matchTheFollowingLayout;
    }

    public LinearLayout getCorrectAndIncorrectAnswerLayout(Map<String, String> answerPairs, Map<String, String> givenAnswerPairs) {
        LinearLayout matchTheFollowingLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        Map<String, View> queTagViewMap = new HashMap<>();
        int lhsCount = 0;
        for (LmsQuestionOptionDataBean option : lhsOptions) {
            lhsCount++;
            View question = getLhsViewForMatchTheFollowing(context, option, lhsCount);
            queTagViewMap.put(option.getOptionValue(), question);
        }
        Map<String, View> ansTagViewMap = new HashMap<>();
        for (LmsQuestionOptionDataBean option : rhsOptions) {
            View ans = getRhsViewForMatchTheFollowing(context, option,false);
            ansTagViewMap.put(option.getOptionValue(), ans);
        }


        for (Map.Entry<String, String> entry : givenAnswerPairs.entrySet()) {
            LinearLayout que = (LinearLayout) queTagViewMap.get(entry.getKey());
            LinearLayout ansLayout = que.findViewById(R.id.lms_mf_ans_layout);
            View ansView = ansTagViewMap.get(entry.getValue());
            if (!entry.getValue().equals(answerPairs.get(entry.getKey()))){
                ansView.setBackgroundResource(R.drawable.lms_match_the_following_incorrect);
            } else {
                ansView.setBackgroundResource(R.drawable.lms_match_the_following_correct);
            }
            ansLayout.addView(ansTagViewMap.get(entry.getValue()));
            matchTheFollowingLayout.addView(que);
        }
        return matchTheFollowingLayout;
    }
}
