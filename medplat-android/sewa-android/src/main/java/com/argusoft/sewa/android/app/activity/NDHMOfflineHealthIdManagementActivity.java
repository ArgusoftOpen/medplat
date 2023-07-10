package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAudioComponent;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.databean.OfflineHealthIdBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@EActivity
public class NDHMOfflineHealthIdManagementActivity extends MenuActivity implements MyAudioComponent.MediaPlayerListener {

    private static final int NDHM_HEALTH_ID_MANAGEMENT_REQUEST_CODE = 1000;
    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout footerLayout;
    private Button nextButton;
    private Integer memberId;
    private String beneficiaryName;
    private MediaPlayer audioMediaPlayer;
    private boolean isAudioRecording = false;
    private MediaRecorder recorder;
    private String fileName = "";

    @OrmLiteDao(helper = DBConnection.class)
    public Dao<ListValueBean, Integer> listValueBeanDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Intent intent = new Intent(context, LoginActivity_.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        memberId = this.getIntent().getIntExtra(RelatedPropertyNameConstants.MEMBER_ID, -1);
        String name = this.getIntent().getStringExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY);
        if (Objects.equals(name, "beneficiary")) {
            name = null;
        }
        beneficiaryName = name;
        setTitle(UtilBean.getTitleText(LabelConstants.HEALTH_ID_MANAGEMENT));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(context, null);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        footerLayout.setVisibility(View.VISIBLE);
        setConsentMessageScreen();
    }

    @UiThread
    public void setConsentMessageScreen() {
        bodyLayoutContainer.removeAllViews();
        TextView consentMessageTextView = MyStaticComponents.getMaterialTextView(this,
                LabelConstants.READY_FOR_GIVE_CONCERN_TO_SHARE_AADHAAR_DETAILS_FOR_CREATE_HEALTH_ID,
                -1, R.style.CustomConsentView, false
        );
        if (consentMessageTextView != null) {
            consentMessageTextView.setLineSpacing(8f,1f);
        }
        bodyLayoutContainer.addView(consentMessageTextView);
        addAudioView();
        nextButton.setOnClickListener(this::showPopUpWindow);
    }

    private void addAudioView() {
        List<ListValueBean> listValueBean = null;
        try {
            listValueBean = listValueBeanDao.queryForEq(FieldNameConstants.Field, "offline_health_id_consent");
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        if (listValueBean != null && !listValueBean.isEmpty()) {
            String fileName = listValueBean.get(0).getValue();
            fileName = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + fileName;
            File file = new File(fileName);
            if (!file.exists()) {
                footerLayout.setVisibility(View.VISIBLE);
                return;
            }
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            if (audioMediaPlayer == null) {
                audioMediaPlayer = MediaPlayer.create(this, uri);
            }
            MyAudioComponent myAudioComponent = new MyAudioComponent(this, R.layout.health_id_consent_audio_player_layout, audioMediaPlayer, this);
            myAudioComponent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            bodyLayoutContainer.addView(myAudioComponent);
        }
    }

    @Override
    public void onCompletion() {
        footerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Intent intent = new Intent();
            intent.putExtra(OfflineHealthIdBean.class.getSimpleName(), data.getStringExtra(OfflineHealthIdBean.class.getSimpleName()));
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        bodyLayoutContainer.removeAllViews();
        setConsentMessageScreen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.audioMediaPlayer != null) {
            audioMediaPlayer.pause();
        }
    }


    private void showPopUpWindow(View view) {
        final View inflate = LayoutInflater.from(this).inflate(R.layout.ndhm_record_consent_popup_window, null);
        boolean focusable = true;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final PopupWindow popupWindow = new PopupWindow(inflate, displayMetrics.widthPixels * 8 / 10, ViewGroup.LayoutParams.MATCH_PARENT, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button addBookmark = inflate.findViewById(R.id.popup_button);
        final ImageButton micButton = inflate.findViewById(R.id.mic_button);
        addBookmark.setOnClickListener(v -> {
            if (isAudioRecording)
                micButton.performClick();
            popupWindow.dismiss();
            Intent intent = new Intent(context, NDHMOfflineHealthIdCreationActivity_.class);
            intent.putExtra(RelatedPropertyNameConstants.MEMBER_ID, memberId);
            intent.putExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, beneficiaryName);
            intent.putExtra(RelatedPropertyNameConstants.AADHAR_SCANNED, getIntent().getStringExtra(RelatedPropertyNameConstants.AADHAR_SCANNED));
            intent.putExtra(RelatedPropertyNameConstants.GENDER, getIntent().getStringExtra(RelatedPropertyNameConstants.GENDER));
            intent.putExtra(RelatedPropertyNameConstants.MOBILE_NUMBER, getIntent().getStringExtra(RelatedPropertyNameConstants.MOBILE_NUMBER));
            intent.putExtra(RelatedPropertyNameConstants.DOB, getIntent().getStringExtra(RelatedPropertyNameConstants.DOB));
            intent.putExtra(RelatedPropertyNameConstants.AUDIO_FILE_NAME, fileName);
            startActivityForResult(intent, NDHM_HEALTH_ID_MANAGEMENT_REQUEST_CODE);
        });

        micButton.setOnClickListener(v -> {
            if (!isAudioRecording) {
                try {
                    isAudioRecording = true;
                    if (recorder == null) {
                        recorder = new MediaRecorder();
                    }
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.getDefault()).format(new Date());
                    fileName = "Record_ABHA_CONSENT_" + timeStamp + ".amr";
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    recorder.setOutputFile(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_AUDIO) + fileName);
                    recorder.prepare();
                    recorder.start();
                    micButton.setImageResource(R.drawable.stop_audio);
                    SewaUtil.generateToast(this, "Recording started");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                addBookmark.setText(R.string.next);
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
                micButton.setImageResource(R.drawable.record_audio);
                SewaUtil.generateToast(this, "Audio Recorded successfully");
                isAudioRecording = false;
            }
        });
    }
}
