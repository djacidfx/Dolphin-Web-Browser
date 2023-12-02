package com.codebyte.dolphinwebbrowser.VdstudioAppAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppActivity.DownloadActivity;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.FileData;

import java.util.ArrayList;
import java.util.Calendar;

import dolphinwebbrowser.R;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    DownloadActivity activity;
    ArrayList<FileData> fileArrayList;
    LayoutInflater inflater;

    public DownloadAdapter(DownloadActivity download_Activity, ArrayList<FileData> arrayList) {
        this.activity = download_Activity;
        this.fileArrayList = arrayList;
        this.inflater = LayoutInflater.from(download_Activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.item_download, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.download_name.setText(this.fileArrayList.get(viewHolder.getAdapterPosition()).getName());
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(Long.parseLong(this.fileArrayList.get(viewHolder.getAdapterPosition()).getModified()));
        if (instance.get(9) == 1) {
            if (instance.get(10) == 0) {
                TextView textView = viewHolder.download_date;
                textView.setText(String.valueOf(instance.get(5) + "/" + (instance.get(2) + 1) + "/" + instance.get(1) + " 12:" + instance.get(12) + " PM"));
                return;
            }
            TextView textView2 = viewHolder.download_date;
            textView2.setText(String.valueOf(instance.get(5) + "/" + (instance.get(2) + 1) + "/" + instance.get(1) + " " + instance.get(10) + ":" + instance.get(12) + " PM"));
        } else if (instance.get(10) == 0) {
            TextView textView3 = viewHolder.download_date;
            textView3.setText(String.valueOf(instance.get(5) + "/" + (instance.get(2) + 1) + "/" + instance.get(1) + " 12:" + instance.get(12) + " AM"));
        } else {
            TextView textView4 = viewHolder.download_date;
            textView4.setText(String.valueOf(instance.get(5) + "/" + (instance.get(2) + 1) + "/" + instance.get(1) + " " + instance.get(10) + ":" + instance.get(12) + " AM"));
        }
    }

    @Override
    public int getItemCount() {
        return this.fileArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView download_date;
        ImageView download_image;
        TextView download_name;

        public ViewHolder(View view) {
            super(view);
            this.download_image = (ImageView) view.findViewById(R.id.download_image);
            this.download_name = (TextView) view.findViewById(R.id.download_name);
            this.download_date = (TextView) view.findViewById(R.id.download_date);
        }
    }
}
