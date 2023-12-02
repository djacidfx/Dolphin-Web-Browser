package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dolphinwebbrowser.R;

public class AboutActivity extends AppCompatActivity {
    private Toolbar aboutTool;
    private TextView appName;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_about);
        initView();
        initListener();
    }

    private void initListener() {
        this.aboutTool.setNavigationOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AboutActivity.this.onBackPressed();
            }
        });
    }

    private void initView() {
        this.aboutTool = (Toolbar) findViewById(R.id.about_tool);
        this.appName = (TextView) findViewById(R.id.app_name);
        this.aboutTool.setTitle("About");
        this.aboutTool.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        TextView textView = this.appName;
        textView.setText(getResources().getString(R.string.app_name) + " (" + "" + ")");
    }
}
