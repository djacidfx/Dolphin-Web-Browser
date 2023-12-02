package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.MyApplication;

import dolphinwebbrowser.R;


public class AdavanceActivity extends AppCompatActivity {
    public String temp;
    public TextView textEncodingSelect;
    public TextView urlBoxSelect;
    private Toolbar advanceTool;
    private CheckBox allowSite;
    private CheckBox cookies;
    private LinearLayout textEncoding;
    private LinearLayout urlBox;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_adavance);
        initView();
        initListener();
    }

    private void initListener() {
        this.allowSite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.putAllowSite(z);
            }
        });
        this.cookies.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.putCookies(z);
            }
        });
        this.advanceTool.setNavigationOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                AdavanceActivity.this.onBackPressed();
            }
        });
        this.textEncoding.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("ResourceType")
            public void onClick(View view) {
                char c;
                final Dialog dialog = new Dialog(AdavanceActivity.this, R.style.WideDialog);
                dialog.setContentView(R.layout.dialog_encoding);
                dialog.getWindow().setBackgroundDrawableResource(17170445);
                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
                Button button = (Button) dialog.findViewById(R.id.ok);
                String textEncoding = MyApplication.getTextEncoding();
                switch (textEncoding.hashCode()) {
                    case 70352:
                        if (textEncoding.equals("GBK")) {
                            c = 2;
                            break;
                        }
                    case 2070357:
                        if (textEncoding.equals("Big5")) {
                            c = 3;
                            break;
                        }
                    case 81070450:
                        if (textEncoding.equals("UTF-8")) {
                            c = 1;
                            break;
                        }
                    case 257295942:
                        if (textEncoding.equals("SHIFT_JS")) {
                            c = 5;
                            break;
                        }
                    case 1450311437:
                        if (textEncoding.equals("ISO-2022-JP")) {
                            c = 4;
                            break;
                        }
                    case 2027158704:
                        if (textEncoding.equals("ISO-8859-1")) {
                            c = 0;
                            break;
                        }
                    case 2055952320:
                        if (textEncoding.equals("EUC-JP")) {
                            c = 6;
                            break;
                        }
                    case 2055952353:
                        if (textEncoding.equals("EUC-KR")) {
                            c = 7;
                            break;
                        }
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        AdavanceActivity.this.temp = "ISO-8859-1";
                        radioGroup.check(R.id.r_iso);
                        break;
                    case 1:
                        AdavanceActivity.this.temp = "UTF-8";
                        radioGroup.check(R.id.r_utf);
                        break;
                    case 2:
                        AdavanceActivity.this.temp = "GBK";
                        radioGroup.check(R.id.r_gbk);
                        break;
                    case 3:
                        AdavanceActivity.this.temp = "Big5";
                        radioGroup.check(R.id.r_big);
                        break;
                    case 4:
                        AdavanceActivity.this.temp = "ISO-2022-JP";
                        radioGroup.check(R.id.r_iso2);
                        break;
                    case 5:
                        AdavanceActivity.this.temp = "SHIFT_JS";
                        radioGroup.check(R.id.r_shift);
                        break;
                    case 6:
                        AdavanceActivity.this.temp = "EUC-JP";
                        radioGroup.check(R.id.r_euc);
                        break;
                    case 7:
                        AdavanceActivity.this.temp = "EUC-KR";
                        radioGroup.check(R.id.r_euc_hr);
                        break;
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        AdavanceActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        char c;
                        MyApplication.putTextEncoding(AdavanceActivity.this.temp);
                        AdavanceActivity.this.textEncodingSelect.setText(AdavanceActivity.this.temp);
                        String str = AdavanceActivity.this.temp;
                        switch (str.hashCode()) {
                            case 70352:
                                if (str.equals("GBK")) {
                                    c = 2;
                                    break;
                                }
                            case 2070357:
                                if (str.equals("Big5")) {
                                    c = 3;
                                    break;
                                }
                            case 81070450:
                                if (str.equals("UTF-8")) {
                                    c = 1;
                                    break;
                                }
                            case 257295942:
                                if (str.equals("SHIFT_JS")) {
                                    c = 5;
                                    break;
                                }
                            case 1450311437:
                                if (str.equals("ISO-2022-JP")) {
                                    c = 4;
                                    break;
                                }
                            case 2027158704:
                                if (str.equals("ISO-8859-1")) {
                                    c = 0;
                                    break;
                                }
                            case 2055952320:
                                if (str.equals("EUC-JP")) {
                                    c = 6;
                                    break;
                                }
                            case 2055952353:
                                if (str.equals("EUC-KR")) {
                                    c = 7;
                                    break;
                                }
                            default:
                                c = 65535;
                                break;
                        }
                        switch (c) {
                            case 0:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("ISO-8859-1");
                                break;
                            case 1:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("UTF-8");
                                break;
                            case 2:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("GBK");
                                break;
                            case 3:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("Big5");
                                break;
                            case 4:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("ISO-2022-JP");
                                break;
                            case 5:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("SHIFT_JS");
                                break;
                            case 6:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("EUC-JP");
                                break;
                            case 7:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("EUC-KR");
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        this.urlBox.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("ResourceType")
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AdavanceActivity.this, R.style.WideDialog);
                dialog.setContentView(R.layout.dialog_url_box);
                dialog.getWindow().setBackgroundDrawableResource(17170445);
                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
                Button button = (Button) dialog.findViewById(R.id.ok);
                String urlBox = MyApplication.getUrlBox();
                int hashCode = urlBox.hashCode();
                if (hashCode != 84303) {
                    if (hashCode != 80818744) {
                        if (hashCode == 1751015924 && urlBox.equals("Domain (default)")) {
                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    AdavanceActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                                }
                            });
                            button.setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    MyApplication.putUrlBox(AdavanceActivity.this.temp);
                                    AdavanceActivity.this.urlBoxSelect.setText(AdavanceActivity.this.temp);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else if (urlBox.equals("Title")) {
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                AdavanceActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                            }
                        });
                        button.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                MyApplication.putUrlBox(AdavanceActivity.this.temp);
                                AdavanceActivity.this.urlBoxSelect.setText(AdavanceActivity.this.temp);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                } else if (urlBox.equals("URL")) {
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            AdavanceActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                        }
                    });
                    button.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            MyApplication.putUrlBox(AdavanceActivity.this.temp);
                            AdavanceActivity.this.urlBoxSelect.setText(AdavanceActivity.this.temp);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        AdavanceActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        MyApplication.putUrlBox(AdavanceActivity.this.temp);
                        AdavanceActivity.this.urlBoxSelect.setText(AdavanceActivity.this.temp);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.advance_tool);
        this.advanceTool = toolbar;
        toolbar.setTitle("Advance Settings");
        this.advanceTool.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        this.allowSite = (CheckBox) findViewById(R.id.allow_site);
        this.cookies = (CheckBox) findViewById(R.id.cookies);
        this.textEncoding = (LinearLayout) findViewById(R.id.text_encoding);
        this.textEncodingSelect = (TextView) findViewById(R.id.text_encoding_select);
        this.urlBox = (LinearLayout) findViewById(R.id.url_box);
        this.urlBoxSelect = (TextView) findViewById(R.id.url_box_select);
        this.allowSite.setChecked(MyApplication.getAllowSite());
        this.cookies.setChecked(MyApplication.getCookies());
        this.textEncodingSelect.setText(MyApplication.getTextEncoding());
        this.urlBoxSelect.setText(MyApplication.getUrlBox());
    }
}
