package ru.goodwin98.pikabuapp;

import android.app.Application;

import ru.goodwin98.pikabuapp.util.RoboErrorReporter;


/**
 * Created by goodwin98 on 20.07.2015.
 */
public class pikabuapp extends Application {

    @Override
    public void onCreate() {
        RoboErrorReporter.bindReporter(getApplicationContext());
        super.onCreate();


    }
}
