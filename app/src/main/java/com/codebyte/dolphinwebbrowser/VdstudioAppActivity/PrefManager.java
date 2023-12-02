package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "status_app";
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public PrefManager(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.pref = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }


    public String getString(String str) {
        if (this.pref.contains(str)) {
            return this.pref.getString(str, null);
        }
        if (str.equals("LANGUAGE_DEFAULT")) {
            return "0";
        }
        if (!str.equals("ORDER_DEFAULT_IMAGE") && !str.equals("ORDER_DEFAULT_GIF") && !str.equals("ORDER_DEFAULT_VIDEO") && !str.equals("ORDER_DEFAULT_JOKE") && !str.equals("ORDER_DEFAULT_STATUS")) {
            return "";
        }
        return "created";
    }

}
