package com.caihua.handset;

/**
 * 
 * @author zhangzhilin
 * 
 *         <p>
 *         rfid模块操作类
 *         上海模块可读gen2和gjb
 *         <P>
 *         北京，R2000只能读gen2
 *         <p>
 */
public class Rfid {
	
	// -------通用接口-----------------------
	/**
	 * 获取版本号
	 * 
	 *@return 版本号（例：15042210, 15代表年，04代表月份，22代表日期，10代表V1.0）
	 *
	 */
	public static native int getLibVer();

	/**
	 * 获取模块类型
	 * 
	 *@return 0:未知类型；1：上海模块；2：北京模块；3：R2000模块
	 * 
	 * 
	 */
	public static native int getModuType();
	
	/**
	 * 打开串口
	 * 
	 * @param type getModuType函数的返回值
	 * @return 串口文件描述符
	 */
	public static native int openDev(int type);
	
	/**
	 * 关闭串口
	 * @param fd 串口文件描述符
	 */
	public static native void closeDev(int fd);
	
	/**
	 * 初始化模块（通用）
	 * @return 设置成功或者失败（0：success；<0：failed）
	 */
	public static native int init();
	
	/**
	 * 获取发射功率值
	 * @return 发射功率值或者失败原因（小于0）
	 */
	public static native int getPower();

	/**
	 * 设置发射功率值
	 * @param power 要设置的发射功率值
	 * @return 设置成功或者失败（0：success；<0：failed）
	 */
	public static native int setPower(int power);

	/**
	 * 用不到
	 * 设置工作天线
	 * @param iAnt 工作天线值（对应天线被设置总和。天线4、天线3、天线2、天线1被设定为工作时，值分别为8、4、2、1）
	 * @return 0:success; <0：fail
	 * @deprecated
	 */
	public static native int setWorkAnt(int iAnt);
	
	/**
	 * 用不到
	 * 获取工作天线
	 * @return 获取天线成功或者失败（>0：success；<=0：failed。其中天线值的含义与setWorkAnt函数中的iAnt相同）
	 *@deprecated
	 */
	public static native int getWorkAnt();
	
	/**
	 * 获取EPC(gen)
	 * @return 标签的EPC数据(单个或多个)或者访问标签失败（NUL）
	 */
	public static native byte[] readEpc();
	
	/**
	 * 获取TID（gen）
	 * @return 标签的Tid数据(单个或多个)或者访问标签失败（NUL）
	 */
	public static native byte[] readTid();
	
	/**
	 * 
	 * 设置读取标签的区域、起始地址、字长度
	 * @param area 标签的区域（国军标与国标不同)
	 * @param startAddr  读取标签的起始地址
	 * @param wordLength 读取标签的字长度
	 * @return 设置成功或者失败（0：success；<0：failed）
	 */
	public static native int initComnParm(int area,int startAddr,int wordLength);
	
	//-----------------上海模块接口------------------------------
	/**
	 * 跳转到主程序（使用此模块首先要发送的指令信息）
	 * @return 设置成功或者失败（0：success；<0：failed）
	 */
	public static native int hoptomain();
	
	/**
	 * 用不到
	 * @return 0:success; <0：fail
	 */
	public static native int resetReader();
	
	/**
	 * 不用
	 * 设置84指令的区域、地址、数量
	 * @param type
	 * @param typeval
	 * @return 
	 */
	public static native int initCmd(int type,int typeval);
	
	/**
	 * 
	 * 设置盘存类型(setInventType) 注：仅有上海模块设置此项，其他模块省略
	 * 必须在读Tid/EPC之前设置
	 * @param type 0:epc,1:tid
	 * @return 0:success; <0：fail.
	 */
	public static native int setInventType(int type);
	
	/**
	 * 不用
	 * @return 0:success; <0：fail
	 */
	public static native int sendCmd60();
	
	/**
	 * 设置工作模式
	 * @param mode 0：gjb,1:epc(gen)
	 * @return 0:success; <0：fail
	 */
	public static native int setWorkMode(int mode);
	//diaoyong
	/**
	 * 获取EPC
	 * @return 标签的EPC数据(多个)或者访问标签失败（NUL）
	 */
	public static native byte[] readGjbEpc();
	
	/**
	 * 获取Gjb的TID
	 * 必须先调用 {@link #initCommonParm(int area,int startAddr,int wordLength)}
	 * @return Gjb标签的Tid数据(单个)或者访问标签失败（NUL）
	 * 
	 */
	public static native byte[] readGjbSingleTid();
	
	/**
	 * 获取TID
	 * 直接调用 {@link #readTid()}即可
	 * 必须先调用 {@link #initComnParm(int area,int startAddr,int wordLength)}
	 * @return 标签的Tid数据(单个)或者访问标签失败（NUL）
	 *
	 */
	public static native byte[] readSingleTid();
	
	/**
	 * 读取国标epc
	 * @return  标签的EPC数据(多个)或者访问标签失败（NUL）
	 */
	public static native byte[]  readGbEpc();
	
	/**
	 * 读取国标singleTid
	 * @return 标签的Tid数据(单个)或者访问标签失败（NUL）
	 */
	public static native byte[]  readGbSingleTid();
	
	//调用以下
	/**
	 * backup module version.
	 * @return 0: success; <0: fail
	 */
	public static native int backupModVer();
	
	/**
	 * restore module version
	 * @return  0: success; <0: fail
	 */
	public static native int restoreModVer();
	
	/**
	 * update module version
	 * @return 0: success; <0: fail(-1: file not exist; )
	 */
	public static native int updateModVer(String path);
	
	// 加载动态库
	static {
		System.loadLibrary("rfidcomnjva");
	}
}
