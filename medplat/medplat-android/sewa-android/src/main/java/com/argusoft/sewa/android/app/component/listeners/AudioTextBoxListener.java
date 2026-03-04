package com.argusoft.sewa.android.app.component.listeners;

import android.content.Context;
import android.media.MediaRecorder;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author alpeshkyada
 */
public class AudioTextBoxListener implements View.OnFocusChangeListener, View.OnClickListener {

    private static final String TAG = "AudioTextBoxListener";
    private final QueFormBean queFormBean;
    private final String[] answer;
    private final File mFileName;
    private MediaRecorder mRecorder = null;
    private boolean flag;
    private String fileType;
    private EditText editText;

    public AudioTextBoxListener(QueFormBean queFormBean, Context context) {
        this.queFormBean = queFormBean;
        fileType = queFormBean.getQuestion();
        if (fileType != null) {
            if (fileType.trim().equalsIgnoreCase("District:")) {
                fileType = GlobalTypes.FILE_TYPE_AUDIO_DISTRICT;
            } else if (fileType.trim().equalsIgnoreCase("Block:")) {
                fileType = GlobalTypes.FILE_TYPE_AUDIO_BLOCK;
            } else if (fileType.trim().equalsIgnoreCase("Village:")) {
                fileType = GlobalTypes.FILE_TYPE_AUDIO_VILLAGE;
            } else {
                fileType = "Other_Audio";
            }
        }

        answer = new String[2];
        answer[0] = "";
        answer[1] = "";
        flag = true;
        String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_AUDIO) + DynamicUtils.getLoopId(queFormBean) + "_" + new Date().getTime() + GlobalTypes.AUDIO_RECORD_FORMAT;
        mFileName = new File(path);
    }

    @Override
    public void onFocusChange(View view, boolean bln) {
        editText = (EditText) view;
        if (!bln) {
            answer[0] = editText.getText().toString();
            setAnswer();
        }
    }

    @Override
    public void onClick(View view) {
        if (editText == null) {
            LinearLayout mainLayout = (LinearLayout) queFormBean.getQuestionTypeView();
            if (mainLayout != null) {
                editText = mainLayout.findViewById(IdConstants.AUDIO_TEXT_BOX_ID);
            }
        }
        ImageView myImageView = (ImageView) view;
        if (flag) {
            // start
            try {
                startRecording();
                myImageView.setImageResource(R.drawable.stop_audio);
                if (editText != null) {
                    editText.setHint(LabelConstants.AUDIO_RECORDING);
                }
                flag = !flag; // reverse the flag like toggle 
                setMandatory(true);
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), LabelConstants.ERROR_IN_AUDIO_RECORDING);
                Log.e(getClass().getSimpleName(), null, e);
                myImageView.setImageResource(R.drawable.record_audio);
                if (editText != null) {
                    editText.setHint(LabelConstants.AUDIO_NOT_RECORDED);
                }
                setMandatory(false);
            } finally {
                answer[1] = "";
            }

        } else {
            //stop
            try {
                stopRecording();
                myImageView.setImageResource(R.drawable.record_audio);
                if (editText != null) {
                    editText.setHint(LabelConstants.AUDIO_RECORDED);
                }
                flag = !flag; // reverse the flag like toggle 
                setMandatory(false);
            } catch (Exception a) {
                Log.e(getClass().getSimpleName(), LabelConstants.RECORDER_NOT_STOPPED);
                Log.e(getClass().getSimpleName(), null, a);
                answer[1] = "";
                myImageView.setImageResource(R.drawable.stop_audio);
                if (editText != null) {
                    editText.setHint(LabelConstants.AUDIO_NOT_RECORDED);
                }
                setMandatory(true);
            } finally {
                setAnswer();
            }
        }

    }

    private void startRecording() throws IOException {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(mFileName.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        mRecorder.prepare();
        mRecorder.start();
        Log.i(TAG, "Recording is started");
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Log.i(TAG, "Recording is stopped");
        // answer is set
        answer[1] = mFileName.getName();
        SharedStructureData.audioFilesToUpload.put(mFileName.getName(), fileType);
    }

    private void setAnswer() {
        if ((answer[0] != null && answer[0].trim().length() > 0) || (answer[1] != null && answer[1].trim().length() > 0)) {
            queFormBean.setAnswer(answer[0] + GlobalTypes.KEY_VALUE_SEPARATOR + answer[1]);
        } else {
            queFormBean.setAnswer(null);
        }
        Log.i(getClass().getSimpleName(), queFormBean.getQuestion() + " = " + queFormBean.getAnswer());

    }

    private void setMandatory(boolean flag) {
        if (flag) {
            queFormBean.setAnswer(null);
            queFormBean.setMandatorymessage(LabelConstants.STOP_RECORDING);
            queFormBean.setIsmandatory(GlobalTypes.TRUE);
        } else {
            queFormBean.setIsmandatory(GlobalTypes.FALSE);
        }
    }
}
