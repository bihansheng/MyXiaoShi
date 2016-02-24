/******************************************************************************
 * Copyright (C) 2015 ShenZhen HeShiDai Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.example.administrator.myapplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * @ClassName: HLog
 * @version 2.2
 * @Desc: 日志工具（基于开源）
 * @author Shen fei
 * @date 2015年8月7日上午10:13:22
 * @history v1.0
 *
 */
public class HLog {
	/**
	 * Priority constant for the println method; use Log.v.
	 */
	public static final int VERBOSE = 2;

	/**
	 * Priority constant for the println method; use Log.d.
	 */
	public static final int DEBUG = 3;

	/**
	 * Priority constant for the println method; use Log.i.
	 */
	public static final int INFO = 4;

	/**
	 * Priority constant for the println method; use Log.w.
	 */
	public static final int WARN = 5;

	/**
	 * Priority constant for the println method; use Log.e.
	 */
	public static final int ERROR = 6;

	/**
	 * Priority constant for the println method.
	 */
	public static final int ASSERT = 7;

	private static final String LOG_TIME_STAMP_FORMAT = "HH:mm:ss.SSS";

	private static File sLogFile;
	private static BufferedWriter sBufferedFileWriter;
	private static boolean sLog;
	private static boolean sLogToFile;

	/**
	 * Push info log of TAG.
	 */
	private static final String TAG = "sa";

	private HLog() {
	}

	/**
	 * Initialize the logger. This is typically done once per app. <br>
	 * If this method is not called to initialize the logger.
	 * 
	 *            The Android context.
	 * @param log
	 *            is logprint.
	 * @param logToFile
	 *            Whether to log to file. If {@code false}, then all subsequent
	 *            calls will just log to LogCat. If {@code true}, then all
	 *            subsequent calls will log to both LogCat as well as to the
	 *            specified file.
	 * @param file
	 *            The file to log to. This file will be cleared if it already
	 *            exists.
	 * @throws java.io.IOException
	 */
	public static void init(boolean log, boolean logToFile, File file) throws IOException {
		sLog = log;
		sLogToFile = logToFile;
		if (sLogToFile) {
			sLogFile = file;
			sBufferedFileWriter = new BufferedWriter(new FileWriter(sLogFile, false));
		}
	}

	private static void ensurePathInitialized() {
		if (sLogFile == null)
			throw new IllegalStateException("File path not initialized. Have you called Log.init() method?");
	}

	/**
	 * Send a {@link #VERBOSE} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int v(String tag, String msg) {
		return printlnInternal(VERBOSE, tag, msg);
	}

	/**
	 * Send a {@link #VERBOSE} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int v(String tag, String msg, Throwable tr) {
		return printlnInternal(VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
	}

	/**
	 * Send a {@link #DEBUG} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int d(String tag, String msg) {
		return printlnInternal(DEBUG, tag, msg);
	}

	/**
	 * Send a {@link #DEBUG} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int d(String tag, String msg, Throwable tr) {
		return printlnInternal(DEBUG, tag, msg + '\n' + getStackTraceString(tr));
	}

	/**
	 * Send an {@link #INFO} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int i(String tag, String msg) {
		return printlnInternal(INFO, tag, msg);
	}

	/**
	 * Send a {@link #INFO} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int i(String tag, String msg, Throwable tr) {
		return printlnInternal(INFO, tag, msg + '\n' + getStackTraceString(tr));
	}

	/**
	 * Send a {@link #WARN} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int w(String tag, String msg) {
		return printlnInternal(WARN, tag, msg);
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int w(String tag, String msg, Throwable tr) {
		return printlnInternal(WARN, tag, msg + '\n' + getStackTraceString(tr));
	}

	/**
	 * Send a {@link #WARN} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int wtf(String tag, String msg) {
		return printlnInternal(WARN, tag, msg);
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int wtf(String tag, String msg, Throwable tr) {
		return printlnInternal(WARN, tag, msg + '\n' + getStackTraceString(tr));
	}

	/**
	 * Checks to see whether or not a log for the specified tag is loggable at
	 * the specified level.
	 *
	 * The default level of any tag is set to INFO. This means that any level
	 * above and including INFO will be logged. Before you make any calls to a
	 * logging method you should check to see if your tag should be logged. You
	 * can change the default level by setting a system property: 'setprop
	 * log.tag.&lt;YOUR_LOG_TAG> &lt;LEVEL>' Where level is either VERBOSE,
	 * DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will turn off all
	 * logging for your tag. You can also create a local.prop file that with the
	 * following in it: 'log.tag.&lt;YOUR_LOG_TAG>=&lt;LEVEL>' and place that in
	 * /data/local.prop.
	 *
	 * @param tag
	 *            The tag to check.
	 * @param level
	 *            The level to check.
	 * @return Whether or not that this is allowed to be logged.
	 * @throws IllegalArgumentException
	 *             is thrown if the tag.length() > 23.
	 */
	public static boolean isLoggable(String tag, int level) {
		return Log.isLoggable(tag, level);
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param tr
	 *            An exception to log
	 */
	public static int w(String tag, Throwable tr) {
		return printlnInternal(WARN, tag, getStackTraceString(tr));
	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int e(String tag, String msg) {
		return printlnInternal(ERROR, tag, msg);
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int e(String tag, String msg, Throwable tr) {
		return printlnInternal(ERROR, tag, msg + '\n' + getStackTraceString(tr));
	}

	/**
	 * 
	 * 描述：专门用来打印跨进程的消息服务,正式发布屏蔽
	 * 
	 * @author Shen fei
	 * @date 2015年8月7日上午10:50:37
	 * @param msg
	 * @return
	 */
	public static int push(String msg) {
		return Log.println(INFO, TAG, msg);
	}

	/**
	 * Handy function to get a loggable stack trace from a Throwable
	 * 
	 * @param tr
	 *            An exception to log
	 */
	public static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}

		// This is to reduce the amount of log spew that apps do in the
		// non-error
		// condition of the network being unavailable.
		Throwable t = tr;
		while (t != null) {
			if (t instanceof UnknownHostException) {
				return "";
			}
			t = t.getCause();
		}

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}

	/**
	 * Low-level logging call.
	 * 
	 * @param priority
	 *            The priority/type of this log message
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @return The number of bytes written.
	 */
	public static int println(int priority, String tag, String msg) {
		return printlnInternal(priority, tag, msg);
	}

	@SuppressLint("SimpleDateFormat")
	private static int printlnInternal(int priority, String tag, String msg) {
		if (sLogToFile) {
			ensurePathInitialized();
			SimpleDateFormat sdf = new SimpleDateFormat(LOG_TIME_STAMP_FORMAT);
			StringBuilder sb = new StringBuilder(sdf.format(new Date())).append("\t")
					.append(getDisplayForPriority(priority)).append("\t").append(tag).append("\t").append(msg);
			try {
				if (sBufferedFileWriter != null) {
					sBufferedFileWriter.write(sb.toString(), 0, sb.length());
					sBufferedFileWriter.newLine();
					sBufferedFileWriter.flush();
				}
			} catch (IOException e) {
				/*
				 * If there is any problem while writing the log, just print to
				 * logcat and continue. Don't crash or abort!
				 */
				e.printStackTrace();
			}
		}
		if (sLog) {
			return Log.println(priority, tag, msg);
		} else
			return 0;
	}

	private static final String[] PRIORITY_DISPLAY_STRINGS = { "", "", "V", "D", "I", "W", "E", "A" };

	private static String getDisplayForPriority(int priority) {
		return PRIORITY_DISPLAY_STRINGS[priority];
	}
}
