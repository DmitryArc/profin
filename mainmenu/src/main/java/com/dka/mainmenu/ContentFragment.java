package com.dka.mainmenu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * @author Dmitry.Kalyuzhnyi 11.12.2015.
 */
public abstract class ContentFragment extends Fragment {

    protected Toolbar mToolbar;

    @Override
    public void onViewCreated(@NonNull View view,
                              Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        if (mToolbar == null) {
            throw new IllegalStateException("Toolbar not found!");
        }

        final AppCompatActivity actionBarActivity = getSupportActivity();
        actionBarActivity.setSupportActionBar(mToolbar);
    }

    @NonNull
    public AppCompatActivity getSupportActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Nullable
    protected ActionBar getSupportActionBar() {
        return getSupportActivity().getSupportActionBar();
    }

}