package io.github.iurimenin.easyprod.app.farm.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindView;
import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.farm.model.FarmModel;
import io.github.iurimenin.easyprod.app.farm.presenter.FarmPresenter;
import io.github.iurimenin.easyprod.app.util.MaterialDialogUtils;
import io.github.iurimenin.easyprod.app.view.EasyProdActivity;
import io.github.iurimenin.easyprod.app.view.LoginActivity;

/**
 * Created by Iuri Menin on 14/06/17.
 */

public class FarmActivity extends EasyProdActivity {

    private FarmAdapter mAdapter;
    private FarmPresenter mPresenter;
    private MaterialDialogUtils mMaterialDialogUtils;

    @BindView(R.id.floatingActionButtonAddFarm) FloatingActionButton mFloatingActionButtonAddFarm;
    @BindView(R.id.superRecyclerViewFarms) SuperRecyclerView mSuperRecyclerViewFarms;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_farms);
        
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(getString(R.string.farms));
        }

        mFloatingActionButtonAddFarm.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                addFarm();
            }
        });
        
        if(this.mPresenter == null) {
            this.mPresenter = new FarmPresenter();
        }
        
        if (this.mAdapter == null) {
            mAdapter = new FarmAdapter(this, mPresenter, this, new ArrayList<>());
        }
        
        mSuperRecyclerViewFarms.setLayoutManager(new LinearLayoutManager(this));
        mSuperRecyclerViewFarms.setAdapter(this.mAdapter);
        

        if(this.mMaterialDialogUtils == null) {
            this.mMaterialDialogUtils = new MaterialDialogUtils(this);
        }

        if(this.mPresenter != null) {
            mPresenter.bindView(this, this.mAdapter);
            mPresenter.loadFarms();
        }
        
        super.updateMenuIcons(Integer.valueOf(this.mAdapter.getItemCount()));
    }

    protected void onDestroy() {
        super.onDestroy();
        if(this.mPresenter != null) {
            mPresenter.unBindView();
        }

        this.mPresenter = null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_farm, menu);
        mMenuItemAbout = menu.findItem(R.id.menuMainItemAbout);
        mMenuItemLogout = menu.findItem(R.id.menuMainItemLogout);
        mMenuItemDelete = menu.findItem(R.id.menuMainItemDelete);
        mMenuItemEdit = menu.findItem(R.id.menuMainItemEdit);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuMainItemAbout) {
            if(this.mPresenter != null) {
                mPresenter.showAbout();
            }
        } else if(item.getItemId() == R.id.menuMainItemLogout) {
            this.logout();
        } else if(item.getItemId() == R.id.menuMainItemEdit) {
            this.updateFarm();
        } else if(item.getItemId() == R.id.menuMainItemDelete) {
            if(this.mPresenter != null) {
                mPresenter.deleteSelectedItems(this.mAdapter.getSelectedItens());
            }
        }

        return true;
    }

    private final void addFarm() {
        if(this.mMaterialDialogUtils != null) {
            mMaterialDialogUtils
                    .getDialog(R.layout.new_farm_view, R.string.new_farm, mPresenter)
                    .show();
        }
    }

    private final void updateFarm() {
        // We can select index 0 because the edit item will only be visible
        // when only 1 item is selected
        FarmModel farm = mAdapter.getSelectedItens().get(0);
        MaterialDialog.Builder builder = mMaterialDialogUtils
                .getDialog(R.layout.new_farm_view, R.string.farm, mPresenter);

        TextView textViewFarmKey = (TextView) builder.build().findViewById(R.id.textViewFarmKey);
        textViewFarmKey.setText(farm.getKey());

        MaterialEditText materialEditTextFarmName =
                (MaterialEditText) builder.build().findViewById(R.id.materialEditTextFarmName);
        materialEditTextFarmName.setText(farm.getName());
        builder.show();

    }

    private final void logout() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener() {
            public final void onComplete(Task it) {
                startActivity(new Intent(FarmActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
