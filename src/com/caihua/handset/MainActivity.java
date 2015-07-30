package com.caihua.handset;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.caihua.newhandset.R;
import com.caihua.util.tools.ByteUtil;
import com.caihua.util.tools.DebugUtil;
import com.caihua.util.tools.MyLog;
import com.caihua.util.tools.PrintUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private static final int EPC = 0;
	private static final int TID = 1;

	private TextView mDetail;
	private TextView mScanResult;
	private Button mGenTid;
	private Button mGenEpc;
	private Button mSingleTid;

	private Button mGjbTid;
	private Button mGjbEpc;

	private Button mGbEpc;
	private Button mGbSingleTid;

	private RelativeLayout mGjb;
	private RelativeLayout mGen;
	private RelativeLayout mGb;

	private myApplication application;
	private int mId;
	private MenuItem power;
	private EditText edtPower;

	private int state = 0;

	private AlertDialog alertDialog;
	private LayoutInflater inflater;
	private View view;
	private EditText editText;

	private int fd;
	private boolean flag = false;
	private boolean start = false;
	private boolean Scan = false;
	private byte[] data = null;

	protected List<String> result = new ArrayList<String>();

	Map<String, Integer> tidMap;

	// private Rfid rfid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();

		state = intent.getIntExtra("id", -1);

		this.setResult(ChooseActivity.success, intent);

		application = (myApplication) this.getApplication();

		mDetail = (TextView) findViewById(R.id.module_datail);
		mScanResult = (TextView) findViewById(R.id.scan_result);

		mGen = (RelativeLayout) findViewById(R.id.gen2);
		mGenTid = (Button) findViewById(R.id.gen2_tid);
		mGenEpc = (Button) findViewById(R.id.gen2_epc);

		mGjb = (RelativeLayout) findViewById(R.id.gjb);
		mGjbEpc = (Button) findViewById(R.id.gjb_epc);
		mGjbTid = (Button) findViewById(R.id.gjb_tid);

		mGb = (RelativeLayout) findViewById(R.id.gb);
		mGbEpc = (Button) findViewById(R.id.gb_epc);
		mGbSingleTid = (Button) findViewById(R.id.gb_single_tid);

		// GEN
		if (state == 0) {
			mGen.setVisibility(View.VISIBLE);
			mGjb.setVisibility(View.GONE);
			mGb.setVisibility(View.GONE);
			mGenEpc.setSelected(true);
			mId = R.id.gen2_epc;
		} else if (state == 1) {
			// GJB
			mGen.setVisibility(View.GONE);
			mGjb.setVisibility(View.VISIBLE);
			mGb.setVisibility(View.GONE);
			mGjbEpc.setSelected(true);
			mId = R.id.gjb_epc;

		} else if (state == 2) {
			// GB
			mGen.setVisibility(View.GONE);
			mGjb.setVisibility(View.GONE);
			mGb.setVisibility(View.VISIBLE);
			mGbEpc.setSelected(true);
			mId = R.id.gb_epc;

		} else {

		}
		tidMap = new HashMap<String, Integer>();
		mScanResult.setFocusable(true);
		mScanResult.requestFocus();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 非上海模块，直接结束，上海模块返回模式选择界面
		if (application.moduleType != 1) {
			Rfid.closeDev(application.fd);
			Gpio.exitGpio();
		}
		start = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (application != null && application.moduleType == 1) {
			getMenuInflater().inflate(R.menu.main, menu);
		} else {
			getMenuInflater().inflate(R.menu.power, menu);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// // as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {
		case R.id.power:
			//startActivity(new Intent());
			LayoutInflater inflater=this.getLayoutInflater();
			View v=inflater.inflate(R.layout.dialog,null);
		    edtPower = (EditText)v.findViewById(R.id.power);
			
			int power = Rfid.getPower();
			if (power <= 0) {
				Toast.makeText(MainActivity.this, "获取功率失败 ",
						Toast.LENGTH_SHORT).show();
			} else {
				edtPower.setText(String.valueOf(power));
				edtPower.setSelection(String.valueOf(power).length());
//				Toast.makeText(MainActivity.this, "获取功率成功： "+power,
//						Toast.LENGTH_SHORT).show();
			}
			alertDialog=new AlertDialog.Builder(this).setIcon(R.drawable.atenna_power)
		.setTitle("设置功率").setPositiveButton("设置",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				if (!DebugUtil.validate(edtPower)) {
					Toast.makeText(MainActivity.this, "功率为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				int power = Integer.parseInt(edtPower.getText()
						.toString());
				int result = Rfid.setPower(power);
				if (result == 0) {
					Toast.makeText(MainActivity.this, "设置功率成功 ",
							Toast.LENGTH_SHORT).show();
					dismissDialog(true);
				}else{
					Toast.makeText(MainActivity.this, "设置功率失败 ",
							Toast.LENGTH_SHORT).show();
					dismissDialog(false);
				}
			}
		}).setNegativeButton("退出",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismissDialog(true);
			}
		}).setView(v).create();
			alertDialog.show();

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		if(start){
			Toast.makeText(this,"请先停止扫描",Toast.LENGTH_SHORT).show();
			return;
		}
		mScanResult.setText("");
		data = null;
		start = false;
		tidMap.clear();
		result.clear();
		switch (v.getId()) {
		case R.id.gen2_tid:
			// 设置盘存类型,只有上海模块设置
			if (application.moduleType == 1) {
				int ret = Rfid.setInventType(TID);
				if (ret == 0) {
					MyLog.e(MainActivity.class,
							"set gen inventType tid success");

				} else {
					MyLog.e(MainActivity.class, "set gen inventType tid failed");
					return;

				}
			}
			mGenTid.setSelected(true);
			mGenEpc.setSelected(false);
			mSingleTid.setSelected(false);
			mId = v.getId();

			break;

		case R.id.gen2_single_tid:

			mGenTid.setSelected(false);
			mGenEpc.setSelected(false);
			mSingleTid.setSelected(true);
			mId = v.getId();

			break;

		case R.id.gen2_epc:

			// 设置盘存类型,只有上海模块设置
			if (application.moduleType == 1) {
				int ret2 = Rfid.setInventType(EPC);
				if (ret2 == 0) {
					MyLog.e(MainActivity.class,
							"set gen inventType epc success");

				} else {
					MyLog.e(MainActivity.class, "set gen inventType epc failed");

					return;
				}
			}

			mGenTid.setSelected(false);
			mGenEpc.setSelected(true);
			mSingleTid.setSelected(false);
			mId = v.getId();

			break;
		case R.id.gjb_epc:

			Log.d(TAG, "set gjb epc");
			// 设置盘存类型,只有上海模块设置
			int ret3 = Rfid.setInventType(EPC);
			if (ret3 == 0) {

				MyLog.e(MainActivity.class, "set gjb inventType epc success");
			} else {

				MyLog.e(MainActivity.class, "set  gjb inventType epc failed");
				return;
			}
			mGjbTid.setSelected(false);
			mGjbEpc.setSelected(true);

			mId = v.getId();

			break;

		case R.id.gjb_tid:
			Log.d(TAG, "set gjb tid");

			int ret4 = Rfid.setInventType(TID);
			if (ret4 == 0) {

				MyLog.e(MainActivity.class, "set gjb inventType tid success");
			} else {

				MyLog.e(MainActivity.class, "set  gjb inventType tid failed");
				return;
			}

			mGjbTid.setSelected(true);
			mGjbEpc.setSelected(false);
			mId = v.getId();

			break;

		case R.id.gb_epc:
			Log.d(TAG, "set gb epc");
			// 设置盘存类型,只有上海模块设置
			int ret5 = Rfid.setInventType(EPC);
			if (ret5 == 0) {

				MyLog.e(MainActivity.class, "set gb inventType epc success");
			} else {

				MyLog.e(MainActivity.class, "set  gb inventType epc failed");
				return;
			}
			mGbSingleTid.setSelected(false);
			mGbEpc.setSelected(true);
			mId = v.getId();

			break;

		case R.id.gb_single_tid:
			Log.d(TAG, "set gb tid");
			// 设置盘存类型,只有上海模块设置
			int ret6 = Rfid.setInventType(TID);
			if (ret6 == 0) {

				MyLog.e(MainActivity.class, "set gb inventType tid success");
			} else {

				MyLog.e(MainActivity.class, "set  gb inventType tid failed");
				return;
			}

			mGbEpc.setSelected(false);
			mGbSingleTid.setSelected(true);
			mId = v.getId();

			break;

		default:
			break;
		}
	}

	/**
	 * 从byte[]中获取tid数据
	 * 
	 * @param data
	 *            byte数组
	 * @return String[]的tid
	 */
	public String[] getTid(byte[] data) {

		if (data.length == 0) {
			Log.d(TAG, "tid data size is 0");

			MyLog.e(MainActivity.class, "error! tid data size is 0");
			return null;
		} else if (data.length == 3) {

			MyLog.e(MainActivity.class, "no tid find");
			return null;
		}
		// 第一位为tid个数
		String tidInfo = ByteUtil.tidToString(data, data.length);
		int num = ByteUtil.toInt(data[0]);
		Log.d(TAG, "num:" + num + " tidinfo:" + tidInfo);

		String tid[] = PrintUtil.printTid(tidInfo, num);
		if (tid.length == 0) {
			return null;
		}

		return tid;
	}

	/**
	 * 从byte[]中获取epc数据
	 * 
	 * @param data
	 *            byte数组
	 * @return String[]的tid
	 */
	public String[] getEpc(byte[] data) {
		if (data.length == 0) {
			Log.d(TAG, "epc data size is 0");

			MyLog.e(MainActivity.class, "error! epc data size is 0");
			return null;
		} else if (data.length == 3) {

			MyLog.e(MainActivity.class, "no epc find");
			return null;
		}

		String epc[] = PrintUtil.printEpc(data, data.length);
		if (epc.length == 0) {

			return null;
		}
		Log.d(TAG, "get epc");

		return epc;
	}

	/**
	 * 读到数据控制蜂鸣器响一声
	 */
	private void Beep() {
		try {
			Gpio.WriteGpio(Gpio.PIN_stop_pwr, 1, Gpio.STOP_CONTROL);
			Thread.sleep(100);
			Gpio.WriteGpio(Gpio.PIN_stop_pwr, 1, Gpio.NORMAL_CONTROL);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
	}

	/**
	 * 保持dialog,或者dismiss dialog
	 * 
	 * @param state
	 *            true:保持dialog false：关闭dialog
	 */
	private void dismissDialog(boolean state) {
		Field field;
		try {
			field = alertDialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(alertDialog, state);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		mScanResult.setFocusable(true);
		mScanResult.requestFocus();
		Log.d(TAG, "keyCode:" + event.getKeyCode());
		Log.d(TAG, "repeat count:" + event.getRepeatCount());
		if (event.getKeyCode() == 62 && event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			event.startTracking();

			// if (!Scan) {
			if (!start) {
				mScanResult.setText("正在扫描....");
				tidMap.clear();
				start = true;
				// Scan = true;
				new workThread().start();
			} else {
				start = false;
				if (TextUtils.equals(mScanResult.getText(), "正在扫描....")) {
					mScanResult.setText("扫描已停止");
				} else {
					mScanResult.append("扫描已停止");
				}
			}
			// }

			return true;
		} else if (event.getKeyCode() == 62
				&& event.getAction() == KeyEvent.ACTION_UP
				&& event.getRepeatCount() == 0) {
			event.startTracking();
			Log.d(TAG, "action up");

			// Scan = false;
			return true;
		} else if (event.getKeyCode() == 62
				&& event.getAction() == KeyEvent.ACTION_MULTIPLE) {

			Log.d(TAG, "mutiple action");

			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	// /**
	// * 扫描一次
	// */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	// if (keyCode == 62 && event.getRepeatCount()==0){
	// Log.d(TAG,"repeat count:"+event.getRepeatCount());
	// event.startTracking();
	// if (!start) {
	// mScanResult.setText("正在扫描....");
	// start = true;
	// new workThread().start();
	// } else {
	// start = false;
	// if (TextUtils.equals(mScanResult.getText(), "正在扫描....")) {
	// mScanResult.setText("扫描已停止");
	// } else {
	// mScanResult.append("扫描已停止");
	// }
	// }
	// return true;
	// }else if(keyCode==KeyEvent.KEYCODE_MENU){
	// Log.d(TAG,"keyCodeMenu");
	// super.openOptionsMenu();
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	/**
	 * 盘存线程
	 * 
	 * @author jiwenbin
	 * @date 2014年11月18日下午2:48:33
	 */
	private class workThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			// boolean bTid=checkbox.isChecked();
			while (start) {
				inventory(mId);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			MyLog.e(MainActivity.class, "workThread onDestroy");
		}
	}

	// private class workThread implements Runnable{
	//
	// private boolean start=false;
	// private boolean scan=false;
	//
	// public workThread(){
	// this.start=true;
	// }
	//
	// public void open(){
	// start=true;
	// }
	//
	// public void close(){
	// start=false;
	// scan=false;
	// }
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// //已开始扫描就不重复扫描
	// if(!scan){
	// scan=true;
	// }else{
	// return;
	// }
	// while (start) {
	// inventory(mId);
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// MyLog.e(MainActivity.class, "workThread onDestroy");
	// }
	// }

	/**
	 * 
	 * 盘存
	 * 
	 * @param bTid
	 */
	protected void inventory(int id) {

		Log.d(TAG, "inventary");
		result.clear();
		// mScanResult.setText("");
		data = null;
		switch (id) {
		case R.id.gen2_tid:
			// mGenTid.setFocusable(true);
			// mGenTid.requestFocus();

			Log.d(TAG, "start read tid");
			MyLog.e(MainActivity.class, "start read tid");
			data = Rfid.readTid();
			if (data != null) {
				String[] tid = getTid(data);
				if (tid != null) {
					for (int i = 0; i < tid.length; i++) {
						result.add(tid[i]);
					}
					inventory_over();
				}
			} else {
				MyLog.e(MainActivity.class, "read tid failed");
			}

			break;

		case R.id.gen2_single_tid:

			Log.d(TAG, "start read siangle tid");
			MyLog.e(MainActivity.class, "start single read tid");
			data = Rfid.readSingleTid();
			if (data != null) {
				String[] tid = getTid(data);
				if (tid != null) {
					for (int i = 0; i < tid.length; i++) {
						result.add(tid[i]);
					}
					inventory_over();
				}
			} else {
				MyLog.e(MainActivity.class, "read single tid failed");
			}
			break;

		case R.id.gen2_epc:

			Log.d(TAG, "start read epc");
			MyLog.e(MainActivity.class, "start read epc");
			data = Rfid.readEpc();
			if (data != null) {
				String[] epc = getEpc(data);
				if (epc != null) {
					for (int i = 0; i < epc.length; i++) {
						result.add(epc[i]);
					}
					inventory_over();
				}
			} else {
				MyLog.e(MainActivity.class, "read gen epc failed");
			}

			break;
		case R.id.gjb_epc:

			Log.d(TAG, "start read gjb epc");
			MyLog.e(MainActivity.class, "start read gjb epc");
			data = Rfid.readGjbEpc();
			if (data != null) {
				String[] epc = getEpc(data);
				if (epc != null) {
					for (int i = 0; i < epc.length; i++) {
						result.add(epc[i]);
					}
					inventory_over();
				}
			} else {
				MyLog.e(MainActivity.class, "read gjb epc failed");
			}

			break;

		case R.id.gjb_tid:

			Log.d(TAG, "start read gjb tid");

			MyLog.e(MainActivity.class, "start read gjb tid");
			data = Rfid.readGjbSingleTid();
			if (data != null) {
				String[] tid = getTid(data);
				if (tid != null) {
					for (int i = 0; i < tid.length; i++) {
						result.add(tid[i]);
					}
					inventory_over();
				}
			} else {
				MyLog.e(MainActivity.class, "read gjb tid failed");
			}
			break;

		case R.id.gb_epc:
			Log.d(TAG, "start read gb epc");
			MyLog.e(MainActivity.class, "start read gb epc");
			data = Rfid.readGbEpc();
			if (data != null) {
				String[] epc = getEpc(data);
				if (epc != null) {
					for (int i = 0; i < epc.length; i++) {
						result.add(epc[i]);
					}
					inventory_over();
				}
			} else {
				MyLog.e(MainActivity.class, "read gb epc failed");
			}

			break;

		case R.id.gb_single_tid:
			Log.d(TAG, "start read gb tid");

			MyLog.e(MainActivity.class, "start read gb tid");
			data = Rfid.readGbSingleTid();
			if (data != null) {
				String[] tid = getTid(data);
				if (tid != null) {
					for (int i = 0; i < tid.length; i++) {
						result.add(tid[i]);
					}
					inventory_over();
				}
			} else {
				MyLog.e(MainActivity.class, "read gb tid failed");
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * 盘存结束
	 * 
	 */
	protected void inventory_over() {
		// TODO Auto-generated method stub
		Log.d(TAG, "inventary over");
		for (String str : result) {
			if (tidMap.keySet().contains(str)) {
				int times = tidMap.get(str);
				tidMap.put(str, times + 1);
			} else {
				tidMap.put(str, 1);
			}
		}
		mhandler.sendEmptyMessage(0);

	}

	Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Beep();
			Log.d(TAG, "set text");
			mScanResult.setText("");
			for (String tid : tidMap.keySet()) {
				mScanResult.append(tid + " " + tidMap.get(tid) + "\n");
			}
		}
	};
}
