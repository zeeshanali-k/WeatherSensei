package com.theweatherapp.theweathersenseiapp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.theweatherapp.theweathersenseiapp.databinding.ActivityWeatherDailyDetailsLayoutBinding;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Daily;

public class WeatherDailyDetails extends AppCompatActivity implements View.OnClickListener{

    private ActivityWeatherDailyDetailsLayoutBinding viewDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_weather_daily_details_layout);
        viewDataBinding.setDailyListener(this);
        viewDataBinding.setIsExtraDetailsExp(true);
        viewDataBinding.setIsTempDetailsExp(true);
        setUpPerfs();

        Bundle extras = getIntent().getExtras();
        if (extras!=null){

            Daily dailyData=extras.getParcelable(MainActivity.DAILY_DATA);
            if (dailyData!=null)
                viewDataBinding.setVariable(BR.dailyWeatherDataDetails,dailyData);
        }
    }
    private void setUpPerfs() {
        viewDataBinding.setIsDDFahrenheitEnabled(getSharedPreferences(MainActivity.WEAHTER_SENSEI_PREFERENCES,MODE_PRIVATE)
                .getBoolean(MainActivity.IS_FAHRENHEIT,false));

    }


    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.daily_details_temp_detail_dropdown){
            if (viewDataBinding.getIsTempDetailsExp()){
                TransitionManager.beginDelayedTransition(viewDataBinding.dailyScView,new AutoTransition());
                viewDataBinding.dailyDetailsTempDetailDropdown
                        .animate().rotation(0);
                viewDataBinding.setIsTempDetailsExp(false);
            }
            else {
                TransitionManager.beginDelayedTransition(viewDataBinding.dailyScView,new AutoTransition());
                viewDataBinding.dailyDetailsTempDetailDropdown
                        .animate().rotation(180);
                viewDataBinding.setIsTempDetailsExp(true);
            }
        }

        else if (view.getId()==R.id.daily_details_temp_extra_dropdown){
            if (viewDataBinding.getIsExtraDetailsExp()){
                TransitionManager.beginDelayedTransition(viewDataBinding.dailyScView,new AutoTransition());
                viewDataBinding.dailyDetailsTempExtraDropdown.animate().rotation(0);
                viewDataBinding.setIsExtraDetailsExp(false);
            }
            else {
                TransitionManager.beginDelayedTransition(viewDataBinding.dailyScView,new AutoTransition());
                viewDataBinding.dailyDetailsTempExtraDropdown.animate().rotation(180);
                viewDataBinding.setIsExtraDetailsExp(true);
            }
        }
        else if (view.getId()==R.id.daily_details_back_btn){
            onBackPressed();
        }


    }
}