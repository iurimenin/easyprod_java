package io.github.iurimenin.easyprod.app.season.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class SeasonModel implements Parcelable {

    public static final String TAG = "SeasonModel";

    private String key;
    private int startYear;
    private int endYear;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeInt(this.startYear);
        dest.writeInt(this.endYear);
    }

    public SeasonModel() {
    }

    protected SeasonModel(Parcel in) {
        this.key = in.readString();
        this.startYear = in.readInt();
        this.endYear = in.readInt();
    }

    public static final Parcelable.Creator<SeasonModel> CREATOR = new Parcelable.Creator<SeasonModel>() {
        @Override
        public SeasonModel createFromParcel(Parcel source) {
            return new SeasonModel(source);
        }

        @Override
        public SeasonModel[] newArray(int size) {
            return new SeasonModel[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeasonModel that = (SeasonModel) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Exclude
    public String toString() {
        return this.startYear + "/" + this.endYear;
    }
}
