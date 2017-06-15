package io.github.iurimenin.easyprod.app.production.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.production.model.ProductionModel;
import io.github.iurimenin.easyprod.app.production.presenter.ProductionPresenter;
import io.github.iurimenin.easyprod.app.util.CallbackInterface;

/**
 * Created by Iuri Menin on 15/06/17.
 */
public class ProductionAdapter extends RecyclerView.Adapter<ProductionAdapter.ViewHolder> {

    private CallbackInterface mCallback;
    private ArrayList<ProductionModel> selectedItems;
    private ArrayList<ProductionModel> mProductions;
    private ProductionPresenter mPresenter;
    private Context mContext;

    public ProductionAdapter(Context context,
                              ProductionPresenter presenter,
                              CallbackInterface callbackInterface,
                              ArrayList<ProductionModel> productionModels) {
        super();
        this.mContext = context;
        this.mPresenter = presenter;
        this.mCallback = callbackInterface;
        this.mProductions = productionModels;
        this.selectedItems = new ArrayList();
    }

    @Override
    public int getItemCount() {
        return mProductions.size();
    }

    @Override
    public ProductionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_production, parent, false);
        return new ProductionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductionAdapter.ViewHolder holder, final int position) {
        if (mProductions != null && mProductions.size() > 0) {
            final ProductionModel productionModel = mProductions.get(position);

            holder.textViewProduction.setText(NumberFormat.getCurrencyInstance()
                    .format(productionModel.getBags()).replace(NumberFormat.getCurrencyInstance()
                            .getCurrency().getSymbol(), ""));
            holder.constraintLayoutItemProduction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedItems.size() > 0)
                        selectDeselectItem(view, productionModel);
                }
            });

            holder.constraintLayoutItemProduction.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    selectDeselectItem(view, productionModel);
                    return true;
                }
            });

            holder.constraintLayoutItemProduction
                    .setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorIcons));
        }
    }

    private void selectDeselectItem(View view, ProductionModel productionModel) {

        if(selectedItems.contains(productionModel)){
            selectedItems.remove(productionModel);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorIcons));
        } else {
            selectedItems.add(productionModel);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDivider));
        }

        mCallback.updateMenuIcons(selectedItems.size());
    }

    public ArrayList<ProductionModel> getSelectedItems() {
        return selectedItems;
    }

    public final void addItem(ProductionModel productionModel) {
        this.mProductions.add(productionModel);
        this.notifyDataSetChanged();
    }

    public final void updateItem(ProductionModel updated) {

        for (ProductionModel it: this.mProductions) {
            if(it.getKey().equals(updated.getKey())) {
                it.setBags(updated.getBags());
            }
        }

        this.notifyDataSetChanged();
    }

    public final void removeItem(ProductionModel field) {
        this.mProductions.remove(field);
        this.selectedItems.remove(field);
        this.notifyDataSetChanged();
    }

    public final void removeSelection() {
        this.selectedItems.clear();
        this.mCallback.updateMenuIcons(selectedItems.size());
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewProduction) TextView textViewProduction;
        @BindView(R.id.constraintLayoutItemProduction) ConstraintLayout constraintLayoutItemProduction;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ArrayList<ProductionModel> getProductions() {
        return mProductions;
    }
}
