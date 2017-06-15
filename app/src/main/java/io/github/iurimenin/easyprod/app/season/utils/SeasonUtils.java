package io.github.iurimenin.easyprod.app.season.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.github.iurimenin.easyprod.app.season.model.SeasonModel;

/**
 * Created by Iuri Menin on 14/06/17.
 */

public class SeasonUtils {

    private final Gson gson = new Gson();
    private final Type type = new TypeToken() {}.getType();
    private SharedPreferences sharedPref;

    public SeasonUtils(Context context) {
        this.sharedPref = context.getSharedPreferences("EASYPROD", 0);
    }

    public final ArrayList<SeasonModel> addStoredSeasonsToList(ArrayList listSeason) {
        String jsonStored = this.sharedPref.getString(SeasonModel.TAG, "");

        if (jsonStored == null || jsonStored.length() == 0) {
            return new ArrayList();
        } else {
            ArrayList<SeasonModel> listStored = this.gson.fromJson(jsonStored, this.type);

            for (SeasonModel it : listStored) {
                if (!listSeason.contains(it)) {
                    listStored.add(it);
                }
            }
            return listStored;
        }
    }

    public final void save(ArrayList listSeason) {
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putString(SeasonModel.TAG, this.gson.toJson(listSeason));
        editor.apply();
    }
}
