/******************************************************************************
 * Copyright (C) 2016 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 ***************************************************************************/
package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author xiao di fa
 * @ClassName: ResultActivity
 * @Desc: 显示结果页面
 * @date 2016-02-18下午16:55:42
 */
public class ResultActivity extends Activity implements View.OnClickListener {

    private ImageView iv_result;
    private TextView tv_result;
    private Button btn_result;

    private int code;//返回结果code
    private String msg = "";//返回结果描述

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initViews();
        initData();
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        //数据
        code = getIntent().getIntExtra("what", -1);
        msg = getIntent().getStringExtra("msg");
        //图片
        iv_result = (ImageView) findViewById(R.id.iv_result);
        //文字
        tv_result = (TextView) findViewById(R.id.tv_result);
        //相关按钮
        btn_result = (Button) findViewById(R.id.btn_result);
        btn_result.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //处理图片
        if(code == Constant.SUCCESS) {
            iv_result.setImageResource(R.mipmap.success);
        } else {
            iv_result.setImageResource(R.mipmap.error);
        }
        //处理文字
        String[] str = (!TextUtils.isEmpty(msg))?msg.split("\\|"):null;
        if(null != str && str.length >= 2) {
            tv_result.setText(str[0]);
            btn_result.setText(str[1]);
        } else {
            tv_result.setText("系统异常");
            btn_result.setText("系统异常");
        }
    }

    /**
     * 处理按钮事件
     */
    private void skipIntent() {
        Intent intent = new Intent();
        if(code == Constant.BAD_2) {
            intent.putExtra("code", Constant.START);
        } else {
            intent.putExtra("code", Constant.RETURN_TO_FIRST);
        }
        ResultActivity.this.setResult(Activity.RESULT_OK, intent);
        ResultActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_result://结果按钮
                skipIntent();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            skipIntent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
