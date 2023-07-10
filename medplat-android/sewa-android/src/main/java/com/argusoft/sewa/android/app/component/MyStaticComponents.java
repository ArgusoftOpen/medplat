package com.argusoft.sewa.android.app.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.core.widget.TextViewCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.activity.CustomMandatoryVideoPlayerActivity;
import com.argusoft.sewa.android.app.activity.CustomVideoPlayerActivity;
import com.argusoft.sewa.android.app.component.listeners.AgeBoxChangeListener;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.FormulaTagBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.qrscanner.QRScannerActivity;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.tooltip.Tooltip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MyStaticComponents {

    private static final String TAG = "MyStaticComponents";

    private MyStaticComponents() {
        throw new IllegalStateException("Utility Class");
    }

    public static MaterialTextView getMaterialTextView(Context context, String text, int id, int style, boolean isCenterAligned) {

        MaterialTextView materialTextView = new MaterialTextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        if (style != -1) {
            materialTextView.setTextAppearance(context, style);
        }
        if (text == null || text.length() == 0) {
            return null;
        } else {
            String label = UtilBean.getMyLabel(text);
            materialTextView.setText(label);
        }
        if (id != -1) {
            materialTextView.setId(id);
        }
        if (isCenterAligned) {
            materialTextView.setGravity(Gravity.CENTER);
        }
        materialTextView.setPadding(0, 10, 0, 10);
        materialTextView.setLayoutParams(layoutParams);
        return materialTextView;
    }

    public static MaterialTextView generateTitleView(Context context, String title) {
        return MyStaticComponents.getMaterialTextView(context, title, -1, R.style.CustomTitleView, true);
    }

    public static MaterialTextView generateSubTitleView(Context context, String subtitle) {
        return MyStaticComponents.getMaterialTextView(context, subtitle, -1, R.style.CustomSubtitleView, false);
    }

    public static MaterialTextView generateInstructionView(Context context, String instruction) {
        return MyStaticComponents.getMaterialTextView(context, instruction, -1, R.style.CustomInstructionView, false);
    }

    //do not use it in future implementation
    public static MaterialTextView generateAnswerView(Context context, String question) {
        if (question == null || question.length() == 0) {
            return null;
        } else {
            MaterialTextView textView = MyStaticComponents.getMaterialTextView(context, question, -1, R.style.CustomAnswerView, false);
            if (textView != null) {
                textView.setPadding(0, 0, 0, 15);
            }
            return textView;
        }
    }

    public static MaterialTextView generateBoldAnswerView(Context context, String question) {
        if (question == null || question.length() == 0) {
            return null;
        } else {
            MaterialTextView textView = MyStaticComponents.getMaterialTextView(context, question, -1, R.style.CustomBoldAnswerView, false);
            if (textView != null) {
                textView.setPadding(0, 0, 0, 15);
            }
            return textView;
        }
    }

    public static MaterialTextView generateLabelView(Context context, String question) {
        if (question == null || question.length() == 0) {
            return null;
        } else {
            MaterialTextView textView = getMaterialTextView(context, question, -1, R.style.CustomLabelView, false);
            if (textView != null) {
                textView.setPadding(0, 15, 0, 10);
            }
            return textView;
        }
    }

    public static LinearLayout generateQuestionViewWithTooltip(String helpVideo, String hint, final Context context, String question) {
        if (question == null || question.length() == 0) {
            return null;
        } else {
            LinearLayout myLayout = getLinearLayout(context, -1, LinearLayout.HORIZONTAL, null);
            myLayout.setGravity(Gravity.CENTER_VERTICAL);
            MaterialTextView textView = MyStaticComponents.getMaterialTextView(context, question, 1, R.style.CustomQuestionView, false);
            if (helpVideo != null && !helpVideo.isEmpty() && textView != null) {
                addHelpVideo(helpVideo, textView, context, question);
            }
            textView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 200));
            myLayout.addView(textView);

            if (hint != null && !hint.isEmpty() && textView != null) {
                ImageView image = MyStaticComponents.getImageView(context, 2, R.drawable.ic_info_black, null);
                String myLabel = UtilBean.getMyLabel(hint);
                Tooltip.Builder builder = new Tooltip.Builder(image)
                        .setCancelable(true)
                        .setDismissOnClick(true)
                        .setBackgroundColor(ContextCompat.getColor(context, R.color.colorSecondary))
                        .setTypeface(SharedStructureData.typeface)
                        .setTextColor(Color.WHITE)
                        .setText(myLabel);

                if (myLabel.length() < 40) {
                    builder.setGravity(Gravity.END);
                }
                final Tooltip tooltip = builder.build();
                View.OnClickListener listener = v -> {
                    if (tooltip.isShowing()) {
                        tooltip.dismiss();
                    } else {
                        tooltip.show();
                    }
                };
                image.setOnClickListener(listener);
                textView.setOnClickListener(listener);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1);
                layoutParams.setMargins(0, 10, 0, 10);
                image.setLayoutParams(layoutParams);
                myLayout.addView(image);
            }

            return myLayout;
        }
    }

    //do not use it in future implementation
    public static MaterialTextView generateQuestionView(String helpVideo, String hint, final Context context, String question) {
        if (question == null || question.length() == 0) {
            return null;
        } else {
            MaterialTextView textView = MyStaticComponents.getMaterialTextView(context, question, -1, R.style.CustomQuestionView, false);
            if (helpVideo != null && !helpVideo.isEmpty() && textView != null) {
                addHelpVideo(helpVideo, textView, context, question);
            }
            if (hint != null && !hint.isEmpty() && textView != null) {
                addTooltip(hint, textView, context);
            }
            return textView;
        }
    }

    public static MaterialTextView getListTitleView(Context context, String title) {
        if (title == null) {
            return null;
        }
        MaterialTextView textView = new MaterialTextView(context);
        textView.setPadding(20, 20, 20, 20);
        textView.setTextAppearance(context, R.style.ListTitleView);
        textView.setText(UtilBean.getMyLabel(title));
        return textView;
    }

    private static void addTooltip(String hint, MaterialTextView textView, Context context) {
        String myLabel = UtilBean.getMyLabel(hint);
        Tooltip.Builder builder = new Tooltip.Builder(textView)
                .setCancelable(true)
                .setDismissOnClick(true)
                .setBackgroundColor(ContextCompat.getColor(context, R.color.colorSecondary))
                .setTypeface(SharedStructureData.typeface)
                .setTextColor(Color.WHITE)
                .setText(myLabel);

        if (myLabel.length() < 40) {
            builder.setGravity(Gravity.END);
        }
        final Tooltip tooltip = builder.build();
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_black, 0);
        textView.setOnClickListener(v -> {
            if (tooltip.isShowing()) {
                tooltip.dismiss();
            } else {
                tooltip.show();
            }
        });
    }

    private static void addHelpVideo(String helpVideo, MaterialTextView textView, final Context context, String question) {
        question = UtilBean.getMyLabel(question);
        if (helpVideo.contains("Audio")) {
            final List<FieldValueMobDataBean> dataSources = UtilBean.getDataMapValues(helpVideo);
            String finalLabel = question + " \uD83D\uDD08";
            final MediaPlayer mPlayer = new MediaPlayer();
            SpannableString ss = new SpannableString(finalLabel);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View textView) {
                    new Thread() {
                        @Override
                        public void run() {
                            if (dataSources == null || dataSources.isEmpty()) {
                                return;
                            }

                            if (mPlayer.isPlaying()) {
                                mPlayer.stop();
                                mPlayer.reset();
                            } else {
                                String filePath = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + dataSources.get(0).getValue();
                                if (UtilBean.isFileExists(filePath)) {
                                    Log.i(TAG, filePath + " is found and ready to play");
                                    try {
                                        mPlayer.setDataSource(filePath);
                                        mPlayer.prepare();
                                        mPlayer.start();
                                    } catch (IOException e) {
                                        Log.e(TAG, null, e);
                                    }
                                }
                            }
                        }
                    }.start();
                }
            };
            ss.setSpan(clickableSpan, question.length() + 1, finalLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(ss);
        } else {
            String finalLabel = question + " \uD83C\uDFAC";

            SpannableString ss = new SpannableString(finalLabel);
            final String dataMap = helpVideo;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View textView) {
                    new Thread() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, CustomVideoPlayerActivity.class);
                            intent.putExtra(GlobalTypes.DATA_MAP, dataMap);
                            context.startActivity(intent);
                        }
                    }.start();
                }
            };

            ss.setSpan(clickableSpan, question.length() + 1, finalLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(ss);
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static TextInputLayout getEditText(Context context, String hint, int id, int length, int inputType) {
        TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(context).inflate(R.layout.text_input_layout, null);
        textInputLayout.setEndIconOnClickListener(v -> {
            if (textInputLayout.getEditText() != null) {
                textInputLayout.getEditText().setText(null);
                textInputLayout.requestFocus();
            }
        });
        EditText textInputEditText = textInputLayout.getEditText();
        if (textInputEditText == null) {
            textInputEditText = new EditText(context);
        }
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);

                    int type = Character.getType(c);
                    if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                        return "";
                    }

                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                String blockChar = "_~#^&`|$%*!@()[]<>{}\":;?+=×÷€£¥₩%《》¡¿¤•";
                return !blockChar.contains(String.valueOf(c));
            }
        };
        if (id != -1) {
            textInputEditText.setId(id);
        }
        if (length > 0) {
            textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length), filter});
        } else {
            textInputEditText.setFilters(new InputFilter[]{filter});
        }
        if (inputType != -1) {
            textInputEditText.setInputType(inputType);
        }
        if (hint != null) {
            textInputEditText.setHint(hint);
        }
        return textInputLayout;
    }

    public static TextInputLayout getChardhamEditText(Context context, String hint, int id, int length, int inputType) {
        TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(context).inflate(R.layout.chardham_text_input_layout, null);
        textInputLayout.setEndIconOnClickListener(v -> {
            if (textInputLayout.getEditText() != null) {
                textInputLayout.getEditText().setText(null);
                textInputLayout.requestFocus();
            }
        });
        EditText textInputEditText = textInputLayout.getEditText();
        if (textInputEditText == null) {
            textInputEditText = new EditText(context);
        }
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);

                    int type = Character.getType(c);
                    if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                        return "";
                    }

                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                String blockChar = "_~#^&`|$%*!@()[]<>{}\":;?+=×÷€£¥₩%《》¡¿¤•";
                return !blockChar.contains(String.valueOf(c));
            }
        };
        if (id != -1) {
            textInputEditText.setId(id);
        }
        if (length > 0) {
            textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length), filter});
        } else {
            textInputEditText.setFilters(new InputFilter[]{filter});
        }
        if (inputType != -1) {
            textInputEditText.setInputType(inputType);
        }
        if (hint != null) {
            textInputEditText.setHint(hint);
        }
        return textInputLayout;
    }

    public static TextInputLayout getEditTextWithQrScan(Context context, String hint, int id, int length, int inputType) {
        final TextInputLayout textInputLayout = new TextInputLayout(context);
        textInputLayout.setStartIconDrawable(R.drawable.ic_qr_scan);
        textInputLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
        textInputLayout.setEndIconOnClickListener(v -> {
            if (textInputLayout.getEditText() != null) {
                textInputLayout.getEditText().setText(null);
                textInputLayout.requestFocus();
            }
        });
        textInputLayout.setStartIconOnClickListener((new View.OnClickListener() {
            private Activity activity;

            public View.OnClickListener setActivity(Activity activity) {
                this.activity = activity;
                return this;
            }

            @Override
            public void onClick(View view) {
                try {
                    // launch barcode activity.
                    //initiate scan with our custom scan activity
                    IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                    intentIntegrator.setCaptureActivity(QRScannerActivity.class);
                    intentIntegrator.initiateScan();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), null, e);
                }
            }
        }).setActivity((Activity) context));
        textInputLayout.setHintEnabled(false);
        TextInputEditText textInputEditText = new TextInputEditText(context);
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);

                    int type = Character.getType(c);
                    if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                        return "";
                    }

                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                String blockChar = "_~#^&`|$%*!@()[]<>{}\":;?+=×÷€£¥₩%《》¡¿¤•";
                return !blockChar.contains(String.valueOf(c));
            }
        };
        if (id != -1) {
            textInputEditText.setId(id);
        }
        if (length > 0) {
            textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length), filter});
        } else {
            textInputEditText.setFilters(new InputFilter[]{filter});
        }
        if (inputType != -1) {
            textInputEditText.setInputType(inputType);
        }
        if (hint != null) {
            textInputEditText.setHint(hint);
        }
        textInputLayout.addView(textInputEditText);
        return textInputLayout;
    }

    public static TextInputLayout getChardhamEditTextWithQrScan(Context context, String hint, int id, int length, int inputType) {
        final TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(context).inflate(R.layout.chardham_text_input_layout, null);
        textInputLayout.setStartIconDrawable(R.drawable.ic_qr_scan);
        textInputLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
        textInputLayout.setEndIconOnClickListener(v -> {
            if (textInputLayout.getEditText() != null) {
                textInputLayout.getEditText().setText(null);
                textInputLayout.requestFocus();
            }
        });
        textInputLayout.setStartIconOnClickListener((new View.OnClickListener() {
            private Activity activity;

            public View.OnClickListener setActivity(Activity activity) {
                this.activity = activity;
                return this;
            }

            @Override
            public void onClick(View view) {
                try {
                    // launch barcode activity.
                    //initiate scan with our custom scan activity
                    IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                    intentIntegrator.setCaptureActivity(QRScannerActivity.class);
                    intentIntegrator.initiateScan();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), null, e);
                }
            }
        }).setActivity((Activity) context));
        textInputLayout.setHintEnabled(false);

        EditText textInputEditText = textInputLayout.getEditText();
        if (textInputEditText == null) {
            textInputEditText = new EditText(context);
        }

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);

                    int type = Character.getType(c);
                    if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                        return "";
                    }

                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                String blockChar = "_~#^&`|$%*!@()[]<>{}\":;?+=×÷€£¥₩%《》¡¿¤•";
                return !blockChar.contains(String.valueOf(c));
            }
        };
        if (id != -1) {
            textInputEditText.setId(id);
        }
        if (length > 0) {
            textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length), filter});
        } else {
            textInputEditText.setFilters(new InputFilter[]{filter});
        }
        if (inputType != -1) {
            textInputEditText.setInputType(inputType);
        }
        if (hint != null) {
            textInputEditText.setHint(hint);
        }
        return textInputLayout;
    }

    public static ListView getListView(Context context, List<String> list, AdapterView.OnItemClickListener onItemClickListener, int selector) {
        ArrayAdapter<String> adapter;
        ListView listView = new ListView(context);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        if (selector != -1) {
            adapter = new ArrayAdapter<>(context, selector, R.id.lists_text, list);
        } else {
            adapter = new ArrayAdapter<>(context, R.layout.listview_row, R.id.lists_text, list);
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setNestedScrollingEnabled(true);
        return listView;
    }

    public static ListView getListView(Context context, BaseAdapter adapter, AdapterView.OnItemClickListener myListener) {
        ListView listView = new ListView(context);
        listView.setAdapter(adapter);
        listView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        adapter.notifyDataSetChanged();
        listView.setNestedScrollingEnabled(true);
        listView.setOnItemClickListener(myListener);
        return listView;
    }

    public static PagingListView getPaginatedListViewWithItem(Context context,
                                                              List<ListItemDataBean> listItems,
                                                              int itemResourceId,
                                                              AdapterView.OnItemClickListener onItemClickListener,
                                                              PagingListView.PagingListener pagingListener) {
        PagingAdapter<ListItemDataBean> adapter = new PagingAdapter<>(context, itemResourceId, listItems, onItemClickListener);
        PagingListView listView = new PagingListView(context);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        listView.setNestedScrollingEnabled(true);
        listView.setAdapter(adapter);
        listView.onFinishLoadingWithItem(true, listItems);
        listView.setOnItemClickListener(onItemClickListener);
        if (pagingListener != null) {
            listView.setHasMoreItems(true);
            listView.setPagingListener(pagingListener);
        } else {
            listView.setHasMoreItems(false);
        }
        if (itemResourceId == R.layout.listview_row_notification
                || itemResourceId == R.layout.listview_row_type) {
            listView.setDivider(null);
        }
        return listView;
    }

    public static MaterialButton getButton(Context context, String text, int id, LinearLayout.LayoutParams layoutParams) {
        MaterialButton button = new MaterialButton(context);
        if (text != null && text.length() != 0) {
            button.setText(UtilBean.getMyLabel(text));
        }
        if (id != -1) {
            button.setId(id);
        }
        if (layoutParams != null) {
            button.setLayoutParams(layoutParams);
        }
        button.setTextColor(Color.WHITE);
        button.setTextSize(18);
        button.setMinHeight(150);
        return button;
    }

    public static MaterialButton getCustomButton(Context context, String text, int id, LinearLayout.LayoutParams layoutParams) {
        MaterialButton button = getButton(context, text, id, layoutParams);
        button.setMinHeight(20);
        button.setTextSize(14f);
        return button;
    }

    public static LinearLayout getLinearLayout(Context context, int id, int orientation, LinearLayout.LayoutParams layoutParams) {
        LinearLayout linearLayout = new LinearLayout(context);
        if (id != -1) {
            linearLayout.setId(id);
        }
        if (orientation != -1) {
            linearLayout.setOrientation(orientation);
        }
        if (layoutParams != null) {
            linearLayout.setLayoutParams(layoutParams);
        }
        return linearLayout;
    }

    public static LinearLayout getDetailsLayout(Context context, int id, int orientation, int bodyLayoutPadding) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        bodyLayoutPadding = bodyLayoutPadding * -1;
        layoutParams.setMargins(bodyLayoutPadding, bodyLayoutPadding, bodyLayoutPadding, 30);

        LinearLayout linearLayout = getLinearLayout(context, id, orientation, layoutParams);
        linearLayout.setPadding(20, 0, 20, 20);
        linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.details_border));
        return linearLayout;
    }

    public static RadioGroup getRadioGroup(Context context, Map<Integer, String> mapIdsAndText, boolean isHorizontal) {
        RadioGroup radioGroup = new RadioGroup(context);
        RadioButton radioButton;

        if (isHorizontal) {
            radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        }

        for (Map.Entry<Integer, String> entry : mapIdsAndText.entrySet()) {
            radioButton = getRadioButton(context, UtilBean.getMyLabel(entry.getValue()), entry.getKey());
            radioGroup.addView(radioButton);
        }

        return radioGroup;
    }

    public static RadioButton getRadioButton(Context context, String text, int id) {
        RadioButton radioButton = new RadioButton(context);
        if (text != null) {
            radioButton.setText(UtilBean.getMyLabel(text));
        }
        if (id != -1) {
            radioButton.setId(id);
        }
        radioButton.setButtonTintList(ContextCompat.getColorStateList(context, R.color.checkbox_selector));
        TextViewCompat.setTextAppearance(radioButton, R.style.CustomAnswerView);
        return radioButton;
    }

    public static MaterialCheckBox getCheckBox(Context context, String text, int id, boolean checked) {
        MaterialCheckBox checkBox = new MaterialCheckBox(context);
        if (text != null) {
            checkBox.setText(UtilBean.getMyLabel(text));
        }
        if (id != -1) {
            checkBox.setId(id);
        }
        checkBox.setChecked(checked);
        TextViewCompat.setTextAppearance(checkBox, R.style.CustomAnswerView);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        return checkBox;
    }

    public static ImageView getImageView(Context context, int id, int resourceId, LinearLayout.LayoutParams layoutParams) {
        ImageView imageView = new ImageView(context);
        if (resourceId != -1) {
            imageView.setImageResource(resourceId);
        }
        if (id != -1) {
            imageView.setId(id);
        }
        if (layoutParams != null) {
            imageView.setLayoutParams(layoutParams);
        }
        return imageView;
    }

    public static ImageView getImageView(Context context, int id, Drawable drawable, LinearLayout.LayoutParams layoutParams) {
        ImageView imageView = new ImageView(context);
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
        if (id != -1) {
            imageView.setId(id);
        }
        if (layoutParams != null) {
            imageView.setLayoutParams(layoutParams);
        }
        return imageView;
    }

    public static LinearLayout getCustomDatePickerForStatic(Context context, View.OnClickListener listener, int id) {
        LinearLayout myLayout = getLinearLayout(context, id, LinearLayout.HORIZONTAL, null);
        myLayout.setGravity(Gravity.CENTER_VERTICAL);

        MaterialTextView txtDate = MyStaticComponents.generateAnswerView(context, GlobalTypes.SELECT_DATE_TEXT);
        if (txtDate != null) {
            txtDate.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 200));
            txtDate.setId(IdConstants.DATE_PICKER_TEXT_DATE_ID);
            myLayout.addView(txtDate);
        }

        ImageView imageCalendar = MyStaticComponents.getImageView(context, 2, R.drawable.ic_calender, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1);
        layoutParams.setMargins(0, 10, 0, 10);
        imageCalendar.setLayoutParams(layoutParams);
        myLayout.addView(imageCalendar);

        if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
            myLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.training_custom_datepicker));
        } else {
            myLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_datepicker));
        }
        myLayout.setOnClickListener(listener);
        //for bottom margin
        myLayout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) myLayout.getLayoutParams();
