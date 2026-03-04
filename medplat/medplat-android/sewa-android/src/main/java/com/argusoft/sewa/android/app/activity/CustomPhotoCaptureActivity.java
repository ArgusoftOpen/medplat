/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;

import java.io.IOException;
import java.util.Date;

/**
 * @author kunjan
 */
public class CustomPhotoCaptureActivity extends MenuActivity implements PictureCallback {

    private final CustomPhotoCaptureActivity currentActivity = this;
    private Camera mCamera;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // keep screen active 
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.photo_capture);
        mCamera = getCameraInstance();

        CameraPreview mCameraPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);

        Button captureButton = findViewById(R.id.button_capture);
        captureButton.setOnClickListener(v -> {
            try {
                mCamera.takePicture(null, null, currentActivity);
            } catch (Exception e) {
                SharedStructureData.sewaService.storeException(e, GlobalTypes.EXCEPTION_TYPE_CAMERA);
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(LabelConstants.TECHO_PLUS_CAMERA);

    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e(getClass().getName(), null, e);
        }
        return camera;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap photo = BitmapFactory.decodeByteArray(data, 0, data.length);
        QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(SharedStructureData.currentQuestion);
        if (queFormBean != null) {

            LinearLayout parentLayout = (LinearLayout) queFormBean.getQuestionTypeView();
            ImageView takenImage = parentLayout.findViewById(GlobalTypes.PHOTO_CAPTURE_ACTIVITY);
            takenImage.setVisibility(View.VISIBLE);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                takenImage.setImageBitmap(Bitmap.createScaledBitmap(rotateBitmap(photo, 90f), 240, getResources().getDisplayMetrics().densityDpi * 2, false));
            } else {
                takenImage.setImageBitmap(Bitmap.createScaledBitmap(photo, 240, getResources().getDisplayMetrics().densityDpi * 2, false));
            }

            String photoName = SewaTransformer.loginBean.getUsername() + "_" + SharedStructureData.currentQuestion + "_" + new Date().getTime() + GlobalTypes.IMAGE_CAPTURE_FORMAT;
            queFormBean.setAnswer(photoName);
            SewaUtil.generateToast(this, LabelConstants.PHOTO_CAPTURED);
        }
        finish();
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}

class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;

    // Constructor that obtains context and camera
    @SuppressWarnings("deprecation")
    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.mCamera = camera;
        SurfaceHolder mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mCamera.setDisplayOrientation(90);
            }
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
                               int width, int height) {
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(getClass().getName(), null, e);
        }
    }
}
