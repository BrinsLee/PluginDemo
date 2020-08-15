package com.brins.plugindemo;

import android.app.Application;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        PluginUtil.loadPlugin(this);
    }
}
