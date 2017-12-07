package com.nanda.calendarSample.app;

import android.app.Application;

public class AppController extends Application {

    private static AppController appController;
    private MainThreadBus bus;

    @Override
    public void onCreate() {
        super.onCreate();

        appController = this;
        bus = new MainThreadBus();
    }

    public static AppController getInstance() {
        return appController;
    }

    public MainThreadBus getBus() {
        return bus;
    }

}
