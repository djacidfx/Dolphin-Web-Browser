package com.codebyte.dolphinwebbrowser.VdstudioAppFragment;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppAdapter.HistoryAdapter;
import com.codebyte.dolphinwebbrowser.VdstudioAppDatabase.Database;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.HistoryData;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.Constant;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.MyApplication;

import java.util.ArrayList;
import java.util.Collections;

import dolphinwebbrowser.R;

public class HistoryFragment extends Fragment {
    public Database database;
    public String showPlateformAd = Constant.APP_NEXT_AD_KEY;
    ArrayList<HistoryData> historyData = new ArrayList<>();
    int typeMode;
    private ImageView clearHistory;
    private LinearLayout noHistory;
    private RecyclerView recyclerHistory;
    private Toolbar toolbarHistory;
    private View view;

    public HistoryFragment() {
    }

    public HistoryFragment(int i) {
        this.typeMode = i;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_history, viewGroup, false);
        this.database = new Database(getActivity());
        this.showPlateformAd = MyApplication.getShowPlateformAd(getActivity());
        initView();
        initAdapter();
        return this.view;
    }

    @SuppressLint("NewApi")
    public void initAdapter() {
        NetworkInfo activeNetworkInfo;
        this.historyData.clear();
        this.historyData.addAll(this.database.getAllHistory());
        if (this.historyData.size() > 0) {
            if (this.historyData.size() >= 12 && (activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo()) != null) {
                activeNetworkInfo.isConnected();
            }
            this.clearHistory.setVisibility(View.VISIBLE);
            this.recyclerHistory.setVisibility(View.VISIBLE);
            this.noHistory.setVisibility(View.GONE);
            Collections.reverse(this.historyData);
            ArrayList arrayList = new ArrayList();
            arrayList.clear();
            for (int i = 0; i < this.historyData.size(); i++) {
                if (!arrayList.contains(this.historyData.get(i).getDate())) {
                    arrayList.add(this.historyData.get(i).getDate());
                }
            }
            ArrayList arrayList2 = new ArrayList();
            arrayList2.clear();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                arrayList2.add(arrayList.get(i2));
                for (int i3 = 0; i3 < this.historyData.size(); i3++) {
                    if (((String) arrayList.get(i2)).equals(this.historyData.get(i3).getDate())) {
                        arrayList2.add(this.historyData.get(i3));
                    }
                }
            }
            HistoryAdapter history_Adapter = new HistoryAdapter(getActivity(), arrayList2, this, this.typeMode);
            this.recyclerHistory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            this.recyclerHistory.setAdapter(history_Adapter);
            return;
        }
        this.clearHistory.setVisibility(View.GONE);
        this.recyclerHistory.setVisibility(View.GONE);
        this.noHistory.setVisibility(View.VISIBLE);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) this.view.findViewById(R.id.toolbar_history);
        this.toolbarHistory = toolbar;
        toolbar.setTitle("History");
        this.toolbarHistory.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        this.toolbarHistory.setNavigationOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                HistoryFragment.this.getFragmentManager().popBackStack();
            }
        });
        this.recyclerHistory = (RecyclerView) this.view.findViewById(R.id.recycler_history);
        this.noHistory = (LinearLayout) this.view.findViewById(R.id.no_history);
        ImageView imageView = (ImageView) this.toolbarHistory.findViewById(R.id.clear_history);
        this.clearHistory = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                HistoryFragment.this.historyData.clear();
                HistoryFragment.this.historyData.addAll(HistoryFragment.this.database.getAllHistory());
                for (int i = 0; i < HistoryFragment.this.historyData.size(); i++) {
                    HistoryFragment.this.database.deleteHistory(HistoryFragment.this.historyData.get(i).getId());
                }
                HistoryFragment.this.initAdapter();
            }
        });
    }
}
