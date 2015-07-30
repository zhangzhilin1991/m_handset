package com.caihua.handset;

public class Scan implements Runnable{
	
	private int mode;
	private boolean Start=false;
	
	public Scan(int mode){
		
		this.mode=mode;
		
	}
	
	private void stop(){
		Start=false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Start=true;
		switch (mode) {
		case 0:
			
			break;
			
		case 1:
			
			break;

		default:
			break;
		}
	}
}
