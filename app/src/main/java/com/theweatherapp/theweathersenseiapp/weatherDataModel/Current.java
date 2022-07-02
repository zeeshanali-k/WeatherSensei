package com.theweatherapp.theweathersenseiapp.weatherDataModel;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current implements Parcelable {

    @SerializedName("dt")
    @Expose
    private long dt;
    @SerializedName("sunrise")
    @Expose
    private long sunrise;
    @SerializedName("sunset")
    @Expose
    private long sunset;
    @SerializedName("temp")
    @Expose
    private double temp;
    @SerializedName("feels_like")
    @Expose
    private double feelsLike;
    @SerializedName("pressure")
    @Expose
    private long pressure;
    @SerializedName("humidity")
    @Expose
    private long humidity;
    @SerializedName("dew_point")
    @Expose
    private double dewPoint;
    @SerializedName("uvi")
    @Expose
    private double uvi;
    @SerializedName("clouds")
    @Expose
    private long clouds;
    @SerializedName("visibility")
    @Expose
    private long visibility;
    @SerializedName("wind_speed")
    @Expose
    private double windSpeed;
    @SerializedName("wind_deg")
    @Expose
    private long windDeg;
    @SerializedName("weather")
    @Expose
    private List<Weather_> weather = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Current() {
    }


    public Current(long dt, long sunrise, long sunset, double temp, double feelsLike, long pressure, long humidity, double dewPoint, double uvi, long clouds, long visibility, double windSpeed, long windDeg, List<Weather_> weather) {
        super();
        this.dt = dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dewPoint = dewPoint;
        this.uvi = uvi;
        this.clouds = clouds;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
        this.weather = weather;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public long getPressure() {
        return pressure;
    }

    public void setPressure(long pressure) {
        this.pressure = pressure;
    }

    public long getHumidity() {
        return humidity;
    }

    public void setHumidity(long humidity) {
        this.humidity = humidity;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public double getUvi() {
        return uvi;
    }

    public void setUvi(double uvi) {
        this.uvi = uvi;
    }

    public long getClouds() {
        return clouds;
    }

    public void setClouds(long clouds) {
        this.clouds = clouds;
    }

    public long getVisibility() {
        return visibility;
    }

    public void setVisibility(long visibility) {
        this.visibility = visibility;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public long getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(long windDeg) {
        this.windDeg = windDeg;
    }

    public List<Weather_> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather_> weather) {
        this.weather = weather;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.dt);
        dest.writeLong(this.sunrise);
        dest.writeLong(this.sunset);
        dest.writeDouble(this.temp);
        dest.writeDouble(this.feelsLike);
        dest.writeLong(this.pressure);
        dest.writeLong(this.humidity);
        dest.writeDouble(this.dewPoint);
        dest.writeDouble(this.uvi);
        dest.writeLong(this.clouds);
        dest.writeLong(this.visibility);
        dest.writeDouble(this.windSpeed);
        dest.writeLong(this.windDeg);
        dest.writeTypedList(this.weather);
    }

    protected Current(Parcel in) {
        this.dt = in.readLong();
        this.sunrise = in.readLong();
        this.sunset = in.readLong();
        this.temp = in.readDouble();
        this.feelsLike = in.readDouble();
        this.pressure = in.readLong();
        this.humidity = in.readLong();
        this.dewPoint = in.readDouble();
        this.uvi = in.readDouble();
        this.clouds = in.readLong();
        this.visibility = in.readLong();
        this.windSpeed = in.readDouble();
        this.windDeg = in.readLong();
        this.weather = in.createTypedArrayList(Weather_.CREATOR);
    }

    public static final Creator<Current> CREATOR = new Creator<Current>() {
        @Override
        public Current createFromParcel(Parcel source) {
            return new Current(source);
        }

        @Override
        public Current[] newArray(int size) {
            return new Current[size];
        }
    };
}