package com.codebyte.dolphinwebbrowser.VdstudioAppAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppActivity.BrowserActivity;
import com.codebyte.dolphinwebbrowser.VdstudioAppActivity.IncognitoActivity;

import java.util.ArrayList;

import dolphinwebbrowser.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    FragmentActivity activity;
    LayoutInflater inflater;
    ArrayList<String> searchHistry;
    int typeMode;

    public SearchAdapter(FragmentActivity fragmentActivity, ArrayList<String> arrayList, int i) {
        this.activity = fragmentActivity;
        this.searchHistry = arrayList;
        this.typeMode = i;
        this.inflater = LayoutInflater.from(fragmentActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.item_search, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.search_url.setText(this.searchHistry.get(i));
    }

    @Override
    public int getItemCount() {
        return this.searchHistry.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView search_url;

        public ViewHolder(View view) {
            super(view);
            this.search_url = (TextView) view.findViewById(R.id.search_url);

            view.setOnClickListener(view1 -> {
                if (SearchAdapter.this.typeMode == 0) {
                    BrowserActivity.web_view.loadUrl(SearchAdapter.this.searchHistry.get(ViewHolder.this.getAdapterPosition()));
                    SearchAdapter.this.activity.onBackPressed();
                    return;
                }
                IncognitoActivity.webView.loadUrl(SearchAdapter.this.searchHistry.get(ViewHolder.this.getAdapterPosition()));
                SearchAdapter.this.activity.onBackPressed();
            });
        }
    }
}
