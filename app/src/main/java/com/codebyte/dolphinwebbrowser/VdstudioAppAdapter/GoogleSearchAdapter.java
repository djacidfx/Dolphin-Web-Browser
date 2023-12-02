package com.codebyte.dolphinwebbrowser.VdstudioAppAdapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dolphinwebbrowser.R;

public class GoogleSearchAdapter extends RecyclerView.Adapter<GoogleSearchAdapter.ViewHolder> {
    public static ClickListener listener;
    FragmentActivity activity;
    LayoutInflater inflater;
    ArrayList<String> searchHistry;

    public GoogleSearchAdapter(FragmentActivity fragmentActivity, ArrayList<String> arrayList) {
        this.activity = fragmentActivity;
        this.searchHistry = arrayList;
        this.inflater = LayoutInflater.from(fragmentActivity);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        listener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.item_google_search, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.search_url.setText(this.searchHistry.get(i));
        viewHolder.iv_arror.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                GoogleSearchAdapter.listener.onItemClick(i, view, 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.searchHistry.size();
    }

    public interface ClickListener {
        void onItemClick(int i, View view, int i2);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_arror;
        TextView search_url;

        public ViewHolder(View view) {
            super(view);
            this.search_url = (TextView) view.findViewById(R.id.search_url);
            this.iv_arror = (ImageView) view.findViewById(R.id.iv_arror);

            view.setOnClickListener(view1 -> GoogleSearchAdapter.listener.onItemClick(ViewHolder.this.getAdapterPosition(), view1, 1));
        }
    }
}
