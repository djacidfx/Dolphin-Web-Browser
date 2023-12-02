package com.codebyte.dolphinwebbrowser.VdstudioAppFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppAdapter.HomeNewsAdapter;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.HomeNewsData;
import com.codebyte.dolphinwebbrowser.vdstudioappretrofit.ApiService;
import com.codebyte.dolphinwebbrowser.vdstudioappretrofit.RestApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import dolphinwebbrowser.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {
    ApiService apiService;
    ArrayList<HomeNewsData> homeNewsData = new ArrayList<>();
    ProgressBar progress;
    RecyclerView rvNewsItems;
    int url;
    View view;

    public MoviesFragment(int i) {
        this.url = i;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_movies, viewGroup, false);
        this.apiService = (ApiService) RestApi.getClient1().create(ApiService.class);
        initView(this.view);
        fetch_data(this.url);
        return this.view;
    }

    private void fetch_data(int i) {
        Call<JsonElement> call;
        this.progress.setVisibility(View.VISIBLE);
        if (i == 0) {
            call = this.apiService.getMoviesUrl();
        } else if (i == 1) {
            call = this.apiService.getSportUrl();
        } else if (i == 2) {
            call = this.apiService.getEnterUrl();
        } else if (i == 3) {
            call = this.apiService.getsciurl();
        } else {
            call = this.apiService.getworldurl();
        }
        call.enqueue(new Callback<JsonElement>() {


            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                MoviesFragment.this.progress.setVisibility(View.GONE);
                try {
                    MoviesFragment.this.homeNewsData.clear();
                    Log.e("STRING", response.body() + "::");
                    JsonArray asJsonArray = response.body().getAsJsonObject().getAsJsonArray("results");
                    for (int i = 0; i < asJsonArray.size(); i++) {
                        JsonObject asJsonObject = asJsonArray.get(i).getAsJsonObject();
                        long j = -1;
                        String str = "";
                        String asString = asJsonObject.has("title") ? asJsonObject.get("title").getAsString() : str;
                        String asString2 = asJsonObject.has("websiteTitle") ? asJsonObject.get("websiteTitle").getAsString() : str;
                        String asString3 = asJsonObject.has("website") ? asJsonObject.get("website").getAsString() : str;
                        String asString4 = asJsonObject.has("iconUrl") ? asJsonObject.get("iconUrl").getAsString() : str;
                        String asString5 = asJsonObject.has("coverUrl") ? asJsonObject.get("coverUrl").getAsString() : str;
                        String asString6 = asJsonObject.has("description") ? asJsonObject.get("description").getAsString() : str;
                        if (asJsonObject.has("visualUrl")) {
                            str = asJsonObject.get("visualUrl").getAsString();
                        }
                        if (asJsonObject.has("lastUpdated")) {
                            j = asJsonObject.get("lastUpdated").getAsLong();
                        }
                        MoviesFragment.this.homeNewsData.add(new HomeNewsData(asString, asString2, asString3, asString4, asString5, asString6, str, j));
                    }
                    MoviesFragment.this.rvNewsItems.setLayoutManager(new LinearLayoutManager(MoviesFragment.this.getContext()));
                    MoviesFragment.this.rvNewsItems.setAdapter(new HomeNewsAdapter(MoviesFragment.this.getContext(), MoviesFragment.this.homeNewsData, new HomeNewsAdapter.click_item() {


                        @Override
                        public void OnCLickitem(HomeNewsData homeNewsData) {
                            Intent intent = new Intent();
                            intent.putExtra("clickData", homeNewsData);
                            MoviesFragment.this.getActivity().setResult(-1, intent);
                            MoviesFragment.this.getActivity().onBackPressed();
                        }
                    }));
                } catch (Exception e) {
                    MoviesFragment.this.progress.setVisibility(View.GONE);
                    Log.e("OSJKSGDGHS2222", e.getMessage() + "::");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable th) {
                MoviesFragment.this.progress.setVisibility(View.GONE);
                Log.e("OSJKSGDGHSs111", th.getMessage() + "::");
            }
        });
    }

    private void initView(View view2) {
        this.rvNewsItems = (RecyclerView) view2.findViewById(R.id.rv_news_items);
        this.progress = (ProgressBar) view2.findViewById(R.id.progress);
    }
}
