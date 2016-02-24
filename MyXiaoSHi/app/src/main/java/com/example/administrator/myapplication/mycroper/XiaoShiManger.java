/******************************************************************************
 * Copyright (C) 2016 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.example.administrator.myapplication.mycroper;

import android.content.Context;
import android.content.Intent;

import com.example.administrator.myapplication.Constant;
import com.example.administrator.myapplication.SPrefUtil;
import com.example.administrator.myapplication.StringUtil;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @author wankun
 * @desc
 * @date 2016/2/19 14:39
 */
public class XiaoShiManger {

    /**
     * 跳转到获取身份证的流程
     * @param context
     */
    public static void takePicture (Context context,String id){
        SPrefUtil.getInstance(context).putString("id", id);//将获取的id存起来，方便后面的步骤去去获取
        Intent identifyIntent = new Intent(context, IdentifActivity.class);
        context.startActivity(identifyIntent);
    }

    /**
     * 将参数转化为UploadCardTask所需要的类型
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public  static MultipartEntity getEntity(String[] params) throws UnsupportedEncodingException {
        MultipartEntity mpEntity = new MultipartEntity();
        if (params[0].endsWith(Constant.step1)) {// 第一步，填写姓名获取id
            StringBody model = new StringBody(params[1]);
            mpEntity.addPart("model", model);
            StringBody systemType = new StringBody(params[2]);
            mpEntity.addPart("systemType", systemType);
            StringBody systemVersion = new StringBody(params[3]);
            mpEntity.addPart("systemVersion", systemVersion);
            StringBody userName = new StringBody(params[4]);
            mpEntity.addPart("userName", userName);
        } else if (params[0].endsWith(Constant.step2)) {// 第二步，上传身份证文件
            File file = new File(params[1]);
            ContentBody cbFile = new FileBody(file);
            mpEntity.addPart("file", cbFile);
            StringBody id = new StringBody(params[2]);
            mpEntity.addPart("id", id);
        } else if (params[0].endsWith(Constant.step3)) {//第三步， 上传活体结果
            StringBody idBody = new StringBody(params[1]);
            mpEntity.addPart("id", idBody);
            if (!StringUtil.isEmptyOrNull(params[2])) {
                StringBody action1 = new StringBody(params[2]);
                mpEntity.addPart("action1", action1);
            }
            if (!StringUtil.isEmptyOrNull(params[3])) {
                StringBody action2 = new StringBody(params[3]);
                mpEntity.addPart("action2", action2);
            }
            if (!StringUtil.isEmptyOrNull(params[4])) {
                StringBody action3 = new StringBody(params[4]);
                mpEntity.addPart("action3", action3);
            }
            if (!StringUtil.isEmptyOrNull(params[5])) {
                File file1 = new File(params[5]);
                ContentBody cbFile1 = new FileBody(file1);
                mpEntity.addPart("file1", cbFile1);
            }
            if (!StringUtil.isEmptyOrNull(params[6])) {
                File file2 = new File(params[6]);
                ContentBody cbFile2 = new FileBody(file2);
                mpEntity.addPart("file2", cbFile2);
            }
            if (!StringUtil.isEmptyOrNull(params[7])) {
                File file3 = new File(params[7]);
                ContentBody cbFile3 = new FileBody(file3);
                mpEntity.addPart("file3", cbFile3);
            }

            if (!StringUtil.isEmptyOrNull(params[8])) {
                StringBody result1 = new StringBody(params[8]);
                mpEntity.addPart("result1", result1);
            }
            if (!StringUtil.isEmptyOrNull(params[9])) {
                StringBody result2 = new StringBody(params[9]);
                mpEntity.addPart("result2", result2);
            }
            if (!StringUtil.isEmptyOrNull(params[10])) {
                StringBody result3 = new StringBody(params[10]);
                mpEntity.addPart("result3", result3);
            }
        }
        return  mpEntity ;
    }
}
