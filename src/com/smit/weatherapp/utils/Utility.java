package com.smit.weatherapp.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.extractors.StringExtractor;
import com.ami.fundapter.interfaces.DynamicImageLoader;
import com.smit.weatherapp.R;
import com.smit.weatherapp.pojo.City;
import com.squareup.picasso.Picasso;

/**
 * @author johntheripp3r
 */
public class Utility
{
	public static void bindDictionary(Context context, BindDictionary<City> mDictionary)
	{
		final Context con = context;
		mDictionary.addDynamicImageField(R.id.weather_icon, new StringExtractor<City>()
		{
			
			@Override
			public String getStringValue(City item, int position)
			{
				return Constants.OPENMAP_WEATHER_ICON_URL + item.getIconUrl() + Constants.OPENMAP_WEATHER_ICON_EXTENSION;
			}
		}, new DynamicImageLoader()
		{
			@Override
			public void loadImage(String url, ImageView view)
			{
				Picasso.with(con).load(url).fit().into(view);
			}
		});
		
		mDictionary.addStringField(R.id.city_name, new StringExtractor<City>()
		{
			
			@Override
			public String getStringValue(City item, int position)
			{
				return item.getCityName();
			}
		});
		
		mDictionary.addStringField(R.id.current_temp, new StringExtractor<City>()
		{
			
			@Override
			public String getStringValue(City item, int position)
			{
				return (String.valueOf(item.getTemprature()) + "°C");
			}
			
		});
		
		mDictionary.addStringField(R.id.main_desc, new StringExtractor<City>()
		{
			@Override
			public String getStringValue(City item, int position)
			{
				return item.getMainDesc();
			}
		});
		
		mDictionary.addStringField(R.id.sub_desc, new StringExtractor<City>()
		{
			
			@Override
			public String getStringValue(City item, int position)
			{
				return item.getDescription();
			}
		});
		
		mDictionary.addStringField(R.id.wind, new StringExtractor<City>()
		{
			@Override
			public String getStringValue(City item, int position)
			{
				return (String.valueOf(item.getWindSpeed()) + " m/s");
			}
		});
		
		mDictionary.addStringField(R.id.date, new StringExtractor<City>()
		{
			@Override
			public String getStringValue(City item, int position)
			{
				return item.getDate() != null ? DateFormat.format("dd/MM/yyyy", (item.getDate().getTime() * 1000)).toString() : null;
			}
		}).visibilityIfNull(View.GONE);
	}
}
