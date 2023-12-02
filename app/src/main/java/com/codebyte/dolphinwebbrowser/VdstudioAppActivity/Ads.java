package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.content.Context;
import android.content.SharedPreferences;

public class Ads {
    Context mContext;

    public Ads(Context context) {
        this.mContext = context;
    }


    public boolean save_STRING(String str, String str2) {
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences("sp", 0).edit();
        edit.putString(str, str2);
        return edit.commit();
    }

    public String get_STRING(String str, String str2) {
        return this.mContext.getSharedPreferences("sp", 0).getString(str, str2);
    }


}
