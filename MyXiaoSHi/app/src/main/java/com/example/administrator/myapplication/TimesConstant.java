/******************************************************************************
 * Copyright (C) 2015 ShenZhen HeShiDai Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.example.administrator.myapplication;

/**
 * @ClassName: TimesConstant
 * @version 2.2
 * @Desc: 时间常量(所有单位都是毫秒)
 * @author Shen fei
 * @date 2015年8月18日下午3:20:03
 * @history v1.0
 *
 */
public class TimesConstant {

	/**
	 * 欢迎页面停留时间
	 */
	public static final long SPLASH = 3 * 1000;

	/**
	 * 获取验证短信码倒计时间隔周期
	 */
	public static final long GET_PSW = 2 * 60 * 1000;

	/**
	 * 注册验证码超时周期
	 */
	public static final long GET_PSW_TIMES_OUT = 5 * 60 * 1000;

	/**
	 * 获取消息平台指令间隔周期
	 */
	public static final long GET_ALLOT = 5 * 60 * 60 * 1000;

	/**
	 * 获取账户菜单间隔周期
	 */
	public static final long GET_ACCOUNT_MENU = 3 * 60 * 60 * 1000;

	/**
	 * 上传崩溃日志间隔周期
	 */
	public static final long UPLOAD_ERROR_LOG = 10 * 60 * 1000;

	/**
	 * 公共网络请求超时周期
	 */
	public static final int REQUEST_TIMEOUT = 20 * 1000;

	/**
	 * 上传头像图片超时周期
	 */
	public static final int UPLOAD_HEAD_PIC_TIMEOUT = 1 * 60 * 1000;

	/**
	 * 下载升级文件超时周期
	 */
	public static final int DOWN_APK_TIMEOUT = 3 * 60 * 1000;

	/**
	 * 更新首页间隔周期
	 */
	public static final long UPLOAD_SHOUYE = 10 * 60 * 1000;

	/**
	 * 手势密码启动间隔周期
	 */
	public static final long LOCK_TIMEOUT = 1 * 60 * 1000;
}
