package com.dka.profin.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dka.mainmenu.ContentFragment;
import com.dka.profin.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RootActivityFragment extends ContentFragment {

    public RootActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_root, container, false);
    }
}
