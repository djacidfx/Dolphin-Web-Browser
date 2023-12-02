package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;

import dolphinwebbrowser.R;


public class StartActivity extends BaseActivity {
    public static StartActivity startActivityone;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);

        Adshandler.loadad(this);

        FrameLayout nativeads = findViewById(R.id.nativeads);
        Adshandler.refreshAd(nativeads, this);

        startActivityone = this;
        SplashScreen.splashScreen.finish();
        new Ads(getApplicationContext()).save_STRING(Constant.typeAds, Constant.adsFb);


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Adshandler.showAd(StartActivity.this, new Adshandler.OnClose() {
                    @Override
                    public void onclick() {
                        startActivity(new Intent(StartActivity.this, BrowserActivity.class));
                    }
                });
            }
        });
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/palin");
                intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + StartActivity.this.getPackageName());
                StartActivity.this.startActivity(Intent.createChooser(intent, "Share link"));
            }
        });
        findViewById(R.id.rate).setOnClickListener(new View.OnClickListener() {


            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + StartActivity.this.getPackageName()));
                intent.addFlags(1208483840);
                try {
                    StartActivity.this.startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                    StartActivity start_Activity = StartActivity.this;
                    start_Activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + StartActivity.this.getPackageName())));
                }
            }
        });
        findViewById(R.id.policy).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.policy_url))));
            }


        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        showRateUsDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void showRateUsDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
        FrameLayout nativeads = dialog.findViewById(R.id.nativeads);
        Adshandler.refreshAd(nativeads, StartActivity.this);
        LinearLayout linearLayout = dialog.findViewById(R.id.dia_close);
        linearLayout.setOnClickListener(view -> dialog.cancel());
        CardView cardView = dialog.findViewById(R.id.cancle_btn);
        cardView.setOnClickListener(view -> {
            finishAffinity();
            dialog.dismiss();
        });
        dialog.show();
    }


}
