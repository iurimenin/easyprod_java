package io.github.iurimenin.easyprod.app.weather.presenter;

import android.location.Location;

import java.util.ArrayList;

import io.github.iurimenin.easyprod.app.weather.model.WeatherModel;
import io.github.iurimenin.easyprod.app.weather.sync.LoadWeatherAsyncTask;
import io.github.iurimenin.easyprod.app.weather.view.WeatherActivity;
import io.github.iurimenin.easyprod.app.weather.view.WeatherAdapter;

/**
 * Created by Iuri Menin on 16/06/17.
 */

public class WeatherPresenter implements WeatherCallback {

    private WeatherActivity mWeatherActivity;
    private WeatherAdapter mAdapter;
    private LoadWeatherAsyncTask mLoadWeatherAsyncTask;

    public void bindView(WeatherActivity weatherActivity, WeatherAdapter adapter) {
        this.mWeatherActivity = weatherActivity;
        this.mAdapter = adapter;
        this.mLoadWeatherAsyncTask = new LoadWeatherAsyncTask(mWeatherActivity, this);
    }

    public void unBindView() {
        this.mWeatherActivity = null;
        this.mAdapter = null;
    }

    public void syncWeather(Location currentLocation) {
        mAdapter.clear();
        mLoadWeatherAsyncTask.execute(currentLocation);
    }

    @Override
    public void loadWeather(ArrayList<WeatherModel> retorno) {
        mAdapter.addAll(retorno);
    }
}
