package com.dka.profin;

import android.app.Application;

/**
 * @author Dmitry.Kalyuzhnyi 15.12.2015.
 */
public class ProfinApp extends Application {

    public volatile static ProfinApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }
    }
}
