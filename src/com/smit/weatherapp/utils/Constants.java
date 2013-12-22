package com.smit.weatherapp.utils;

/**
 * @author johntheripp3r
 */
public interface Constants
{
	String OPENMAP_URL = "http://api.openweathermap.org/data/2.5/";
	String OPENMAP_HISTORY_URL = OPENMAP_URL + "history/city";
	String OPENMAP_CITY_URL = OPENMAP_URL + "weather";
	String OPENMAP_WEATHER_ICON_URL = "http://openweathermap.org/img/w/";
	String OPENMAP_WEATHER_ICON_EXTENSION = ".png";
	
	String API_KEY = "506ffbbe419d671bdd7d67c8d5164564";
	
	String PARAM_API_KEY = "APPID";
	String PARAM_UNITS = "units";
	String PARAM_CITY = "q";
	String PARAM_MATRICS = "metric";
	String PARAM_COUNT = "cnt";
	String PARAM_HISTORY_TYPE = "type";
	String PARAM_DAY = "day";
	String PARAM_START_DATE = "start";
	String PARAM_END_DATE = "end";
	
	String PARCEL_CITY_NAMES = "citynames";
	String PARCEL_CITY_NAME = "cityname";
	
}
