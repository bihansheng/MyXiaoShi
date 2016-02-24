/******************************************************************************
 * Copyright (C) 2016 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 *****************************************************************************/
package com.example.administrator.myapplication.mycroper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.DisplayUtil;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.camera.CameraManager;

import java.io.IOException;

/**
 * @author lixiangxiang
 *         相机
 * @date 2016/2/17 9:42
 */
public class IdentifActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "yxy";
    private CameraManager cameraManager;
    private boolean isHasSurface = false;

    private SurfaceView surface;
    private ImageButton ibPic;



    private ImageView imPicTake; //边框
    private int x;
    private int y;
    float previewRate = -1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraManager = new CameraManager(getApplicationContext(),IdentifActivity.this);
        cameraManager.createDirectory(IdentifActivity.this,"xiaoshi.jpg");
        setContentView(R.layout.activity_identif);
        initLayout();
    }

    private void initLayout() {
        previewRate = DisplayUtil.getHeight(this)/(float)(DisplayUtil.getWidth(this));
        surface = (SurfaceView) findViewById(R.id.capture_preview);
        ibPic = (ImageButton) findViewById(R.id.ib_pic);
        imPicTake = (ImageView) findViewById(R.id.im_pic_take);

        ibPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraManager.isOpen()) {
                    cameraManager.doTakePicture(imPicTake);
                }

            }
        });

        surface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surface.getHolder().setFixedSize(300, 300);//
        cameraManager.setDealBack(new CameraManager.PictureDealCallBack() {
            @Override
            public void dealCallBackListenter(String path) {
                Intent intent = new Intent();
                intent.setClass(IdentifActivity.this, UseIdentityActivity.class);
                intent.putExtra("url", path);
                IdentifActivity.this.startActivity(intent);
                IdentifActivity.this.finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surface.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surface.getHolder().addCallback(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraManager.closeDriver();
        surface.getHolder().removeCallback(this);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder, previewRate);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(IdentifActivity.this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
            initCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
            cameraManager.stopPreview();

    }
}
