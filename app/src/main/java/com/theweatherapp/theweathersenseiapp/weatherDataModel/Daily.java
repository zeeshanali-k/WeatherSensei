package com.theweatherapp.theweathersenseiapp.weatherDataModel;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Daily implements Parcelable {

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
    private Temp temp;
    @SerializedName("feels_like")
    @Expose
    private FeelsLike feelsLike;
    @SerializedName("pressure")
    @Expose
    private long pressure;
    @SerializedName("humidity")
    @Expose
    private long humidity;
    @SerializedName("dew_point")
    @Expose
    private double dewPoint;
    @SerializedName("wind_speed")
    @Expose
    private double windSpeed;
    @SerializedName("wind_deg")
    @Expose
    private long windDeg;
    @SerializedName("weather")
    @Expose
    private List<Weather___> weather = null;
    @SerializedName("clouds")
    @Expose
    private long clouds;
    @SerializedName("pop")
    @Expose
    private float pop;
    @SerializedName("uvi")
    @Expose
    private double uvi;
    public Daily() {
    }


    public Daily(long dt, long sunrise, long sunset, Temp temp, FeelsLike feelsLike, long pressure, long humidity, double dewPoint, double windSpeed, long windDeg, List<Weather___> weather, long clouds, float pop, double uvi) {
        super();
        this.dt = dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dewPoint = dewPoint;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
        this.weather = weather;
        this.clouds = clouds;
        this.pop = pop;
        this.uvi = uvi;
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

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public FeelsLike getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(FeelsLike feelsLike) {
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

    public List<Weather___> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather___> weather) {
        this.weather = weather;
    }

    public long getClouds() {
        return clouds;
    }

    public void setClouds(long clouds) {
        this.clouds = clouds;
    }

    public float getPop() {
        return pop;
    }

    public void setPop(float pop) {
        this.pop = pop;
    }

    public double getUvi() {
        return uvi;
    }

    public void setUvi(double uvi) {
        this.uvi = uvi;
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
        dest.writeParcelable(this.temp, flags);
        dest.writeParcelable(this.feelsLike, flags);
        dest.writeLong(this.pressure);
        dest.writeLong(this.humidity);
        dest.writeDouble(this.dewPoint);
        dest.writeDouble(this.windSpeed);
        dest.writeLong(this.windDeg);
        dest.writeTypedList(this.weather);
        dest.writeLong(this.clouds);
        dest.writeFloat(this.pop);
        dest.writeDouble(this.uvi);
    }

    protected Daily(Parcel in) {
        this.dt = in.readLong();
        this.sunrise = in.readLong();
        this.sunset = in.readLong();
        this.temp = in.readParcelable(Temp.class.getClassLoader());
        this.feelsLike = in.readParcelable(FeelsLike.class.getClassLoader());
        this.pressure = in.readLong();
        this.humidity = in.readLong();
        this.dewPoint = in.readDouble();
        this.windSpeed = in.readDouble();
        this.windDeg = in.readLong();
        this.weather = in.createTypedArrayList(Weather___.CREATOR);
        this.clouds = in.readLong();
        this.pop = in.readFloat();
        this.uvi = in.readDouble();
    }

    public static final Creator<Daily> CREATOR = new Creator<Daily>() {
        @Override
        public Daily createFromParcel(Parcel source) {
            return new Daily(source);
        }

        @Override
        public Daily[] newArray(int size) {
            return new Daily[size];
        }
    };
}
