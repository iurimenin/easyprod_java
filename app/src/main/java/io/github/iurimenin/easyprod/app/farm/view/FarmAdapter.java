package io.github.iurimenin.easyprod.app.farm.view;

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
import io.github.iurimenin.easyprod.app.farm.model.FarmModel;
import io.github.iurimenin.easyprod.app.farm.presenter.FarmPresenter;
import io.github.iurimenin.easyprod.app.util.CallbackInterface;

/**
 * Created by Iuri Menin on 14/06/17.
 */

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.ViewHolder> {

    private Context mContext;
    private CallbackInterface mCallback;
    private ArrayList<FarmModel> mFarms;
    private FarmPresenter mPresenter;
    private ArrayList<FarmModel> selectedItems;

    public FarmAdapter(Context context,
                       FarmPresenter presenter,
                       CallbackInterface callbackInterface,
                       ArrayList<FarmModel> farmsModel) {
        super();
        this.mContext = context;
        this.mPresenter = presenter;
        this.mCallback = callbackInterface;
        this.mFarms = farmsModel;
        this.selectedItems = new ArrayList();
    }

    @Override
    public int getItemCount() {
        return mFarms.size();
    }

    @Override
    public FarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_farm, parent, false);
        return new FarmAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FarmAdapter.ViewHolder holder, final int position) {
        if (mFarms != null && mFarms.size() > 0) {
            final FarmModel farmModel = mFarms.get(position);

            holder.textViewFarm.setText(farmModel.getName());
            holder.constraintLayoutItemFarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedItems.size() > 0)
                        selectDeselectItem(view, farmModel);
                    else
                        mPresenter.clickItem(farmModel);
                }
            });

            holder.constraintLayoutItemFarm.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    selectDeselectItem(view, farmModel);
                    return true;
                }
            });

            holder.constraintLayoutItemFarm
                    .setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorIcons));
        }
    }

    private void selectDeselectItem(View view, FarmModel farmModel) {

        if(selectedItems.contains(farmModel)){
            selectedItems.remove(farmModel);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorIcons));
        } else {
            selectedItems.add(farmModel);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDivider));
        }

        mCallback.updateMenuIcons(selectedItems.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewFarm) TextView textViewFarm;
        @BindView(R.id.constraintLayoutItemFarm) ConstraintLayout constraintLayoutItemFarm;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ArrayList<FarmModel> getSelectedItens() {
        return selectedItems;
    }

    public void addItem(FarmModel farm) {
        mFarms.add(farm);
        this.notifyDataSetChanged();
    }

    public void updateItem(FarmModel updated) {

        for (FarmModel farm : mFarms) {

            if (farm.getKey() == updated.getKey()) {
                farm.setName(updated.getName());
                break;
            }
        }
        this.notifyDataSetChanged();
    }

    public void removeItem(FarmModel farm) {
        mFarms.remove(farm);
        selectedItems.remove(farm);
        this.notifyDataSetChanged();
    }

    public void removeSelecionts() {
        selectedItems.clear();
        mCallback.updateMenuIcons(selectedItems.size());
        this.notifyDataSetChanged();
    }
}
