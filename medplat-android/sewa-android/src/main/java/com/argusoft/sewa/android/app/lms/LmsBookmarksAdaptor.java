package com.argusoft.sewa.android.app.lms;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyAudioPlayer;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.model.LmsBookMarkBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LmsBookmarksAdaptor extends BaseAdapter {

    private final Context context;
    private final List<LmsBookMarkBean> lmsBookMarkBeans;
    private MyAlertDialog alertDialog;
    private final LmsServiceImpl lmsService;
    private final MyVideoView selectedVideoView;
    private boolean isAudioRecording;
    private MediaRecorder recorder;
    private String recordedFileName;

    public LmsBookmarksAdaptor(@NonNull Context context, List<LmsBookMarkBean> bookMarkBeans, LmsServiceImpl lmsService, MyVideoView selectedVideoView) {
        this.context = context;
        this.lmsBookMarkBeans = bookMarkBeans;
        this.lmsService = lmsService;
        this.selectedVideoView = selectedVideoView;
    }

    @Override
    public int getCount() {
        if (lmsBookMarkBeans != null) {
            return lmsBookMarkBeans.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (lmsBookMarkBeans != null) {
            return lmsBookMarkBeans.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LmsBookMarkBean item = lmsBookMarkBeans.get(position);

        final LinearLayout mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listview_row_bookmark, null);

        MaterialTextView bookmarkName = mainLayout.findViewById(R.id.bookmark_name);

        MaterialTextView bookmarkDuration = mainLayout.findViewById(R.id.bookmark_duration);
        bookmarkDuration.setText(String.format(Locale.getDefault(), "%d sec", item.getPosition() / 1000));

        MaterialTextView bookmarkNote = mainLayout.findViewById(R.id.bookmark_note);

        ImageButton bookmarkPlay = mainLayout.findViewById(R.id.bookmark_play);

        if (!item.getBookmarkNote().isEmpty()) {
            StringBuilder name = new StringBuilder();
            String[] text = item.getBookmarkNote().split(" ");
            for (int i = 0; i < text.length && i < 3; i++) {
                name.append(text[i]).append(" ");
            }
            bookmarkName.setText(name.toString());
            bookmarkNote.setText(item.getBookmarkNote());
        }

        if (item.getFileName() != null) {
            bookmarkPlay.setVisibility(View.VISIBLE);
            bookmarkPlay.setFocusable(false);
            bookmarkPlay.setOnClickListener(getPlayListener(position, item));
        } else {
            bookmarkPlay.setVisibility(View.INVISIBLE);
        }

        ImageButton editBookmark = mainLayout.findViewById(R.id.edit_bookmark);
        editBookmark.setFocusable(false);
        editBookmark.setOnClickListener(getEditListener(position, item));

        ImageButton deleteBookmark = mainLayout.findViewById(R.id.delete_bookmark);
        deleteBookmark.setFocusable(false);
        deleteBookmark.setOnClickListener(getDeleteListener(position, item));
        return mainLayout;
    }

    private View.OnClickListener getPlayListener(final int position, final LmsBookMarkBean bookMarkBean) {
        return v -> {
            String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_BOOKMARK) + bookMarkBean.getFileName();
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(path));
            MediaPlayer player = MediaPlayer.create(context, uri);
            MyAudioPlayer myAudioPlayer = new MyAudioPlayer(context, bookMarkBean.getBookmarkText(), player);
            myAudioPlayer.show();
        };
    }

    private View.OnClickListener getDeleteListener(final int position, final LmsBookMarkBean bookMarkBean) {
        return v -> {
            View.OnClickListener myListener = v1 -> {
                if (v1.getId() == BUTTON_POSITIVE) {
                    lmsService.deleteBookmark(bookMarkBean);
                    lmsBookMarkBeans.remove(position);
                    notifyDataSetInvalidated();
                }
                alertDialog.dismiss();
            };
            alertDialog = new MyAlertDialog(context,
                    "Are you sure you want to delete this bookmark?",
                    myListener, DynamicUtils.BUTTON_YES_NO);
            alertDialog.show();
        };
    }

    private View.OnClickListener getEditListener(final int position, final LmsBookMarkBean bookMarkBean) {
        return v -> {
            if (selectedVideoView != null && selectedVideoView.isPlaying()) {
                selectedVideoView.pause();
            }

            final View inflate = LayoutInflater.from(context).inflate(R.layout.text_popup_window, null);
            boolean focusable = true;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            final PopupWindow popupWindow = new PopupWindow(inflate, displayMetrics.widthPixels * 8 / 10, ViewGroup.LayoutParams.MATCH_PARENT, focusable);
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            Button editBookmark = inflate.findViewById(R.id.popup_button);
            editBookmark.setText("Update");
            final TextInputLayout note = inflate.findViewById(R.id.popup_note);
            note.getEditText().setText(bookMarkBean.getBookmarkNote());
            editBookmark.setOnClickListener(v12 -> {
                if (!Objects.requireNonNull(note.getEditText()).getText().toString().trim().isEmpty()) {
                    if (recordedFileName != null) {
                        bookMarkBean.setFileName(recordedFileName);
                    }
                    bookMarkBean.setBookmarkNote(note.getEditText().getText().toString().trim());
                    lmsService.updateBookmark(bookMarkBean);
                    recordedFileName = null;
                    popupWindow.dismiss();
                    lmsBookMarkBeans.set(position, bookMarkBean);
                    notifyDataSetInvalidated();
                } else {
                    SewaUtil.generateToast(context, "Please enter bookmark note");
                }
            });

            final ImageButton micButton = inflate.findViewById(R.id.mic_button);
            micButton.setOnClickListener(v1 -> {
                if (!isAudioRecording) {
                    try {
                        isAudioRecording = true;
                        if (recorder == null) {
                            recorder = new MediaRecorder();
                        }
                        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                        recordedFileName = "Record" + timeStamp + ".amr";
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        recorder.setOutputFile(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_BOOKMARK) + recordedFileName);
                        recorder.prepare();
                        recorder.start();
                        micButton.setImageResource(R.drawable.stop_audio);
                        SewaUtil.generateToast(context, "Recording started");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    recorder.stop();
                    recorder.reset();
                    recorder.release();
                    recorder = null;
                    micButton.setImageResource(R.drawable.record_audio);
                    SewaUtil.generateToast(context, "Audio Recorded successfully");
                    isAudioRecording = false;
                }
            });
        };
    }
}
