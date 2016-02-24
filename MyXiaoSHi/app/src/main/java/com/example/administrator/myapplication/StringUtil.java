package com.example.administrator.myapplication;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Editable;



public class StringUtil {

	// 手机正则表达式
	private final static String phone = "^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";

	// 邮箱正则表达式
	private final static String email = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	// 用户名正则表达式
	private final static String userName = "^\\w{6,16}$";

	/**
	 * 判断是不是一个合法的用户名
	 * 
	 * @return
	 */
	public static boolean isUserName(String name) {
		if (name == null || name.trim().length() == 0) {
			return false;
		}
		Pattern userNameP = Pattern.compile(userName);
		return userNameP.matcher(name).matches();
	}

	/**
	 * 
	 * 描述：用正则表达式判断字符串是否为数字
	 * 
	 * @author Shen fei
	 * @date 2015年5月19日下午6:28:19
	 * @param value
	 * @return
	 */
	public static boolean isNumeric(String value) {
		String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
		return value.matches(regex);
	}

	/**
	 * 判断是不是一个合法的手机号码
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isPhoneNumber(String number) {
		if (number == null || number.trim().length() == 0) {
			return false;
		}
		Pattern phoneP = Pattern.compile(phone);
		return phoneP.matcher(number).matches();
	}

	/**
	 * 判断是不是一个合法的邮箱
	 * 
	 * @return
	 */
	public static boolean isEmail(String mail) {
		if (mail == null || mail.trim().length() == 0) {
			return false;
		}
		Pattern emailP = Pattern.compile(email);
		return emailP.matcher(mail).matches();
	}

	/**
	 * @Desc: 身份证号码，前三后四 其余用“****”代替
	 * @author yxy
	 * @date 2014-11-12上午10:55:53
	 * @param id
	 * @return
	 */
	public static String changeIdNumber(String id) {
		if(isEmptyOrNull(id))
			return id;
		return id.substring(0, 3) + "***********" + id.substring(id.length() - 4, id.length());
	}

