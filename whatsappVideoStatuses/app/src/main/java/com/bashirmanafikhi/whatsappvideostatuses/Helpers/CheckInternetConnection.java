package com.bashirmanafikhi.whatsappvideostatuses.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetConnection {

    Context context;

    public CheckInternetConnection(Context context) {
        this.context = context;
    }

    public boolean isConnecting(){
        ConnectivityManager connetivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connetivityManager != null){
            NetworkInfo networkInfo = connetivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()){
                return true;
            }
        }
        return false;

    }
}