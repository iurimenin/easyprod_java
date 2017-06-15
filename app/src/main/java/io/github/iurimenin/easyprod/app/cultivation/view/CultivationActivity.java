package io.github.iurimenin.easyprod.app.cultivation.view;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.cultivation.model.CultivationModel;
import io.github.iurimenin.easyprod.app.cultivation.presenter.CultivationPresenter;
import io.github.iurimenin.easyprod.app.field.model.FieldModel;
import io.github.iurimenin.easyprod.app.util.MaterialDialogUtils;
import io.github.iurimenin.easyprod.app.view.EasyProdActivity;

/**
 * Created by Iuri Menin on 14/06/17.
 */
public class CultivationActivity extends EasyProdActivity {

    private FieldModel mField;
    private CultivationPresenter mPresenter;
    private MaterialDialogUtils mMaterialDialogUtils;
    private CultivationAdapter mAdapter;

    @BindView(R.id.textViewFieldName) TextView mTextViewFieldName;
    @BindView(R.id.floatingActionButtonAddCultivation) FloatingActionButton mFloatingActionButtonAddCultivation;
    @BindView(R.id.superRecyclerViewCultivations) SuperRecyclerView mSuperRecyclerViewCultivations;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cultivation);

        ButterKnife.bind(this);

        Drawable arrowBack =
                ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp);
        arrowBack.setColorFilter(ContextCompat
                .getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        if(this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(getString(R.string.cultivations));
            this.getSupportActionBar().setDisplayShowTitleEnabled(true);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeAsUpIndicator(arrowBack);
        }

        mField = this.getIntent().getExtras().getParcelable(FieldModel.TAG);

        mTextViewFieldName.setText(this.mField.getName());
        mFloatingActionButtonAddCultivation.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                addFiled();
            }
        });

        if(this.mPresenter == null) {
            this.mPresenter = new CultivationPresenter(this.mField.getKey());
            mAdapter = new CultivationAdapter(this, mField,
                    mPresenter, this, new ArrayList<>());
        }

        mSuperRecyclerViewCultivations.setLayoutManager(new LinearLayoutManager(this));
        mSuperRecyclerViewCultivations.setAdapter(this.mAdapter);


        if(this.mMaterialDialogUtils == null) {
            this.mMaterialDialogUtils = new MaterialDialogUtils(this);
        }

        if(this.mPresenter != null) {
            this.mPresenter.bindView(this, this.mAdapter);
            this.mPresenter.loadCultivations();
        }

        super.updateMenuIcons(this.mAdapter.getItemCount());
    }

    protected void onDestroy() {
        super.onDestroy();
        if(this.mPresenter != null) {
            this.mPresenter.unBindView();
        }
        this.mPresenter = null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_cultivation, menu);
        mMenuItemDelete = menu.findItem(R.id.menuCultivationItemDelete);
        mMenuItemEdit = menu.findItem(R.id.menuCultivationItemEdit);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        } else if(item.getItemId() == R.id.menuCultivationItemEdit) {
            this.updateCultivation();
        } else if(item.getItemId() == R.id.menuCultivationItemDelete) {
            if(this.mPresenter != null) {
                this.mPresenter.deleteSelectedItems(this.mAdapter.getSelectedItems());
            }
        }

        return true;
    }

    private final void addFiled() {
        MaterialDialog.Builder builder =
                this.mMaterialDialogUtils.getDialog(R.layout.new_cultivation_view,
                        R.string.new_cultivation, mPresenter);

        AppCompatSpinner appCompatSpinnerSeason = (AppCompatSpinner) builder.build().findViewById(R.id.appCompatSpinnerSeason);
        appCompatSpinnerSeason.setAdapter(
                new ArrayAdapter(this, R.layout.spinner_season, mPresenter.getSeasons()));
        builder.show();
    }

    private final void updateCultivation() {
        CultivationModel cultivation = this.mAdapter.getSelectedItems().get(0);
        MaterialDialog.Builder builder = this.mMaterialDialogUtils
                .getDialog(R.layout.new_cultivation_view, R.string.cultivation, mPresenter);

        TextView textViewCultivationKey = (TextView) builder.build().findViewById(R.id.textViewCultivationKey);
        textViewCultivationKey.setText(cultivation.getKey());

        MaterialEditText materialEditTextCultivationName = (MaterialEditText) builder.build().findViewById(R.id.materialEditTextCultivationName);
        materialEditTextCultivationName.setText(cultivation.getName());

        AppCompatSpinner appCompatSpinnerSeason = (AppCompatSpinner) builder.build().findViewById(R.id.appCompatSpinnerSeason);
        ArrayAdapter seasonAdpter = new ArrayAdapter(this, R.layout.spinner_season, mPresenter.getSeasons());
        appCompatSpinnerSeason.setAdapter(seasonAdpter);

        appCompatSpinnerSeason.setSelection(seasonAdpter.getPosition(cultivation));
    }
}