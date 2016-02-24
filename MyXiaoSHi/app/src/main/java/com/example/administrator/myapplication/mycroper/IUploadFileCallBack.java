/******************************************************************************
 * Copyright (C) 2014 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.example.administrator.myapplication.mycroper;

import org.json.JSONObject;

/**
 * @ClassName: IUploadFileCallBack
 * @Desc: 文件上传结果回调
 * @author Shen fei
 * @date 2014-9-23上午11:42:44
 * 
 */
public interface IUploadFileCallBack {

	/**
	 * @Desc: 文件上传后返回JSONObject
	 * @author Shen fei
	 * @date 2014-9-23上午11:43:57
	 * @param jsonObj
	 */
	public void setUploadFileCallBack(JSONObject jsonObj);

	/**
	 * @Desc: 上传文件失败
	 * @author Shen fei
	 * @date 2014-10-30上午10:22:35
	 */
	public void setUploadErrorCallBack(String msg);
}
