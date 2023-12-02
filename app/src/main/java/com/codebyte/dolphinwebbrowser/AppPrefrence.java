package com.codebyte.dolphinwebbrowser;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefrence {
    String amAdaptiveBanner = "amAdaptiveBanner";
    String amAppopen = "amAppopen";
    String amIntertitial = "amIntertitial";
    String amNativeBigHome = "amNativeBigHome";
    String amRewardedVideo = "amRewardedVideo";
    SharedPreferences appSharedPref;
    SharedPreferences.Editor prefEditor;

    public AppPrefrence(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ADS USER PREFS", 0);
        this.appSharedPref = sharedPreferences;
        this.prefEditor = sharedPreferences.edit();
    }


    public String getAM_ADAPTIVE_BANNER() {
        return this.appSharedPref.getString(this.amAdaptiveBanner, "");
    }

    public void setAM_ADAPTIVE_BANNER(String str) {
        this.prefEditor.putString(this.amAdaptiveBanner, str).commit();
    }

    public String getAmIntertitial() {
        return this.appSharedPref.getString(this.amIntertitial, "");
    }

    public void setAmIntertitial(String str) {
        this.prefEditor.putString(this.amIntertitial, str).commit();
    }

    public String getAmNativeBigHome() {
        return this.appSharedPref.getString(this.amNativeBigHome, "");
    }

    public void setAmNativeBigHome(String str) {
        this.prefEditor.putString(this.amNativeBigHome, str).commit();
    }

    public void setAmRewardedVideo(String str) {
        this.prefEditor.putString(this.amRewardedVideo, str).commit();
    }

    public String getAmAppopen() {
        return this.appSharedPref.getString(this.amAppopen, "ca-app-pub-3940256099942544/3419835294");
    }

    public void setAmAppopen(String str) {
        this.prefEditor.putString(this.amAppopen, str).commit();
    }


}
