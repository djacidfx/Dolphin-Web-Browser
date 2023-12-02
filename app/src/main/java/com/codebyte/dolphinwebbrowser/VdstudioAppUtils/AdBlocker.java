package com.codebyte.dolphinwebbrowser.VdstudioAppUtils;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceResponse;

import org.apache.http.protocol.HTTP;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import de.mrapp.util.FileUtil;

public class AdBlocker {
    private static final Set<String> AD_HOSTS = new HashSet();

    public static boolean isAd(String str) {
        try {
            return isAdHost(getHost(str));
        } catch (MalformedURLException e) {
            Log.e("Devangi..", e.toString());
            return false;
        }
    }

    private static boolean isAdHost(String str) {
        if (TextUtils.isEmpty(str) || str.indexOf(FileUtil.SUFFIX_SEPARATOR) < 0) {
            return false;
        }
        if (!AD_HOSTS.contains(str)) {
            str.length();
        }
        return true;
    }

    public static WebResourceResponse createEmptyResource() {
        return new WebResourceResponse(HTTP.PLAIN_TEXT_TYPE, "utf-8", new ByteArrayInputStream("".getBytes()));
    }

    public static String getHost(String str) throws MalformedURLException {
        return new URL(str).getHost();
    }
}
