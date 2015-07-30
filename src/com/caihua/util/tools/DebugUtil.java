package com.caihua.util.tools;

import java.io.File;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 调试工具
 * 
 * @author jiwenbin(jiwenbin010@163.com)
 * @Date 2014-4-2下午02:21:49
 */
public class DebugUtil {
	public static final int Ok = 1;
	public static final int Cancel = 2;

	/**
	 * 获取sd卡路径
	 * 
	 * @return
	 */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		if (sdDir != null)
			return sdDir.toString();
		else
			return null;
	}

	/**
	 * 弹出消息
	 * 
	 * @param msg
	 */
	public static void showToast(Context _context, String msg) {
		Toast.makeText(_context, msg, Toast.LENGTH_LONG).show();
	}

	// 将资源名称转换为id
	public static int[] getResouces(String[] ss, Resources res) {
		int[] ids = new int[ss.length];
		for (int i = 0; i < ss.length; i++) {
			int id = res.getIdentifier(ss[i], // 资源名称的字符串数组
					"drawable", // 资源类型
					"com.caihua.handset"); // R类所在的包名
			ids[i] = id;
		}
		return ids;
	}

	// 将资源名称转换为id
	public static int getResoucesId(String idstr, Resources res) {
		int id = res.getIdentifier(idstr, // 资源名称的字符串数组
				"id", // 资源类型
				"com.caihua.handset"); // R类所在的包名
		return id;
	}

	// 完整性验证
	public static boolean validate(EditText editText) {
		String message = editText.getText().toString();
		if (message == null || message.equals("")) {
			return false;
		}
		return true;
	}
}
