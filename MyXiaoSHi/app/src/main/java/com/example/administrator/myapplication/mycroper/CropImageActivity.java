/******************************************************************************
 * Copyright (C) 2014 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.example.administrator.myapplication.mycroper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.myapplication.HLog;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @author Shen fei
 * @ClassName: CropImageActivity
 * @Desc: 拍照裁剪视图
    * @date 2014-11-12下午7:06:53
            */
    public class CropImageActivity extends Activity implements View.OnClickListener {

        private CropImageView civ;

        private Button crop_cancel, crop_save;

        private String savePath; // 图片保存在SD卡的路径

        private int maxKB = 0; // 压缩图片大小限制

        private boolean isSave;// 是否保存截图成功

        private boolean isSave() {
            return isSave;
        }

    private void setSave(boolean isSave) {
        this.isSave = isSave;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.a_cropimage);
        super.onCreate(savedInstanceState);

        findViews();
        initData();
    }

    /**
     * @Desc: 加载视图
     * @author Shen fei
     * @date 2014-11-13下午2:02:03
     */
    private void findViews() {
        civ = (CropImageView) findViewById(R.id.cropImg);
        crop_save = (Button) findViewById(R.id.crop_save);
        crop_save.setOnClickListener(this);
        crop_cancel = (Button) findViewById(R.id.crop_cancel);
        crop_cancel.setOnClickListener(this);
    }

    /**
     * @Desc: 初始化数据
     * @author Shen fei
     * @date 2014-11-13下午2:03:39
     */
    private void initData() {
        savePath = getIntent().getStringExtra("d_savePath");
        maxKB = getIntent().getIntExtra("d_maxKB", 100);
        HLog.i("yxy", "crop savePath=" + savePath + " maxKB=" + maxKB);
        Bitmap photo = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // 缩减比例
            photo = BitmapFactory.decodeFile(savePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();
        }
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photo != null) {
            civ.setImageBitmap(photo, exifInterface);
        } else {
            Toast.makeText(this, "您的拍照出了点小问题，请再次尝试!", Toast.LENGTH_LONG).show();
            Intent mIntent2 = new Intent();
            setResult(RESULT_CANCELED, mIntent2);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.crop_save:
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean flag = writeImage(civ.getCroppedImage(), savePath, maxKB);
                        HLog.i("yxy", "isSave flag=" + flag);
                        setSave(flag);
                    }
                });
                thread.start();
                HLog.i("yxy", "thread stauts=" + thread.isAlive());
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HLog.i("yxy", "thread stauts=" + thread.isAlive());
                Intent mIntent = new Intent();
                mIntent.putExtra("isSave", isSave());
                setResult(RESULT_OK, mIntent);
                finish();
                break;
            case R.id.crop_cancel:
                Intent mIntent2 = new Intent();
                setResult(RESULT_CANCELED, mIntent2);
                finish();
                break;
        }
    }

    public static boolean writeImage(Bitmap bitmap, String destPath, int maxKB) {
        boolean flag = false;
        try {
            // 压缩
            if (bitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                int options = 100;
                while (baos.toByteArray().length / 1024 > maxKB) {
                    baos.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                    options -= 10;
                }
                // 保存
                HLog.i("yxy", "baos.toByteArray().length=" + baos.toByteArray().length / 1024);
                saveToFile(baos, destPath);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();
        }
        return flag;
    }

    public static void saveToFile(ByteArrayOutputStream os, String fileName) {
        FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(fileName);
                    os.writeTo(fos);
                    os.flush();
                    fos.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
