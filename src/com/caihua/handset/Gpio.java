/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek Software")
 * have been modified by MediaTek Inc. All revisions are subject to any receiver's
 * applicable license agreements with MediaTek Inc.
 */

/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 */
/* MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek Software")
 * have been modified by MediaTek Inc. All revisions are subject to any receiver's
 * applicable license agreements with MediaTek Inc.
 */

package com.caihua.handset;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;



public class Gpio {
	private static final String TAG="Gpio";
	
	public final static int PIN_pwr=118;
	/*为外围设备的电源控制IO，作为output,
	 * 开机默认为低电平，
	 * 设备软件运行时或设备工作时，将GPIO118置高电平，
	 * 则所以外设电源打开，包括红外模块、RFID模块。*/

	public final static int PIN_infrared_trigger=19;
	/*作为红外触发设备的检测IO，
	 * 作为INPUT，默认为高电平输入，
	 * 当有物体经过，则输入低电平。*/

	public final static int PIN_stop_pwr=110;
	/*为隔断设备控制IO，
	 *作为OUTPUT，默认为低电平，
	 *当需要进行阻止箱体经过时，输出高电平。*/

	private static  File file_setpin;
	private static  File file_getpin;

	static{
		file_setpin = new File("/sys/class/misc/mtgpio/setpin");		// 设置gpio口设备文件路径
		file_getpin = new File("/sys/class/misc/mtgpio/getpin");		// 读取gpio口设备文件路径
	}
	
	public static int STOP_CONTROL=1;
	public static int NORMAL_CONTROL=0;
	
	/**
	 * 初始化Gpio
	 */
	public static void initGpio()
	{
		Log.d(TAG, "init gpio");
		WriteGpio(PIN_pwr,1,1);
		//WriteGpio(PIN_infrared_trigger, 0,0);
		WriteGpio(PIN_stop_pwr, 1, NORMAL_CONTROL);
		/**
		 * 等待GPIO初始化完成
		 */
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	/**
	 * 退出Gpio
	 */
	public static void exitGpio()
	{
	Log.d(TAG, "exit gpio");
		WriteGpio(PIN_pwr,1,0);
		WriteGpio(PIN_stop_pwr, 1, NORMAL_CONTROL);
	}
	
	/**
	 * 读GPIO
	 * @param pin gpio号码
	 * @return 高返回"1" 低返回"0"
	 */
	public static int readGpio(int pin){
		String cmd=String.valueOf(pin)+"\n";
		writeFile(file_getpin , cmd);
		String result=readFile(file_getpin);
//		MyLog.e(EmGpio.class, result);
		return result.startsWith("0")?0:1;
	}

	/**
	 * 设置GPIO
	 * @param pin gpio号码
	 * @param mode 0为输入, 1为输出
	 * @param value 1为高，0为低
	 */
	public static void WriteGpio(int pin,int mode,int value){
		String cmd=String.valueOf(pin)+" 0 "+String.valueOf(mode)+" "+String.valueOf(value)+"\n";
		writeFile(file_setpin , cmd);
	}

	private static String readFile(File fn) {
		FileReader f;
		int len;

		f = null;
		try {
			f = new FileReader(fn);
			String s = "";
			char[] cbuf = new char[200];
			while ((len = f.read(cbuf, 0, cbuf.length)) >= 0) {
				s += String.valueOf(cbuf, 0, len);
			}
			return s;
		} catch (IOException ex) {
			return "0";
		} finally {
			if (f != null) {
				try {
					f.close();
				} catch (IOException ex) {
					return "0";
				}
			}
		}
	}

	// 参数1 gpio号码，
	// 参数2 gpio模式（0为gpio模式，贵司只用gpio模式，参数3为gpio方向）
	// 参数3 gpio方向 (0为输入, 1为输出）,注意此为方案公司这边的方向，我司的输入对应贵司那边的输出
	// 参数4 gpio电平（1为高，0为低）
	private static String writeFile(File fn, String str) {
		if (fn.exists()) {
			try {
				FileWriter fw = new FileWriter(fn);
				fw.write(str);
				fw.flush();
				fw.close();
			} catch (IOException ex) {
				return "0";
			}
		}
		return "0";
	}
}