	/**
	 * 
	 * 描述：手机号码，前三后四，其余用“****”
	 * 
	 * @author yxy
	 * @date 2014-12-16下午2:21:02
	 * @param num
	 * @return
	 */
	public static String changePhoneNumber(String num) {
		return num.substring(0, 3) + "****" + num.substring(num.length() - 4, num.length());
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmptyOrNull(String input) {
		boolean flag = true;
		if (input == null || "".equals(input) || "null".equals(input)) {
			flag = true;
		} else {
			int length = input.length();
			for (int i = 0; i < length; i++) {
				char c = input.charAt(i);
				if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 查找字符串的子串
	 * 
	 */
	public static boolean indexOfSubstring(String oldstring, String substring) {

		return (oldstring.indexOf(substring) != -1) ? true : false;
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 */
	public static String md5(String string) {
		byte[] hash = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * 将byte数组转换成字符串
	 * 
	 * @param b
	 *            字节数组
	 * @return 字符串
	 */
	public static String byteToString(byte[] b) {
		String result = null;

		try {
			result = new String(b, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @Desc: 格式化double，保留两位小数,四舍五入 但是整数时只有一个小数；如18.0
	 * @author yxy
	 * @date 2014-8-25下午3:44:07
	 * @param a
	 * @param b
	 * @return
	 */
	public static double formatDouble(double a, int b) {
		if (b < 0)
			return a;
		int k = 1;
		for (int i = 0; i < b; i++) {
			k = k * 10;
		}
		return ((double) Math.round(a * k)) / k;
	}

	/**
	 * @Desc: 格式化double，保留两位小数，四舍五入 18.00
	 * @author yxy
	 * @date 2014-9-4下午4:08:40
	 * @param a
	 * @return
	 */
	public static String formatDouble(double a) {
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(a);
	}

	/**
	 * @Desc: 将double类型数据转成 36125.0——》36,125.00的格式,固定保留两位小数 切割方式
	 * @author yxy
	 * @date 2014-8-25下午3:54:00
	 * @return
	 */
	public static String formatNumToString(double obj) {
		if(0 == obj)
			return "0.00";
		String d = formatNumToChina(obj);
		String[] strings = d.split("\\.");
		if (strings != null) {
			if (strings.length < 2) {
				// 没有小数点
				return d + ".00";
			} else {
				String point = strings[1];
				if (point.length() == 0) {
					return strings[0] + ".00";
				}
				if (point.length() == 1) {
					return strings[0] + "." + strings[1] + "0";
				}
				if (point.length() > 2) {
					return strings[0] + "." + point.substring(0, 2);
				}
				return d;
			}
		}
		return "";
	}

	/**
	 * 
	 * 描述：将double类型数据转成 36125.0——》36,125.00的格式，少于两位小数，保留两位小数，多余两位，保留多位小数
	 * 
	 * @author yxy
	 * @date 2015-7-6下午2:59:14
	 * @param obj
	 * @return
	 */
	private static String formatNumToStringWithMorePoint(double obj) {
		String d = addComma(obj);
		String[] strings = d.split("\\.");
		if (strings != null) {
			if (strings.length < 2) {
				// 没有小数点
				return d + ".00";
			} else {
				String point = strings[1];
				if (point.length() == 0) {
					return strings[0] + ".00";
				}
				if (point.length() == 1) {
					return strings[0] + "." + point + "0";
				}
				if (point.length() > 2) {
					return strings[0] + "." + point;
				}
				return d;
			}
		}
		return "";
	}

	/**
	 * @Desc: 将double类型数据转成 36125.0——》36,125的格式
	 * @author yxy
	 * @date 2014-11-10下午5:47:35
	 * @param obj
	 * @return
	 */
	public static String formatNumToChina(double obj) {
		NumberFormat nf = NumberFormat.getInstance(Locale.CHINA); // 格式化数值成1,236.5的形式
		String d = nf.format(obj);
		return d;
	}

	/**
	 * 
	 * 描述：将每三个数字加上逗号处理
	 * 
	 * @author yxy
	 * @date 2015-7-6下午3:21:03
	 * @param str
	 * @return
	 */
	private static String addComma(double str) {
		String value = String.valueOf(str);
		String point = "";
		String[] strings = value.split("\\.");
		if (strings != null) {
			if (strings.length >= 2) {
				// 有小数点
				value = strings[0];
				point = "." + strings[1];
			}
		}
		// 将传进数字反转
		String reverseStr = new StringBuilder(value).reverse().toString();
		String strTemp = "";
		for (int i = 0; i < reverseStr.length(); i++) {
			if (i * 3 + 3 > reverseStr.length()) {
				strTemp += reverseStr.substring(i * 3, reverseStr.length());
				break;
			}
			strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
		}
		// 将[789,456,] 中最后一个[,]去除
		if (strTemp.endsWith(",")) {
			strTemp = strTemp.substring(0, strTemp.length() - 1);
		}
		// 将数字重新反转
		String resultStr = new StringBuilder(strTemp).reverse().toString();
		return resultStr + point;
	}

	/**
	 * @Desc: 将金额转换为以万为单位 500000---》50.00万
	 * @author yxy
	 * @date 2014-9-4下午3:30:45
	 * @param money
	 * @return
	 */
	public static String money2Chinese(double money) {
		double m = money / 10000;
		return formatNumToStringWithMorePoint(m) + "万";
	}

	/**
	 * @Desc: 去除字符串中的空格、回车、换行符、制表符
	 * @author Shen fei
	 * @date 2014-9-22上午11:11:26
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 删除指定字符
	 * 
	 * @param s
	 * @param ch
	 */
	public static void deleteChar(Editable s, char ch) {
		if (s.length() > 0) {
			int pos = s.length() - 1;
			char c = s.charAt(pos);
			if (c == ch) {
				HLog.d("mark", "c= " + c);
				s.delete(pos, pos + 1);
			}
		}
	}

//	/**
//	 * 删除指定字符 重载方法，可以指定多个需要删除的字符
//	 *
//	 * @param s
//	 * @param ch
//	 */
//	public static void deleteChar(Editable s, char[] ch) {
//		if (s.length() > 0) {
//			int pos = s.length() - 1;
//			char c = s.charAt(pos);
//			int len = ch.length;
//			for (int i = 0; i < len; i++) {
//				if (c == ch[i]) {
//					HLog.i("mark", "c= " + c);
//					s.delete(pos, pos + 1);
//					PromptManager.showToast(HApplication.getInstance(), "Error letter.");
//				}
//			}
//		}
//	}

	/**
	 * @Desc: 删除字符串中的某个字符
	 * @author yxy
	 * @date 2014-11-10下午5:38:50
	 * @param s
	 *            原字符串
	 * @param c
	 *            需删除字符串
	 */
	public static String deleteChar(String s, String c) {
		if (s.contains(c)) {
			s = s.replaceAll(c, " ");
			s = StringUtil.replaceBlank(s);
		}
		return s;
	}

	/**
	 * 该字符是否包含中文字符
	 * 
	 * @param c
	 *            字符
	 * @return 如果包含中文返回true，否则false
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 该字符串是否包含中文字符
	 * 
	 * @param str
	 *            字符串
	 * @return 如果包含中文返回true，否则返回false
	 */
	public static boolean isChinese(String str) {
		String endString = "";
		StringBuilder builder = new StringBuilder(endString);
		if ((str != null) && (!"".equals(str))) {
			char[] ch = str.toCharArray();
			int len = ch.length;
			for (int i = 0; i < len; i++) {
				// Log.d("mark", "<---> " + ch[i]);
				if (isChinese(ch[i])) {
					builder.append("该字符串中第一个中文：" + ch[i] + "\n");
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @Desc: 判断是否是由同一字符构成的，true代表由同一字符构成的 false反之
	 * @author yxy
	 * @date 2014-10-16下午12:00:54
	 * @param s
	 * @return
	 */
	public static boolean isSameSymbol(String s) {
		boolean flag = false;
		// 当s为空字符串或者null,认为不是由同一字符构成的
		if (StringUtil.isEmptyOrNull(s)) {
			return flag;
		}
		// 当只有一个字符的时候，认为由同一字符构成
		if (1 == s.length()) {
			flag = true;
			return flag;
		}
		char chacter = s.charAt(0);
		for (int i = 1; i <= s.length() - 1; i++) {
			if (chacter != s.charAt(i)) {
				flag = false;
				return flag;
			}
		}
		flag = true;
		return flag;
	}

	/**
	 * 
	 * 描述：提取字符串中的数字和字符.
	 * 
	 * @author yxy
	 * @date 2015-4-15下午6:55:53
	 * @param str
	 * @return
	 */
	public static String getNumAndPointFromString(String str) {
		str = str.trim();
		String str2 = "";
		if (!isEmptyOrNull(str)) {
			for (int i = 0; i < str.length(); i++) {
				if ((str.charAt(i) >= 48 && str.charAt(i) <= 57) || str.charAt(i) == 46) {// 46=.
					str2 += str.charAt(i);
				}
			}
		}
		return str2;
	}

	/**
	 *
	 * @param context
	 * @param permission
	 * @return
	 */
	public static boolean isPermission(Context context, String permission) {
		PackageManager pm = context.getPackageManager();
		boolean has_permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, "packageName"));
		return has_permission;
	}

}
