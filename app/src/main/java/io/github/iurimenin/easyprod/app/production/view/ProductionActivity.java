package io.github.iurimenin.easyprod.app.production.view;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.cultivation.model.CultivationModel;
import io.github.iurimenin.easyprod.app.field.model.FieldModel;
import io.github.iurimenin.easyprod.app.production.model.ProductionModel;
import io.github.iurimenin.easyprod.app.production.presenter.ProductionPresenter;
import io.github.iurimenin.easyprod.app.util.MaterialDialogUtils;
import io.github.iurimenin.easyprod.app.util.MoneyMaskMaterialEditText;
import io.github.iurimenin.easyprod.app.util.ProductionInterface;
import io.github.iurimenin.easyprod.app.view.EasyProdActivity;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class ProductionActivity extends EasyProdActivity implements ProductionInterface {

    private FieldModel mField;
    private CultivationModel mCultivation;
    private ProductionPresenter mPresenter;
    private MaterialDialogUtils mMaterialDialogUtils;
    private ProductionAdapter mAdapter;

    @BindView(R.id.floatingActionButtonAddProduction) FloatingActionButton mFloatingActionButtonAddProduction;
    @BindView(R.id.textViewFieldName) TextView mTextViewFieldName;
    @BindView(R.id.superRecyclerViewCultivation) SuperRecyclerView mSuperRecyclerViewCultivation;
    @BindView(R.id.textViewTotalProduction) TextView mTextViewTotalProduction;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_production);

        ButterKnife.bind(this);

        Drawable arrowBack = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp);
        arrowBack.setColorFilter(ContextCompat.getColor(this, android.R.color.white),
                PorterDuff.Mode.SRC_ATOP);

        if(this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(getString(R.string.fiels));
            this.getSupportActionBar().setDisplayShowTitleEnabled(true);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeAsUpIndicator(arrowBack);
        }

        this.mCultivation = getIntent().getExtras().getParcelable(CultivationModel.TAG);
        this.mField = getIntent().getExtras().getParcelable(FieldModel.TAG);

        mTextViewFieldName.setText(mCultivation.getName() + getString(R.string.season) + " " + mCultivation.getSeasonName());
        mFloatingActionButtonAddProduction.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                addProduction();
            }
        });

        if(this.mPresenter == null) {
            this.mPresenter = new ProductionPresenter(this.mCultivation, this.mField.getTotalArea());
        }

        mAdapter = new ProductionAdapter(this, mPresenter, this, new ArrayList<>());
        mSuperRecyclerViewCultivation.setLayoutManager((new LinearLayoutManager(this)));
        mSuperRecyclerViewCultivation.setAdapter(this.mAdapter);

        if(this.mMaterialDialogUtils == null) {
            this.mMaterialDialogUtils = new MaterialDialogUtils(this);
        }

        if(this.mPresenter != null) {
            mPresenter.bindView(this, this.mAdapter);
            mPresenter.loadProductions();
        }

        super.updateMenuIcons(this.mAdapter.getItemCount());
    }

    protected void onDestroy() {
        super.onDestroy();
        ProductionPresenter var10000 = this.mPresenter;
        if(this.mPresenter != null) {
            var10000.unBindView();
        }

        this.mPresenter = (ProductionPresenter)null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_production, menu);
        mMenuItemDelete = menu.findItem(R.id.menuProductionItemDelete);
        mMenuItemEdit = menu.findItem(R.id.menuProductionItemEdit);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        } else if(item.getItemId() == R.id.menuProductionItemEdit) {
            this.updateProduction();
        } else if(item.getItemId() == R.id.menuProductionItemDelete) {
            if(this.mPresenter != null) {
                mPresenter.deleteSelectedItems(this.mAdapter.getSelectedItems());
            }
        }

        return true;
    }

    @Override
    public void updateProduction(Double result) {

        mTextViewTotalProduction.setText(NumberFormat.getCurrencyInstance()
                .format(result).replace(NumberFormat.getCurrencyInstance()
                        .getCurrency().getSymbol(), "") +
                " " + getString(R.string.bags_per_hectare));
    }

    private final void addProduction() {
        if (this.mMaterialDialogUtils != null) {
            mMaterialDialogUtils
                    .getDialog(R.layout.new_production_view, R.string.new_production, mPresenter)
                    .show();
        }
    }

    private final void updateProduction() {
        //We can select index 0 because the edit item will only be visible
        // when only 1 item is selected
        ProductionModel field = this.mAdapter.getSelectedItems().get(0);
        MaterialDialog.Builder builder = mMaterialDialogUtils
                .getDialog(R.layout.new_production_view, R.string.production, mPresenter);

        TextView textViewProductionKey = (TextView) builder.build().findViewById(R.id.textViewProductionKey);
        textViewProductionKey.setText(field.getKey());

        MoneyMaskMaterialEditText materialEditTextProductionArea =
                (MoneyMaskMaterialEditText) builder.build().findViewById(R.id.materialEditTextProductionBags);
        materialEditTextProductionArea.setTextFromDouble(field.getBags());

        builder.show();
    }

}
