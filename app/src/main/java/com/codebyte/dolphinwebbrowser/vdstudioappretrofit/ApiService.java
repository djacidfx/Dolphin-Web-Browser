package com.codebyte.dolphinwebbrowser.vdstudioappretrofit;

import com.google.gson.JsonElement;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    public static final String enterurl = "feeds/?query=Entertainment";
    public static final String moviesurl = "feeds/?query=Movies";
    public static final String newsurl = "feeds/?query=Latest%20News";
    public static final String sciurl = "feeds/?query=Sci%20and%20Tech";
    public static final String search_url = "search?output=toolbar&";
    public static final String sporturl = "feeds/?query=Sport";
    public static final String worldurl = "feeds/?query=World%20News";

    @GET(enterurl)
    Call<JsonElement> getEnterUrl();

    @GET(moviesurl)
    Call<JsonElement> getMoviesUrl();

    @GET(newsurl)
    Call<JsonElement> getNewsUrl();

    @GET(search_url)
    Call<ResponseBody> getSearchUrl(@Query("q") String str);

    @GET(sporturl)
    Call<JsonElement> getSportUrl();

    @GET(sciurl)
    Call<JsonElement> getsciurl();

    @GET(worldurl)
    Call<JsonElement> getworldurl();
}
