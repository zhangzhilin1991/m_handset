package com.caihua.handset;

import android.app.Application;

public class myApplication extends Application {
	//------------全局变量--------------------------
	/**
	 * 模块类型
	 */
	public int moduleType;
	/**
	 * 版本号
	 */
	public int libVer;
	/**
	 * 串口文件描述符
	 */
	public int fd;
	/**
	 * 模块类型
	 * 版本信息
	 */
	public String detail;
}
