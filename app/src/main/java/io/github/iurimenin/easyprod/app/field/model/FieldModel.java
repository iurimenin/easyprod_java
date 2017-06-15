package io.github.iurimenin.easyprod.app.field.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import io.github.iurimenin.easyprod.app.util.FirebaseUtils;

/**
 * Created by Iuri Menin on 15/06/17.
 */
public class FieldModel implements Parcelable {

    public static final String TAG = "FieldModel";

    private String key;
    private String name;
    private double totalArea;

    public FieldModel() {
    }

    public FieldModel(String key, String name, double totalArea) {

        this.key = key;
        this.name = name;
        this.totalArea = totalArea;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(double totalArea) {
        this.totalArea = totalArea;
    }

    @Exclude
    public final void save(String farmKey) {
        DatabaseReference myRef = new FirebaseUtils().getFieldReference(farmKey);

        if(this.key == null || this.key.length() == 0) {
            this.key = myRef.push().getKey();
        }

        myRef.child(this.key).setValue(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeDouble(this.totalArea);
    }

    protected FieldModel(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.totalArea = in.readDouble();
    }

    public static final Parcelable.Creator<FieldModel> CREATOR = new Parcelable.Creator<FieldModel>() {
        @Override
        public FieldModel createFromParcel(Parcel source) {
            return new FieldModel(source);
        }

        @Override
        public FieldModel[] newArray(int size) {
            return new FieldModel[size];
        }
    };
}
