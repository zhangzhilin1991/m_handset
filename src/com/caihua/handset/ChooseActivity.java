package com.caihua.handset;

import com.caihua.newhandset.R;
import com.caihua.util.tools.MyLog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseActivity extends BaseActivity {

	private static final String TAG = "ChooseActivity";
	private static int requestCode = 0x100;
	public static int success = 0x101;
	public static final int GJB = 0;
	public static final int GEN2 = 1;
	public static final int GB = 4;

	private Button mBtnGen;
	private Button mBtnGjb;
	private Intent intent;
	private TextView mDetail;
	private TextView mMode;
	private int version = 0;
	private int moduleType = 0;

	private boolean flag = false;
	private boolean start = false;
	private myApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);
		mBtnGen = (Button) findViewById(R.id.btn_gen);
		mBtnGjb = (Button) findViewById(R.id.btn_gjb);
		mDetail = (TextView) findViewById(R.id.tv_detail);
		mMode = (TextView) findViewById(R.id.mode_detail);
		application = (myApplication) this.getApplication();
		moduleType = application.moduleType;
		intent = new Intent(ChooseActivity.this, MainActivity.class);
		mDetail.setText(application.detail);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 上海模块退出程序
		Rfid.closeDev(application.fd);
		Gpio.exitGpio();
	}

	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_gen:
			setGen();
			break;
		case R.id.btn_gjb:
			setGjb();
			break;
		case R.id.btn_gb:
			setGb();
			break;
		default:
			break;
		}

	}

	/**
	 * 设置gen模式
	 */
	private void setGen() {
		// 上海模块设置工作模式为gen\
		if (moduleType == 1) {
			int ret = Rfid.setWorkMode(GEN2);
			if (ret == 0) {
			} else {
				mMode.setText("设置工作模式失败,请重试");
				return;
			}
		}
		// gen设置标签
		int tag = Rfid.initComnParm(2, 2, 4);
		MyLog.e(ChooseActivity.class, "init common parm");
		if (tag == 0) {
		} else {
			mMode.setText("设置标签失败,请重试");
			return;
		}
		intent.putExtra("id", 0);
		startActivity(intent);
	}

	private void setGjb() {
		// 上海模块工作模式为gjb
		Log.d(TAG, "moduleType:" + moduleType);
		if (moduleType == 1) {
			Rfid.setWorkMode(GJB);
			Log.d(TAG, "set work mode gjb");
		}

		// gjb设置标签
		int tag1 = Rfid.initComnParm(0, 2, 4);
		MyLog.e(ChooseActivity.class, "init common parm");
		if (tag1 == 0) {
		} else {
			mMode.setText("设置标签失败,请重试");
			return;
		}
		intent.putExtra("id", 1);
		startActivity(intent);
	}

	private void setGb() {
		Log.d(TAG, "moduleType:" + moduleType);
		if (moduleType == 1) {
			Rfid.setWorkMode(GB);
			Log.d(TAG, "set work mode gjb");
		}

		// gb设置标签
		int tag1 = Rfid.initComnParm(0, 2, 4);
		MyLog.e(ChooseActivity.class, "init common parm");
		if (tag1 == 0) {
		} else {
			mMode.setText("设置标签失败,请重试");
			return;
		}
		intent.putExtra("id", 2);
		startActivity(intent);
	}

}
