package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.activity.CustomImageViewerActivity_;
import com.argusoft.sewa.android.app.activity.CustomPDFViewerActivity_;
import com.argusoft.sewa.android.app.activity.CustomVideoPlayerActivity;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.model.AnnouncementBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class AnnouncementArrayAdaptor extends ArrayAdapter<AnnouncementBean> {

    @NonNull
    private Context context;
    private List<AnnouncementBean> list;
    private MyAudioPlayer myAudioPlayer;
    private MediaPlayer player;
    private Dao<AnnouncementBean, Integer> announcementBeansDao;


    public AnnouncementArrayAdaptor(@NonNull Context context, List<AnnouncementBean> list, Dao<AnnouncementBean, Integer> announcementBeansDao) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
        this.announcementBeansDao = announcementBeansDao;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final AnnouncementBean announcementBean = list.get(position);
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.listview_row_announcement, null);

        MaterialTextView announcement = constraintLayout.findViewById(R.id.titleTextView);
        announcement.setText(announcementBean.getSubject());

        MaterialTextView date = constraintLayout.findViewById(R.id.dateTextView);
        date.setText(UtilBean.convertDateToString(announcementBean.getPublishedOn(), false, true, true));
        AppCompatImageView play = constraintLayout.findViewById(R.id.playTextView);
        play.setVisibility(View.INVISIBLE);
        play.setImageResource(R.drawable.ic_play);
        String extension = null;
        if (announcementBean.getFileName() != null) {
            extension = announcementBean.getFileName().substring(announcementBean.getFileName().lastIndexOf(".") + 1);
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("pdf")) {
                play.setImageResource(R.drawable.ic_eye);
            } else {
                play.setImageResource(R.drawable.ic_play);
            }
        }
        if (announcementBean.getIsPlayedAnnouncement() == 1) {
            constraintLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        } else {
            constraintLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.video_view_light_gray));
        }
        if (announcementBean.getFileName() != null) {
            play.setVisibility(View.VISIBLE);

            String finalExtension = extension;
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (announcementBean.getFileName() == null) {
                        SewaUtil.generateToast(context, LabelConstants.NO_FILE_TO_PLAY);
                        return;
                    }
                    String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + announcementBean.getFileName();
                    if (UtilBean.isFileExists(path)) {
                        if (finalExtension.equalsIgnoreCase("jpg") || finalExtension.equalsIgnoreCase("png")) {
                            Intent intent = new Intent(context, CustomImageViewerActivity_.class);
                            intent.putExtra("path", path);
                            intent.putExtra("fileName", announcementBean.getFileName());
                            context.startActivity(intent);
                        } else if (finalExtension.equalsIgnoreCase("pdf")) {
                            Intent intent = new Intent(context, CustomPDFViewerActivity_.class);
                            intent.putExtra("path", path);
                            intent.putExtra("fileName", announcementBean.getFileName());
                            context.startActivity(intent);
                        } else {
                            Uri uri = Uri.parse(path);
                            player = MediaPlayer.create(context, uri);
                            if (player != null) {
                                if ((finalExtension.equalsIgnoreCase("3gp") || finalExtension.equalsIgnoreCase("mp4"))) {
                                    Intent intent = new Intent(context, CustomVideoPlayerActivity.class);
                                    intent.putExtra(GlobalTypes.DATA_MAP, announcementBean.getFileName());
                                    intent.putExtra("isFromAnnouncement", Boolean.TRUE);
                                    context.startActivity(intent);
                                } else {
                                    myAudioPlayer = new MyAudioPlayer(context, announcementBean.getSubject(), player);
                                    myAudioPlayer.show();
                                }
                            } else if (!UtilBean.getSupportedExtensions().contains(finalExtension)) {
                                SewaUtil.generateToast(context, LabelConstants.FILE_IS_NOT_SUPPORTED);
                            } else {
                                SewaUtil.generateToast(context, LabelConstants.FILE_CORRUPTED);
                            }
                        }
                    } else {
                        SewaUtil.generateToast(context, LabelConstants.FILE_DOES_NOT_EXIST);
                    }
                    try {
                        announcementBean.setIsPlayedAnnouncement(1);
                        announcementBeansDao.update(announcementBean);
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "Playing the announcement");
                    }
                }
            };
            play.setOnClickListener(onClickListener);
        }
        constraintLayout.setOnClickListener(v -> {
            try {
                if (announcementBean.getFileName() == null) {
                    announcementBean.setIsPlayedAnnouncement(1);
                    announcementBeansDao.update(announcementBean);
                    notifyDataSetChanged();
                }
            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Playing the announcement");
            }
        });

        return constraintLayout;
    }
}
