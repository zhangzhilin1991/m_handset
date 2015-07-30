package com.caihua.handset;

import java.io.File;
import java.lang.reflect.Field;

import com.caihua.newhandset.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class plusActionProvider extends ActionProvider implements
		OnMenuItemClickListener {

	private static final String TAG = "ActionProvider";
	
	private static final String path="/sdcard/rfid/update/EPC_GJB_P_9600_njtest.bin";
	
	private String title;
	private String text;
	private String process;
	
	private AlertDialog alertDialog;
	private LayoutInflater inflater;
	private View view;
	private TextView textView;
	private ProgressBar progressBar;
	private Context context;

	public plusActionProvider(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		// TODO Auto-generated method stub
		super.onPrepareSubMenu(subMenu);
		subMenu.clear();
		subMenu.add(0, 0, 0, "备份").setIcon(R.drawable.backup)
				.setOnMenuItemClickListener(plusActionProvider.this);
		subMenu.add(0, 1, 0, "还原").setIcon(R.drawable.restore)
				.setOnMenuItemClickListener(plusActionProvider.this);
		subMenu.add(0, 2, 0, "更新").setIcon(R.drawable.update)
				.setOnMenuItemClickListener(plusActionProvider.this);

	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generate	
		inflater =LayoutInflater.from(context);
		view = inflater.inflate(R.layout.dialog_1, null);
		textView=(TextView) view.findViewById(R.id.tv_progress);
		progressBar=(ProgressBar) view.findViewById(R.id.progress_circular);
		switch (item.getItemId()) {
		case 0:
			backup();
			break;
		case 1:
			restore();
			break;
		case 2:
			update();

			break;

		default:
			break;
		}

		return false;
	}

	@Override
	public boolean hasSubMenu() {
		// TODO Auto-generated method stub

		return true;
	}
	
	/**
	 * 保持dialog,或者dismiss dialog
	 * @param state true:保持dialog false：关闭dialog
	 */
	private void dismissDialog(boolean state) {
		Field field;
		try {
			field = alertDialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(alertDialog,state);
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
	
	/**
	 * 备份
	 * @return true:备份成功 false:备份失败
	 */
	private void backup(){
		Toast.makeText(context, "备份",Toast.LENGTH_SHORT).show();
		
		alertDialog=new AlertDialog.Builder(context).setTitle("备份")
		.setIcon(R.drawable.aleart).setPositiveButton("确定",new OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
				textView.setText("正在备份...");
				dismissDialog(false);
				// 关闭设备
				Gpio.exitGpio();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Gpio.initGpio();
				
				int flag=Rfid.backupModVer();
				//int flag=0;
				if(flag==0){
					dismissDialog(true);
					Toast.makeText(context,"备份成功",Toast.LENGTH_SHORT).show();
				}else{
					
					progressBar.setVisibility(View.GONE);
					textView.setText("备份失败，请重试");
					Toast.makeText(context,"备份失败",Toast.LENGTH_SHORT).show();
				}
			}
		}).setNegativeButton("退出",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismissDialog(true);
			}
		}).setCancelable(true).setView(view).create();
		textView.setText("是否进行备份？");
		alertDialog.show();
	}
	
	/**
	 * 还原
	 * @return true：成功  false：失败
	 */
	private void restore(){
		Toast.makeText(context, "还原",Toast.LENGTH_SHORT).show();
		alertDialog=new AlertDialog.Builder(context).setTitle("还原")
		.setIcon(R.drawable.aleart).setPositiveButton("确定",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
				textView.setText("正在还原...");
				
				Gpio.exitGpio();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Gpio.initGpio();
				
				dismissDialog(false);
				int flag=Rfid.restoreModVer();
				if(flag==0){
					dismissDialog(true);
					Toast.makeText(context,"还原成功",Toast.LENGTH_SHORT).show();
				}else{
				
					progressBar.setVisibility(View.GONE);
					textView.setText("还原 失败，请重试");
					Toast.makeText(context,"还原成功",Toast.LENGTH_SHORT).show();
				}
			}
		}).setNegativeButton("退出",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismissDialog(true);
			}
		}).setCancelable(true).setView(view).create();
		textView.setText("是否进行还原？");
		alertDialog.show();
	}
	
	/**
	 * 更新
	 * @return true:成功 false:失败
	 */
	private void update(){
		Toast.makeText(context, "更新",Toast.LENGTH_SHORT).show();
		alertDialog=new AlertDialog.Builder(context).setTitle("更新")
		.setIcon(R.drawable.aleart).setPositiveButton("确定",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
				textView.setText("正在更新...");
			
				Gpio.exitGpio();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Gpio.initGpio();
				dismissDialog(false);
				//线程??
				
				int flag=Rfid.updateModVer(path);
				if(flag==0){
					dismissDialog(true);
					Toast.makeText(context,"更新成功",Toast.LENGTH_SHORT).show();
				}else{
					
					progressBar.setVisibility(View.GONE);
					if(flag==-1){
						textView.setText("更新失败,找不到更新文件");
					}else{
					textView.setText("更新失败，请重试");
					}
					Toast.makeText(context,"更新失败",Toast.LENGTH_SHORT).show();
				}
			}
		}).setNegativeButton("退出",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismissDialog(true);
			}
		}).setCancelable(true).setView(view).create();
		textView.setText("是否进行更新？");
		alertDialog.show();
	}
	
}
