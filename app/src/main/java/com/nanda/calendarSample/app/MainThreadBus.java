package com.nanda.calendarSample.app;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by Nandagopal on 10/27/2017.
 */

public class MainThreadBus extends Bus {
    private final Handler mainThreadHandler;

    public MainThreadBus() {
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.this.post(event);
                }
            });
        } else {
            super.post(event);
        }
    }
}
