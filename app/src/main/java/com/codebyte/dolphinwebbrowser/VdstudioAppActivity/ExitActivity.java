package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dolphinwebbrowser.R;

public class ExitActivity extends BaseActivity {
    Button btncancel;
    Button btnexit;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_exit);
        this.btnexit = (Button) findViewById(R.id.btnexit);
        Button button = (Button) findViewById(R.id.btncancel);
        this.btncancel = button;
        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                ExitActivity.this.onBackPressed();
            }
        });
        this.btnexit.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                StartActivity.startActivityone.finish();
                SplashScreen.splashScreen.finish();
                ExitActivity.this.finish();
                System.exit(0);
            }
        });
    }
}
