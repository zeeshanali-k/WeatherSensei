package com.theweatherapp.theweathersenseiapp.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.theweatherapp.theweathersenseiapp.backEnd.RetrofitDao;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.WeatherData;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataFragViewModel extends ViewModel {

    private String TAG="tagg";
    private MutableLiveData<WeatherData> weatherDataMutableLiveData;
    private RetrofitDao retrofitDao;


    public WeatherDataFragViewModel(){
        retrofitDao=RetrofitDao.retroFit.create(RetrofitDao.class);
    }

    public LiveData<WeatherData> getLiveData(){
        if (weatherDataMutableLiveData==null)
            weatherDataMutableLiveData=new MutableLiveData<>();
        return weatherDataMutableLiveData;

    }


    public void getWeatherData(double lat,double lon,String unit){

        Map<String,String> queryMap=new HashMap<>();

        queryMap.put("lat",String.valueOf(lat));
        queryMap.put("lon",String.valueOf(lon));
        queryMap.put("units",unit);
        queryMap.put("appid","2e6bf386ab7eb4a6650f129879bd2e03");
        queryMap.put("exclude","minutely");

        Call<WeatherData> weatherdata = retrofitDao.getWeatherdata(queryMap);

        weatherdata.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    weatherDataMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });



    }
}