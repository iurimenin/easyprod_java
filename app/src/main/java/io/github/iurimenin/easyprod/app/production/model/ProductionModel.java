package io.github.iurimenin.easyprod.app.production.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import io.github.iurimenin.easyprod.app.util.FirebaseUtils;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class ProductionModel implements Parcelable {

    public static final String TAG = "ProductionModel";

    private String key;
    private double bags;

    public ProductionModel() {}

    public ProductionModel(String key, double bags) {
        this.key = key;
        this.bags = bags;
    }

    @Exclude
    public final void save(String cultivationKey) {
        DatabaseReference myRef = new FirebaseUtils().getProductionReference(cultivationKey);
        if(this.key == null || this.key.length() == 0) {
            this.key = myRef.push().getKey();
        }

        myRef.child(this.key).setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getBags() {
        return bags;
    }

    public void setBags(double bags) {
        this.bags = bags;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeDouble(this.bags);
    }

    protected ProductionModel(Parcel in) {
        this.key = in.readString();
        this.bags = in.readDouble();
    }

    public static final Parcelable.Creator<ProductionModel> CREATOR = new Parcelable.Creator<ProductionModel>() {
        @Override
        public ProductionModel createFromParcel(Parcel source) {
            return new ProductionModel(source);
        }

        @Override
        public ProductionModel[] newArray(int size) {
            return new ProductionModel[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductionModel that = (ProductionModel) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
