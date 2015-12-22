package com.dka.mainmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.dka.mainmenu.utils.KeyboardUtils;

import java.util.List;

/**
 * @author Dmitry.Kalyuzhnyi 10.12.2015.
 */
public abstract class DrawerActivity extends AppCompatActivity implements DrawerFragment.MainMenuListener,
        DrawerLayout.DrawerListener, View.OnClickListener {

    public static final String START_MENU_ITEM_ID = "START_MENU_ITEM_ID";
    private static final String CURRENT_MENU_ITEM_ID = "CURRENT_MENU_ITEM_ID";

    private int mCurrentMenuItemId;
    private DrawerLayout mDrawerLayout;
    @Nullable
    private ActionBarDrawerToggle mDrawerToggle;

    protected abstract int getDefaultMenuItemId();

    @NonNull
    protected abstract DrawerFragment.MainMenuItem[] getMainMenuItemArray();

    protected abstract void replaceContentFragmentByMenuId(int menuItemId,
                                                           Bundle extras);

    public static void setMenuItemChecked(@Nullable FragmentActivity activity,
                                          int menuItemId) {
        if (activity != null && activity instanceof DrawerActivity) {
            DrawerActivity drawerActivity = (DrawerActivity) activity;
            if (drawerActivity.mCurrentMenuItemId != menuItemId) {
                drawerActivity.mCurrentMenuItemId = menuItemId;
                DrawerFragment.selectMenuItem(drawerActivity.getSupportFragmentManager(), menuItemId);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_MENU_ITEM_ID, mCurrentMenuItemId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(START_MENU_ITEM_ID)) {
            onMainMenuItemSelect(intent.getIntExtra(START_MENU_ITEM_ID, getDefaultMenuItemId()), intent.getExtras());
            DrawerFragment.selectMenuItem(getSupportFragmentManager(), mCurrentMenuItemId);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_drawer);
        if (savedInstanceState == null) {
            mCurrentMenuItemId = getIntent().getIntExtra(START_MENU_ITEM_ID, getDefaultMenuItemId());
        } else {
            mCurrentMenuItemId = savedInstanceState.getInt(CURRENT_MENU_ITEM_ID);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerFragment.setUp(getSupportFragmentManager(), mDrawerLayout, mCurrentMenuItemId, getMainMenuItemArray(), this);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            replaceContentFragmentByMenuId(mCurrentMenuItemId, getIntent().getExtras());
        }
    }

    @Override
    public void onMainMenuItemSelect(@StringRes int menuItemTitleResId,
                                     Bundle args) {
        if (mCurrentMenuItemId != menuItemTitleResId) {
            mCurrentMenuItemId = menuItemTitleResId;
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            replaceContentFragmentByMenuId(menuItemTitleResId, args);
        }
    }

    @Override
    public void onMainMenuItemClick(@StringRes int menuItemTitleResId,
                                    Bundle args) {
    }

    @Override
    public void onBackPressed() {
        final List<Fragment> currentFragmentList = getSupportFragmentManager().getFragments();
        if (currentFragmentList != null && !currentFragmentList.isEmpty()) {
            for (Fragment currentFragment : currentFragmentList) {
                if (currentFragment instanceof OnBackPressListener && currentFragment.isAdded()) {
                    if (((OnBackPressListener) currentFragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        KeyboardUtils.closeKeyboard(this);
        super.onBackPressed();
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        Log.d(">>>", "set support action bar");
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mDrawerToggle.setToolbarNavigationClickListener(this);
        mDrawerLayout.post(new Runnable() {

            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        setMainMenuEnabled(true);
    }

    public void setMainMenuEnabled(boolean enabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(enabled);
        mDrawerLayout.setDrawerLockMode(enabled
                        ? DrawerLayout.LOCK_MODE_UNLOCKED
                        : DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.START);
    }

    @Override
    public void onDrawerSlide(View drawerView,
                              float slideOffset) {
        mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        KeyboardUtils.closeKeyboard(this);
        mDrawerToggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerToggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        mDrawerToggle.onDrawerStateChanged(newState);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

}