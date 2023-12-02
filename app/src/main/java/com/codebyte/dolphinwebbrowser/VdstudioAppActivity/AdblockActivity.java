package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.MyApplication;

import dolphinwebbrowser.R;

public class AdblockActivity extends AppCompatActivity {
    private CheckBox adblockCheck;
    private Toolbar adblockTool;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_adblock);
        initView();
        initListener();
    }

    private void initListener() {
        this.adblockTool.setNavigationOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AdblockActivity.this.onBackPressed();
            }
        });
        this.adblockCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.putAdBlock(z);
            }
        });
    }

    private void initView() {
        this.adblockTool = (Toolbar) findViewById(R.id.adblock_tool);
        this.adblockCheck = (CheckBox) findViewById(R.id.adblock_check);
        this.adblockTool.setTitle("Ad Block");
        this.adblockTool.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        if (MyApplication.getAdBlock()) {
            this.adblockCheck.setChecked(true);
        } else {
            this.adblockCheck.setChecked(false);
        }
    }
}
