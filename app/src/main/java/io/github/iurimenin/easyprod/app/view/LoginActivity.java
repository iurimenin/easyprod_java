package io.github.iurimenin.easyprod.app.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.HashMap;

import io.github.iurimenin.easyprod.R;
import io.github.iurimenin.easyprod.app.farm.view.FarmActivity;

/**
 * Created by Iuri Menin on 14/06/17.
 */

public class LoginActivity extends AppCompatActivity {
    private final int RC_SIGN_IN = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            this.startMain();
        } else {
            this.startLogin();
        }

    }

    private final void startMain() {
        Intent i = new Intent(this, FarmActivity.class);
        this.startActivity(i);
        this.finish();
    }

    private final void startLogin() {
        this.startActivityForResult(AuthUI
                .getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.GreenTheme)
                .setProviders(Arrays
                        .asList(new AuthUI.IdpConfig.Builder("password").build(),
                                new AuthUI.IdpConfig.Builder("google.com").build()))
                .build(), this.RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == this.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == -1) {
                this.startMain();
                return;
            }

            if(response == null) {
                this.finish();
                return;
            }

            if(response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            if(response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }

            showSnackbar(R.string.unknown_sign_in_response);
        }

    }

    private final void showSnackbar(int idString) {
        Toast.makeText(this, this.getString(idString), Toast.LENGTH_LONG).show();
    }
}