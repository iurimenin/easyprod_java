package io.github.iurimenin.easyprod.app.cultivation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import io.github.iurimenin.easyprod.app.util.FirebaseUtils;


/**
 * Created by Iuri Menin on 14/06/17.
 */
public class CultivationModel implements Parcelable {

    public static final String TAG = "CultivationModel";

    private String key;
    private String name;
    private String seasonKey;
    private String seasonName;

    public CultivationModel(String key, String name,
                            String seasonKey, String seasonName) {

        this.key = key;
        this.name = name;
        this.seasonKey = seasonKey;
        this.seasonName = seasonName;
    }

    @Exclude
    public final void save(String farmKey) {
        DatabaseReference myRef = (new FirebaseUtils()).getFieldReference(farmKey);
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
        dest.writeString(this.seasonKey);
        dest.writeString(this.seasonName);
    }

    public CultivationModel() {
    }

    protected CultivationModel(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.seasonKey = in.readString();
        this.seasonName = in.readString();
    }

    public static final Creator<CultivationModel> CREATOR = new Creator<CultivationModel>() {
        @Override
        public CultivationModel createFromParcel(Parcel source) {
            return new CultivationModel(source);
        }

        @Override
        public CultivationModel[] newArray(int size) {
            return new CultivationModel[size];
        }
    };

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

    public String getSeasonKey() {
        return seasonKey;
    }

    public void setSeasonKey(String seasonKey) {
        this.seasonKey = seasonKey;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CultivationModel that = (CultivationModel) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
