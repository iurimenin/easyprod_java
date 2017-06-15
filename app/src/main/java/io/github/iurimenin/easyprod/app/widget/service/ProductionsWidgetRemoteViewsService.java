package io.github.iurimenin.easyprod.app.widget.service;

import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.NumberFormat;
import java.util.ArrayList;

import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.widget.model.WidgetModel;
import io.github.iurimenin.easyprod.app.widget.utils.WidgetSharedPreferences;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class ProductionsWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private ArrayList<WidgetModel> listItens = new ArrayList();
            private final WidgetSharedPreferences widgetPreferences =
                    new WidgetSharedPreferences(getApplicationContext());

            public void onCreate() {
            }

            public void onDataSetChanged() {
                if(!this.listItens.isEmpty()) {
                    this.listItens.clear();
                }

                long identityToken = Binder.clearCallingIdentity();
                this.listItens = this.widgetPreferences.getStored();
                Binder.restoreCallingIdentity(identityToken);
            }

            public void onDestroy() {
                if(!this.listItens.isEmpty()) {
                    this.listItens.clear();
                }

            }

            public int getCount() {
                return this.listItens.size();
            }

            public RemoteViews getViewAt(int position) {

                WidgetModel widgetItem = listItens.get(position);

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item);

                views.setTextViewText(R.id.textViewWidgetCultivation, widgetItem.getCultivationName());
                views.setTextViewText(R.id.textViewWidgetSeason, widgetItem.getSeasonName());
                views.setTextViewText(R.id.textViewWidgetBags, NumberFormat.getCurrencyInstance()
                        .format(widgetItem.getBags()).replace(NumberFormat.getCurrencyInstance()
                                .getCurrency().getSymbol(), "") + " " +
                        getString(R.string.bags_per_hectare));

                return views;
            }

            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            public int getViewTypeCount() {
                return 1;
            }

            public long getItemId(int position) {
                return (long)position;
            }

            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
