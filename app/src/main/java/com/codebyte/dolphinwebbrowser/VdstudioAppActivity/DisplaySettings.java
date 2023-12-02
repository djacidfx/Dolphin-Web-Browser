package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.MyApplication;

import dolphinwebbrowser.R;

public class DisplaySettings extends AppCompatActivity {
    public int temp;
    private CheckBox blackStatus;
    private Toolbar displayTool;
    private CheckBox fullScreen;
    private CheckBox hideStatus;
    private TextView textSize;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_display_settings);
        initView();
        initListener();
    }

    private void initListener() {
        this.displayTool.setNavigationOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                DisplaySettings.this.onBackPressed();
            }
        });
        this.hideStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.putHideStatus(z);
            }
        });
        this.fullScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.putFullScreen(z);
            }
        });
        this.blackStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.putBlackStatus(z);
            }
        });
        this.textSize.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("ResourceType")
            public void onClick(View view) {
                final Dialog dialog = new Dialog(DisplaySettings.this, R.style.WideDialog);
                dialog.setContentView(R.layout.dialog_textsize);
                dialog.getWindow().setBackgroundDrawableResource(17170445);
                SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.size_seekbar);
                final TextView textView = (TextView) dialog.findViewById(R.id.check);
                textView.setTextSize((float) Integer.parseInt(MyApplication.getTextSize()));
                DisplaySettings.this.temp = Integer.parseInt(MyApplication.getTextSize());
                seekBar.setProgress(DisplaySettings.this.temp);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                        textView.setTextSize((float) i);
                        DisplaySettings.this.temp = i;
                    }
                });
                ((Button) dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        MyApplication.putTextSize(String.valueOf(DisplaySettings.this.temp));
                        BrowserActivity.web_view.getSettings().setDefaultFontSize(DisplaySettings.this.temp);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.display_tool);
        this.displayTool = toolbar;
        toolbar.setTitle("Display Settings");
        this.displayTool.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        this.hideStatus = (CheckBox) findViewById(R.id.hide_status);
        this.fullScreen = (CheckBox) findViewById(R.id.full_screen);
        this.blackStatus = (CheckBox) findViewById(R.id.black_status);
        this.textSize = (TextView) findViewById(R.id.text_size);
        this.hideStatus.setChecked(MyApplication.getHideStatus());
        this.fullScreen.setChecked(MyApplication.getFullScreen());
        this.blackStatus.setChecked(MyApplication.getBlackStatus());
    }
}