//        lp.setMargins(0, 0, 0, 50);
        return myLayout;
    }

    public static Spinner getSpinner(Context context, String[] options, int defaultIndex, int id) {
        Spinner spinner = new Spinner(context);
        spinner.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        if (options != null) {
            MyArrayAdapter myAdapter = new MyArrayAdapter(context, R.layout.spinner_item_top, options);
            myAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
            spinner.setAdapter(myAdapter);
            spinner.setSelection(defaultIndex, false);
        }
        if (id != -1) {
            spinner.setId(id);
        }

//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) spinner.getLayoutParams();
//        layoutParams.setMargins(0, -20, 0, 50);
        return spinner;
    }

    public static MaterialButtonToggleGroup getYesNoToggleButton(final Context context) {
        MaterialButtonToggleGroup buttonToggleGroup = new MaterialButtonToggleGroup(context);
        buttonToggleGroup.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        MaterialButton yesButton = MyStaticComponents.getCustomButton(context, LabelConstants.YES, IdConstants.RADIO_BUTTON_ID_YES,
                new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        yesButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.yes_toggle_button_selector));
        yesButton.setTextColor(ContextCompat.getColorStateList(context, R.color.toggle_button_text_color_selector));
        yesButton.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
        yesButton.setStrokeWidth(2);
        buttonToggleGroup.addView(yesButton);

        MaterialButton noButton = MyStaticComponents.getCustomButton(context, LabelConstants.NO, IdConstants.RADIO_BUTTON_ID_NO,
                new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        noButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.no_toggle_button_selector));
        noButton.setTextColor(ContextCompat.getColorStateList(context, R.color.toggle_button_text_color_selector));
        noButton.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
        noButton.setStrokeWidth(2);
        buttonToggleGroup.addView(noButton);

        MaterialButtonToggleGroup.OnButtonCheckedListener onButtonCheckedListener = (group, checkedId, isChecked) -> {
            MaterialButton selectedButton = (MaterialButton) group.getChildAt(checkedId - 10002);
            MaterialButton childAt1 = (MaterialButton) group.getChildAt(0);
            MaterialButton childAt2 = (MaterialButton) group.getChildAt(1);
            childAt1.setSelected(false);
            childAt2.setSelected(false);
            selectedButton.setSelected(true);
        };

        buttonToggleGroup.addOnButtonCheckedListener(onButtonCheckedListener);
        return buttonToggleGroup;
    }

    public static ListView getButtonList(Context context, List<String> buttonTexts, AdapterView.OnItemClickListener onItemClickListener) {

        MyExpandedListView listView = new MyExpandedListView(context);
        listView.setExpanded(true);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setNestedScrollingEnabled(true);
        listView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.button_listview_row, R.id.lists_button, buttonTexts);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setOnItemClickListener(onItemClickListener);

        return listView;
    }

    public static View getSeparator(Context context) {
        View separatorView = new View(context);
        separatorView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 1));
        separatorView.setBackgroundColor(ContextCompat.getColor(context, R.color.listSeparatorBackground));
        return separatorView;
    }

    public static MaterialTextView getOrTextView(Context context) {
        MaterialTextView materialTextView = new MaterialTextView(context);
        materialTextView.setText(LabelConstants.OR);
        materialTextView.setTextAppearance(context, R.style.OrTextView);
        materialTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return materialTextView;
    }

    public static MaterialTextView getAndTextView(Context context) {
        MaterialTextView materialTextView = new MaterialTextView(context);
        materialTextView.setText(LabelConstants.AND);
        materialTextView.setTextAppearance(context, R.style.OrTextView);
        materialTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return materialTextView;
    }

    public static LinearLayout getAgeBox(Context context, int id, QueFormBean queFormBean) {
        int year = -1;
        int month = 0;
        String calculatedDate = LabelConstants.YEAR_AND_MONTH_NOT_YET_ENTERED;
        if (queFormBean.getRelatedpropertyname() != null) {
            String relatedPropertyName = queFormBean.getRelatedpropertyname();
            if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                relatedPropertyName += queFormBean.getLoopCounter();
            }
            String stringLongDate = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
            if (stringLongDate != null) {
                long longDate = Long.parseLong(stringLongDate);
                calculatedDate = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(new Date(longDate));
                int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(longDate, new Date().getTime());
                year = ageYearMonthDayArray[0];
                month = ageYearMonthDayArray[1];
                queFormBean.setAnswer(longDate);
            }
        }

        AgeBoxChangeListener myListener = new AgeBoxChangeListener(context, queFormBean);

        LinearLayout mainLayout = getLinearLayout(context, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        if (id != -1) {
            mainLayout.setId(id);
        }

        // year
        LinearLayout linearLayout = getLinearLayout(context, -1, LinearLayout.HORIZONTAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        TextInputLayout editText = MyStaticComponents.getEditText(context, GlobalTypes.YEAR, IdConstants.AGE_BOX_YEAR_EDIT_TEXT_ID, 3, InputType.TYPE_CLASS_NUMBER);
        if (year != -1) {
            Objects.requireNonNull(editText.getEditText()).setText(String.format(Locale.getDefault(), "%d", year));
        }
        Objects.requireNonNull(editText.getEditText()).setOnFocusChangeListener(myListener);
        linearLayout.addView(editText, new LinearLayout.LayoutParams(0, WRAP_CONTENT, 9));
        MaterialTextView textView = MyStaticComponents.getMaterialTextView(context, GlobalTypes.YEAR, -1, R.style.CustomAnswerView, false);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(0, WRAP_CONTENT, 2));
        mainLayout.addView(linearLayout);

        // month
        linearLayout = getLinearLayout(context, -1, LinearLayout.HORIZONTAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        editText = MyStaticComponents.getEditText(context, GlobalTypes.MONTH, IdConstants.AGE_BOX_MONTH_EDIT_TEXT_ID, 2, InputType.TYPE_CLASS_NUMBER);
        Objects.requireNonNull(editText.getEditText()).setText(String.format(Locale.getDefault(), "%d", month));
        editText.getEditText().setOnFocusChangeListener(myListener);
        linearLayout.addView(editText, new LinearLayout.LayoutParams(0, WRAP_CONTENT, 9));
        textView = MyStaticComponents.getMaterialTextView(context, GlobalTypes.MONTH, -1, R.style.CustomAnswerView, false);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(0, WRAP_CONTENT, 2));
        mainLayout.addView(linearLayout);

        // day
//        linearLayout = getLinearLayout(context, -1, LinearLayout.HORIZONTAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
//        editText = getEditText(context, GlobalTypes.DAY, IdConstants.AGE_BOX_DAY_EDIT_TEXT_ID, 2, InputType.TYPE_CLASS_NUMBER);
//        editText.setOnFocusChangeListener(myListener);
//        linearLayout.addView(editText, new LinearLayout.LayoutParams(0, WRAP_CONTENT, 9));
//        textView = getMaterialTextView(context, GlobalTypes.DAY, -1, R.style.CustomAnswerView, false);
//        linearLayout.addView(textView, new LinearLayout.LayoutParams(0, WRAP_CONTENT, 2));
//        mainLayout.addView(linearLayout);

        boolean showDate = false;
        String dateQue = "Calculated Date";
        if (queFormBean.getFormulas() != null && !queFormBean.getFormulas().isEmpty()) {
            for (FormulaTagBean bean : queFormBean.getFormulas()) {
                if (bean.getFormulavalue() != null && bean.getFormulavalue().toLowerCase(Locale.ROOT).contains("showdate")) {
                    String formulaValue = bean.getFormulavalue().trim();
                    String formulaLC = formulaValue.toLowerCase(Locale.ROOT);
                    showDate = true;
                    dateQue = formulaValue.substring(formulaLC.toLowerCase(Locale.ROOT).indexOf("showdate-") + 9).trim();
                    break;
                }
            }
        }

        if (showDate) {
            mainLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, dateQue));
            MaterialTextView tv = MyStaticComponents.generateAnswerView(context, calculatedDate);
            tv.setId(IdConstants.DATE_PICKER_AGE_DISPLAY_TEXT_ID);
            myListener.setTextDate(tv);
            mainLayout.addView(tv);
        }

        return mainLayout;
    }

    public static NestedScrollView getScrollView(Context context, int id, LinearLayout.LayoutParams layoutParams) {

        NestedScrollView scrollView = getScrollView(context);
        if (id != -1) {
            scrollView.setId(id);
        }
        if (layoutParams != null) {
            scrollView.setLayoutParams(layoutParams);
        }
        return scrollView;
    }

    public static NestedScrollView getScrollView(Context context) {
        return new NestedScrollView(context);
    }

    public static LinearLayout getVideoShow(Context context, String dataMap, int id) {
        LinearLayout mainLayout = getLinearLayout(context, -1, LinearLayout.VERTICAL,
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        if (id != -1) {
            mainLayout.setId(id);
        }

        ImageView imageView = getImageView(context, 1, R.drawable.play_video,
                new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        mainLayout.addView(imageView);
        imageView.setOnClickListener((new View.OnClickListener() {
                    private Context context;
                    private String dataMap;

                    public View.OnClickListener setContext(Context context, String dataMap) {
                        this.context = context;
                        this.dataMap = dataMap;
                        return this;
                    }

                    @Override
                    public void onClick(View view) {
                        try {
                            final MyProcessDialog dialog = new MyProcessDialog(context, GlobalTypes.PLEASE_WAIT);
                            dialog.show();

                            new Thread() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(context, CustomVideoPlayerActivity.class);
                                    if (dataMap != null) {
                                        intent.putExtra(GlobalTypes.DATA_MAP, dataMap);
                                    }
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            }.start();
                        } catch (Exception e) {
                            SharedStructureData.sewaService.storeException(e, GlobalTypes.EXCEPTION_TYPE_SHOW_VIDEO);
                        }
                    }
                }).setContext(context, dataMap)
        );

        return mainLayout;
    }

    public static LinearLayout getMandatoryVideoShow(Context context, String dataMap, int id, Integer questionId) {
        LinearLayout mainLayout = getLinearLayout(context, -1, LinearLayout.VERTICAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        if (id != -1) {
            mainLayout.setId(id);
        }

        ImageView imageView = getImageView(context, 1, R.drawable.play_video, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        mainLayout.addView(imageView);

        imageView.setOnClickListener((new View.OnClickListener() {
                    private Context context;
                    private String dataMap;
                    private Integer questionId;

                    public View.OnClickListener setContext(Context context, String dataMap, Integer questionId) {
                        this.context = context;
                        this.dataMap = dataMap;
                        this.questionId = questionId;
                        return this;
                    }

                    @Override
                    public void onClick(View view) {
                        try {

                            final MyProcessDialog dialog = new MyProcessDialog(context, GlobalTypes.PLEASE_WAIT);
                            dialog.show();

                            new Thread() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(context, CustomMandatoryVideoPlayerActivity.class);
                                    if (dataMap != null) {
                                        intent.putExtra(GlobalTypes.DATA_MAP, dataMap);
                                    }
                                    intent.putExtra("questionId", questionId);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            }.start();
                        } catch (Exception e) {
                            SharedStructureData.sewaService.storeException(e, GlobalTypes.EXCEPTION_TYPE_SHOW_VIDEO);
                        }
                    }
                }).setContext(context, dataMap, questionId)
        );

        return mainLayout;
    }

    public static LinearLayout getCallView(Context context, String phoneNo, int id, int gravity) {

        LinearLayout mainLayout = getLinearLayout(context, -1, LinearLayout.HORIZONTAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        if (gravity > 0) {
            mainLayout.setGravity(gravity);
        }
        if (id != -1) {
            mainLayout.setId(id);
        }

        MaterialTextView phone = new MaterialTextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 10, 10);
        phone.setTextAppearance(context, R.style.CustomAnswerView);
        phone.setText(UtilBean.getMyLabel(phoneNo));
        phone.setPadding(0, 15, 0, 15);
        phone.setLayoutParams(layoutParams);
        mainLayout.addView(phone);

        ImageView imageView = getImageView(context, 1, R.drawable.call, new LinearLayout.LayoutParams(80, 80));
        mainLayout.addView(imageView);

        imageView.setOnClickListener((new View.OnClickListener() {
            private Context context;
            private String phoneNo;

            public View.OnClickListener setContext(Context context, String phoneNo) {
                this.context = context;
                this.phoneNo = phoneNo;
                return this;
            }

            @Override
            public void onClick(View view) {
                try {
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo)));
                } catch (Exception e) {
                    Log.i(TAG, "Call not done number is not valid");
                    Log.e(getClass().getSimpleName(), null, e);
                    if (context != null) {
                        SewaUtil.generateToast(context, LabelConstants.CALL_NOT_DONE_DUE_TO_INVALID_PHONE_NUMBER);
                    }
                }
            }
        }).setContext(context, phoneNo));

        return mainLayout;
    }

    public static RadioGroup getRadioGroup(Context context, RadioGroup.OnCheckedChangeListener myListener, List<String> options, int defaultIndex,
                                           int id, int orientation) {

        RadioGroup radioGroup = new RadioGroup(context);
        if (myListener != null) {
            radioGroup.setOnCheckedChangeListener(myListener);
        }

        if (id > 0) {
            radioGroup.setId(id);
        }

        int counter = 0;
        if (options != null && !options.isEmpty()) {
            for (String value : options) {
                radioGroup.addView(getRadioButton(context, UtilBean.getMyLabel(value), counter++));
            }
            if (defaultIndex != -1) {
                ((RadioButton) radioGroup.getChildAt(defaultIndex)).setChecked(true);
            }
        }

        if (orientation != -1) {
            radioGroup.setOrientation(orientation);
        }
        return radioGroup;
    }

    public static Drawable getImageDrawable(Context context, Long mediaId, String filename) {
        if (mediaId != null) {
            String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS) + UtilBean.getLMSFileName(mediaId, filename);
            if (UtilBean.isFileExists(path)) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                return new BitmapDrawable(context.getResources(),
                        Bitmap.createScaledBitmap(bitmap, displayMetrics.widthPixels / 4, displayMetrics.widthPixels / 4, true));
            }
        }
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    public static AutoCompleteTextView getAutoCompleteTextView(Context context, String hint, List<String> items, AdapterView.OnItemClickListener adaptorClickListener) {
        AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.select_dialog_item, items);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setPadding(24, 24, 24, 24);
        autoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black, 0);
        autoCompleteTextView.setCompoundDrawablePadding(20);
        autoCompleteTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.chardham_edit_text_background));
        autoCompleteTextView.setHintTextColor(ContextCompat.getColor(context, R.color.gray));
        autoCompleteTextView.setMaxLines(1);
        if (hint != null) {
            autoCompleteTextView.setHint(hint);
        }
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(adaptorClickListener);
        autoCompleteTextView.setOnTouchListener((view, motionEvent) -> {
            autoCompleteTextView.showDropDown();
            return false;
        });
        return autoCompleteTextView;
    }
}
