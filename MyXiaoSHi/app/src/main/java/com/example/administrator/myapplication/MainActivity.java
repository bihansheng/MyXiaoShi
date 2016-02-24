/******************************************************************************
 * Copyright (C) 2016 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 * ***************************************************************************/
package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.myapplication.mycroper.IUploadFileCallBack;
import com.example.administrator.myapplication.mycroper.IdentifActivity;
import com.example.administrator.myapplication.mycroper.UploadCardTask;
import com.example.administrator.myapplication.mycroper.XiaoShiManger;

import org.json.JSONObject;

/**
 * @author xiao di fa
 * @ClassName: MainActivity
 * @Desc: 信息输入页面
 * @date 2016-02-18下午15:34:01
 */
public class MainActivity extends Activity implements IUploadFileCallBack {

    private EditText et_username;
    private Button btn_commit;

    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        //姓名
        et_username = (EditText) findViewById(R.id.et_username);
        //身份证号
        //et_idcard = (EditText) findViewById(R.id.et_idcard);
        //开始验证
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString().trim();
                SPrefUtil.getInstance(MainActivity.this).putString("username", username);
                //String id = et_idcard.getText().toString().trim();
                if (StringUtil.isEmptyOrNull(username)) {
                    Toast.makeText(MainActivity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                    et_username.requestFocus();
                    return;
                }
                //上传信息，检查是否已注册
                String[] params = new String[5];
                params[0] = Constant.baseUrl + Constant.step1;//路径
                params[1] = android.os.Build.MODEL;//model  手机型号
                params[2] = String.valueOf(1);//系统类型 1 android
                params[3] = android.os.Build.VERSION.RELEASE;//系统版本
                params[4] = URLEncoderUtil.encodeToCharset(username);

                UploadCardTask uct = new UploadCardTask(MainActivity.this, MainActivity.this, getString(R.string.loading_msg));
                uct.execute(params);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化日志输出
        try {
            HLog.init(true, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //姓名
        username = SPrefUtil.getInstance(MainActivity.this).getString("username", username);
        if(!TextUtils.isEmpty(username)) {
            et_username.setText(username);
            et_username.setSelection(username.length());
        } else {
            et_username.setText("");
        }
    }

    @Override
    public void setUploadFileCallBack(JSONObject jsonObj) {
        if (jsonObj != null) {
            try {
                String id = "";
                if(jsonObj.has("id")) {
                    id = jsonObj.getString("id");
                }
                HLog.i("yxy", "返回的ID是：" + id);
                int code = Integer.parseInt(jsonObj.getString("code"));
                String msg = jsonObj.getString("msg");
                if (code == 0) {
                    XiaoShiManger.takePicture(MainActivity.this, id);//跳转到获取身份证照片流出（第二步）
                } else if (code == 1) {//您已经内测通过了
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                } else if (code == 99) {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "返回数据由有误", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "返回数据为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUploadErrorCallBack(final String msg) {
        if(!TextUtils.isEmpty(msg)) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
