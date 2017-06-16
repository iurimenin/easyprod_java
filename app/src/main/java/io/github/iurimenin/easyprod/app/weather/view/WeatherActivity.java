package io.github.iurimenin.easyprod.app.weather.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.weather.presenter.WeatherPresenter;

public class WeatherActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private WeatherPresenter mPresenter;
    private WeatherAdapter mAdapter;

    protected String mLastUpdateTime;
    protected Location mCurrentLocation;
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    protected Boolean mRequestingLocationUpdates;

    @BindView(R.id.superRecyclerViewWeather) SuperRecyclerView mSuperRecyclerViewWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_weather);

        ButterKnife.bind(this);

        mRequestingLocationUpdates = false;

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

        if(this.mPresenter == null) {
            this.mPresenter = new WeatherPresenter();
            mAdapter = new WeatherAdapter(this, new ArrayList<>());
        }

        mSuperRecyclerViewWeather.setLayoutManager(new LinearLayoutManager(this));
        mSuperRecyclerViewWeather.setAdapter(this.mAdapter);

        if(this.mPresenter != null) {
            this.mPresenter.bindView(this, this.mAdapter);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ArrayList<String> permissions = new ArrayList<>();

            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

            String[] permissionsArr = new String[permissions.size()];
            permissionsArr = permissions.toArray(permissionsArr);
            requestPermissions(permissionsArr, MY_PERMISSIONS_REQUEST);
        }

        buildGoogleApiClient();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {

                if (grantResults.length > 0) {

                    boolean allPermissionGranted = true;
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED)
                            allPermissionGranted = false;
                    }

                    if (allPermissionGranted) {

                    } else {

                        MaterialDialog.Builder builder =
                                new MaterialDialog.Builder(WeatherActivity.this)
                                .title(R.string.oops)
                                .titleColorRes(R.color.colorPrimary)
                                .content(R.string.location_required)
                                .contentColor(ContextCompat.getColor(WeatherActivity.this, R.color.colorPrimaryText))
                                .positiveColor(ContextCompat.getColor(WeatherActivity.this, R.color.colorPrimary))
                                .positiveText(R.string.text_ok)
                                .autoDismiss(true)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        if (ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                                ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                            ArrayList<String> permissions = new ArrayList<>();

                                            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                                            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

                                            String[] permissionsArr = new String[permissions.size()];
                                            permissionsArr = permissions.toArray(permissionsArr);
                                            requestPermissions(permissionsArr, MY_PERMISSIONS_REQUEST);
                                        }
                                    }
                                });

                        builder.show();
                    }
                }
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void onDestroy() {
        super.onDestroy();
        if(this.mPresenter != null) {
            this.mPresenter.unBindView();
        }
        this.mPresenter = null;
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (mCurrentLocation == null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

            this.mPresenter.syncWeather(mCurrentLocation);
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        this.mPresenter.syncWeather(mCurrentLocation);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}
