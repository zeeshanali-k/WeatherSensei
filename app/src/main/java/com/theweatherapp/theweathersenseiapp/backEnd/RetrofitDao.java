package com.theweatherapp.theweathersenseiapp.backEnd;

import com.theweatherapp.theweathersenseiapp.weatherDataModel.WeatherData;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RetrofitDao {

    String BASE_URL="https://api.openweathermap.org/";
    String RELATIVE_URL ="data/2.5/onecall";

    OkHttpClient OK_HTTP_CLIENT=new OkHttpClient.Builder()
            .callTimeout(5, TimeUnit.MINUTES)
            .build();

    Retrofit retroFit=new Retrofit.Builder()
            .client(OK_HTTP_CLIENT)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET(RELATIVE_URL)
    Call<WeatherData> getWeatherdata(@QueryMap Map<String,String> queryParams);


}
