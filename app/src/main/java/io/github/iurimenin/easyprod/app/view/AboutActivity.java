package io.github.iurimenin.easyprod.app.view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.easyprod.R;

/**
 * Created by Iuri Menin on 14/06/17.
 */

public class AboutActivity  extends AppCompatActivity {

    @BindView(R.id.textViewVersion) TextView mTextViewVersion;
    @BindView(R.id.textViewDevelopedBy) TextView mTextViewDevelopedBy;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        Drawable arrowBack = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp);
        arrowBack.setColorFilter(ContextCompat.getColor(this, android.R.color.white),
                PorterDuff.Mode.SRC_ATOP);

        if(this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(getString(R.string.about));
            this.getSupportActionBar().setDisplayShowTitleEnabled(true);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeAsUpIndicator(arrowBack);
        }

        mTextViewDevelopedBy.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                Intent browserIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse(getString(R.string.url_iuri)));
                AboutActivity.this.startActivity(browserIntent);
            }
        });

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            mTextViewVersion.setText(getString(R.string.version, pInfo.versionName));
        } catch (PackageManager.NameNotFoundException var6) {
            var6.printStackTrace();
        }

    }

    public boolean onOptionsItemSelected( MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        } else {
           return super.onOptionsItemSelected(item);
        }
    }
}