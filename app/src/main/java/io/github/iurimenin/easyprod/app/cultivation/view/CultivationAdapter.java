package io.github.iurimenin.easyprod.app.cultivation.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.cultivation.model.CultivationModel;
import io.github.iurimenin.easyprod.app.cultivation.presenter.CultivationPresenter;
import io.github.iurimenin.easyprod.app.field.model.FieldModel;
import io.github.iurimenin.easyprod.app.util.CallbackInterface;

/**
 * Created by Iuri Menin on 14/06/17.
 */
public class CultivationAdapter extends RecyclerView.Adapter<CultivationAdapter.ViewHolder> {

    private Context mContext;
    private FieldModel mField;
    private CallbackInterface mCallback;
    private CultivationPresenter mPresenter;
    private ArrayList<CultivationModel> selectedItems;
    private ArrayList<CultivationModel> mCultivations;

    public CultivationAdapter(Context context,
                              FieldModel field,
                              CultivationPresenter presenter,
                              CallbackInterface callbackInterface,
                              ArrayList<CultivationModel> cultivationsModel) {
        super();
        this.mField = field;
        this.mContext = context;
        this.mPresenter = presenter;
        this.mCallback = callbackInterface;
        this.mCultivations = cultivationsModel;
        this.selectedItems = new ArrayList();
    }

    @Override
    public int getItemCount() {
        return mCultivations.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_cultivation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mCultivations != null && mCultivations.size() > 0) {
            final CultivationModel cultivationModel = mCultivations.get(position);

            holder.textViewCultivation.setText(cultivationModel.getName());
            holder.textViewSeason.setText(cultivationModel.getSeasonName());
            holder.constraintLayoutItemCultivation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedItems.size() > 0)
                        selectDeselectItem(view, cultivationModel);
                    else
                        mPresenter.clickItem(cultivationModel, mField);
                }
            });

            holder.constraintLayoutItemCultivation.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    selectDeselectItem(view, cultivationModel);
                    return true;
                }
            });

            holder.constraintLayoutItemCultivation
                    .setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorIcons));
        }
    }

    private void selectDeselectItem(View view, CultivationModel cultivationModel) {

        if(selectedItems.contains(cultivationModel)){
            selectedItems.remove(cultivationModel);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorIcons));
        } else {
            selectedItems.add(cultivationModel);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDivider));
        }

        mCallback.updateMenuIcons(selectedItems.size());
    }

    public ArrayList<CultivationModel> getSelectedItems() {
        return selectedItems;
    }

    public final void addItem(CultivationModel field) {
        this.mCultivations.add(field);
        this.notifyDataSetChanged();
    }

    public final void updateItem(CultivationModel updated) {

        for (CultivationModel it: this.mCultivations) {
            if(it.getKey().equals(updated.getKey())) {
                it.setName(updated.getName());
                it.setSeasonKey(updated.getSeasonKey());
                it.setSeasonName(updated.getSeasonName());
            }
        }

        this.notifyDataSetChanged();
    }

    public final void removeItem(CultivationModel field) {
        this.mCultivations.remove(field);
        this.selectedItems.remove(field);
        this.notifyDataSetChanged();
    }

    public final void removeSelection() {
        this.selectedItems.clear();
        this.mCallback.updateMenuIcons(this.getItemCount());
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewSeason) TextView textViewSeason;
        @BindView(R.id.textViewCultivation) TextView textViewCultivation;
        @BindView(R.id.constraintLayoutItemCultivation) ConstraintLayout constraintLayoutItemCultivation;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}