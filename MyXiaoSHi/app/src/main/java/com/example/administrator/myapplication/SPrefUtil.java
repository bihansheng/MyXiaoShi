package com.example.administrator.myapplication;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @ClassName: SPrefUtil
 * @version 2.2
 * @Desc: SharedPreference的包装类(单例)
 * @author Shen fei
 * @date 2015年9月18日下午6:02:49
 * @history v1.0
 */
public final class SPrefUtil {

	private static final String NAME = "hsdjr";
	private static SPrefUtil mSharedPrefer;
	private SharedPreferences preferences;

	/** 用户账号 */
	public static final String KEY_USER_ACCOUNT = "user_account";
	/** 用户密码 */
	public static final String KEY_USER_PWD = "user_pwd";
	/** token */
	public static final String KEY_ACCESS_TOKEN = "access_token";
	/** session */
	public static final String KEY_SESSION_ID = "session_Id";
	/** 用户是否已经登录 */
	public static final String KEY_IS_USER_LOGINED = "is_user_logined";
	/** 用户是否已经实名认证 */
	public static final String KEY_IS_USER_CER = "is_user_certificate";
	/** 用户是否已经邮箱认证 */
	public static final String KEY_IS_USER_EMAIL = "is_user_email";
	/** 用户邮箱 */
	public static final String KEY_EMAIL = "key_email";
	/** 真实姓名 */
	public static final String KEY_REAL_NAME = "realName";
	/** 身份证号码 */
	public static final String KEY_ID_NO = "idNo";
	/** 标示已经开户 */
	public static final String KEY_IS_SYNC = "is_sync";
	/** 记录是否已经设置过手势密码 */
	public static final String KEY_IS_LOCKED = "is_locked";
	/** 记录已经输入错误几次手势密码 */
	public static final String KEY_LOCK_WRONG_NUM = "lock_wrong_num";
	/** 手势 */
	public static final String LOCK_KEY = "lock_key";
	/** 标记是否第一次运行（包括新版本） */
	public static final String FIRST_RUN = "first_run";
	/** 标记第一次运行的版本 */
	public static final String VERSION_CODE = "version_code";
	/** 渠道号 */
	public static final String CHANNEL = "channel";
	/** 签名信息 */
	public static final String SIGN = "singCode";
	/** 当前版本号 */
	public static final String VERSION_NAME = "version_name";
	/** 当前系统版本 */
	public static final String CURRENTOS = "systemType";
	/** 是否上报过装机量 */
	public static final String IS_SUBMIT_INSTALL = "is_submit_install";
	/** 获取机器指令请求是否成功 */
	public static final String GET_ALLOT = "get_allot";
	/** 获取机器指令时间戳 */
	public static final String GET_ALLOT_TIME = "get_allot_time";
	/** 崩溃日志上传天数计数 */
	public static final String UPLOAD_FILE_LOG = "upload_file_log";
	/** 我的菜单更新天数计数 */
	public static final String SETTING_MENU = "setting_menu";
	/** 我的菜单数据 */
	public static final String SETTING_MENU_DATA = "setting_menu_data";
	/** 是否开启用户体验计划 */
	public static final String CHANGE_USER_TY = "change_user_ty";
	/** 上次获取版本时间 */
	public static final String LAST_GETVERSION_TIME = "last_getversion_time";
	/** 服务器app版本名称 */
	public static final String SERVER_VERSION_NAME = "server_version_name";
	/** 服务器app版本号 */
	public static final String SERVER_VERSION_CODE = "server_version_code";
	/** 投资列表选项 */
	public static final String INVEST_TITLE = "invest_title";
	/** 更新首页频率 */
	public static final String UPDATE_SHOUYE = "update_shouye";

	/**
	 * 私有化构造
	 * 
	 * @param context
	 */
	private SPrefUtil(Context context) {
		preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 
	 * 描述：获取单例
	 * 
	 * @author Shen fei
	 * @date 2015年9月23日下午3:34:06
	 * @param context
	 * @return
	 */
	public static SPrefUtil getInstance(Context context) {
		if (mSharedPrefer == null) {
			synchronized (SPrefUtil.class) {
				if (mSharedPrefer == null) {
					mSharedPrefer = new SPrefUtil(context);
				}
			}
		}
		return mSharedPrefer;
	}

	public void putString(String key, String value) {
		preferences.edit().putString(key, value).commit();
	}

	public String getString(String key, String defaultValue) {
		return preferences.getString(key, defaultValue);
	}

	public void putBoolean(String key, boolean value) {
		preferences.edit().putBoolean(key, value).commit();
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return preferences.getBoolean(key, defaultValue);
	}

	public void putInt(String key, int value) {
		preferences.edit().putInt(key, value).commit();
	}

	public int getInt(String key, int defaultValue) {
		return preferences.getInt(key, defaultValue);
	}

	public void putFloat(String key, float value) {
		preferences.edit().putFloat(key, value).commit();
	}

	public float getFloat(String key, float defaultValue) {
		return preferences.getFloat(key, defaultValue);
	}

	public void putLong(String key, long value) {
		preferences.edit().putLong(key, value).commit();
	}

	public long getLong(String key, long defaultValue) {
		return preferences.getLong(key, defaultValue);
	}

	/**
	 * 
	 * 描述：移除某个key值已经对应的值
	 * 
	 * @author Shen fei
	 * @date 2015年9月23日下午3:56:07
	 * @param key
	 */
	public void remove(String key) {
		preferences.edit().remove(key).commit();
	}

	/**
	 * 
	 * 描述：清除所有数据
	 * 
	 * @author Shen fei
	 * @date 2015年9月23日下午3:56:52
	 */
	public void clear() {
		preferences.edit().clear().commit();
	}

	/**
	 * 
	 * 描述：查询某个key是否已经存在
	 * 
	 * @author Shen fei
	 * @date 2015年9月23日下午3:57:58
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		return preferences.contains(key);
	}

	/**
	 * 
	 * 描述：返回所有的键值对
	 * 
	 * @author Shen fei
	 * @date 2015年9月23日下午4:00:07
	 * @return
	 */
	public Map<String, ?> getAll() {
		return preferences.getAll();
	}
}
