/******************************************************************************
 * Copyright (C) 2014 ShenZhen HeShiDai Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.example.administrator.myapplication;

/**
 * @ClassName: Constant
 * @version 1.0
 * @Desc: 常量配置
 * @author shenfei
 * @date 2016年1月7日下午4:39:48
 * @history v1.0
 *
 */
public class Constant {
    public static final String baseUrl = "http://183.62.205.226:8928/hsd-approve-web/";

    public static final String step1 = "step1.do";
    public static final String step2 = "step2.do";
    public static final String step3 = "step3.do";

    public static final int RETURN_TO_FIRST = 100;//返回第一步
    public static final int START = 101;//开始活体检测
    public static final int SUCCESS = 102;//成功
    public static final int BAD_99 = 103;//系统异常
    public static final int BAD_2 = 104;//验证失败但可继续验证
    public static final int BAD_3 = 105;//验证失败不可继续验证
    public static final int BAD_6 = 106;//该程序异常
}
