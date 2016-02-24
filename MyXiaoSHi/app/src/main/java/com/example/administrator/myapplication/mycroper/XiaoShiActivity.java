/******************************************************************************
 * Copyright (C) 2016 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 * ***************************************************************************/
package com.example.administrator.myapplication.mycroper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.administrator.myapplication.Constant;
import com.example.administrator.myapplication.HLog;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Result;
import com.example.administrator.myapplication.ResultActivity;
import com.example.administrator.myapplication.SPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.minivision.livebodyauthentication.LiveBodyAuthenticationActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * @author xiao di fa
 * @ClassName: ResultActivity
 * @Desc: 处理结果中转页面
 * @date 2016-02-18下午16:55:42
 */
public class XiaoShiActivity extends Activity implements IUploadFileCallBack {

    private final int REQUEST_CODE = 99;
    private final int RESULT_CODE = 98;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.START:// 开始活体检测
                    Intent intent = new Intent(XiaoShiActivity.this, LiveBodyAuthenticationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("flag", 1);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
                case Constant.RETURN_TO_FIRST:// 返回第一步
                    finish();
                    break;
                default://跳转到结果页
                    Intent resultIntent = new Intent(XiaoShiActivity.this, ResultActivity.class);
                    resultIntent.putExtra("what", msg.what);
                    resultIntent.putExtra("msg", (String) msg.obj);
                    startActivityForResult(resultIntent, RESULT_CODE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoshi);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Message mesg = new Message();
        mesg.what = Constant.START;
        mesg.obj = "人脸识别开启……";
        mHandler.sendMessageDelayed(mesg, 10);
    }

    @Override
    public void setUploadFileCallBack(JSONObject jsonObj) {
        if (jsonObj != null) {
            int code = -1;
            Message mesg = new Message();
            try {
                code = jsonObj.getInt("code");
                mesg.obj = jsonObj.getString("msg");
                HLog.i("yxy", "code=" + code);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (0 == code) {
                mesg.what = Constant.SUCCESS;
                mHandler.sendMessage(mesg);
            } else if (99 == code) {
                mesg.what = Constant.BAD_99;
                mHandler.sendMessage(mesg);
            } else if (2 == code) {
                mesg.what = Constant.BAD_2;
                mHandler.sendMessage(mesg);
            } else if (3 == code) {
                mesg.what = Constant.BAD_3;
                mHandler.sendMessage(mesg);
            }
        }
    }

    @Override
    public void setUploadErrorCallBack(String msg) {
        Message message = new Message();
        message.what = Constant.BAD_6;
        message.obj = msg;
        mHandler.sendMessage(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (data != null) {
                    String listOfFacialResult = data.getStringExtra("facialResult");
                    if(!TextUtils.isEmpty(listOfFacialResult)) {
                        HLog.i("onActivityResult", "size of listOfFacialResult = " + listOfFacialResult.length());
                        HLog.i("yxy", "lsitresult--" + listOfFacialResult);

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ArrayList<Result> al = gson.fromJson(listOfFacialResult, new TypeToken<ArrayList<Result>>() {}.getType());
                        //判断al的数组长度
                        Result rlt1 = null, rlt2 = null, rlt3 = null;
                        Uri picUri1 = null, picUri2 = null, picUri3 = null;

                        if (al != null && al.size() >= 1) {
                            rlt1 = al.get(0);
                        }
                        if (al != null && al.size() >= 2) {
                            rlt2 = al.get(1);
                        }
                        if (al != null && al.size() >= 3) {
                            rlt3 = al.get(2);
                        }

                        String[] params = new String[11];
                        params[0] = Constant.baseUrl + Constant.step3;
                        params[1] = String.valueOf(SPrefUtil.getInstance(XiaoShiActivity.this).getString("id", ""));

                        if (rlt1 != null) {
                            params[2] = rlt1.getName();//action1
                            picUri1 = Uri.fromFile(new File(rlt1.getSave_path()));
                            params[5] = picUri1.getEncodedPath();//file1
                            params[8] = rlt1.getResult().equalsIgnoreCase("YES") ? String.valueOf(1) : String.valueOf(0);//result1
                        }
                        if (rlt2 != null) {
                            params[3] = rlt2.getName();//action2
                            picUri2 = Uri.fromFile(new File(rlt2.getSave_path()));
                            params[6] = picUri2.getEncodedPath();//file2
                            params[9] = rlt2.getResult().equalsIgnoreCase("YES") ? String.valueOf(1) : String.valueOf(0);//result2
                        }
                        if (rlt3 != null) {
                            params[4] = rlt3.getName();//action3
                            picUri3 = Uri.fromFile(new File(rlt3.getSave_path()));
                            params[7] = picUri3.getEncodedPath();//file3
                            params[10] = rlt3.getResult().equalsIgnoreCase("YES") ? String.valueOf(1) : String.valueOf(0);//result3
                        }
                        UploadCardTask uct = new UploadCardTask(XiaoShiActivity.this, XiaoShiActivity.this, "验证中...");
                        uct.execute(params);
                    }
                }
                break;
            case RESULT_CODE:
                if (data != null) {
                    Message mesg = new Message();
                    mesg.what = data.getIntExtra("code", -1);
                    mHandler.sendMessage(mesg);
                }
                break;
            default:
                break;
        }
    }

}