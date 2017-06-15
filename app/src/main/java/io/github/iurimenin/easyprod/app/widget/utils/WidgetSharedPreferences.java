package io.github.iurimenin.easyprod.app.widget.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.github.iurimenin.easyprod.app.widget.model.WidgetModel;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class WidgetSharedPreferences {

    private final Gson gson;
    private final Type type;
    private final SharedPreferences sharedPref;

    public WidgetSharedPreferences (Context context) {

        gson = new Gson();
        type = new TypeToken<ArrayList<WidgetModel>>() {}.getType();
        sharedPref = context.getSharedPreferences("EASYPROD", Context.MODE_PRIVATE);
    }

    public ArrayList<WidgetModel> getStored() {

        String jsonStored = sharedPref.getString(WidgetModel.TAG, "");

        if (jsonStored == null || jsonStored.length() == 0) {
            return new ArrayList();
        }

        return gson.fromJson(jsonStored, type);
    }

    public void save(ArrayList<WidgetModel> listSeason) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(WidgetModel.TAG, gson.toJson(listSeason));
        editor.apply();
    }
}
