package com.theweatherapp.theweathersenseiapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.theweatherapp.theweathersenseiapp.weatherDataModel.Hourly;

public class WeatherHourlyDetails extends AppCompatActivity implements View.OnClickListener {
    private ViewDataBinding viewDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather_hourly_details_layout);

        setUpPerfs();
        setSupportActionBar(viewDataBinding.getRoot().findViewById(R.id.hourly_activity_toolbar));

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            Hourly hourlyData=extras.getParcelable(MainActivity.HOURLY_DATA);
            if (hourlyData!=null){
                viewDataBinding.setVariable(BR.hourlyWeatherDataDetails,hourlyData);
            }
        }

    }


    private void setUpPerfs() {
        viewDataBinding.setVariable(BR.isHDFahrenheitEnabled,getSharedPreferences(MainActivity.WEAHTER_SENSEI_PREFERENCES,MODE_PRIVATE)
        .getBoolean(MainActivity.IS_FAHRENHEIT,false));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.hourly_details_back_btn){
            onBackPressed();
        }
    }
}