package com.codebyte.dolphinwebbrowser.VdstudioAppAdapter;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppActivity.BrowserActivity;
import com.codebyte.dolphinwebbrowser.VdstudioAppActivity.IncognitoActivity;
import com.codebyte.dolphinwebbrowser.VdstudioAppDatabase.Database;
import com.codebyte.dolphinwebbrowser.VdstudioAppFragment.HistoryFragment;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.HistoryData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dolphinwebbrowser.R;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final Database database;
    FragmentActivity activity;
    ArrayList<Object> historyData;
    HistoryFragment historyFragment;
    LayoutInflater inflater;
    int typeMode;

    public HistoryAdapter(FragmentActivity fragmentActivity, ArrayList<Object> arrayList, HistoryFragment history_Fragment, int i) {
        this.activity = fragmentActivity;
        this.historyData = arrayList;
        this.database = new Database(fragmentActivity);
        this.historyFragment = history_Fragment;
        this.typeMode = i;
        this.inflater = LayoutInflater.from(fragmentActivity);
    }

    @Override
    public int getItemViewType(int i) {
        return this.historyData.get(i) instanceof String ? 1 : 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.item_history_header, viewGroup, false));
        }
        if (i != 2) {
            return null;
        }
        return new HistoryViewHolder(this.inflater.inflate(R.layout.item_history, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 1) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            String str = (String) this.historyData.get(i);
            if (String.valueOf(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime())).equals(str)) {
                headerViewHolder.header.setText("Today");
            } else {
                headerViewHolder.header.setText(str);
            }
        } else if (itemViewType == 2) {
            HistoryViewHolder historyViewHolder = (HistoryViewHolder) viewHolder;
            final HistoryData history_Data = (HistoryData) this.historyData.get(i);
            historyViewHolder.bookmark_optionmenu.setImageDrawable(this.activity.getResources().getDrawable(R.drawable.iv_cancel));
            historyViewHolder.bookmark_title.setText(history_Data.getName());
            historyViewHolder.bookmark_url.setText(history_Data.getUrl());
            try {
                byte[] decode = Base64.decode(history_Data.getImage(), 0);
                historyViewHolder.bookmark_icon.setImageBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));
            } catch (Exception e) {
                e.getMessage();
            }
            historyViewHolder.bookmark_optionmenu.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    HistoryAdapter.this.database.deleteHistory(history_Data.getId());
                    HistoryAdapter.this.historyFragment.initAdapter();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.historyData.size();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView header;

        public HeaderViewHolder(View view) {
            super(view);
            this.header = (TextView) view.findViewById(R.id.header);
        }
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView bookmark_icon;
        ImageView bookmark_optionmenu;
        TextView bookmark_title;
        TextView bookmark_url;

        public HistoryViewHolder(View view) {
            super(view);
            this.bookmark_icon = (ImageView) view.findViewById(R.id.bookmark_image);
            this.bookmark_optionmenu = (ImageView) view.findViewById(R.id.bookmark_optionmenu);
            this.bookmark_title = (TextView) view.findViewById(R.id.bookmark_title);
            this.bookmark_url = (TextView) view.findViewById(R.id.bookmark_url);

            view.setOnClickListener(view1 -> {
                HistoryData history_Data = (HistoryData) HistoryAdapter.this.historyData.get(HistoryViewHolder.this.getAdapterPosition());
                if (HistoryAdapter.this.typeMode == 0) {
                    BrowserActivity.web_view.loadUrl(history_Data.getUrl());
                } else {
                    IncognitoActivity.webView.loadUrl(history_Data.getUrl());
                }
                HistoryAdapter.this.activity.onBackPressed();
            });
        }
    }
}
