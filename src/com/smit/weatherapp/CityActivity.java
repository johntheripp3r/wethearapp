package com.smit.weatherapp;

import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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
import com.smit.weatherapp.utils.Constants;
import com.smit.weatherapp.utils.Utility;

/**
 * @author johntheripp3r
 */
@SuppressLint("InlinedApi")
@SuppressWarnings("unchecked")
public class CityActivity extends RoboActivity
{
	@InjectView(R.id.citys_list_view)
	ListView mListView;
	
	ProgressDialog mProgressDialog;
	AsyncHttpClient mHttpClient;
	CityWeather mCityWeather;
	Gson mGson;
	FunDapter<City> mFunDapter;
	BindDictionary<City> mDictionary;
	ResponseHandler mResponseHandler;
	ArrayList<City> mCities;
	RequestParams mParams;
	ArrayList<String> cityNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_city);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.setupActionBar();
		// cityNames = (ArrayList<String>) getIntent().getSerializableExtra(Constants.PARCEL_CITY_NAMES);
		if(Build.VERSION.SDK_INT >= 11)
			this.mProgressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_DARK);
		else
			this.mProgressDialog = new ProgressDialog(this);
		this.mProgressDialog.setIndeterminate(true);
		this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.mProgressDialog.setCancelable(false);
		this.mProgressDialog.setMessage("Doing some random task...");
		
		this.mGson = new Gson();
		
		this.mCities = new ArrayList<City>();
		
		this.mDictionary = new BindDictionary<City>();
		Utility.bindDictionary(this, this.mDictionary);
		this.mFunDapter = new FunDapter<City>(this, this.mCities, R.layout.city_list_item, this.mDictionary);
		
		this.mHttpClient = new AsyncHttpClient();
		this.mResponseHandler = new ResponseHandler();
		this.mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		this.mListView.addHeaderView(this.getLayoutInflater().inflate(R.layout.city_list_header, null), null, false);
		this.mListView.setHeaderDividersEnabled(true);
		this.mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = new Intent(CityActivity.this, WeatherActivity.class);
				String city = ((TextView) view.findViewById(R.id.city_name)).getText().toString();
				intent.putExtra(Constants.PARCEL_CITY_NAME, city);
				CityActivity.this.startActivity(intent);
			}
		});
		this.mListView.setAdapter(this.mFunDapter);
	}
	
	@Override
	protected void onStart()
	{
		try
		{
			if(this.cityNames != null)
				this.cityNames.clear();
			if(this.getIntent().getSerializableExtra(Constants.PARCEL_CITY_NAMES) != null)
			{
				this.cityNames = (ArrayList<String>) this.getIntent().getSerializableExtra(Constants.PARCEL_CITY_NAMES);
				for(String city : this.cityNames)
				{
					this.mParams = new RequestParams();
					this.mParams.add(Constants.PARAM_CITY, city);
					this.mParams.add(Constants.PARAM_UNITS, Constants.PARAM_MATRICS);
					this.mParams.add(Constants.PARAM_API_KEY, Constants.API_KEY);
					this.mHttpClient.get(Constants.OPENMAP_CITY_URL, this.mParams, this.mResponseHandler);
				}
			}
			
		}
		catch(Exception e)
		{
		}
		super.onStart();
	}
	
	private class ResponseHandler extends AsyncHttpResponseHandler
	{
		@Override
		public void onStart()
		{
			super.onStart();
			if(!CityActivity.this.mProgressDialog.isShowing())
				CityActivity.this.mProgressDialog.show();
		}
		
		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
		{
			super.onSuccess(arg0, arg1, arg2);
			try
			{
				CityActivity.this.mCityWeather = CityActivity.this.mGson.fromJson(new String(arg2), CityWeather.class);
				City city = new City();
				city.setIconUrl(CityActivity.this.mCityWeather.getList().get(0).getIcon());
				city.setCityName(CityActivity.this.mCityWeather.getName());
				city.setDate(new Date(CityActivity.this.mCityWeather.getDt()));
				city.setDescription(CityActivity.this.mCityWeather.getList().get(0).getDescription());
				city.setMainDesc(CityActivity.this.mCityWeather.getList().get(0).getMain());
				city.setHumidity(CityActivity.this.mCityWeather.getMain().getHumidity());
				city.setPressure(CityActivity.this.mCityWeather.getMain().getPressure());
				city.setTemprature(CityActivity.this.mCityWeather.getMain().getTemp());
				city.setWindSpeed(CityActivity.this.mCityWeather.getWind().getSpeed());
				CityActivity.this.mCities.add(city);
				CityActivity.this.mFunDapter.updateData(CityActivity.this.mCities);
			}
			catch(Exception e)
			{
				CityActivity.this.Toast("Error parsing response from server");
			}
			finally
			{
				if(CityActivity.this.mProgressDialog.isShowing())
					CityActivity.this.mProgressDialog.hide();
			}
		}
		
		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
		{
			super.onFailure(arg0, arg1, arg2, arg3);
			if(CityActivity.this.mProgressDialog.isShowing())
				CityActivity.this.mProgressDialog.hide();
			CityActivity.this.Toast("No reply from server :(");
		}
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
