package io.github.iurimenin.easyprod.app.farm.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.farm.model.FarmModel;
import io.github.iurimenin.easyprod.app.farm.view.FarmActivity;
import io.github.iurimenin.easyprod.app.farm.view.FarmAdapter;
import io.github.iurimenin.easyprod.app.field.view.FieldActivity;
import io.github.iurimenin.easyprod.app.season.model.SeasonModel;
import io.github.iurimenin.easyprod.app.season.utils.SeasonUtils;
import io.github.iurimenin.easyprod.app.util.CallbackInterface;
import io.github.iurimenin.easyprod.app.util.FirebaseUtils;
import io.github.iurimenin.easyprod.app.util.PresenterInterface;
import io.github.iurimenin.easyprod.app.view.AboutActivity;

/**
 * Created by Iuri Menin on 14/06/17.
 */

public class FarmPresenter implements PresenterInterface {

    private Context mContext;
    private FarmAdapter mAdapter;
    private CallbackInterface mCallback;
    private final DatabaseReference farmRef = new FirebaseUtils().getFarmReference();

    public final void bindView(FarmActivity farmActivity, FarmAdapter adapter) {
        this.mAdapter = adapter;
        this.mCallback = farmActivity;
        this.mContext = farmActivity;
        this.syncSeasons(farmActivity);
    }

    private final void syncSeasons(Context context) {

        final ArrayList<SeasonModel> listSeason = new ArrayList();
        final SeasonUtils seasonUtils = new SeasonUtils(context);

        DatabaseReference seasonReference = new FirebaseUtils().getSeasonReference();

        seasonReference.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                SeasonModel vo = dataSnapshot.getValue(SeasonModel.class);
                seasonUtils.addStoredSeasonsToList(listSeason);

                if (!listSeason.contains(vo)) {
                    listSeason.add(vo);
                }

                seasonUtils.save(listSeason);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                SeasonModel updated = dataSnapshot.getValue(SeasonModel.class);
                seasonUtils.addStoredSeasonsToList(listSeason);

                for (SeasonModel season : listSeason) {
                    if (season.getKey().equals(updated.getKey())) {
                        season.setStartYear(updated.getStartYear());
                        season.setEndYear(updated.getEndYear());
                    }
                }

                seasonUtils.save(listSeason);
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                SeasonModel vo = dataSnapshot.getValue(SeasonModel.class);
                seasonUtils.addStoredSeasonsToList(listSeason);
                listSeason.remove(vo);
                seasonUtils.save(listSeason);
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public final void unBindView() {
        this.mAdapter = null;
        this.mCallback = null;
    }

    public final void loadFarms() {
        this.farmRef.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FarmModel vo = dataSnapshot.getValue(FarmModel.class);
                if (mAdapter != null) {
                    mAdapter.addItem(vo);
                }
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                FarmModel updated = dataSnapshot.getValue(FarmModel.class);
                if (mAdapter != null) {
                    mAdapter.updateItem(updated);
                }
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                FarmModel removed = dataSnapshot.getValue(FarmModel.class);
                if (mAdapter != null) {
                    mAdapter.removeItem(removed);
                }

                if (mCallback != null) {
                    mCallback.updateMenuIcons(mAdapter.getSelectedItens().size());
                }
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public final void deleteSelectedItems(ArrayList<FarmModel> selectedItens) {
        for (FarmModel farm : selectedItens) {
            farmRef.child(farm.getKey()).removeValue();
        }

        this.mAdapter.notifyDataSetChanged();
    }

    public final void clickItem(FarmModel farmModel) {
        Intent i = new Intent(this.mContext, FieldActivity.class);
        i.putExtra(FarmModel.TAG, farmModel);
        if (this.mContext != null) {
            mContext.startActivity(i);
        }

    }

    public final void showAbout() {
        Intent i = new Intent(this.mContext, AboutActivity.class);
        if (this.mContext != null) {
            this.mContext.startActivity(i);
        }
    }

    public void save(MaterialDialog materialDialog, boolean isPositive) {

        if (isPositive) {
            if (this.isDialogValid(materialDialog)) {

                TextView textViewFarmKey =
                        (TextView) materialDialog.findViewById(R.id.textViewFarmKey);
                MaterialEditText  materialEditTextFarmName =
                        (MaterialEditText)materialDialog.findViewById(R.id.materialEditTextFarmName);
                FarmModel farm = new FarmModel(
                        textViewFarmKey.getText().toString(),
                        materialEditTextFarmName.getText().toString()
                );

                farm.save();
                materialDialog.dismiss();
                if (this.mAdapter != null) {
                    mAdapter.removeSelecionts();
                }
            }
        } else {
            materialDialog.dismiss();
        }
    }

    private boolean isDialogValid(MaterialDialog materialDialog) {

        EditText editTextFarmName =
                (EditText) materialDialog.findViewById(R.id.materialEditTextFarmName);

        if (editTextFarmName.getText().toString() == null ||
                editTextFarmName.getText().toString().length() == 0) {
            editTextFarmName.setError(mContext.getString(R.string.error_field_required));
            return false;
        }

        return true;
    }
}
