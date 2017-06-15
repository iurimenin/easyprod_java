package io.github.iurimenin.easyprod.app.farm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import io.github.iurimenin.easyprod.app.util.FirebaseUtils;

/**
 * Created by Iuri Menin on 14/06/17.
 */

public class FarmModel implements Parcelable {

    public static final String TAG = "FarmModel";

    private String key;
    private String name;

    public FarmModel() {}

    public FarmModel(String key, String name) {
        this.key = key;
        this.name = name;
    }

    @Exclude
    public final void save() {

        FirebaseUtils farmUtils = new FirebaseUtils();
        DatabaseReference myRef = farmUtils.getFarmReference();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FarmModel farmModel = (FarmModel) o;

        return key != null ? key.equals(farmModel.key) : farmModel.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FarmModel{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
    }

    protected FarmModel(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<FarmModel> CREATOR = new Parcelable.Creator<FarmModel>() {
        @Override
        public FarmModel createFromParcel(Parcel source) {
            return new FarmModel(source);
        }

        @Override
        public FarmModel[] newArray(int size) {
            return new FarmModel[size];
        }
    };
}
