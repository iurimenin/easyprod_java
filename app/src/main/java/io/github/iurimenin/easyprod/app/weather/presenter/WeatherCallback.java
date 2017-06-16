package io.github.iurimenin.easyprod.app.weather.presenter;

import java.util.ArrayList;

import io.github.iurimenin.easyprod.app.weather.model.WeatherModel;

/**
 * Created by Iuri Menin on 16/06/17.
 */

public interface WeatherCallback {

    void loadWeather(ArrayList<WeatherModel> retorno);
}
