package io.github.iurimenin.easyprod.app.cultivation.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.cultivation.model.CultivationModel;
import io.github.iurimenin.easyprod.app.cultivation.view.CultivationActivity;
import io.github.iurimenin.easyprod.app.cultivation.view.CultivationAdapter;
import io.github.iurimenin.easyprod.app.field.model.FieldModel;
import io.github.iurimenin.easyprod.app.production.view.ProductionActivity;
import io.github.iurimenin.easyprod.app.season.model.SeasonModel;
import io.github.iurimenin.easyprod.app.util.CallbackInterface;
import io.github.iurimenin.easyprod.app.util.FirebaseUtils;
import io.github.iurimenin.easyprod.app.util.PresenterInterface;

/**
 * Created by Iuri Menin on 14/06/17.
 */
public class CultivationPresenter implements PresenterInterface {

    private String mFieldKey;
    private ArrayList mSeasons;
    private Gson mGson;
    private Context mContext;
    private CultivationAdapter mAdapter;
    private CallbackInterface mCallback;
    private DatabaseReference mCultivationRef;

    public CultivationPresenter(String mFieldKey) {
        this.mFieldKey = mFieldKey;
        this.mGson = new Gson();
        this.mSeasons = new ArrayList();
        this.mCultivationRef = new FirebaseUtils().getFieldReference(this.mFieldKey);
    }

    public final void bindView(CultivationActivity cultivationActivity, CultivationAdapter adapter) {
        this.mAdapter = adapter;
        this.mContext = cultivationActivity;
        this.mCallback = cultivationActivity;
        this.loadSeasons();
    }

    public final void unBindView() {
        this.mAdapter = null;
        this.mCallback = null;
    }

    public final void clickItem(CultivationModel cultivationModel, FieldModel mField) {
        Intent i = new Intent(this.mContext, ProductionActivity.class);
        i.putExtra("CultivationModel", cultivationModel);
        i.putExtra(FieldModel.TAG, mField);
        if(this.mContext != null) {
            this.mContext.startActivity(i);
        }

    }

    public final void loadCultivations() {
        this.mCultivationRef.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CultivationModel vo = dataSnapshot.getValue(CultivationModel.class);
                if(mAdapter != null) {
                    mAdapter.addItem(vo);
                }

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                CultivationModel updated = dataSnapshot.getValue(CultivationModel.class);
                if(mAdapter != null) {
                    mAdapter.updateItem(updated);
                }

            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                CultivationModel removed = dataSnapshot.getValue(CultivationModel.class);
                if(mAdapter != null) {
                    mAdapter.removeItem(removed);
                }

                if(mCallback != null) {
                    mCallback.updateMenuIcons(mAdapter != null? mAdapter.getItemCount() : 0);
                }

            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void save(MaterialDialog materialDialog, boolean isPositive) {

        if(isPositive) {

            TextView textViewCultivationKey =
                    (TextView) materialDialog.findViewById(R.id.textViewCultivationKey);

            MaterialEditText materialEditTextCultivationName =
                    (MaterialEditText) materialDialog.findViewById(R.id.materialEditTextCultivationName);

            AppCompatSpinner appCompatSpinnerSeason =
                    (AppCompatSpinner) materialDialog.findViewById(R.id.appCompatSpinnerSeason);

            SeasonModel season = (SeasonModel) appCompatSpinnerSeason.getSelectedItem();

            String fieldName = materialEditTextCultivationName.getText().toString();
            if(fieldName == null || fieldName.length() == 0) {
                materialEditTextCultivationName
                        .setError(this.mContext.getString(R.string.error_field_required));
            } else {
                CultivationModel field =
                        new CultivationModel(textViewCultivationKey.getText().toString(), fieldName,
                                season.getKey(), season.toString());

                field.save(this.mFieldKey);
                materialDialog.dismiss();
                if(this.mAdapter != null) {
                    this.mAdapter.removeSelection();
                }
            }
        } else {
            materialDialog.dismiss();
        }

    }

    public final void deleteSelectedItems(ArrayList<CultivationModel> selectedItens) {
        for (CultivationModel item: selectedItens) {
            this.mCultivationRef.child(item.getKey()).removeValue();
        }
    }

    private final void loadSeasons() {
        Type type = (new TypeToken() {}).getType();
        SharedPreferences sharedPref = this.mContext.getSharedPreferences("EASYPROD", 0);
        String jsonStored = sharedPref.getString(SeasonModel.TAG, "");

        if(jsonStored != null && jsonStored.length() != 0) {
            ArrayList<SeasonModel> listStored = this.mGson.fromJson(jsonStored, type);

            for (SeasonModel it :listStored) {
                if(!this.mSeasons.contains(it)) {
                    mSeasons.add(it);
                }
            }
        }
    }

    public ArrayList getSeasons() {
        return mSeasons;
    }
}
