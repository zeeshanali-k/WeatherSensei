package com.theweatherapp.theweathersenseiapp.ui.customInrterfaces;

import android.util.Pair;
import android.view.View;

import com.theweatherapp.theweathersenseiapp.weatherDataModel.Daily;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Hourly;

public interface MyClickInterFace {

    void onHourlyItemClick(Hourly hourlyData, Pair<View,String> []pairsList);

    void onDailyItemClick(Daily dailyData, Pair<View, String> []pairsList);



}
