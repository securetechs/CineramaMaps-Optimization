package main.com.cineramamaps.app;

import android.app.Application;

import main.com.cineramamaps.restapi.ApiCall;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiCall.init(this);
       // FirebaseApp.initializeApp(getApplicationContext());
        }
}
