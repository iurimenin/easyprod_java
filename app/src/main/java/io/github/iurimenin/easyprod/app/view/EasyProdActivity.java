package io.github.iurimenin.easyprod.app.view;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.github.iurimenin.easyprod.app.util.CallbackInterface;

/**
 * Created by Iuri Menin on 14/06/17.
 */

public abstract class EasyProdActivity extends AppCompatActivity implements CallbackInterface {

    protected MenuItem mMenuItemEdit;
    protected MenuItem mMenuItemDelete;
    protected MenuItem mMenuItemAbout;
    protected MenuItem mMenuItemLogout;
    protected MenuItem mMenuItemWeather;

    public void updateMenuIcons(Integer itemsCount) {
        if (itemsCount == 0) {

            if(this.mMenuItemEdit != null) {
                this.mMenuItemEdit.setVisible(false);
            }

            if(this.mMenuItemDelete != null) {
                this.mMenuItemDelete.setVisible(false);
            }

            if(this.mMenuItemAbout != null) {
                this.mMenuItemAbout.setVisible(true);
            }

            if(this.mMenuItemLogout != null) {
                this.mMenuItemLogout.setVisible(true);
            }

            if(this.mMenuItemWeather != null) {
                this.mMenuItemWeather.setVisible(true);
            }
        } else if (itemsCount == 1) {

            if(this.mMenuItemDelete != null) {
                this.mMenuItemDelete.setVisible(true);
            }

            if(this.mMenuItemEdit != null) {
                this.mMenuItemEdit.setVisible(true);
            }

            if(this.mMenuItemAbout != null) {
                this.mMenuItemAbout.setVisible(false);
            }

            if(this.mMenuItemLogout != null) {
                this.mMenuItemLogout.setVisible(false);
            }

            if(this.mMenuItemWeather != null) {
                this.mMenuItemWeather.setVisible(false);
            }
        } else if (itemsCount >= 2) {

            if(this.mMenuItemDelete != null) {
                this.mMenuItemDelete.setVisible(true);
            }

            if(this.mMenuItemEdit != null) {
                this.mMenuItemEdit.setVisible(false);
            }

            if(this.mMenuItemAbout != null) {
                this.mMenuItemAbout.setVisible(false);
            }

            if(this.mMenuItemLogout != null) {
                this.mMenuItemLogout.setVisible(false);
            }

            if(this.mMenuItemWeather != null) {
                this.mMenuItemWeather.setVisible(false);
            }
        } else {

            if(this.mMenuItemDelete != null) {
                this.mMenuItemDelete.setVisible(false);
            }

            if(this.mMenuItemEdit != null) {
                this.mMenuItemEdit.setVisible(false);
            }

            if(this.mMenuItemAbout != null) {
                this.mMenuItemAbout.setVisible(false);
            }

            if(this.mMenuItemLogout != null) {
                this.mMenuItemLogout.setVisible(false);
            }

            if(this.mMenuItemWeather != null) {
                this.mMenuItemWeather.setVisible(false);
            }
        }
    }
}
