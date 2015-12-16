package com.dka.profin;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;

import com.dka.mainmenu.DrawerActivity;
import com.dka.mainmenu.DrawerFragment;
import com.dka.mainmenu.utils.NavigateUtils;
import com.dka.profin.fragment.ReportFragment;
import com.dka.profin.fragment.RootActivityFragment;
import com.dka.profin.fragment.SettingsFragment;

public class RootActivity extends DrawerActivity {
    private static final DrawerFragment.MainMenuItem[] MAIN_MENU_ITEMS = new DrawerFragment.MainMenuItem[]{
            new DrawerFragment.MainMenuItem(R.string.menu_dashboard, android.R.drawable.ic_menu_agenda),
            new DrawerFragment.MainMenuItem(R.string.menu_report, android.R.drawable.ic_menu_report_image),
            new DrawerFragment.MainMenuItem(R.string.menu_settings, android.R.drawable.ic_menu_preferences),
            new DrawerFragment.MainMenuItem(R.string.menu_item4, android.R.drawable.ic_menu_zoom, false).setDisabled(true)
    };

    @Override
    protected int getDefaultMenuItemId() {
        return R.string.menu_dashboard;
    }

    @Override
    protected DrawerFragment.MainMenuItem[] getMainMenuItemArray() {
        return MAIN_MENU_ITEMS;
    }

    @Override
    protected void replaceContentFragmentByMenuId(int menuItemId,
                                                  Bundle extras) {
        switch (menuItemId) {
            case R.string.menu_dashboard:
                Log.d(">>>", "Item 1 selected");
                NavigateUtils.replaceContent(getSupportFragmentManager(), new RootActivityFragment());
                break;
            case R.string.menu_report:
                Log.d(">>>", "Item 2 selected");
                NavigateUtils.replaceContent(getSupportFragmentManager(), new ReportFragment());
                break;
            case R.string.menu_settings:
                Log.d(">>>", "Item 3 selected");
                NavigateUtils.replaceContent(getSupportFragmentManager(), new SettingsFragment());
                break;
        }
    }

    @Override
    public void onMainMenuItemClick(@StringRes int menuItemTitleResId,
                                    Bundle args) {
        switch (menuItemTitleResId) {
            case R.string.menu_item4:
                Log.d(">>>", "Item 4 clicked");
                break;
        }
    }

    @Override
    public int getHeaderBackgroundResource() {
        return 0;
    }
}
