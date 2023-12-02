package com.codebyte.dolphinwebbrowser.VdstudioAppAdapter;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codebyte.dolphinwebbrowser.VdstudioAppDatabase.Database;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.HomeNewsData;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.Calendar;

import dolphinwebbrowser.R;

public class HomeNewsAdapter extends RecyclerView.Adapter<HomeNewsAdapter.ViewHolder> {
    private final Database database;
    Context context;
    ArrayList<HomeNewsData> homeNewsData = new ArrayList<>();
    LayoutInflater inflater;
    click_item itemClick;

    public HomeNewsAdapter(Context context2, ArrayList<HomeNewsData> arrayList, click_item click_item2) {
        this.homeNewsData = arrayList;
        this.context = context2;
        this.database = new Database(context2);
        this.itemClick = click_item2;
        this.inflater = LayoutInflater.from(context2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.toplist_items, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (!this.homeNewsData.get(viewHolder.getAdapterPosition()).getCoverUrl().equalsIgnoreCase("")) {
            if (!this.homeNewsData.get(viewHolder.getAdapterPosition()).getCoverUrl().endsWith(".svg")) {
                Glide.with(this.context).load(this.homeNewsData.get(i).getCoverUrl()).into(viewHolder.img_icon);
            } else {
                GlideToVectorYou.init().with(this.context).setPlaceHolder(R.drawable.blank_image, R.drawable.blank_image).withListener(new GlideToVectorYouListener() {


                    @Override
                    public void onLoadFailed() {
                    }

                    @Override
                    public void onResourceReady() {
                    }
                }).load(Uri.parse(this.homeNewsData.get(i).getCoverUrl()), viewHolder.img_icon);
            }
        } else if (!this.homeNewsData.get(viewHolder.getAdapterPosition()).getVisualUrl().endsWith(".svg")) {
            Glide.with(this.context).load(this.homeNewsData.get(i).getVisualUrl()).into(viewHolder.img_icon);
        } else {
            GlideToVectorYou.init().with(this.context).withListener(new GlideToVectorYouListener() {


                @Override
                public void onLoadFailed() {
                }

                @Override
                public void onResourceReady() {
                }
            }).load(Uri.parse(this.homeNewsData.get(i).getVisualUrl()), viewHolder.img_icon);
        }
        if (!this.homeNewsData.get(viewHolder.getAdapterPosition()).getIconUrl().equalsIgnoreCase("")) {
            if (!this.homeNewsData.get(viewHolder.getAdapterPosition()).getIconUrl().endsWith(".svg")) {
                Glide.with(this.context).load(this.homeNewsData.get(i).getIconUrl()).into(viewHolder.item_icon);
            } else {
                GlideToVectorYou.init().with(this.context).withListener(new GlideToVectorYouListener() {


                    @Override
                    public void onLoadFailed() {
                    }

                    @Override
                    public void onResourceReady() {
                    }
                }).load(Uri.parse(this.homeNewsData.get(i).getIconUrl()), viewHolder.item_icon);
            }
        }
        viewHolder.txt_title.setText(this.homeNewsData.get(i).getWebsiteTitle());
        viewHolder.txt_description.setText(this.homeNewsData.get(i).getDescription());
        viewHolder.txt_title_web.setText(this.homeNewsData.get(i).getTitle());
        String charSequence = DateUtils.getRelativeTimeSpanString(this.homeNewsData.get(i).getLastUpdated(), Calendar.getInstance().getTimeInMillis(), (long) OkGo.DEFAULT_MILLISECONDS).toString();
        TextView textView = viewHolder.txt_lastUpdate;
        textView.setText(charSequence + "");
    }

    @Override
    public int getItemCount() {
        return this.homeNewsData.size();
    }

    public interface click_item {
        void OnCLickitem(HomeNewsData homeNewsData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_icon;
        ImageView item_icon;
        TextView txt_description;
        TextView txt_lastUpdate;
        TextView txt_title;
        TextView txt_title_web;

        public ViewHolder(View view) {
            super(view);
            this.img_icon = (ImageView) view.findViewById(R.id.img_icon);
            this.item_icon = (ImageView) view.findViewById(R.id.item_icon);
            this.txt_title = (TextView) view.findViewById(R.id.txt_title);
            this.txt_title_web = (TextView) view.findViewById(R.id.txt_title_web);
            this.txt_description = (TextView) view.findViewById(R.id.txt_description);
            this.txt_lastUpdate = (TextView) view.findViewById(R.id.txt_lastUpdate);

            view.setOnClickListener(view1 -> HomeNewsAdapter.this.itemClick.OnCLickitem(HomeNewsAdapter.this.homeNewsData.get(ViewHolder.this.getAdapterPosition())));
        }
    }
}
