package io.github.iurimenin.easyprod.app.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import io.github.iurimenin.easyprod.R;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class MaterialDialogUtils {

    private Context mContext;

    public MaterialDialogUtils(Context context){
        mContext = context;
    }

    public MaterialDialog.Builder getDialog(Integer viewId, Integer title,
                                            PresenterInterface presenter) {
        return new MaterialDialog.Builder(mContext)
                .title(title)
                .titleColorRes(R.color.colorPrimary)
                .contentColor(ContextCompat.getColor(mContext, R.color.colorPrimaryText))
                .negativeColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light))
                .positiveColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .customView(viewId, true)
                .positiveText(R.string.text_save)
                .negativeText(R.string.text_cancel)
                .autoDismiss(false)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        presenter.save(materialDialog, dialogAction == DialogAction.POSITIVE);
                    }
                });

    }
}
