/******************************************************************************
 * Copyright (C) 2016 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 * ***************************************************************************/
package com.example.administrator.myapplication;

import java.net.URLEncoder;

/**
 * @author xiao di fa
 * @ClassName: URLEncoderUtil
 * @Desc: 字符串编码转换为UTF-8
 * @date 2016-02-18下午15:35:01
 */
public class URLEncoderUtil {
	
	/**将字符串转换为UTF-8，可以用于上传带特殊字符到Web端
	 * @param s
	 * @return
	 */
	public static String encodeToCharset(String s) {
		String result = s;
		try {
			if(null != s && s.length() > 0 && !"null".equalsIgnoreCase(s)) {
				result = URLEncoder.encode(s, "UTF-8");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
