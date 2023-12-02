package com.codebyte.dolphinwebbrowser.VdstudioAppFragment;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppAdapter.BookmarkAdapter;
import com.codebyte.dolphinwebbrowser.VdstudioAppDatabase.Database;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.BookmarkData;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.Constant;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.MyApplication;

import java.util.ArrayList;

import dolphinwebbrowser.R;

public class BookmarkFragment extends Fragment {
    public static LinearLayout noBookmark;
    public static RecyclerView recycleBookmark;
    public String showPlateformAd = Constant.APP_NEXT_AD_KEY;
    ArrayList<BookmarkData> bookmarkData = new ArrayList<>();
    int typeMode;
    private Database database;
    private Toolbar toolbarBookmark;
    private View view;

    public BookmarkFragment() {
    }

    public BookmarkFragment(int i) {
        this.typeMode = i;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_bookmark, viewGroup, false);
        this.database = new Database(getActivity());
        this.showPlateformAd = MyApplication.getShowPlateformAd(getActivity());
        initView();
        initAdapter();
        return this.view;
    }

    @SuppressLint("NewApi")
    private void initAdapter() {
        NetworkInfo activeNetworkInfo;
        this.bookmarkData.clear();
        this.bookmarkData.addAll(this.database.getAllBookmark());
        if (this.bookmarkData.size() > 0) {
            if (this.bookmarkData.size() >= 12 && (activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo()) != null) {
                activeNetworkInfo.isConnected();
            }
            recycleBookmark.setVisibility(View.VISIBLE);
            noBookmark.setVisibility(View.GONE);
            BookmarkAdapter bookmark_Adapter = new BookmarkAdapter(getActivity(), this.bookmarkData, this.typeMode);
            recycleBookmark.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recycleBookmark.setAdapter(bookmark_Adapter);
            return;
        }
        recycleBookmark.setVisibility(View.GONE);
        noBookmark.setVisibility(View.VISIBLE);
    }

    private void initView() {
        this.toolbarBookmark = (Toolbar) this.view.findViewById(R.id.toolbar_bookmark);
        noBookmark = (LinearLayout) this.view.findViewById(R.id.no_bookmark);
        this.toolbarBookmark.setTitle("Bookmark");
        this.toolbarBookmark.setNavigationIcon(getResources().getDrawable(R.drawable.iv_back));
        this.toolbarBookmark.setNavigationOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                BookmarkFragment.this.getFragmentManager().popBackStack();
            }
        });
        recycleBookmark = (RecyclerView) this.view.findViewById(R.id.recycle_bookmark);
    }
}
