package io.github.iurimenin.easyprod.app.field.view;

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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.farm.model.FarmModel;
import io.github.iurimenin.easyprod.app.field.model.FieldModel;
import io.github.iurimenin.easyprod.app.field.presenter.FieldPresenter;
import io.github.iurimenin.easyprod.app.util.MaterialDialogUtils;
import io.github.iurimenin.easyprod.app.util.MoneyMaskMaterialEditText;
import io.github.iurimenin.easyprod.app.view.EasyProdActivity;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class FieldActivity extends EasyProdActivity {

    private FarmModel mFarm;
    private FieldPresenter mPresenter;
    private MaterialDialogUtils mMaterialDialogUtils;
    private FieldAdapter mAdapter;

    @BindView(R.id.textViewFarm) TextView mTextViewFarm;
    @BindView(R.id.floatingActionButtonAddField) FloatingActionButton mFloatingActionButtonAddField;
    @BindView(R.id.superRecyclerViewFields) SuperRecyclerView mSuperRecyclerViewFields;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_fields);

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

        mFarm = this.getIntent().getExtras().getParcelable(FarmModel.TAG);

        mTextViewFarm.setText(this.mFarm.getName());

        mFloatingActionButtonAddField.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                addFiled();
            }
        });

        if(this.mPresenter == null) {
            this.mPresenter = new FieldPresenter(this.mFarm != null?this.mFarm.getKey():null);
        }

        mAdapter = new FieldAdapter(this, mPresenter, this, new ArrayList<>());
        mSuperRecyclerViewFields.setLayoutManager(new LinearLayoutManager(this));
        mSuperRecyclerViewFields.setAdapter(this.mAdapter);


        if(this.mMaterialDialogUtils == null) {
            this.mMaterialDialogUtils = new MaterialDialogUtils(this);
        }

        if(this.mPresenter != null) {
            mPresenter.bindView(this, this.mAdapter);
            mPresenter.loadFields();
        }

        this.updateMenuIcons(this.mAdapter.getItemCount());
    }

    protected void onDestroy() {
        super.onDestroy();
        if(this.mPresenter != null) {
            mPresenter.unBindView();
        }
        this.mPresenter = null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_field, menu);
        mMenuItemDelete = menu.findItem(R.id.menuFieldItemDelete);
        mMenuItemEdit = menu.findItem(R.id.menuFieldItemEdit);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        } else if(item.getItemId() == R.id.menuFieldItemEdit) {
            this.updateField();
        } else if(item.getItemId() == R.id.menuFieldItemDelete) {
            if(this.mPresenter != null) {
                mPresenter.deleteSelectedItems(this.mAdapter.getSelectedItems());
            }
        }

        return true;
    }

    private final void addFiled() {
        if(this.mMaterialDialogUtils != null) {
            mMaterialDialogUtils
                    .getDialog(R.layout.new_field_view, R.string.new_field, mPresenter)
                    .show();
        }

    }

    private final void updateField() {
        //We can select index 0 because the edit item will only be visible
        // when only 1 item is selected
        FieldModel field = mAdapter.getSelectedItems().get(0);
        MaterialDialog.Builder builder =
                mMaterialDialogUtils.getDialog(R.layout.new_field_view, R.string.field, mPresenter);

        TextView textViewFieldKey = (TextView) builder.build().findViewById(R.id.textViewFieldKey);
        textViewFieldKey.setText(field.getKey());

        MaterialEditText materialEditTextFieldName =
                (MaterialEditText) builder.build().findViewById(R.id.materialEditTextFieldName);
        materialEditTextFieldName.setText(field.getName());

        MoneyMaskMaterialEditText materialEditTextFieldArea =
                (MoneyMaskMaterialEditText) builder.build().findViewById(R.id.materialEditTextFieldArea);
        materialEditTextFieldArea.setTextFromDouble(field.getTotalArea());

        builder.show();
    }
}
