package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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


public class GeneralActivity extends AppCompatActivity {
    public TextView homePageSelect;
    public TextView searchEngineSelect;
    public String temp;
    public TextView userAgentSelect;
    private CheckBox blockImage;
    private LinearLayout downloadLoacation;
    private TextView downloadLoacationSelect;
    private CheckBox enableJava;
    private String finalPath;
    private Toolbar generalTool;
    private LinearLayout homePage;
    private CheckBox requestData;
    private LinearLayout searchEngine;
    private LinearLayout userAgent;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_general);
        initView();
        initListener();
    }

    private void initListener() {
        this.generalTool.setNavigationOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                GeneralActivity.this.onBackPressed();
            }
        });
        this.blockImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.putBlockImage(z);
                if (z) {
                    BrowserActivity.web_view.getSettings().setLoadsImagesAutomatically(false);
                } else {
                    BrowserActivity.web_view.getSettings().setLoadsImagesAutomatically(true);
                }
            }
        });
        this.requestData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.putRequestData(z);
            }
        });
        this.enableJava.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                MyApplication.putEnableJava(z);
                if (z) {
                    BrowserActivity.web_view.getSettings().setJavaScriptEnabled(true);
                } else {
                    BrowserActivity.web_view.getSettings().setJavaScriptEnabled(false);
                }
            }
        });
        this.userAgent.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("ResourceType")
            public void onClick(View view) {
                final Dialog dialog = new Dialog(GeneralActivity.this, R.style.WideDialog);
                dialog.setContentView(R.layout.dialog_useragent);
                dialog.getWindow().setBackgroundDrawableResource(17170445);
                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
                Button button = (Button) dialog.findViewById(R.id.ok);
                String userAgent = MyApplication.getUserAgent();
                int hashCode = userAgent.hashCode();
                if (hashCode != -1984987966) {
                    if (hashCode != -1085510111) {
                        if (hashCode == -1073207300 && userAgent.equals("Desktop")) {
                            GeneralActivity.this.temp = "Default";
                            radioGroup.check(R.id.r_default);
                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    GeneralActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                                }
                            });
                            button.setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    MyApplication.putUserAgent(GeneralActivity.this.temp);
                                    GeneralActivity.this.userAgentSelect.setText(GeneralActivity.this.temp);
                                    String str = GeneralActivity.this.temp;
                                    int hashCode = str.hashCode();
                                    if (hashCode != -1984987966) {
                                        if (hashCode != -1085510111) {
                                            if (hashCode == -1073207300 && str.equals("Desktop")) {
                                                BrowserActivity.web_view.getSettings().setUserAgentString("Android");
                                                dialog.dismiss();
                                            }
                                        } else if (str.equals("Default")) {
                                            dialog.dismiss();
                                        }
                                    } else if (str.equals("Mobile")) {
                                        dialog.dismiss();
                                    }
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else if (userAgent.equals("Default")) {
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                GeneralActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                            }
                        });
                        button.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                MyApplication.putUserAgent(GeneralActivity.this.temp);
                                GeneralActivity.this.userAgentSelect.setText(GeneralActivity.this.temp);
                                String str = GeneralActivity.this.temp;
                                int hashCode = str.hashCode();
                                if (hashCode != -1984987966) {
                                    if (hashCode != -1085510111) {
                                        if (hashCode == -1073207300 && str.equals("Desktop")) {
                                            BrowserActivity.web_view.getSettings().setUserAgentString("Android");
                                            dialog.dismiss();
                                        }
                                    } else if (str.equals("Default")) {
                                        dialog.dismiss();
                                    }
                                } else if (str.equals("Mobile")) {
                                    dialog.dismiss();
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                } else if (userAgent.equals("Mobile")) {
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            GeneralActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                        }
                    });
                    button.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            MyApplication.putUserAgent(GeneralActivity.this.temp);
                            GeneralActivity.this.userAgentSelect.setText(GeneralActivity.this.temp);
                            String str = GeneralActivity.this.temp;
                            int hashCode = str.hashCode();
                            if (hashCode != -1984987966) {
                                if (hashCode != -1085510111) {
                                    if (hashCode == -1073207300 && str.equals("Desktop")) {
                                        BrowserActivity.web_view.getSettings().setUserAgentString("Android");
                                        dialog.dismiss();
                                    }
                                } else if (str.equals("Default")) {
                                    dialog.dismiss();
                                }
                            } else if (str.equals("Mobile")) {
                                dialog.dismiss();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        GeneralActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        MyApplication.putUserAgent(GeneralActivity.this.temp);
                        GeneralActivity.this.userAgentSelect.setText(GeneralActivity.this.temp);
                        String str = GeneralActivity.this.temp;
                        int hashCode = str.hashCode();
                        if (hashCode != -1984987966) {
                            if (hashCode != -1085510111) {
                                if (hashCode == -1073207300 && str.equals("Desktop")) {
                                    BrowserActivity.web_view.getSettings().setUserAgentString("Android");
                                    dialog.dismiss();
                                }
                            } else if (str.equals("Default")) {
                                dialog.dismiss();
                            }
                        } else if (str.equals("Mobile")) {
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        this.downloadLoacation.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.addCategory("android.intent.category.DEFAULT");
                GeneralActivity.this.startActivityForResult(Intent.createChooser(intent, "Choose directory"), 100);
            }
        });
        this.homePage.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("ResourceType")
            public void onClick(View view) {
                char c;
                final Dialog dialog = new Dialog(GeneralActivity.this, R.style.WideDialog);
                dialog.setContentView(R.layout.dialog_homepage);
                dialog.getWindow().setBackgroundDrawableResource(17170445);
                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
                Button button = (Button) dialog.findViewById(R.id.ok);
                String homePage = MyApplication.getHomePage();
                switch (homePage.hashCode()) {
                    case -1406075965:
                        if (homePage.equals("Webpage")) {
                            c = 2;
                            break;
                        }
                    case -1085510111:
                        if (homePage.equals("Default")) {
                            c = 0;
                            break;
                        }
                    case -253812259:
                        if (homePage.equals("Bookmarks")) {
                            c = 3;
                            break;
                        }
                    case 64266548:
                        if (homePage.equals("Blank")) {
                            c = 1;
                            break;
                        }
                    default:
                        c = 65535;
                        break;
                }
                if (c == 0) {
                    GeneralActivity.this.temp = "Default";
                    radioGroup.check(R.id.r_default);
                } else if (c == 1) {
                    GeneralActivity.this.temp = "Blank";
                    radioGroup.check(R.id.r_blank);
                } else if (c == 2) {
                    GeneralActivity.this.temp = "Webpage";
                    radioGroup.check(R.id.r_webpage);
                } else if (c == 3) {
                    GeneralActivity.this.temp = "Bookmarks";
                    radioGroup.check(R.id.r_bookmark);
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        GeneralActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        MyApplication.putHomePage(GeneralActivity.this.temp);
                        GeneralActivity.this.homePageSelect.setText(GeneralActivity.this.temp);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        this.searchEngine.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("ResourceType")
            public void onClick(View view) {
                char c;
                final Dialog dialog = new Dialog(GeneralActivity.this, R.style.WideDialog);
                dialog.setContentView(R.layout.dialog_serachengine);
                dialog.getWindow().setBackgroundDrawableResource(17170445);
                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
                Button button = (Button) dialog.findViewById(R.id.ok);
                String searchEngine = MyApplication.getSearchEngine();
                switch (searchEngine.hashCode()) {
                    case 66137:
                        if (searchEngine.equals("Ask")) {
                            c = 3;
                            break;
                        }
                    case 2070624:
                        if (searchEngine.equals("Bing")) {
                            c = 1;
                            break;
                        }
                    case 85186592:
                        if (searchEngine.equals("Yahoo")) {
                            c = 2;
                            break;
                        }
                    case 2138589785:
                        if (searchEngine.equals("Google")) {
                            c = 0;
                            break;
                        }
                    default:
                        c = 65535;
                        break;
                }
                if (c == 0) {
                    GeneralActivity.this.temp = "Google";
                    radioGroup.check(R.id.r_google);
                } else if (c == 1) {
                    GeneralActivity.this.temp = "Bing";
                    radioGroup.check(R.id.r_bing);
                } else if (c == 2) {
                    GeneralActivity.this.temp = "Yahoo";
                    radioGroup.check(R.id.r_yahoo);
                } else if (c == 3) {
                    GeneralActivity.this.temp = "Ask";
                    radioGroup.check(R.id.r_ask);
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        GeneralActivity.this.temp = ((RadioButton) dialog.findViewById(i)).getText().toString();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        MyApplication.putSearchEngine(GeneralActivity.this.temp);
                        GeneralActivity.this.searchEngineSelect.setText(GeneralActivity.this.temp);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void initView() {
        this.generalTool = (Toolbar) findViewById(R.id.general_tool);
        this.blockImage = (CheckBox) findViewById(R.id.block_image);
        this.requestData = (CheckBox) findViewById(R.id.request_data);
        this.enableJava = (CheckBox) findViewById(R.id.enable_java);
        this.userAgent = (LinearLayout) findViewById(R.id.user_agent);
        this.userAgentSelect = (TextView) findViewById(R.id.user_agent_select);
        this.homePage = (LinearLayout) findViewById(R.id.home_page);
        this.downloadLoacation = (LinearLayout) findViewById(R.id.download_loacation);
        this.downloadLoacationSelect = (TextView) findViewById(R.id.download_loacation_select);
        this.homePageSelect = (TextView) findViewById(R.id.home_page_select);
        this.searchEngine = (LinearLayout) findViewById(R.id.search_engine);
        this.searchEngineSelect = (TextView) findViewById(R.id.search_engine_select);
        this.generalTool.setTitle("General Settings");
        this.generalTool.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        this.blockImage.setChecked(MyApplication.getBlockImage());
        this.enableJava.setChecked(MyApplication.getEnableJava());
        this.requestData.setChecked(MyApplication.getRequestData());
        this.userAgentSelect.setText(MyApplication.getUserAgent());
        this.downloadLoacationSelect.setText(MyApplication.getDownloadPath());
        this.homePageSelect.setText(MyApplication.getHomePage());
        this.searchEngineSelect.setText(MyApplication.getSearchEngine());
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            String[] split = intent.getData().toString().split("%3A");
            if (split.length <= 1) {
                this.finalPath = "";
            } else if (split[1].contains("%2F")) {
                String replace = split[1].replace("%2F", "/");
                if (replace.contains("%20")) {
                    this.finalPath = replace.replace("%20", " ");
                } else {
                    this.finalPath = replace;
                }
                Log.d("data", "onActivityResult: ====>" + this.finalPath);
            } else {
                this.finalPath = split[1];
            }
            MyApplication.putDownload(Environment.DIRECTORY_DOWNLOADS);
            this.downloadLoacationSelect.setText(MyApplication.getDownloadPath());
        }
    }
}
