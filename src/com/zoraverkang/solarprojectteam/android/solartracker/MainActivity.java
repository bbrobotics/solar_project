package com.zoraverkang.solarprojectteam.android.solartracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.view.Menu;

public class MainActivity extends Activity {

	/**
	 * An intent extra which contains a UsbAccessory which is an arduino.
	 */
	static String EXTRA_ARDUINO = "com.solarprojectteam.android.intent.EXTRA_ARDUINO";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent i = new Intent(this, SolarService.class);
		i.putExtra(EXTRA_ARDUINO, getIntent().getParcelableExtra(UsbManager.EXTRA_ACCESSORY));
		
		this.startService(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
