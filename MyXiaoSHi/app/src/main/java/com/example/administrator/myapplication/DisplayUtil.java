/******************************************************************************
 * Copyright (C) 2014 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
import android.view.View;

/**动态获取屏幕宽高
 * @author xiao di fa
 */
public class DisplayUtil {
	
	/**获取屏幕宽度
	 * @param activity Activity
	 * @return 宽度
	 */
	public static int getWidth(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
	
	/**获取屏幕高度
	 * @param activity Activity
	 * @return 高度
	 */
	public static int getHeight(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context Context
     * @param dpValue
     * @return px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context Context
     * @param pxValue
     * @return dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

	/**
	 * 扩大View的触摸和点击响应范围,最大不超过其父View范围
	 * @param view
	 */
	public static void expandViewTouch(final View view) {
		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				view.setEnabled(true);
				view.getHitRect(bounds);
				bounds.top -= 100;
				bounds.bottom += 100;
				bounds.left -= 100;
				bounds.right += 100;
				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}

}
