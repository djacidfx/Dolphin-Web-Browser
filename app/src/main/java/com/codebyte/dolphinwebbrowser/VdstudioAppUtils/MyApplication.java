package com.codebyte.dolphinwebbrowser.VdstudioAppUtils;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.codebyte.dolphinwebbrowser.AppPrefrence;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    public static final String CHANNEL_ID1 = "channel1";
    public static final String CHANNEL_ID2 = "channel2";
    public static Context context;
    public static MyApplication instance;
    public static MyApplication myApp;
    static SharedPreferences.Editor prefEditor;
    static SharedPreferences preferences;
    private static MyApplication mInstance;
    String appopen = "";

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context2) {
        context = context2;
    }

    public static boolean isFirstTimeRateUs(Context context2) {
        return PreferenceManager.getDefaultSharedPreferences(context2).getBoolean("isRate", true);
    }

    public static String getSearchHistory() {
        return preferences.getString("history", "");
    }

    public static void putSearcHistory(String str) {
        prefEditor.putString("history", str);
        prefEditor.commit();
    }

    public static String getDownloadPath() {
        return preferences.getString("download_path", "");
    }

    public static void putDownload(String str) {
        prefEditor.putString("download_path", str);
        prefEditor.commit();
    }

    public static void putFirst(boolean z) {
        prefEditor.putBoolean("first", z);
        prefEditor.commit();
    }

    public static boolean getFirst() {
        return preferences.getBoolean("first", false);
    }

    public static void putAdBlock(boolean z) {
        prefEditor.putBoolean("adblock", z);
        prefEditor.commit();
    }

    public static boolean getAdBlock() {
        return preferences.getBoolean("adblock", false);
    }

    public static void putBlockImage(boolean z) {
        prefEditor.putBoolean("block_image", z);
        prefEditor.commit();
    }

    public static boolean getBlockImage() {
        return preferences.getBoolean("block_image", false);
    }

    public static void putRequestData(boolean z) {
        prefEditor.putBoolean("request_data", z);
        prefEditor.commit();
    }

    public static boolean getRequestData() {
        return preferences.getBoolean("request_data", false);
    }

    public static void putEnableJava(boolean z) {
        prefEditor.putBoolean("enable_java", z);
        prefEditor.commit();
    }

    public static boolean getEnableJava() {
        return preferences.getBoolean("enable_java", false);
    }

    public static String getUserAgent() {
        return preferences.getString("user_agent", "");
    }

    public static void putUserAgent(String str) {
        prefEditor.putString("user_agent", str);
        prefEditor.commit();
    }

    public static String getHomePage() {
        return preferences.getString("home_page", "");
    }

    public static void putHomePage(String str) {
        prefEditor.putString("home_page", str);
        prefEditor.commit();
    }

    public static String getSearchEngine() {
        return preferences.getString("search_engine", "");
    }

    public static void putSearchEngine(String str) {
        prefEditor.putString("search_engine", str);
        prefEditor.commit();
    }

    public static void putHideStatus(boolean z) {
        prefEditor.putBoolean("hide_status", z);
        prefEditor.commit();
    }

    public static boolean getHideStatus() {
        return preferences.getBoolean("hide_status", false);
    }

    public static void putFullScreen(boolean z) {
        prefEditor.putBoolean("full_screen", z);
        prefEditor.commit();
    }

    public static boolean getFullScreen() {
        return preferences.getBoolean("full_screen", false);
    }

    public static void putBlackStatus(boolean z) {
        prefEditor.putBoolean("black_status", z);
        prefEditor.commit();
    }

    public static boolean getBlackStatus() {
        return preferences.getBoolean("black_status", false);
    }

    public static String getTextSize() {
        return preferences.getString("text_size", "");
    }

    public static void putTextSize(String str) {
        prefEditor.putString("text_size", str);
        prefEditor.commit();
    }

    public static void putAllowSite(boolean z) {
        prefEditor.putBoolean("allow_site", z);
        prefEditor.commit();
    }

    public static boolean getAllowSite() {
        return preferences.getBoolean("allow_site", false);
    }

    public static void putCookies(boolean z) {
        prefEditor.putBoolean("cookies", z);
        prefEditor.commit();
    }

    public static boolean getCookies() {
        return preferences.getBoolean("cookies", false);
    }

    public static String getTextEncoding() {
        return preferences.getString("text_encoding", "");
    }

    public static void putTextEncoding(String str) {
        prefEditor.putString("text_encoding", str);
        prefEditor.commit();
    }

    public static void putRendering(String str) {
        prefEditor.putString("rendering", str);
        prefEditor.commit();
    }

    public static String getUrlBox() {
        return preferences.getString("url_box", "");
    }

    public static void putUrlBox(String str) {
        prefEditor.putString("url_box", str);
        prefEditor.commit();
    }

    public static boolean getCacheExit() {
        return preferences.getBoolean("cache_exit", false);
    }

    public static boolean getHistoryExit() {
        return preferences.getBoolean("history_exit", false);
    }

    public static boolean getCookieExit() {
        return preferences.getBoolean("cookie_exit", false);
    }

    public static String getShowPlateformAd(Context context2) {
        return PreferenceManager.getDefaultSharedPreferences(context2).getString(Constant.PREF_SHOW_PLATEFORM_ID, "");
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onCreate() {
        super.onCreate();
        myApp = this;
        SharedPreferences sharedPreferences = getSharedPreferences("light_browser", 0);
        preferences = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        prefEditor = edit;
        edit.apply();
        mInstance = this;
        createNotificationChannel();
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        context = getApplicationContext();
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        registerActivityLifecycleCallbacks(this);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        this.appopen = new AppPrefrence(this).getAmAppopen();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        instance = this;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > 26) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID1, "Channel(1)", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel 1 desc....");
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(1);
            NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_ID2, "Channel(2)", NotificationManager.IMPORTANCE_LOW);
            notificationChannel2.setDescription("Channel 2 desc...");
            notificationChannel2.setShowBadge(true);
            notificationChannel2.setLockscreenVisibility(1);
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel2);
        }
    }


}
