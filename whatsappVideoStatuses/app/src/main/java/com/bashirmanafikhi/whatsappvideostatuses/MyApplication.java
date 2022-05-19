package com.bashirmanafikhi.whatsappvideostatuses;

import com.appsflyer.AppsFlyerLib;
import android.app.Application;



public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        AppsFlyerLib.getInstance().init("amDSrit2yhKZEiaE5SWmNn", null, this);
        AppsFlyerLib.getInstance().start(this);

    }
}