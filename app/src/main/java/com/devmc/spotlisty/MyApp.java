package com.devmc.spotlisty;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApp extends Application {
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("App Created>>>",">>>");
    }

}
