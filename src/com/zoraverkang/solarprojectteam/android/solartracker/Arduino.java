package com.zoraverkang.solarprojectteam.android.solartracker;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class Arduino 
{

	Context arduinoContext;
	UsbAccessory arduino;
	UsbManager usbManager;

	ParcelFileDescriptor pfd;
	
	FileDescriptor fd;
	FileInputStream in;
	FileOutputStream out;
	
	boolean arduinoNotNull;
	byte mPosition = 0;
	byte mProtectionStatus = 0;
	/*
	 * The receive buffer has the following values in this order:
	 * Index: 0					1			2			3				4				5			6			7			
	 * Value: protectionStatus	position	windSpeed	internalTemp	externalTemp	moisture	current1	current2
	 */
	byte protectionStatus = 0, position = 0, internalTemp = 0, externalTemp = 0, windSpeed = 0, moisture = 0, encoder = 0, current1 = 0, current2 = 0;
	byte[] sendBuffer;
	
	public Arduino(Context context, UsbAccessory connectedAccessory)
	{
		arduinoContext = context;
		arduino = connectedAccessory;
		usbManager = (UsbManager) arduinoContext.getSystemService(Context.USB_SERVICE);
		
		if(arduino != null)
		{
			arduinoNotNull = true;
			
			pfd = usbManager.openAccessory(arduino);
			fd = pfd.getFileDescriptor();
			in = new FileInputStream(fd);
			out = new FileOutputStream(fd);
		}
	}
	
	public void sendByteArray(byte[] buffer)
	{
		if(arduinoNotNull)
		{
			try {
				out.write(buffer);
			} catch (IOException e) {
				Log.e("Arduino - sendByteArray", e.getMessage());
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * Receives a byte[] from the arduino.  Returns null if unsuccessful.
	 * @return A byte[] from the arduino.
	 */
	public byte[] receiveByteArray()
	{
		byte[] buffer = null;
		if(arduinoNotNull)
		{
			try {
				in.read(buffer);
			} catch (IOException e) {
				Log.e("Arduino - receiveByteArray", e.getMessage());
				e.printStackTrace();
			}
		}	
		return buffer;
	}
	
	public void setAngle(byte angle)
	{
		mPosition = angle;
		refreshOutput();
	}
	
	
	public byte getAngle()
	{
		refreshInput();
		
		return position;
	}
	
	public void setProtectionStatus(boolean open)
	{
		if(open)
			mProtectionStatus = 1;
		else
			mProtectionStatus = 0;
		
		refreshOutput();
	}
	
	/**
	 * Returns the status of the protected mode.  
	 * Returns true if protected mode in enabled and returns false if protected mode is disabled. 
	 * @return The status of protected mode.
	 */
	public boolean getProtectionStatus()
	{
		refreshInput();
		
		if(protectionStatus == 1)
			return true;
		else return false;
	}
	
	/**
	 * Returns the wind speed detected by the wind speed sensor connected to the arduino.
	 * @return The wind speed.
	 */
	public byte getWindSpeed()
	{
		refreshInput();
		
		return windSpeed;
	}
	
	/**
	 * Returns the internal temperature as reported by the internal temperature sensor connected to the arduino.
	 * @return The internal temperature.
	 */
	public byte getInternalTemp()
	{
		refreshInput();
		
		return internalTemp;
	}
	
	/**
	 * Returns the external temperature as reported by the external temperature sensor connected to the arduino.
	 * @return The external temperature.
	 */
	public byte getExternalTemp()
	{
		refreshInput();
		
		return externalTemp;
	}
	
	/**
	 * Returns the moisture as reported by the moisture sensor connected to the arduino.
	 * @return The moisture.
	 */
	public byte getMoisture()
	{
		refreshInput();
		
		return moisture;
	}
	
	/**
	 * Returns the current draw from the solar panels as reported by a current sensor connected to the arduino.
	 * @return
	 */
	public byte getSolarInputCurrent()
	{
		refreshInput();
		
		return current1;
	}
	
	private void refreshOutput()
	{
		sendBuffer[0] = mPosition;
		sendBuffer[1] = mProtectionStatus;
		
		sendByteArray(sendBuffer);
	}
	
	private void refreshInput()
	{
		byte[] receiveBuffer;
		receiveBuffer = receiveByteArray();
		
		if(receiveBuffer != null)
		{
			if(receiveBuffer.length == 8)
			{
				protectionStatus = receiveBuffer[0];
				position = receiveBuffer[1];
				windSpeed = receiveBuffer[2];
				internalTemp = receiveBuffer[3];
				externalTemp = receiveBuffer[4];
				moisture = receiveBuffer[5];
				current1 = receiveBuffer[6];
				current2 = receiveBuffer[7];
			}
		}
	}
}
