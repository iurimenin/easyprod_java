package io.github.iurimenin.easyprod.app.production.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.cultivation.model.CultivationModel;
import io.github.iurimenin.easyprod.app.production.model.ProductionModel;
import io.github.iurimenin.easyprod.app.production.view.ProductionActivity;
import io.github.iurimenin.easyprod.app.production.view.ProductionAdapter;
import io.github.iurimenin.easyprod.app.util.FirebaseUtils;
import io.github.iurimenin.easyprod.app.util.MoneyMaskMaterialEditText;
import io.github.iurimenin.easyprod.app.util.PresenterInterface;
import io.github.iurimenin.easyprod.app.util.ProductionInterface;
import io.github.iurimenin.easyprod.app.widget.model.WidgetModel;
import io.github.iurimenin.easyprod.app.widget.utils.WidgetSharedPreferences;
import io.github.iurimenin.easyprod.app.widget.view.LastProductionsWidget;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class ProductionPresenter implements PresenterInterface {

    private Context mContext;
    private ProductionAdapter mAdapter;
    private ProductionInterface mCallback;
    private WidgetSharedPreferences mWidgetPreferences;
    private DatabaseReference mProductionRef;
    private CultivationModel mCultivation;
    private Double totalAreaField;

    public ProductionPresenter(CultivationModel mCultivation, Double totalAreaField) {
        this.mCultivation = mCultivation;
        this.totalAreaField = totalAreaField;
        this.mProductionRef = new FirebaseUtils().getProductionReference(this.mCultivation.getKey());
    }

    public final void bindView(ProductionActivity productionActivity, ProductionAdapter adapter) {
        this.mAdapter = adapter;
        this.mContext = productionActivity;
        this.mCallback = productionActivity;
        this.mWidgetPreferences = new WidgetSharedPreferences(productionActivity);
    }

    public final void unBindView() {
        this.mAdapter = null;
        this.mCallback = null;
    }

    public final void loadProductions() {
        this.mProductionRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ProductionModel vo = dataSnapshot.getValue(ProductionModel.class);
                if(mAdapter != null) {
                    mAdapter.addItem(vo);
                }

                updateTotalProduction();
                updateWidget();
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ProductionModel updated = dataSnapshot.getValue(ProductionModel.class);
                if(mAdapter != null) {
                    mAdapter.updateItem(updated);
                }
                updateTotalProduction();
                updateWidget();
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ProductionModel removed = dataSnapshot.getValue(ProductionModel.class);
                if(mAdapter != null) {
                    mAdapter.removeItem(removed);
                }

                if(mCallback != null) {
                    mCallback.updateMenuIcons(mAdapter.getSelectedItems().size());
                }

                ProductionPresenter.this.updateTotalProduction();
                ProductionPresenter.this.updateWidget();
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void save(MaterialDialog materialDialog, boolean isPositive) {

        if(isPositive) {

            if(this.isDialogValid(materialDialog)) {
                TextView textViewProductionKey =
                        (TextView) materialDialog.findViewById(R.id.textViewProductionKey);
                MoneyMaskMaterialEditText materialEditTextProductionBags =
                        (MoneyMaskMaterialEditText) materialDialog.findViewById(R.id.materialEditTextProductionBags);

                ProductionModel production = new ProductionModel(
                        textViewProductionKey.getText().toString(),
                        materialEditTextProductionBags.getDouble());

                production.save(mCultivation.getKey());
                materialDialog.dismiss();
                mAdapter.removeSelection();
            }
        } else {
            materialDialog.dismiss();
        }

    }

    private boolean isDialogValid(MaterialDialog materialDialog) {

        MoneyMaskMaterialEditText materialEditTextProductionBags =
                (MoneyMaskMaterialEditText) materialDialog.findViewById(R.id.materialEditTextProductionBags);

        if(materialEditTextProductionBags.getText().toString() == null ||
                materialEditTextProductionBags.getText().toString().length() == 0 ||
                materialEditTextProductionBags.getDouble() == 0.0D) {
            materialEditTextProductionBags.setError(materialDialog.getContext().getString(R.string.error_field_required_and_bigger_than_0));
            return false;
        }
        return true;
    }

    public final void deleteSelectedItems(ArrayList<ProductionModel> selectedItens) {

        for(ProductionModel item : selectedItens) {
            this.mProductionRef.child(item.getKey()).removeValue();
        }

        this.mAdapter.notifyDataSetChanged();
    }

    private final void updateTotalProduction() {
        Double totalBags = 0.0D;
        if(this.mAdapter != null) {
            if(mAdapter.getProductions() != null) {
                for(ProductionModel item : mAdapter.getProductions()) {
                    totalBags += item.getBags();
                }
            }
        }

        double result = totalBags / totalAreaField;
        if (mCallback != null) {
            mCallback.updateProduction(result);
        }
    }

    private final void updateWidget() {
        ArrayList<WidgetModel> list = new ArrayList();

        if(this.mAdapter != null) {

            if(mAdapter.getProductions() != null) {

                for (ProductionModel item : mAdapter.getProductions()) {

                    WidgetModel widgetModel = new WidgetModel(item.getKey(),
                            item.getBags(), mCultivation.getKey(), mCultivation.getName(),
                            mCultivation.getSeasonName());
                    list.add(widgetModel);
                }
            }
        }

        mWidgetPreferences.save(list);

        Intent dataUpdatedIntent = new Intent(LastProductionsWidget.ACTION_DATA_UPDATED)
                .setPackage(this.mContext.getPackageName());
        mContext.sendBroadcast(dataUpdatedIntent);
    }
}
