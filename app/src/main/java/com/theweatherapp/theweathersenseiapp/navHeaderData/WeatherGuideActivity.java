package com.theweatherapp.theweathersenseiapp.navHeaderData;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.theweatherapp.theweathersenseiapp.MainActivity;
import com.theweatherapp.theweathersenseiapp.R;
import com.theweatherapp.theweathersenseiapp.databinding.ActivityWeatherGuideBinding;

public class WeatherGuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityWeatherGuideBinding weatherGuideDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherGuideDB = DataBindingUtil
                .setContentView(this, R.layout.activity_weather_guide);
        setSupportActionBar(weatherGuideDB.guideToolbar);
        MobileAds.initialize(this);
        weatherGuideDB.weatherGuideNativeAd.setVisibility(View.GONE);
        setUpAds();
    }

    public void setUpAds() {
        AdLoader adLoader=new AdLoader.Builder(this, MainActivity.NATIVE_AD_UNIT_ID)
                .forNativeAd(unifiedNativeAd -> {
                    weatherGuideDB.weatherGuideNativeAd.setVisibility(View.VISIBLE);
                    NativeTemplateStyle templateStyle=new NativeTemplateStyle
                            .Builder()
                            .build();
                    weatherGuideDB.weatherGuideNativeAd.setStyles(templateStyle);
                    weatherGuideDB.weatherGuideNativeAd.setNativeAd(unifiedNativeAd);
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
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

        switch (view.getId()){

            case R.id.cardView:
                weatherGuideDB.setIsPressureExpanded(!weatherGuideDB.getIsPressureExpanded());
                break;

            case R.id.cardView3:
                weatherGuideDB.setIsPOPExpanded(!weatherGuideDB.getIsPOPExpanded());
                break;
            case R.id.windSpeedCard:
                weatherGuideDB.setIsWindspeedExpanded(!weatherGuideDB.getIsWindspeedExpanded());
                break;
            case R.id.WindDegCard:
                weatherGuideDB.setIsWinddegExpanded(!weatherGuideDB.getIsWinddegExpanded());
                break;

            case R.id.humidityCard:
                weatherGuideDB.setIsHumidityExpanded(!weatherGuideDB.getIsHumidityExpanded());
                break;

            case R.id.uviCard:
                weatherGuideDB.setIsUviExpanded(!weatherGuideDB.getIsUviExpanded());
                break;
            case R.id.guide_activity_back_btn:
                onBackPressed();
                break;
            default:
                Toast.makeText(getApplicationContext(),"Invalid Activity",
                        Toast.LENGTH_SHORT).show();
                break;

        }

    }
}