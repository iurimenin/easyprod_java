package io.github.iurimenin.easyprod.app.field.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.cultivation.view.CultivationActivity;
import io.github.iurimenin.easyprod.app.field.model.FieldModel;
import io.github.iurimenin.easyprod.app.field.view.FieldActivity;
import io.github.iurimenin.easyprod.app.field.view.FieldAdapter;
import io.github.iurimenin.easyprod.app.util.CallbackInterface;
import io.github.iurimenin.easyprod.app.util.FirebaseUtils;
import io.github.iurimenin.easyprod.app.util.MoneyMaskMaterialEditText;
import io.github.iurimenin.easyprod.app.util.PresenterInterface;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class FieldPresenter implements PresenterInterface {

    private FieldAdapter mAdapter;
    private CallbackInterface mCallback;
    private Context mContext;
    private final DatabaseReference mFieldRef;
    private String mFarmKey;

    public FieldPresenter(String mFarmKey) {
        this.mFarmKey = mFarmKey;
        this.mFieldRef = new FirebaseUtils().getFieldReference(mFarmKey);
    }

    public final void bindView(FieldActivity fieldActivity, FieldAdapter adapter) {
        this.mAdapter = adapter;
        this.mContext = fieldActivity;
        this.mCallback = fieldActivity;
    }

    public final void unBindView() {
        this.mAdapter = null;
        this.mCallback = null;
    }

    public final void clickItem(FieldModel fieldModel) {
        Intent i = new Intent(this.mContext, CultivationActivity.class);
        i.putExtra(FieldModel.TAG, fieldModel);
        if(this.mContext != null) {
            mContext.startActivity(i);
        }
    }

    public final void loadFields() {

        this.mFieldRef.addChildEventListener((new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FieldModel vo = dataSnapshot.getValue(FieldModel.class);
                if(mAdapter != null) {
                    mAdapter.addItem(vo);
                }
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                FieldModel updated = dataSnapshot.getValue(FieldModel.class);
                if(mAdapter != null) {
                    mAdapter.updateItem(updated);
                }
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                FieldModel removed = dataSnapshot.getValue(FieldModel.class);
                if(mAdapter != null) {
                    mAdapter.removeItem(removed);
                }

                if(mCallback != null) {
                    mCallback.updateMenuIcons(mAdapter.getSelectedItems().size());
                }
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        }));
    }

    @Override
    public void save(MaterialDialog materialDialog, boolean isPositive) {

        if(isPositive) {
            if(isDialogValid(materialDialog)) {

                TextView textViewFieldKey =
                        (TextView) materialDialog.findViewById(R.id.textViewFieldKey);
                MaterialEditText materialEditTextFieldName =
                        (MaterialEditText) materialDialog.findViewById(R.id.materialEditTextFieldName);

                MoneyMaskMaterialEditText materialEditTextFieldArea =
                        (MoneyMaskMaterialEditText) materialDialog.findViewById(R.id.materialEditTextFieldArea);

                FieldModel field = new FieldModel(
                        textViewFieldKey.getText().toString(),
                        materialEditTextFieldName.getText().toString(),
                        materialEditTextFieldArea.getDouble());

                field.save(mFarmKey);
                materialDialog.dismiss();
                mAdapter.removeSelection();
            }

        } else {
            materialDialog.dismiss();
        }

    }

    private boolean isDialogValid(MaterialDialog materialDialog) {

        MaterialEditText materialEditTextFieldName =
                (MaterialEditText) materialDialog.findViewById(R.id.materialEditTextFieldName);

        MoneyMaskMaterialEditText materialEditTextFieldArea =
                (MoneyMaskMaterialEditText) materialDialog.findViewById(R.id.materialEditTextFieldArea);

        boolean valid = true;

        if (materialEditTextFieldName.getText().toString() == null ||
                materialEditTextFieldName.getText().toString().length() == 0) {
            materialEditTextFieldName.setError(
                    materialDialog.getContext().getString(R.string.error_field_required));
            valid = false;
        }

        if (materialEditTextFieldArea.getText().toString() == null ||
                materialEditTextFieldArea.getText().toString().length() ==  0||
                materialEditTextFieldArea.getDouble() == 0.0) {
            materialEditTextFieldArea.setError(
                    materialDialog.getContext().getString(R.string.error_field_required_and_bigger_than_0));
            valid = false;
        }

        return valid;
    }

    public final void deleteSelectedItems(ArrayList<FieldModel> selectedItens) {
        for(FieldModel item : selectedItens){
            this.mFieldRef.child(item.getKey()).removeValue();
        }

        this.mAdapter.notifyDataSetChanged();
    }
}
