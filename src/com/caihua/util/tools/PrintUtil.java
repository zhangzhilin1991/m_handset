package com.caihua.util.tools;


import android.util.Log;

/**
 * 12个字节打印
 * 
 * @author Guying
 * 
 */
public class PrintUtil {
	private static final String TAG="PrintUtil";

	/**
	 * 从String中获得tid
	 * @param info 包含TidString
	 * @param num tid个数
	 * @return String[]的tid
	 */
	public static String[] printTid(String info, int num) {
		Log.d(TAG,"split tid info");
		int length = info.length() / num;
		String[] tid = new String[num];
		for (int i = 1; i <= num; i++) {
			String subinfo = info.substring(length * (i - 1), length * i);
			tid[i - 1] = subinfo;
		}
		return tid;
	}
	
	/**
	 * 从byte[]中获得epc数据
	 * @param info
	 * @param length
	 * @return String[]的epc
	 */
	public static String[] printEpc(byte[] info,int length){
		Log.d(TAG,"split epc info");
		Log.d(TAG, "epc info length:"+length);
		
		int num=0;
		int len=0;
		int count=0;
		for(int i=0;i<length;i++){
			//获取epc个数
			len=ByteUtil.toInt(info[i]);
			Log.d(TAG,"len:"+len);
			i+=len;
			num++;
		}
		Log.d(TAG,"num:"+num);
		String[] epc=new String[num];
		for(int i=0;i<length;i++){
			//获取每个epc长度
			len=ByteUtil.toInt(info[i]);
			epc[count]=ByteUtil.byteToString(info,i+1,len);
			i+=len;
			count++;
		}

		
		return epc;
	}
}
