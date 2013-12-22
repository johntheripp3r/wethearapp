package com.smit.weatherapp.pojo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author johntheripp3r
 */
public class History
{
	@SerializedName("list")
	private List<CityWeather> cityWeather;
	
	public List<CityWeather> getCityWeather()
	{
		return this.cityWeather;
	}
	
	public void setCityWeather(List<CityWeather> cityWeather)
	{
		this.cityWeather = cityWeather;
	}
	
}
