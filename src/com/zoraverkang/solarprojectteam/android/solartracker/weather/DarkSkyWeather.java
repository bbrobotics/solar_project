package com.zoraverkang.solarprojectteam.android.solartracker.weather;

import android.text.format.Time;

import dme.forecastiolib.*;


public class DarkSkyWeather implements WeatherInterface{

	double m_latitude, m_longitude, hour, minute, second;
	
	String apiKey = " ";
	
	Time updateTime;
	
	ForecastIO io;
	
	public DarkSkyWeather()
	{
		io = new ForecastIO(apiKey);
		io.setUnits(io.UNITS_SI);
		
		updateTime = new Time();
	}

	@Override
	public int[] timeLastUpdated() {
		return getTimeIntArrayFromString(io.getTime());
	}

	@Override
	public void setCoordinates(double latitude, double longitude) {
		m_latitude = latitude;
		m_longitude = longitude;
	}

	@Override
	public boolean update() {
		return io.getForecast(String.valueOf(m_latitude), String.valueOf(m_longitude));
	}

	@Override
	public void getSevere(int timeFromNow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getWindSpeed(int timeFromNow) {
		if(timeFromNow == 0 && io.hasCurrently())
		{
			FIOCurrently currently = new FIOCurrently(io);
			return currently.get().windSpeed();
		}
		else if(io.hasMinutely())
		{
			FIOMinutely minutely = new FIOMinutely(io);
			return minutely.getMinute(timeFromNow).windSpeed();
		}
		else 
		{
			return -1;
		}
	}

	@Override
	public double getTemperatureF(int timeFromNow) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTemperatureC(int timeFromNow) {
		if(timeFromNow == 0 && io.hasCurrently())
		{
			FIOCurrently currently = new FIOCurrently(io);
			return currently.get().temperature();
		}
		else if(io.hasMinutely())
		{
			FIOMinutely minutely = new FIOMinutely(io);
			return minutely.getMinute(timeFromNow).temperature();
		}
		else 
		{
			return -274.15;
		}
	}

	@Override
	public double getTemperatureK(int timeFromNow) {
		if(timeFromNow == 0 && io.hasCurrently())
		{
			FIOCurrently currently = new FIOCurrently(io);
			return currently.get().temperature() + 273.15;
		}
		else if(io.hasMinutely())
		{
			FIOMinutely minutely = new FIOMinutely(io);
			return minutely.getMinute(timeFromNow).temperature() + 273.15;
		}
		else 
		{
			return -1;
		}
	}

	@Override
	public boolean getPrecipitation(int timeFromNow) {
		if(timeFromNow == 0 && io.hasCurrently())
		{
			FIOCurrently currently = new FIOCurrently(io);
			if(currently.get().precipIntensity() == 0)
				return false;
			else return true;
		}
		else if(io.hasMinutely())
		{
			FIOMinutely minutely = new FIOMinutely(io);
			if(minutely.getMinute(timeFromNow).precipIntensity() == 0)
				return false;
			else return true;
		}
		else 
		{
			return false; //Would true be safer?
		}
	}

	@Override
	public PrecipitationType getPrecipitationType(int timeFromNow) {
		if(timeFromNow == 0 && io.hasCurrently())
		{
			FIOCurrently currently = new FIOCurrently(io);
			if(currently.get().precipIntensity() != 0)
			{
				if(currently.get().precipType() == "rain")
				{
					return PrecipitationType.RAIN;
				}
				else if(currently.get().precipType() == "snow")
				{
					return PrecipitationType.SNOW;
				}
				else if(currently.get().precipType() == "sleet")
				{
					return PrecipitationType.FREEZING_RAIN;
				}
				else if(currently.get().precipType() == "hail")
				{
					return PrecipitationType.HAIL;
				}
				else if(currently.get().precipType() == "sharks")
				{
					return PrecipitationType.SHARKS;
				}
			}
		}
		else if(timeFromNow > 0 && io.hasMinutely())
		{
			FIOMinutely minutely = new FIOMinutely(io);
			if(minutely.getMinute(timeFromNow).precipType() == "rain")
			{
				return PrecipitationType.RAIN;
			}
			else if(minutely.getMinute(timeFromNow).precipType() == "snow")
			{
				return PrecipitationType.SNOW;
			}
			else if(minutely.getMinute(timeFromNow).precipType() == "sleet")
			{
				return PrecipitationType.FREEZING_RAIN;
			}
			else if(minutely.getMinute(timeFromNow).precipType() == "hail")
			{
				return PrecipitationType.HAIL;
			}
			else if(minutely.getMinute(timeFromNow).precipType() == "sharks")
			{
				return PrecipitationType.SHARKS;
			}
		}
		
		return PrecipitationType.NONE;
	}

	@Override
	public double getHumidity(int timeFromNow) {
		if(timeFromNow == 0 && io.hasCurrently())
		{
			FIOCurrently currently = new FIOCurrently(io);
			return currently.get().humidity();
		}
		else if(io.hasMinutely())
		{
			FIOMinutely minutely = new FIOMinutely(io);
			return minutely.getMinute(timeFromNow).humidity();
		}
		else 
		{
			return -1;
		}
	}

	@Override
	public double getDewPoint() {
		if(io.hasCurrently())
		{
			FIOCurrently currently = new FIOCurrently(io);
			return currently.get().dewPoint();
		}
		
		return -1;
	}

	@Override
	public int[] getSunriseTime() {
		if(io.hasCurrently())
		{
			FIOCurrently currently = new FIOCurrently(io);
			String timeString = currently.get().sunriseTime();
			return getTimeIntArrayFromString(timeString);
		}
		
		int[] error = { -1, -1, -1}; 
		return error;
	}

	@Override
	public int[] getSunsetTime() {
		if(io.hasCurrently())
		{
			FIOCurrently currently = new FIOCurrently(io);
			String timeString = currently.get().sunsetTime();
			return getTimeIntArrayFromString(timeString);
		}
		
		int[] error = { -1, -1, -1}; 
		return error;
	}
	
	private int[] getTimeIntArrayFromString(String timeString)
	{
		int hours = Integer.parseInt(timeString.substring(0, 3));
		int minutes = Integer.parseInt(timeString.substring(3,5));
		int seconds = Integer.parseInt(timeString.substring(5, 7));
		
		int[] timeInt = {
				hours, minutes, seconds
		};
		return timeInt;
	}
	
	
}				
