package com.codebyte.dolphinwebbrowser.VdstudioAppModel;

import java.io.Serializable;

public class HomeNewsData implements Serializable {
    String coverUrl;
    String description;
    String iconUrl;
    long lastUpdated;
    String title;
    String visualUrl;
    String website;
    String websiteTitle;

    public HomeNewsData(String str, String str2, String str3, String str4, String str5, String str6, String str7, long j) {
        this.title = str;
        this.websiteTitle = str2;
        this.website = str3;
        this.iconUrl = str4;
        this.coverUrl = str5;
        this.description = str6;
        this.visualUrl = str7;
        this.lastUpdated = j;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getWebsiteTitle() {
        return this.websiteTitle;
    }

    public String getWebsite() {
        return this.website;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public String getVisualUrl() {
        return this.visualUrl;
    }

    public long getLastUpdated() {
        return this.lastUpdated;
    }
}
