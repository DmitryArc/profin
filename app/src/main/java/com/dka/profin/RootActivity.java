package com.dka.profin;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.dka.mainmenu.DrawerActivity;
import com.dka.mainmenu.DrawerFragment;
import com.dka.mainmenu.utils.NavigateUtils;

public class RootActivity extends DrawerActivity {
    private static final DrawerFragment.MainMenuItem[] MAIN_MENU_ITEMS = new DrawerFragment.MainMenuItem[]{
            new DrawerFragment.MainMenuItem(R.string.menu_item1, android.R.drawable.ic_media_play),
            new DrawerFragment.MainMenuItem(R.string.menu_item2, android.R.drawable.ic_menu_edit),
            new DrawerFragment.MainMenuItem(R.string.menu_item3, android.R.drawable.ic_menu_camera, false),
            new DrawerFragment.MainMenuItem(R.string.menu_item4, android.R.drawable.ic_menu_zoom, false).setDisabled(true)
    };

    @Override
    protected int getDefaultMenuItemId() {
        return R.string.menu_item1;
    }

    @Override
    protected DrawerFragment.MainMenuItem[] getMainMenuItemArray() {
        return MAIN_MENU_ITEMS;
    }

    @Override
    protected void replaceContentFragmentByMenuId(int menuItemId,
                                                  Bundle extras) {
        switch (menuItemId) {
            case R.string.menu_item1:
                Log.d(">>>", "Item 1 selected");
                NavigateUtils.replaceContent(getSupportFragmentManager(), new RootActivityFragment());
                break;
            case R.string.menu_item2:
                Log.d(">>>", "Item 2 selected");
                break;
        }
    }

    @Override
    public void onMainMenuItemClick(@StringRes int menuItemTitleResId,
                                    Bundle args) {
        switch (menuItemTitleResId) {
            case R.string.menu_item1:
                Log.d(">>>", "Item 1 clicked");
                NavigateUtils.replaceContent(getSupportFragmentManager(), new RootActivityFragment());
                break;
            case R.string.menu_item3:
                Log.d(">>>", "Item 3 clicked");
                break;
        }
    }

    @Override
    public int getHeaderBackgroundResource() {
        return 0;
    }
}
