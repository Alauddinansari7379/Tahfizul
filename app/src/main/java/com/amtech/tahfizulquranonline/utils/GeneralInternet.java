package com.amtech.tahfizulquranonline.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Shourav Paul on 18-02-2022.
 **/
public class GeneralInternet {

    //private static int internetStatus;

    public static boolean checkInternet(final Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            //Log.i("internet info", "connected!");
            //internetStatus = 1;
            return true;
        }
//        else {
//            //Toasty.warning(this, "No internet connection!", Toast.LENGTH_LONG, true).show();
//            //internetStatus = 0;
//        }
        //return internetStatus;
        return false;
    }
}