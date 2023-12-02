package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constant {
    public static String adsFb = "facebook";
    public static String adsGoogle = "google";
    public static String typeAds = "typeAds";

    public static boolean isOnline(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
