package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.codebyte.dolphinwebbrowser.AppPrefrence;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dolphinwebbrowser.R;

public class SplashScreen extends BaseActivity {
    public static String admobBanner = "";
    public static String admobFull = "";
    public static String admobNative = "";
    public static String admobOpen = "";
    public static String admobReward = "";
    public static String fbBanner = "";
    public static String fbFull = "";
    public static String fbNetive = "";
    public static String fbReward = "";
    public static String qurekaId = "";
    public static SplashScreen splashScreen;
    public PrefManager prf;
    AppPrefrence appPrefrence;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        splashScreen = this;
        this.prf = new PrefManager(getApplicationContext());
        this.appPrefrence = new AppPrefrence(this);
        final Application application = getApplication();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("pkg", getPackageName());
            ((PostRequest) OkGo.post("https://todajiqd.omshivasoft.com/api/AdsAPI.php?Service=GetAdsId").upJson(jSONObject)).execute(new Callback<Object>() {

                @Override
                public void downloadProgress(Progress progress) {
                }

                @Override
                public void onCacheSuccess(Response<Object> response) {
                }

                @Override
                public void onFinish() {
                }

                @Override
                public void onStart(Request<Object, ? extends Request> request) {
                }

                @Override
                public void uploadProgress(Progress progress) {
                }

                @Override
                public Object convertResponse(okhttp3.Response response) throws Throwable {
                    if (!response.isSuccessful() || response.body() == null) {
                        return null;
                    }
                    return response.body().string();
                }

                @Override
                public void onSuccess(Response<Object> response) {
                    if (SplashScreen.this.getApplication() != null && response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray jSONArray = new JSONObject(response.body().toString()).getJSONArray("data");
                            for (int i = 0; i < jSONArray.length(); i++) {
                                try {
                                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                                    if (jSONObject.getInt("admob_status") == 1) {
                                        SplashScreen.admobBanner = jSONObject.getString("admob_banner");
                                        SplashScreen.admobFull = jSONObject.getString("admob_full");
                                        SplashScreen.admobNative = jSONObject.getString("admob_netive");
                                        SplashScreen.admobReward = jSONObject.getString("admob_reward");
                                        SplashScreen.admobOpen = jSONObject.getString("admob_open");
                                        Log.d("AppOpen", SplashScreen.admobOpen);
                                        SplashScreen.this.appPrefrence.setAmAppopen(jSONObject.getString("admob_open"));
                                        SplashScreen.this.appPrefrence.setAM_ADAPTIVE_BANNER(jSONObject.getString("admob_banner"));
                                        SplashScreen.this.appPrefrence.setAmIntertitial(jSONObject.getString("admob_full"));
                                        SplashScreen.this.appPrefrence.setAmNativeBigHome(jSONObject.getString("admob_netive"));
                                        SplashScreen.this.appPrefrence.setAmRewardedVideo(jSONObject.getString("admob_reward"));
                                    } else {
                                        SplashScreen.admobBanner = jSONObject.getString("adx_banner");
                                        SplashScreen.admobFull = jSONObject.getString("adx_full");
                                        SplashScreen.admobNative = jSONObject.getString("adx_netive");
                                        SplashScreen.admobReward = jSONObject.getString("adx_reward");
                                        SplashScreen.admobOpen = jSONObject.getString("adx_open");
                                        SplashScreen.this.appPrefrence.setAmAppopen(jSONObject.getString("adx_open"));
                                        SplashScreen.this.appPrefrence.setAM_ADAPTIVE_BANNER(jSONObject.getString("adx_banner"));
                                        SplashScreen.this.appPrefrence.setAmIntertitial(jSONObject.getString("adx_full"));
                                        SplashScreen.this.appPrefrence.setAmNativeBigHome(jSONObject.getString("adx_netive"));
                                        SplashScreen.this.appPrefrence.setAmRewardedVideo(jSONObject.getString("adx_reward"));
                                    }
                                    SplashScreen.fbBanner = jSONObject.getString("fb_banner");
                                    SplashScreen.fbFull = jSONObject.getString("fb_full");
                                    SplashScreen.fbNetive = jSONObject.getString("fb_netive");
                                    SplashScreen.fbReward = jSONObject.getString("fb_reward");
                                    SplashScreen.qurekaId = jSONObject.getString("qureka_link");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            new Handler().postDelayed(new Runnable() {


                                public void run() {
                                    SplashScreen.this.nextactivity();
                                }
                            }, 4000);
                        } catch (JSONException unused) {
                            new Handler().postDelayed(new Runnable() {


                                public void run() {
                                    SplashScreen.this.nextactivity();
                                }
                            }, 4000);
                        }
                    }
                }

                @Override
                public void onError(Response<Object> response) {
                    new Handler().postDelayed(new Runnable() {


                        public void run() {
                            SplashScreen.this.nextactivity();
                        }
                    }, 4000);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getWindow().clearFlags(16);
    }


    @Override
    public void onPause() {
        super.onPause();
        getWindow().addFlags(16);
    }


    private void nextactivity() {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }
}
