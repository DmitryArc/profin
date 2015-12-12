package com.dka.mainmenu.utils;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dka.mainmenu.R;

/**
 * @author Dmitry.Kalyuzhnyi 11.12.2015.
 */
public class NavigateUtils {
    public static void replaceContent(FragmentManager fm,
                                      Fragment fragment) {
        replaceContent(fm, fragment, false);
    }

    @SuppressLint("CommitTransaction")
    public static void replaceContent(FragmentManager fm,
                                      Fragment fragment,
                                      boolean addToBackStack) {
        final FragmentTransaction ft = fm.beginTransaction();
        replaceContent(ft, fragment, addToBackStack);
    }

    public static void replaceContent(FragmentTransaction ft,
                                      Fragment fragment,
                                      boolean addToBackStack) {
        ft.replace(R.id.container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(fragment.getClass().getSimpleName());
        }
        ft.commit();
    }
}
