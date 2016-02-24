/******************************************************************************
 * Copyright (C) 2014 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.example.administrator.myapplication.mycroper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.example.administrator.myapplication.AnimationLoading;
import com.example.administrator.myapplication.HLog;
import com.example.administrator.myapplication.TimesConstant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Arrays;


/**
 * @author Shen fei
 * @ClassName: UploadCardTask
 * @Desc: 文件上传工具类
 * @date 2014-9-19下午2:49:49
 */
public class UploadCardTask extends AsyncTask<String, Integer, JSONObject> {

    private IUploadFileCallBack iufcb; // 回调实例

    private Context mContext;

    private String msg;

    public UploadCardTask(IUploadFileCallBack instance, Context context, String message) {
        this.iufcb = instance;
        this.mContext = context;
        this.msg = message;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (msg != null)
            AnimationLoading.hideLoading();
        // 最终结果的显示
        if (result != null) {
            this.iufcb.setUploadFileCallBack(result);
        }
    }

    @Override
    protected void onPreExecute() {
        // 开始前的准备工作
        HLog.i("yxy", "load...");
        if (msg != null)
            AnimationLoading.showLoading(false, (Activity) mContext, msg);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // 显示进度
        HLog.i("yxy", "loading..." + values[0] + "%");
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        // 这里params[0]和params[1]是execute传入的两个参数
        String uploadUrl = params[0];
//        String userName = "";
//        String userId = "";
//        String filePath = "";
//        if (params.length == 4) {// 上传身份证信息
//            filePath = params[1];
//            userName = params[2];
//            userId = params[3];
//        } else {// 上传活体结果
//
//        }
        HLog.e(">>>>>>", Arrays.asList(params).toString());
        JSONObject json = null;

        try {
            HttpClient httpclient = MySSLSocketFactory.getNewHttpClient(TimesConstant.UPLOAD_HEAD_PIC_TIMEOUT);

            HttpPost httppost = new HttpPost(uploadUrl);
            MultipartEntity mpEntity = XiaoShiManger.getEntity(params);
            httppost.setEntity(mpEntity);
            HLog.i("yxy", "executing request " + httppost.getRequestLine());

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            HLog.i("yxy", response.getStatusLine() + "");
            if (resEntity != null) {
                String str = EntityUtils.toString(resEntity, "UTF-8");
                HLog.e(">>>>", str);
                try {
                    json = new JSONObject(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (resEntity != null) {
                resEntity.consumeContent();
            }

            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            if (msg != null)
                AnimationLoading.hideLoading();
            iufcb.setUploadErrorCallBack(e.getMessage());
        }
        return json;
    }

}
