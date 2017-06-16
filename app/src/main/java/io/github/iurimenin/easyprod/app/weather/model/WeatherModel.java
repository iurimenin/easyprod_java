package io.github.iurimenin.easyprod.app.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iuri Menin on 16/06/17.
 */

public class WeatherModel implements Parcelable {

    private int weatherId;
    private long dateTime;
    private int humidity;
    private double pressure;
    private double windSpeed;
    private double windDirection;
    private double high;
    private double low;
    private String description;

    public WeatherModel(long dateTime, int humidity, double pressure, double windSpeed,
                        double windDirection, double high, double low, String description,
                        int weatherId) {

        this.dateTime = dateTime;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.high = high;
        this.low = low;
        this.description = description;
        this.weatherId = weatherId;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherModel that = (WeatherModel) o;

        return weatherId == that.weatherId;
    }

    @Override
    public int hashCode() {
        return weatherId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.weatherId);
        dest.writeLong(this.dateTime);
        dest.writeInt(this.humidity);
        dest.writeDouble(this.pressure);
        dest.writeDouble(this.windSpeed);
        dest.writeDouble(this.windDirection);
        dest.writeDouble(this.high);
        dest.writeDouble(this.low);
        dest.writeString(this.description);
    }

    protected WeatherModel(Parcel in) {
        this.weatherId = in.readInt();
        this.dateTime = in.readLong();
        this.humidity = in.readInt();
        this.pressure = in.readDouble();
        this.windSpeed = in.readDouble();
        this.windDirection = in.readDouble();
        this.high = in.readDouble();
        this.low = in.readDouble();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<WeatherModel> CREATOR = new Parcelable.Creator<WeatherModel>() {
        @Override
        public WeatherModel createFromParcel(Parcel source) {
            return new WeatherModel(source);
        }

        @Override
        public WeatherModel[] newArray(int size) {
            return new WeatherModel[size];
        }
    };
}
