package com.smit.weatherapp.pojo;

import java.util.Date;

/**
 * @author johntheripp3r
 */
public class City
{
	private String iconUrl;
	private String cityName;
	private String mainDesc;
	private String description;
	private float temprature;
	private float pressure;
	private float humidity;
	private float windSpeed;
	private Date date;
	
	public City()
	{
		
	}
	
	public City(String iconUrl, String cityName, String mainMessage, String description, float temprature, long pressure, long humidity, long windSpeed, Date date)
	{
		this.iconUrl = iconUrl;
		this.cityName = cityName;
		this.mainDesc = mainMessage;
		this.description = description;
		this.temprature = temprature;
		this.pressure = pressure;
		this.humidity = humidity;
		this.windSpeed = windSpeed;
		this.date = date;
	}
	
	public String getIconUrl()
	{
		return this.iconUrl;
	}
	
	public void setIconUrl(String iconUrl)
	{
		this.iconUrl = iconUrl;
	}
	
	public String getCityName()
	{
		return this.cityName;
	}
	
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}
	
	public String getMainDesc()
	{
		return this.mainDesc;
	}
	
	public void setMainDesc(String mainMessage)
	{
		this.mainDesc = mainMessage;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public float getTemprature()
	{
		return this.temprature;
	}
	
	public void setTemprature(float temprature)
	{
		this.temprature = temprature;
	}
	
	public float getPressure()
	{
		return this.pressure;
	}
	
	public void setPressure(float pressure)
	{
		this.pressure = pressure;
	}
	
	public float getHumidity()
	{
		return this.humidity;
	}
	
	public void setHumidity(float humidity)
	{
		this.humidity = humidity;
	}
	
	public float getWindSpeed()
	{
		return this.windSpeed;
	}
	
	public void setWindSpeed(float windSpeed)
	{
		this.windSpeed = windSpeed;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	
	public void setDate(Date date)
	{
		this.date = date;
	}
	
}
