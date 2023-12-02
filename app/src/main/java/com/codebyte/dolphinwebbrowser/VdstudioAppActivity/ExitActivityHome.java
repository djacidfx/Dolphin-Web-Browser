package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dolphinwebbrowser.R;

public class ExitActivityHome extends BaseActivity {
    Button btncancel;
    Button btnexit;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_exithome);


        this.btnexit = (Button) findViewById(R.id.btnexit);
        Button button = (Button) findViewById(R.id.btncancel);
        this.btncancel = button;
        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                ExitActivityHome.this.finish();
            }
        });
        this.btnexit.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                BrowserActivity.browser_activity.finish();
                ExitActivityHome.this.onBackPressed();
            }
        });
    }
}
