package io.github.iurimenin.easyprod.app.widget.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class WidgetModel implements Parcelable {

    public static final String TAG = "WidgetModel";

    private String productionKey;
    private double bags;
    private String cultivationKey;
    private String cultivationName;
    private String seasonName;

    public WidgetModel() {}

    public WidgetModel(String productionKey, double bags, String cultivationKey,
                       String cultivationName, String seasonName) {
        this.productionKey = productionKey;
        this.bags = bags;
        this.cultivationKey = cultivationKey;
        this.cultivationName = cultivationName;
        this.seasonName = seasonName;
    }

    public String getProductionKey() {
        return productionKey;
    }

    public void setProductionKey(String productionKey) {
        this.productionKey = productionKey;
    }

    public double getBags() {
        return bags;
    }

    public void setBags(double bags) {
        this.bags = bags;
    }

    public String getCultivationKey() {
        return cultivationKey;
    }

    public void setCultivationKey(String cultivationKey) {
        this.cultivationKey = cultivationKey;
    }

    public String getCultivationName() {
        return cultivationName;
    }

    public void setCultivationName(String cultivationName) {
        this.cultivationName = cultivationName;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productionKey);
        dest.writeDouble(this.bags);
        dest.writeString(this.cultivationKey);
        dest.writeString(this.cultivationName);
        dest.writeString(this.seasonName);
    }

    protected WidgetModel(Parcel in) {
        this.productionKey = in.readString();
        this.bags = in.readDouble();
        this.cultivationKey = in.readString();
        this.cultivationName = in.readString();
        this.seasonName = in.readString();
    }

    public static final Parcelable.Creator<WidgetModel> CREATOR = new Parcelable.Creator<WidgetModel>() {
        @Override
        public WidgetModel createFromParcel(Parcel source) {
            return new WidgetModel(source);
        }

        @Override
        public WidgetModel[] newArray(int size) {
            return new WidgetModel[size];
        }
    };
}
