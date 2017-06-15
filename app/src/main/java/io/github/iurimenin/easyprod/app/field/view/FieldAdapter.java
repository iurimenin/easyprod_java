package io.github.iurimenin.easyprod.app.field.view;

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
import io.github.iurimenin.easyprod.app.field.model.FieldModel;
import io.github.iurimenin.easyprod.app.field.presenter.FieldPresenter;
import io.github.iurimenin.easyprod.app.util.CallbackInterface;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {

    private ArrayList<FieldModel> selectedItems;
    private CallbackInterface mCallback;
    private ArrayList<FieldModel> mFields;
    private FieldPresenter mPresenter;
    private Context mContext;

    public FieldAdapter(Context context,
                              FieldPresenter presenter,
                              CallbackInterface callbackInterface,
                              ArrayList<FieldModel> fieldModels) {
        super();
        this.mContext = context;
        this.mPresenter = presenter;
        this.mCallback = callbackInterface;
        this.mFields = fieldModels;
        this.selectedItems = new ArrayList();
    }

    @Override
    public int getItemCount() {
        return mFields.size();
    }

    @Override
    public FieldAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_field, parent, false);
        return new FieldAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FieldAdapter.ViewHolder holder, final int position) {
        if (mFields != null && mFields.size() > 0) {
            final FieldModel fieldModel = mFields.get(position);

            holder.textViewField.setText(fieldModel.getName());
            holder.constraintLayoutItemField.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedItems.size() > 0)
                        selectDeselectItem(view, fieldModel);
                    else
                        mPresenter.clickItem(fieldModel);
                }
            });

            holder.constraintLayoutItemField.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    selectDeselectItem(view, fieldModel);
                    return true;
                }
            });

            holder.constraintLayoutItemField
                    .setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorIcons));
        }
    }

    private void selectDeselectItem(View view, FieldModel fieldModel) {

        if(selectedItems.contains(fieldModel)){
            selectedItems.remove(fieldModel);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorIcons));
        } else {
            selectedItems.add(fieldModel);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDivider));
        }

        mCallback.updateMenuIcons(selectedItems.size());
    }

    public ArrayList<FieldModel> getSelectedItems() {
        return selectedItems;
    }

    public final void addItem(FieldModel field) {
        this.mFields.add(field);
        this.notifyDataSetChanged();
    }

    public final void updateItem(FieldModel updated) {

        for (FieldModel it: this.mFields) {
            if(it.getKey().equals(updated.getKey())) {
                it.setName(updated.getName());
                it.setTotalArea(updated.getTotalArea());
            }
        }

        this.notifyDataSetChanged();
    }

    public final void removeItem(FieldModel field) {
        this.mFields.remove(field);
        this.selectedItems.remove(field);
        this.notifyDataSetChanged();
    }

    public final void removeSelection() {
        this.selectedItems.clear();
        this.mCallback.updateMenuIcons(selectedItems.size());
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewField) TextView textViewField;
        @BindView(R.id.constraintLayoutItemField) ConstraintLayout constraintLayoutItemField;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
