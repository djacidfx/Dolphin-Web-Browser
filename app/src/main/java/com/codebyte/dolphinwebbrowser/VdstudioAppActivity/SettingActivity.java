package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dolphinwebbrowser.R;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView about;
    private TextView adBlock;
    private TextView advance;
    private TextView bookmark;
    private TextView display;
    private TextView general;
    private Toolbar settingTool;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_setting);
        initView();
        initListener();
    }

    private void initListener() {
        this.settingTool.setNavigationOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                SettingActivity.this.onBackPressed();
            }
        });
        this.adBlock.setOnClickListener(this);
        this.general.setOnClickListener(this);
        this.bookmark.setOnClickListener(this);
        this.display.setOnClickListener(this);
        this.advance.setOnClickListener(this);
        this.about.setOnClickListener(this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_tool);
        this.settingTool = toolbar;
        toolbar.setTitle("Settings");
        this.settingTool.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        this.adBlock = (TextView) findViewById(R.id.ad_block);
        this.general = (TextView) findViewById(R.id.general);
        this.bookmark = (TextView) findViewById(R.id.bookmark);
        this.display = (TextView) findViewById(R.id.display);
        this.advance = (TextView) findViewById(R.id.advance);
        this.about = (TextView) findViewById(R.id.about);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about:
                Adshandler.ShowIntertialads(this, AboutActivity.class);
                return;
            case R.id.ad_block:
                startActivity(new Intent(this, AdblockActivity.class));
                return;
            case R.id.advance:
                Adshandler.ShowIntertialads(this, AdavanceActivity.class);
                return;
            case R.id.bookmark:
                Adshandler.ShowIntertialads(this, DisplaySettings.class);
                return;
            case R.id.display:
                startActivity(new Intent(this, DisplaySettings.class));
                return;
            case R.id.general:
                Adshandler.ShowIntertialads(this, GeneralActivity.class);
                return;
            default:
                return;
        }
    }
}
