package io.github.iurimenin.easyprod.app.weather.view;

import android.content.Context;
import android.support.v13.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.field.model.FieldModel;
import io.github.iurimenin.easyprod.app.util.WeatherUtils;
import io.github.iurimenin.easyprod.app.weather.model.WeatherModel;

/**
 * Created by Iuri Menin on 16/06/17.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context mContext;
    private FieldModel mField;
    private ArrayList<WeatherModel> mWeathers;

    public WeatherAdapter(WeatherActivity weatherActivity, ArrayList<WeatherModel> objects) {

        this.mContext = weatherActivity;
        this.mWeathers = objects;
    }

    @Override
    public int getItemCount() {
        return mWeathers.size();
    }

    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_weather, parent, false);
        return new WeatherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WeatherAdapter.ViewHolder holder, final int position) {
        if (mWeathers != null && mWeathers.size() > 0) {
            final WeatherModel weatherModel = mWeathers.get(position);

            Glide.with(mContext)
                    .load(WeatherUtils.getArtUrlForWeatherCondition(mContext, weatherModel.getWeatherId()))
                    .crossFade()
                    .into(holder.imageViewItemIcon);

            ViewCompat.setTransitionName(holder.imageViewItemIcon, "iconView" + position);

            holder.textViewItemDate.setText(WeatherUtils.getFriendlyDayString(mContext, weatherModel.getDateTime()));

            String description = WeatherUtils.getStringForWeatherCondition(mContext, weatherModel.getWeatherId());

            holder.textViewItemWeather.setText(description);
            holder.textViewItemWeather.setContentDescription(mContext.getString(R.string.a11y_forecast, description));

            String highString = WeatherUtils.formatTemperature(mContext, weatherModel.getHigh());
            holder.textViewItemHigh.setText(highString);
            holder.textViewItemHigh.setContentDescription(mContext.getString(R.string.a11y_high_temp, highString));

            String lowString = WeatherUtils.formatTemperature(mContext, weatherModel.getLow());
            holder.textViewItemLow.setText(lowString);
            holder.textViewItemLow.setContentDescription(mContext.getString(R.string.a11y_low_temp, lowString));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewItemIcon) ImageView imageViewItemIcon;
        @BindView(R.id.textViewItemDate) TextView textViewItemDate;
        @BindView(R.id.textViewItemWeather) TextView textViewItemWeather;
        @BindView(R.id.textViewItemHigh) TextView textViewItemHigh;
        @BindView(R.id.textViewItemLow) TextView textViewItemLow;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void clear() {
        mWeathers.clear();
    }

    public void addAll(ArrayList<WeatherModel> retorno) {
        mWeathers.addAll(retorno);
        notifyDataSetChanged();
    }
}
