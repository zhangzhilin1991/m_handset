package com.caihua.handset;

import com.caihua.newhandset.R;
import com.caihua.util.tools.MyLog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OpenDevActivity extends BaseActivity {

	private static final String TAG = "openDev";

	private Button mBtnStart;
	private TextView mTvDetail;
	private int version = 0;
	private int moduleType = 0;

	private boolean flag = false;
	private boolean start = false;
	private myApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opendev);

		mBtnStart = (Button) findViewById(R.id.btn_open);
		mTvDetail = (TextView) findViewById(R.id.tv_dev_datail);

		application = (myApplication) this.getApplication();

		Log.d(TAG, "on start");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 关闭gpio以及dev
		if (flag == false) {
			if (application.fd != 0) {
				Rfid.closeDev(application.fd);

			}
			Gpio.exitGpio();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		MyLog.e(OpenDevActivity.class, ""+keyCode);
		if (keyCode==62) {
			openDev();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	


	public void onClick(View v) {
		// 初始化设备在此做，判断模块类型跳转不同activity
		Log.d(TAG, "on click");
		openDev();
	}
	
	
	/**
	 * 打开设备并跳转
	 */
	public void openDev(){
		if (flag == false) {
			flag = true;
			flag = initDev();
			if (flag == true) {
				if (application.moduleType == 1) {
					// 上海模块调到gen嗯哼gjb选择界面，然后再调到到主界面
					Intent intent = new Intent(this, ChooseActivity.class);
					startActivity(intent);
					this.finish();
				} else {
					// 其他模块直接跳到主界面
					// gen设置标签
					int tag = Rfid.initComnParm(2, 2, 4);
					MyLog.e(OpenDevActivity.class,"init common parm");
					if (tag == 0) {
						// Toast.makeText(this, "设置标签设置成功 ", Toast.LENGTH_SHORT)
						// .show();
					} else {
						// Toast.makeText(this, "设置标签设置失败 ", Toast.LENGTH_SHORT)
						// .show();
						mTvDetail.setText("标签设置失败，请重试");
						flag = false;
						return;
					}
					Intent intent = new Intent(this, MainActivity.class);
					intent.putExtra("id", 0);
					startActivity(intent);
					this.finish();
				}
			}
		}
	}

	
	/**
	 * 模块初始化
	 * 
	 * @return 成功或者失败
	 */
	private boolean initDev() {
		// gpio初始化？？？
		mTvDetail.setText("正在初始化串口......");
		Gpio.initGpio();
		// 等待初始化完成
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 获取版本号和模块类型
		mTvDetail.setText("正在获取版本号和模块类型......");
		version = Rfid.getLibVer();
		if (version == 0) {
			
			mTvDetail.setText("获取版本号失败，请重试");
			return false;
		}
		moduleType = Rfid.getModuType();
		// 保存moduleType为全局变量
		application.moduleType = moduleType;
		String text;
		if (moduleType == 1) {
			text = "JY001" + "\n" + "version:" + version;
		} else if (moduleType == 2) {
			text = "北京模块" + "\n" + "version:" + version;
		} else if (moduleType == 3) {
			text = "R2000模块" + "\n" + "version:" + version;
		} else {
			mTvDetail.setText("获取模块类型失败，请重试");
			return false;
		}
		application.detail = text;

		// 打开串口，获得文件描述符保存为全局变量
		application.fd = Rfid.openDev(moduleType);

		mTvDetail.setText("正在初始化模块......");
		// 初始化模块
		int ret = Rfid.init();
		if (ret == 0) {
			if (moduleType == 1) {
				// 上海模块必须先跳转到主程序？？？
				mTvDetail.setText("正在尝试跳转到主程序");
				return shHopToMain();
			}
			return true;
		} else {
			mTvDetail.setText("初始化模块失败，请重试");
			return false;
		}
	}

	/**
	 * 上海模块跳转主程序
	 * 
	 * @return
	 */
	public boolean shHopToMain() {
		int i = Rfid.hoptomain();
		Log.e("TAG", i + "");
		if (i == 0) {
			
			return true;
		} else {
			
			mTvDetail.setText("跳转主程序失败，请重试");
			return false;
		}
	}

}
