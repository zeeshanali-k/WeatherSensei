package com.theweatherapp.theweathersenseiapp.weatherDataModel;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hourly implements Parcelable {

    @SerializedName("dt")
    @Expose
    private long dt;
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
    private List<Weather__> weather = null;
    @SerializedName("pop")
    @Expose
    private float pop;

    /**
     * No args constructor for use in serialization
     *
     */
    public Hourly() {
    }


    public Hourly(long dt, double temp, double feelsLike, long pressure, long humidity, double dewPoint, long clouds, long visibility, double windSpeed, long windDeg, List<Weather__> weather, float pop) {
        super();
        this.dt = dt;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dewPoint = dewPoint;
        this.clouds = clouds;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
        this.weather = weather;
        this.pop = pop;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
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

    public List<Weather__> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather__> weather) {
        this.weather = weather;
    }

    public float getPop() {
        return pop;
    }

    public void setPop(float pop) {
        this.pop = pop;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.dt);
        dest.writeDouble(this.temp);
        dest.writeDouble(this.feelsLike);
        dest.writeLong(this.pressure);
        dest.writeLong(this.humidity);
        dest.writeDouble(this.dewPoint);
        dest.writeLong(this.clouds);
        dest.writeLong(this.visibility);
        dest.writeDouble(this.windSpeed);
        dest.writeLong(this.windDeg);
        dest.writeTypedList(this.weather);
        dest.writeFloat(this.pop);
    }

    protected Hourly(Parcel in) {
        this.dt = in.readLong();
        this.temp = in.readDouble();
        this.feelsLike = in.readDouble();
        this.pressure = in.readLong();
        this.humidity = in.readLong();
        this.dewPoint = in.readDouble();
        this.clouds = in.readLong();
        this.visibility = in.readLong();
        this.windSpeed = in.readDouble();
        this.windDeg = in.readLong();
        this.weather = in.createTypedArrayList(Weather__.CREATOR);
        this.pop = in.readFloat();
    }

    public static final Creator<Hourly> CREATOR = new Creator<Hourly>() {
        @Override
        public Hourly createFromParcel(Parcel source) {
            return new Hourly(source);
        }

        @Override
        public Hourly[] newArray(int size) {
            return new Hourly[size];
        }
    };
}
