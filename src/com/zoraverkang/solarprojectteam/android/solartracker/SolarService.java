package com.zoraverkang.solarprojectteam.android.solartracker;

import android.app.Service;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.text.format.Time;
import android.os.IBinder;

public class SolarService extends Service{

	Time time;
	Arduino arduino;
	
	@Override
	public void onCreate()
	{
		time = new Time();
		
		//Start runnable to execute solar tracking calculations
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		arduino = new Arduino(this, (UsbAccessory) intent.getParcelableExtra(MainActivity.EXTRA_ARDUINO));
		
		startTracking();
		
		return START_STICKY;
	}
	
	//Does not bind...
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public byte getTrackingAngle()
	{
		
		return 0;  //Stub to prevent red lines...  remove when method is actually written...
	}
	
	void startTracking()
	{
		while(true)
		{
			
		}
	}

}
