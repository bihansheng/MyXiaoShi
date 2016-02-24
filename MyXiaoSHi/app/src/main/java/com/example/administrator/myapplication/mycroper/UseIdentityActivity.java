/******************************************************************************
 * Copyright (C) 2016 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 *****************************************************************************/
package com.example.administrator.myapplication.mycroper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.AnimationLoading;
import com.example.administrator.myapplication.Constant;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.SPrefUtil;

import org.json.JSONObject;

/**
 * @author lixiangxiang
 * @desc
 * @date 2016/2/17 10:53
 */
public class UseIdentityActivity extends Activity {

    private Button btConfirm;
    private TextView tvRefresh;
    private ImageView imUse;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useidentity);
        initLayout();
    }

    private void initLayout() {
        url = getIntent().getStringExtra("url");
        imUse = (ImageView) findViewById(R.id.im_use);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        btConfirm = (Button) findViewById(R.id.btn_confirm);

        if (url!=null){
            //显示的时候旋转固定度数
            try{
                Bitmap bitmap = BitmapFactory.decodeFile(url);
                imUse.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(UseIdentityActivity.this,"图片未生成，请检查sd卡",Toast.LENGTH_SHORT).show();
                return;
            }

        }

        imUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationLoading.showLoading(false,UseIdentityActivity.this,"");
                UseIdentityActivity.this.startActivity(new Intent(UseIdentityActivity.this, IdentifActivity.class));
                finish();
                AnimationLoading.hideLoading();
            }
        });

        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UseIdentityActivity.this.startActivity(new Intent(UseIdentityActivity.this, IdentifActivity.class));
                finish();
            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadCardTask uploadCardTask = new UploadCardTask(new IUploadFileCallBack() {
                    @Override
                    public void setUploadFileCallBack(JSONObject jsonObj) {
                        String code = jsonObj.optString("code");
                        String msg = jsonObj.optString("msg");
                        if ("0".equals(code)) {
                            Intent i = new Intent(UseIdentityActivity.this, XiaoShiActivity.class);
                            UseIdentityActivity.this.startActivity(i);
                            finish();
                        } else if ("1".equals(code)) {
                            Toast.makeText(UseIdentityActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UseIdentityActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void setUploadErrorCallBack(final String msg) {

                        UseIdentityActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UseIdentityActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }, UseIdentityActivity.this, getString(R.string.loading_msg));

                String[] params = new String[3];
                params[0] = Constant.baseUrl + Constant.step2;//请求地址
                params[1] = url; //图片路径
                params[2] = SPrefUtil.getInstance(UseIdentityActivity.this).getString("id", ""); //id
                uploadCardTask.execute(params);//执行异步任务
            }
        });
    }

}
