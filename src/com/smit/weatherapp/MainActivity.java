package com.smit.weatherapp;

import java.io.IOException;
import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.inject.Inject;
import com.smit.weatherapp.utils.Constants;

/**
 * @author johntheripp3r
 */
@SuppressLint("InlinedApi")
public class MainActivity extends RoboActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
{
	@InjectView(R.id.city_list_view)
	private ListView mListView;
	@InjectView(R.id.add_city)
	private Button addCityBtn;
	@InjectView(R.id.city_name_edit_text)
	private EditText cityNameEt;
	@InjectView(R.id.go)
	private Button goBtn;
	
	@Inject
	private ConnectivityManager mConnectivityManager;
	@Inject
	private LocationManager mLocationManager;
	
	private ArrayAdapter<String> mAdapter;
	
	private boolean gpsAvailable;
	private LocationClient mLocationClient;
	private Geocoder mGeocoder;
	
	private ProgressDialog mProgressDialog;
	
	private ArrayList<String> list;
	
	boolean firstTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.firstTime = true;
		this.list = new ArrayList<String>();
		this.mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.list);
		this.mListView.setAdapter(this.mAdapter);
		if(Build.VERSION.SDK_INT >= 11)
			this.mProgressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_DARK);
		else
			this.mProgressDialog = new ProgressDialog(this);
		this.mProgressDialog.setIndeterminate(true);
		this.mProgressDialog.setCancelable(false);
		this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.mProgressDialog.setMessage("Hmm.. Let me see where you live");
		
		this.addCityBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(!MainActivity.this.cityNameEt.getText().toString().trim().equalsIgnoreCase(""))
				{
					MainActivity.this.mAdapter.add(MainActivity.this.cityNameEt.getText().toString());
					MainActivity.this.mAdapter.notifyDataSetChanged();
					MainActivity.this.cityNameEt.setText("");
				}
				else
				{
					MainActivity.this.Toast("Space, Space everywhere.");
				}
			}
		});
		
		this.goBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, CityActivity.class);
				ArrayList<String> list;
				if(MainActivity.this.mAdapter.getCount() > 0)
				{
					if(MainActivity.this.mConnectivityManager.getActiveNetworkInfo() != null)
					{
						if(MainActivity.this.mConnectivityManager.getActiveNetworkInfo().isConnected())
						{
							list = new ArrayList<String>();
							for(int i = 0; i < MainActivity.this.mAdapter.getCount(); i++)
							{
								list.add(MainActivity.this.mAdapter.getItem(i));
							}
							intent.putExtra(Constants.PARCEL_CITY_NAMES, list);
							MainActivity.this.startActivity(intent);
						}
						else
						{
							MainActivity.this.Toast("No Internet. No cookies.");
						}
					}
					else
					{
						MainActivity.this.Toast("No Internet. No cookies.");
					}
				}
				else
				{
					MainActivity.this.Toast("but where?");
				}
			}
		});
		
		if(!this.mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			this.Toast("Please turn on GPS to get your current location");
		}
		if(this.mConnectivityManager.getActiveNetworkInfo() != null)
		{
			if(!this.mConnectivityManager.getActiveNetworkInfo().isConnected())
				this.Toast("Please turn on internet to get your location");
		}
		else
		{
			this.Toast("Please turn on internet to get your location");
		}
		
		if(this.mConnectivityManager.getActiveNetworkInfo() != null)
		{
			if(this.mConnectivityManager.getActiveNetworkInfo().isConnected() && this.mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				if(ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(this))
					this.gpsAvailable = true;
				else
					this.gpsAvailable = false;
			}
		}
		
		this.mLocationClient = new LocationClient(this, this, this);
		this.mGeocoder = new Geocoder(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		if(this.gpsAvailable && this.firstTime)
		{
			this.mProgressDialog.show();
			this.mLocationClient.connect();
		}
	}
	
	@Override
	protected void onPause()
	{
		if(this.mProgressDialog.isShowing())
			this.mProgressDialog.hide();
		super.onPause();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0)
	{
		if(this.mProgressDialog.isShowing())
			this.mProgressDialog.hide();
		this.Toast("Sorry I cannot find your location :(");
	}
	
	@Override
	public void onConnected(Bundle arg0)
	{
		Location location = this.mLocationClient.getLastLocation();
		if(location != null)
		{
			Address address = null;
			try
			{
				address = this.mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
				if(address != null)
				{
					this.mAdapter.add(address.getLocality());
					this.Toast("Simon says, you live in " + address.getLocality());
				}
				else
				{
					this.Toast("Sorry I cannot find your location :(");
				}
				this.firstTime = false;
			}
			catch(IOException e)
			{
				this.Toast("Sorry I cannot find your location :(");
			}
			finally
			{
				if(this.mProgressDialog.isShowing())
					this.mProgressDialog.hide();
			}
		}
		else
		{
			if(this.mProgressDialog.isShowing())
				this.mProgressDialog.hide();
			this.Toast("Sorry I cannot find your location :(");
		}
	}
	
	@Override
	public void onDisconnected()
	{
		if(this.mProgressDialog.isShowing())
			this.mProgressDialog.hide();
		this.Toast("Sorry I cannot find your location :(");
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
