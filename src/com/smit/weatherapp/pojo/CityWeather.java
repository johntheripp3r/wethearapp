package com.smit.weatherapp.pojo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author johntheripp3r
 */
public class CityWeather
{
	
	private String message;
	private int cod;
	private int count;
	private long dt;
	private String name;
	private Main main;
	private Wind wind;
	private Sys sys;
	private Coord coord;
	private Clouds clouds;
	@SerializedName("weather")
	private List<Weather> list;
	
	public CityWeather()
	{
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public int getCod()
	{
		return this.cod;
	}
	
	public void setCod(int cod)
	{
		this.cod = cod;
	}
	
	public int getCount()
	{
		return this.count;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public List<Weather> getList()
	{
		return this.list;
	}
	
	public void setList(List<Weather> list)
	{
		this.list = list;
	}
	
	public long getDt()
	{
		return this.dt;
	}
	
	public void setDt(long dt)
	{
		this.dt = dt;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Main getMain()
	{
		return this.main;
	}
	
	public void setMain(Main main)
	{
		this.main = main;
	}
	
	public Wind getWind()
	{
		return this.wind;
	}
	
	public void setWind(Wind wind)
	{
		this.wind = wind;
	}
	
	public Sys getSys()
	{
		return this.sys;
	}
	
	public void setSys(Sys sys)
	{
		this.sys = sys;
	}
	
	public Coord getCoord()
	{
		return this.coord;
	}
	
	public void setCoord(Coord coord)
	{
		this.coord = coord;
	}
	
	public Clouds getClouds()
	{
		return this.clouds;
	}
	
	public void setClouds(Clouds clouds)
	{
		this.clouds = clouds;
	}
	
	@Override
	public String toString()
	{
		return "CityWeather [message=" + this.message + ", cod=" + this.cod + ", count=" + this.count + ", dt=" + this.dt + ", name=" + this.name + ", main=" + this.main + ", wind=" + this.wind + ", sys=" + this.sys + ", coord=" + this.coord + ", clouds=" + this.clouds + ", list=" + this.list + "]";
	}
	
}
