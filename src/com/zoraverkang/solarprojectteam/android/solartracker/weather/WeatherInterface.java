package com.zoraverkang.solarprojectteam.android.solartracker.weather;

public interface WeatherInterface {

	/**
	 * Returns the time last updated.
	 * @return An array with {hour, minute, second} that the weather data was last updated.
	 */
	public int[] timeLastUpdated();
	
	/**
	 * Sets the coordinates to fetch weather data for.
	 * @param latitude
	 * @param longitude
	 */
	public void setCoordinates(double latitude, double longitude);
	
	/**
	 * Updates weather data through the Internet.  Returns true if successful and false if unsuccessful.
	 * @return Success
	 */
	public boolean update();
	
	/**
	 * Returns severe weather for the set time into the future.  Returns null if there is none.
	 * @param timeFromNow Time in minutes into the future.
	 */
	public void getSevere(int timeFromNow);// Will be changed to the appropriate data type once determined.
	
	/**
	 * Returns the wind speed for the set time into the future.
	 * @param timeFromNow Time in minutes into the future.
	 * @return
	 */
	public double getWindSpeed(int timeFromNow); //All data types may be changed in the future.
	
	/**
	 * Returns the temperature in Fahrenheit for the set time into the future. 
	 * @param timeFromNow Time in minutes into the future.
	 * @return The temperature in Fahrenheit
	 */
	public double getTemperatureF(int timeFromNow);
	
	/**
	 * Returns the temperature in Celsius for the set time into the future. 
	 * @param timeFromNow Time in minutes into the future.
	 * @return The temperature in Celsius
	 */
	public double getTemperatureC(int timeFromNow);
	
	/**
	 * Returns the temperature in Kelvin for the set time into the future.
	 * @param timeFromNow Time in minutes into the future.
	 * @return The temperature in Kelvin
	 */
	public double getTemperatureK(int timeFromNow);
	
	/**
	 * Returns the precipitation for the set time into the future.
	 * @param timeFromNow Time in minutes into the future.
	 * @return The precipitation status
	 */
	public boolean getPrecipitation(int timeFromNow);
	
	/**
	 * Returns the precipitation type for the set time into the future.
	 * @param timeFromNow Time in minutes into the future.
	 * @return The precipitation type
	 */
	public PrecipitationType getPrecipitationType(int timeFromNow);

	/**
	 * Returns the humidity in percentage of air saturation.
	 * @param timeFromNow
	 * @return The humidity
	 */
	public double getHumidity(int timeFromNow);
	
	/**
	 * Returns the dew point
	 * @return the dew point
	 */
	public int getMaxDewPoint();
	
	/**
	 * Returns the sunrise time in the same format as lastTimeUpdated().
	 * @return An array with {hour, minute, second} for the point in time that the sun rises
	 */
	public int[] getSunriseTime();
	
	/**
	 * Returns the sunset time in the same format as lastTimeUpdated(). 
	 * @return An array with {hour, minute, second} for the point in time that the sun sets
	 */
	public int[] getSunsetTime();
	
}
