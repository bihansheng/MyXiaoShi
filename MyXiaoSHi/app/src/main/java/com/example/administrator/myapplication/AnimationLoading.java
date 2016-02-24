/******************************************************************************
 * Copyright (C) 2014 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.example.administrator.myapplication;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * @ClassName: AnimationLoading
 * @Desc: 网络加载动画转圈
 * @author Shen fei
 * @date 2014-9-18下午3:23:56
 * 
 */
public class AnimationLoading {

	private static Activity mContext;
	private static Dialog loadingView;

	/**
	 * This show loading
	 * 
	 * @param cancelable
	 * @param context
	 * @param message
	 */
	public static void showLoading(boolean cancelable, Activity context, String message) {
		mContext = context;
		if (loadingView != null && loadingView.isShowing()) {
			return;
		}
		if (StringUtil.isEmptyOrNull(message)) {
			// 如果为空，则设置为默认值：loading...
			message = mContext.getString(R.string.loading_msg);
		}
		Message msg = new Message();
		msg.what = STATUS.SHOW.value();
		msg.arg1 = cancelable == true ? 1 : 0;
		msg.obj = message;
		handler.sendMessage(msg);
	}

	/**
	 * This close the loading view
	 */
	public static void hideLoading() {
		handler.sendEmptyMessage(STATUS.HIDE.value());

	}


	/**
	 * This change loading view text
	 * 
	 * @param message
	 */
	public static void refreshLoadingMessage(String message) {
		if (loadingView != null && loadingView.isShowing()) {
			Message msg = new Message();
			msg.what = STATUS.REFRESH.value();
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}

	static void showLoadingViewInWindow(final boolean cancelable, final String message) {
		try {
			mContext.runOnUiThread(new Runnable() {

				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void run() {
					LayoutInflater inflater = LayoutInflater.from(mContext);
					View v = inflater.inflate(R.layout.anim_loading_layout, null);// 得到加载view
					LinearLayout layout = (LinearLayout) v.findViewById(R.id.anim_layout);// 加载布局
					// main.xml中的ImageView
					ImageView image = (ImageView) v.findViewById(R.id.imageView1);

					ValueAnimator rotationAnim = ObjectAnimator.ofFloat(image, "rotation", 0, 360);
					rotationAnim.setDuration(800);
					rotationAnim.setInterpolator(new LinearInterpolator());
					rotationAnim.setRepeatCount(ValueAnimator.INFINITE);
					rotationAnim.setRepeatMode(ValueAnimator.RESTART);
					rotationAnim.start();
					TextView tipTextView = (TextView) v.findViewById(R.id.loading_label);// 提示文字
					if (!StringUtil.isEmptyOrNull(message)) {
						tipTextView.setVisibility(View.VISIBLE);
						tipTextView.setText(message);// 设置加载信息
					} else {
						tipTextView.setVisibility(View.GONE);
						image.setPadding(30, 30, 30, 30);
					}
					loadingView = new Dialog(mContext, R.style.loading_dialog);// 创建自定义样式dialog
					loadingView.setCancelable(cancelable);
					// loadingView.setCanceledOnTouchOutside(true);
					loadingView.setContentView(layout, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT));// 设置布局
					Window dialogWindow = loadingView.getWindow();
					WindowManager.LayoutParams lp = dialogWindow.getAttributes();
					lp.dimAmount = 0.5f;
					dialogWindow.setAttributes(lp);
					if (!((Activity) mContext).isFinishing()) {
						loadingView.show();
					}
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void hideLoadingViewInWindow() {
		try {
			if (loadingView != null && loadingView.isShowing()) {
				loadingView.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void refreshLoadingView(String message) {
		if (loadingView != null && loadingView.isShowing()) {
			TextView tipTextView = (TextView) loadingView.findViewById(R.id.loading_label);// 提示文字
			tipTextView.setText(message);
		}
	}

	static Handler handler = new Handler() {

		/**
		 * {@inheritDoc}
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			STATUS status = STATUS.valueOf(msg.what);
			switch (status) {
			case HIDE:
				hideLoadingViewInWindow();
				break;
			case REFRESH:
				refreshLoadingView(String.valueOf(msg.obj));
				break;
			case SHOW:
				boolean cancelable = msg.arg1 == 1;
				showLoadingViewInWindow(cancelable, String.valueOf(msg.obj));
				break;
			default:
				break;

			}
			super.handleMessage(msg);
		}

	};

	public enum STATUS {
		SHOW(0), HIDE(1), REFRESH(2);

		private int value = 0;

		STATUS(int value) {
			this.value = value;
		}

		/**
		 * This
		 * 
		 * @return
		 */
		public int value() {
			return this.value;
		}

		/**
		 * This
		 * 
		 * @param value
		 * @return
		 */
		public static STATUS valueOf(int value) {
			switch (value) {
			case 0:
				return SHOW;
			case 1:
				return HIDE;
			case 2:
				return REFRESH;

			}
			return SHOW;
		}
	}
}
