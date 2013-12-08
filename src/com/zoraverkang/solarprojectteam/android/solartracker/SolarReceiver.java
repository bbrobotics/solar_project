package com.zoraverkang.solarprojectteam.android.solartracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SolarReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if(intent.getAction() == Intent.ACTION_BOOT_COMPLETED)
		{
			//Start solar service.
		}
	}

}
