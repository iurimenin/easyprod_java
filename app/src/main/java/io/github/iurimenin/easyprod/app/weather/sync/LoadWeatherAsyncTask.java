package io.github.iurimenin.easyprod.app.weather.sync;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.util.WeatherUtils;
import io.github.iurimenin.easyprod.app.weather.model.WeatherModel;
import io.github.iurimenin.easyprod.app.weather.presenter.WeatherCallback;

/**
 * Created by Iuri Menin on 16/06/17.
 */

public class LoadWeatherAsyncTask extends AsyncTask<Object, Void, ArrayList<WeatherModel>> {

    private static final String TAG = "LoadWeatherAsyncTask";
    private final WeatherCallback mCallback;
    private final Context mContext;
    private ProgressDialog progressDialog;

    public LoadWeatherAsyncTask(Context context, WeatherCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(mContext.getString(R.string.loading_weather));
        progressDialog.show();
    }

    @Override
    protected ArrayList<WeatherModel> doInBackground(Object... objects) {
        return getWeather((Location) objects[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<WeatherModel> retorno) {
        progressDialog.dismiss();

        mCallback.loadWeather(retorno);
    }

    public ArrayList<WeatherModel> getWeather(Location location) {
        Log.d(TAG, "Starting sync");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        String format = "json";
        String units = "metric";
        int numDays = 14;

        try {

            final String FORECAST_BASE_URL =
                    "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "q";
            final String LAT_PARAM = "lat";
            final String LON_PARAM = "lon";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID_PARAM = "APPID";

            Uri.Builder uriBuilder = Uri.parse(FORECAST_BASE_URL).buildUpon();
            uriBuilder.appendQueryParameter(LAT_PARAM, String.valueOf(location.getLatitude()))
                    .appendQueryParameter(LON_PARAM, String.valueOf(location.getLongitude()));


            Uri builtUri = uriBuilder.appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .appendQueryParameter(APPID_PARAM, WeatherUtils.OPEN_WEATHER_MAP_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return new ArrayList<>();
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return new ArrayList<>();
            }
            forecastJsonStr = buffer.toString();
            return getWeatherDataFromJson(forecastJsonStr);
        } catch (JSONException| IOException e) {
            Log.d(TAG, e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.d(TAG, "Error closing stream", e);
                }
            }
        }
        return new ArrayList<>();
    }

    private ArrayList<WeatherModel> getWeatherDataFromJson(String forecastJsonStr)
            throws JSONException {

        final String OWM_CITY = "city";
        final String OWM_CITY_NAME = "name";

        final String OWM_LIST = "list";

        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WINDSPEED = "speed";
        final String OWM_WIND_DIRECTION = "deg";

        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";

        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";
        final String OWM_WEATHER_ID = "id";

        final String OWM_MESSAGE_CODE = "cod";

        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);

            if ( forecastJson.has(OWM_MESSAGE_CODE) ) {
                int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

                switch (errorCode) {
                    case HttpURLConnection.HTTP_OK:
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        return new ArrayList<>();
                    default:
                        return new ArrayList<>();
                }
            }

            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            ArrayList<WeatherModel> weatherModelArrayList = new ArrayList<>();

            for(int i = 0; i < weatherArray.length(); i++) {
                long dateTime;
                double pressure;
                int humidity;
                double windSpeed;
                double windDirection;

                double high;
                double low;

                String description;
                int weatherId;

                JSONObject dayForecast = weatherArray.getJSONObject(i);

                dateTime = dayTime.setJulianDay(julianStartDay+i);

                pressure = dayForecast.getDouble(OWM_PRESSURE);
                humidity = dayForecast.getInt(OWM_HUMIDITY);
                windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
                windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

                JSONObject weatherObject =
                        dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);
                weatherId = weatherObject.getInt(OWM_WEATHER_ID);

                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                high = temperatureObject.getDouble(OWM_MAX);
                low = temperatureObject.getDouble(OWM_MIN);

                weatherModelArrayList.add(new WeatherModel(dateTime, humidity, pressure, windSpeed,
                        windDirection, high, low, description, weatherId));
            }


            return weatherModelArrayList;
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage(), e);
        }
        return new ArrayList<>();
    }
}
