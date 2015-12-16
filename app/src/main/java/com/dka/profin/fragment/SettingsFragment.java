package com.dka.profin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dka.mainmenu.ContentFragment;
import com.dka.profin.R;

/**
 * @author Dmitry.Kalyuzhnyi 15.12.2015.
 */
public class SettingsFragment extends ContentFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fr_under_construction, container, false);
        return rootView;
    }
}
