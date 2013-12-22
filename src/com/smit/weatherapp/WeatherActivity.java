package com.smit.weatherapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.smit.weatherapp.pojo.City;
import com.smit.weatherapp.pojo.CityWeather;
import com.smit.weatherapp.pojo.History;
import com.smit.weatherapp.pojo.Weather;
import com.smit.weatherapp.utils.Constants;
import com.smit.weatherapp.utils.Utility;

public class WeatherActivity extends RoboActivity
{
	@InjectView(R.id.city_weather_list_view)
	private ListView mListView;
	
	private String mCityName;
	private BindDictionary<City> mDictionary;
	private FunDapter<City> mFunDapter;
	private ArrayList<City> mCities;
	
	private AsyncHttpClient mHttpClient;
	private RequestParams mParams;
	private ResponseHandler mResponseHandler;
	
	private ProgressDialog mProgressDialog;
	
	private History mHistory;
	private Gson mGson;
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_weather);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.setupActionBar();
		
		this.mCityName = this.getIntent().getStringExtra(Constants.PARCEL_CITY_NAME);
		
		this.mGson = new Gson();
		this.mCities = new ArrayList<City>();
		
		if(Build.VERSION.SDK_INT >= 11)
			this.mProgressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_DARK);
		else
			this.mProgressDialog = new ProgressDialog(this);
		this.mProgressDialog.setIndeterminate(true);
		this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.mProgressDialog.setMessage("Doing important stuff.");
		
		this.mDictionary = new BindDictionary<City>();
		Utility.bindDictionary(this, this.mDictionary);
		this.mFunDapter = new FunDapter<City>(this, this.mCities, R.layout.city_list_item, this.mDictionary);
		
		RelativeLayout header = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.city_list_header, null);
		((TextView) header.findViewById(R.id.header_text)).setText("Last 30 days weather");
		this.mListView.addHeaderView(header);
		this.mListView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
		this.mListView.setAdapter(this.mFunDapter);
		
		this.mHttpClient = new AsyncHttpClient();
		this.mResponseHandler = new ResponseHandler();
		
		this.mParams = new RequestParams();
		this.mParams.add(Constants.PARAM_CITY, this.mCityName);
		this.mParams.add(Constants.PARAM_UNITS, Constants.PARAM_MATRICS);
		this.mParams.add(Constants.PARAM_COUNT, "30");
		this.mParams.add(Constants.PARAM_HISTORY_TYPE, Constants.PARAM_DAY);
		this.mParams.add(Constants.PARAM_API_KEY, Constants.API_KEY);
		this.mHttpClient.get(Constants.OPENMAP_HISTORY_URL, this.mParams, this.mResponseHandler);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar()
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			this.getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		this.getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		this.finish();
	}
	
	private class ResponseHandler extends AsyncHttpResponseHandler
	{
		
		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
		{
			super.onSuccess(arg0, arg1, arg2);
			try
			{
				String g = new String(arg2);
				Log.d("string", g);
				WeatherActivity.this.mHistory = WeatherActivity.this.mGson.fromJson(new String(arg2), History.class);
				List<CityWeather> cityWeatherList = WeatherActivity.this.mHistory.getCityWeather();
				for(CityWeather cityWeather : cityWeatherList)
				{
					Weather weather = cityWeather.getList().get(0);
					City city = new City();
					city.setIconUrl(weather.getIcon());
					city.setCityName(WeatherActivity.this.mCityName);
					city.setDate(new Date(cityWeather.getDt()));
					city.setDescription(weather.getDescription());
					city.setMainDesc(weather.getMain());
					city.setHumidity(cityWeather.getMain().getHumidity());
					city.setPressure(cityWeather.getMain().getPressure());
					city.setTemprature(cityWeather.getMain().getTemp());
					city.setWindSpeed(cityWeather.getWind().getSpeed());
					WeatherActivity.this.mCities.add(city);
				}
				WeatherActivity.this.mFunDapter.updateData(WeatherActivity.this.mCities);
			}
			catch(Exception e)
			{
				WeatherActivity.this.Toast("Tech stuff " + e.toString());
			}
			finally
			{
				if(WeatherActivity.this.mProgressDialog.isShowing())
					WeatherActivity.this.mProgressDialog.hide();
			}
		}
		
		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
		{
			super.onFailure(arg0, arg1, arg2, arg3);
			if(WeatherActivity.this.mProgressDialog.isShowing())
				WeatherActivity.this.mProgressDialog.hide();
			WeatherActivity.this.Toast("Someone has connection troubles.");
		}
		
		@Override
		public void onStart()
		{
			super.onStart();
			this.getRequestURI().toString();
			if(!WeatherActivity.this.mProgressDialog.isShowing())
				WeatherActivity.this.mProgressDialog.show();
		}
	}
	
	private void Toast(String message)
	{
		try
		{
			Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
			TextView msg = (TextView) toast.getView().findViewById(android.R.id.message);
			if(msg != null)
				msg.setGravity(Gravity.CENTER);
			toast.show();
		}
		catch(Exception e)
		{
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}
	}
}
