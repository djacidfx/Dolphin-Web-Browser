package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppAdapter.HomeNewsAdapter;
import com.codebyte.dolphinwebbrowser.VdstudioAppDatabase.Database;
import com.codebyte.dolphinwebbrowser.VdstudioAppFragment.BookmarkFragment;
import com.codebyte.dolphinwebbrowser.VdstudioAppFragment.HistoryFragment;
import com.codebyte.dolphinwebbrowser.VdstudioAppFragment.SearchFragment;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.BookmarkData;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.HistoryData;
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.HomeNewsData;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.AdBlocker;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.Constant;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.MyApplication;
import com.codebyte.dolphinwebbrowser.vdstudioappretrofit.ApiService;
import com.codebyte.dolphinwebbrowser.vdstudioappretrofit.RestApi;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.mrapp.android.tabswitcher.AbstractState;
import de.mrapp.android.tabswitcher.AddTabButtonListener;
import de.mrapp.android.tabswitcher.Animation;
import de.mrapp.android.tabswitcher.Layout;
import de.mrapp.android.tabswitcher.PeekAnimation;
import de.mrapp.android.tabswitcher.PullDownGesture;
import de.mrapp.android.tabswitcher.RevealAnimation;
import de.mrapp.android.tabswitcher.StatefulTabSwitcherDecorator;
import de.mrapp.android.tabswitcher.SwipeGesture;
import de.mrapp.android.tabswitcher.Tab;
import de.mrapp.android.tabswitcher.TabPreviewListener;
import de.mrapp.android.tabswitcher.TabSwitcher;
import de.mrapp.android.tabswitcher.TabSwitcherListener;
import de.mrapp.android.tabswitcher.view.TabSwitcherButton;
import de.mrapp.android.util.DisplayUtil;
import de.mrapp.android.util.ThemeUtil;
import de.mrapp.android.util.multithreading.AbstractDataBinder;
import de.mrapp.util.Condition;
import dolphinwebbrowser.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowserActivity extends BaseActivity implements TabSwitcherListener {
    public static final String ADAPTER_STATE_EXTRA = (State.class.getName() + "::%s::AdapterState");
    public static final String VIEW_TYPE_EXTRA = (BrowserActivity.class.getName() + "::ViewType");
    public static BrowserActivity browser_activity;
    public static WebView web_view;
    public Dialog bookmark_dialog;
    public LinearLayout bottom_button;
    public DataBinder dataBinder;
    public Decorator decorator;
    public TextView more_news_menu;
    public ImageView new_incognito;
    public ProgressBar progress;
    public ImageView refresh;
    public ImageView search_close;
    public ImageView search_down;
    public TextView search_keyword;
    public LinearLayout search_lay;
    public TextView search_link;
    public ImageView search_up;
    public String showPlateformAd = Constant.APP_NEXT_AD_KEY;
    public TabSwitcher tabSwitcher;
    public TabSwitcherButton tab_switcher_button;
    public ImageView web_voice_search;
    int TAB_COUNT = 1;
    ArrayList<BookmarkData> bookmark_data = new ArrayList<>();
    RelativeLayout custom_web;
    Database database;
    LinearLayout facebook_button;
    FrameLayout fragment_load;
    LinearLayout google_button;
    LinearLayout instagram_button;
    boolean isload = false;
    LinearLayout li_amazon;
    LinearLayout li_google;
    LinearLayout li_insta;
    LinearLayout li_linkdin;
    LinearLayout li_spotify;
    LinearLayout li_twitter;
    RecyclerView rv_news_items;
    LinearLayout search_text;
    boolean temp_next = false;
    ImageView web_menu;
    LinearLayout youtube_button;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private Snackbar snackbar;

    @Override
    public void onSelectionChanged(TabSwitcher tabSwitcher2, int i, Tab tab) {
    }

    @Override
    public void onSwitcherShown(TabSwitcher tabSwitcher2) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_browser);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        browser_activity = this;
        this.showPlateformAd = MyApplication.getShowPlateformAd(this);
        if (!MyApplication.getFirst()) {
            MyApplication.putDownload(Constant.path);
            MyApplication.putFirst(true);
            MyApplication.putRequestData(true);
            MyApplication.putEnableJava(true);
            MyApplication.putBlockImage(false);
            MyApplication.putUserAgent("Default");
            MyApplication.putHomePage("Default");
            MyApplication.putSearchEngine("Google");
            MyApplication.putTextSize("14");
            MyApplication.putTextEncoding("UTF-8");
            MyApplication.putRendering("Normal");
            MyApplication.putUrlBox("Domain (default)");
            MyApplication.putCookies(true);
        }
        if (!checkPermission()) {
            requestPermission();
        } else if (!new File(MyApplication.getDownloadPath()).exists()) {
            new File(MyApplication.getDownloadPath()).mkdirs();
        }
        if (MyApplication.getHideStatus()) {
            getWindow().addFlags(1024);
        } else {
            getWindow().clearFlags(1024);
        }
        if (MyApplication.getFullScreen()) {
            getWindow().clearFlags(2048);
            getWindow().addFlags(1024);
        } else {
            getWindow().clearFlags(1024);
            getWindow().addFlags(2048);
        }
        if (MyApplication.getBlackStatus()) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            if (Build.VERSION.SDK_INT >= 21) {
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.Black));
            }
        } else {
            Window window2 = getWindow();
            window2.addFlags(Integer.MIN_VALUE);
            if (Build.VERSION.SDK_INT >= 21) {
                window2.setStatusBarColor(ContextCompat.getColor(this, R.color.White));
            }
            getWindow().getDecorView().setSystemUiVisibility(8192);
        }
        Log.d("Data", "onCreate: ====>" + new File(MyApplication.getDownloadPath()).getName());
        Log.d("Data", "onCreate: ====>" + MyApplication.getDownloadPath());
        initView();
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, this.perms, 1);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        boolean z = true;
        if (i == 1 && iArr.length > 0) {
            boolean z2 = iArr[0] == 0;
            if (iArr[1] != 0) {
                z = false;
            }
            if (!z2 || !z) {
                if (Build.VERSION.SDK_INT >= 23 && shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE") && shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                    Toast.makeText(this, "You need to allow access all the permissions", Toast.LENGTH_SHORT).show();
                }
            } else if (!new File(MyApplication.getDownloadPath()).exists()) {
                new File(MyApplication.getDownloadPath()).mkdirs();
            }
        }
    }

    private void initView() {
        this.decorator = new Decorator();
        this.dataBinder = new DataBinder(this);
        TabSwitcher tabSwitcher2 = (TabSwitcher) findViewById(R.id.tab_switcher);
        this.tabSwitcher = tabSwitcher2;
        tabSwitcher2.clearSavedStatesWhenRemovingTabs(false);
        ViewCompat.setOnApplyWindowInsetsListener(this.tabSwitcher, createWindowInsetsListener());
        this.tabSwitcher.setDecorator(this.decorator);
        this.tabSwitcher.addListener(this);
        this.tabSwitcher.showToolbars(true);
        for (int i = 0; i < this.TAB_COUNT; i++) {
            this.tabSwitcher.addTab(createTab(i));
        }
        this.tabSwitcher.showAddTabButton(createAddTabButtonListener());
        this.tabSwitcher.setToolbarNavigationIcon(R.drawable.ic_plus_24dp, createAddTabListener());
        TabSwitcher.setupWithMenu(this.tabSwitcher, createTabSwitcherButtonListener());
        inflateMenu();
    }

    private AddTabButtonListener createAddTabButtonListener() {
        return new AddTabButtonListener() {


            @Override
            public void onAddTab(TabSwitcher tabSwitcher) {
                tabSwitcher.addTab(BrowserActivity.this.createTab(tabSwitcher.getCount()), 0);
            }
        };
    }

    private OnApplyWindowInsetsListener createWindowInsetsListener() {
        return new OnApplyWindowInsetsListener() {


            @Override
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                int systemWindowInsetLeft = windowInsetsCompat.getSystemWindowInsetLeft();
                int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
                int systemWindowInsetRight = windowInsetsCompat.getSystemWindowInsetRight();
                BrowserActivity.this.tabSwitcher.setPadding(systemWindowInsetLeft, systemWindowInsetTop, systemWindowInsetRight, windowInsetsCompat.getSystemWindowInsetBottom());
                float f = (float) systemWindowInsetTop;
                if (BrowserActivity.this.tabSwitcher.getLayout() == Layout.TABLET) {
                    f += (float) BrowserActivity.this.getResources().getDimensionPixelSize(R.dimen.tablet_tab_container_height);
                }
                RectF rectF = new RectF((float) systemWindowInsetLeft, f, (float) (DisplayUtil.getDisplayWidth(BrowserActivity.this) - systemWindowInsetRight), ((float) ThemeUtil.getDimensionPixelSize(BrowserActivity.this, 0)) + f);
                BrowserActivity.this.tabSwitcher.addDragGesture(((SwipeGesture.Builder) new SwipeGesture.Builder().setTouchableArea(rectF)).create());
                BrowserActivity.this.tabSwitcher.addDragGesture(((PullDownGesture.Builder) new PullDownGesture.Builder().setTouchableArea(rectF)).create());
                return windowInsetsCompat;
            }
        };
    }

    private View.OnClickListener createAddTabListener() {
        return new View.OnClickListener() {


            public void onClick(View view) {
                BrowserActivity.this.tabSwitcher.addTab(BrowserActivity.this.createTab(BrowserActivity.this.tabSwitcher.getCount()), 0, BrowserActivity.this.createRevealAnimation());
            }
        };
    }

    private void inflateMenu() {
        this.tabSwitcher.inflateToolbarMenu(R.menu.tab_switcher, createToolbarMenuListener());
    }

    private Toolbar.OnMenuItemClickListener createToolbarMenuListener() {
        return new Toolbar.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.add_tab_menu_item) {
                    BrowserActivity browser_Activity = BrowserActivity.this;
                    Tab createTab = browser_Activity.createTab(browser_Activity.tabSwitcher.getCount());
                    if (BrowserActivity.this.tabSwitcher.isSwitcherShown()) {
                        BrowserActivity.this.tabSwitcher.addTab(createTab, 0, BrowserActivity.this.createRevealAnimation());
                    } else {
                        BrowserActivity.this.tabSwitcher.addTab(createTab, 0, BrowserActivity.this.createPeekAnimation());
                    }
                    return true;
                } else if (itemId == R.id.clear_tabs_menu_item) {
                    BrowserActivity.this.tabSwitcher.clear();
                    return true;
                } else {
                    Tab selectedTab = BrowserActivity.this.tabSwitcher.getSelectedTab();
                    if (selectedTab != null) {
                        BrowserActivity.this.tabSwitcher.removeTab(selectedTab);
                    }
                    return true;
                }
            }
        };
    }

    public Animation createPeekAnimation() {
        return new PeekAnimation.Builder().setX(((float) this.tabSwitcher.getWidth()) / 2.0f).create();
    }

    public Animation createRevealAnimation() {
        float f;
        View navigationMenuItem = getNavigationMenuItem();
        float f2 = 0.0f;
        if (navigationMenuItem != null) {
            int[] iArr = new int[2];
            navigationMenuItem.getLocationInWindow(iArr);
            f = ((float) iArr[1]) + (((float) navigationMenuItem.getHeight()) / 2.0f);
            f2 = ((float) iArr[0]) + (((float) navigationMenuItem.getWidth()) / 2.0f);
        } else {
            f = 0.0f;
        }
        return new RevealAnimation.Builder().setX(f2).setY(f).create();
    }

    private View getNavigationMenuItem() {
        Toolbar[] toolbars = this.tabSwitcher.getToolbars();
        if (toolbars == null) {
            return null;
        }
        Toolbar toolbar2 = toolbars.length > 1 ? toolbars[1] : toolbars[0];
        int childCount = toolbar2.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = toolbar2.getChildAt(i);
            if (childAt instanceof ImageButton) {
                return childAt;
            }
        }
        return null;
    }

    public Tab createTab(int i) {
        Tab tab = new Tab(getString(R.string.tab_title));
        Bundle bundle = new Bundle();
        bundle.putInt(VIEW_TYPE_EXTRA, i % 3);
        tab.setParameters(bundle);
        return tab;
    }

    @Override
    public void onSwitcherHidden(TabSwitcher tabSwitcher2) {
        Snackbar snackbar2 = this.snackbar;
        if (snackbar2 != null) {
            snackbar2.dismiss();
        }
    }

    @Override
    public void onTabAdded(TabSwitcher tabSwitcher2, int i, Tab tab, Animation animation) {
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher2, createTabSwitcherButtonListener());
    }

    @Override
    public void onTabRemoved(TabSwitcher tabSwitcher2, int i, Tab tab, Animation animation) {
        showUndoSnackbar(getString(R.string.removed_tab_snackbar, new Object[]{tab.getTitle()}), i, tab);
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher2, createTabSwitcherButtonListener());
    }

    @Override
    public void onAllTabsRemoved(TabSwitcher tabSwitcher2, Tab[] tabArr, Animation animation) {
        showUndoSnackbar(getString(R.string.cleared_tabs_snackbar), 0, tabArr);
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher2, createTabSwitcherButtonListener());
    }

    private void showUndoSnackbar(CharSequence charSequence, int i, Tab... tabArr) {
        Snackbar actionTextColor = Snackbar.make(this.tabSwitcher, charSequence, BaseTransientBottomBar.LENGTH_LONG).setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_text_color));
        this.snackbar = actionTextColor;
        actionTextColor.setAction(R.string.undo, createUndoSnackbarListener(actionTextColor, i, tabArr));
        this.snackbar.addCallback(createUndoSnackbarCallback(tabArr));
        this.snackbar.show();
    }

    private BaseTransientBottomBar.BaseCallback<Snackbar> createUndoSnackbarCallback(Tab[] tabArr) {
        return new BaseTransientBottomBar.BaseCallback<Snackbar>() {


            public void onDismissed(Snackbar snackbar, int i) {
            }
        };
    }

    private View.OnClickListener createUndoSnackbarListener(final Snackbar snackbar2, final int i, final Tab[] tabArr) {
        return new View.OnClickListener() {


            public void onClick(View view) {
                snackbar2.setAction((CharSequence) null, (View.OnClickListener) null);
                if (BrowserActivity.this.tabSwitcher.isSwitcherShown()) {
                    BrowserActivity.this.tabSwitcher.addAllTabs(tabArr, i);
                } else if (tabArr.length == 1) {
                    BrowserActivity.this.tabSwitcher.addTab(tabArr[0], 0, BrowserActivity.this.createPeekAnimation());
                }
            }
        };
    }

    public void LoadData() {
        Call<JsonElement> newsUrl = ((ApiService) RestApi.getClient1().create(ApiService.class)).getNewsUrl();
        final ArrayList arrayList = new ArrayList();
        newsUrl.enqueue(new Callback<JsonElement>() {


            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                BrowserActivity.this.progress.setVisibility(View.GONE);
                try {
                    arrayList.clear();
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
                        arrayList.add(new HomeNewsData(asString, asString2, asString3, asString4, asString5, asString6, str, j));
                    }
                    BrowserActivity.this.rv_news_items.setLayoutManager(new LinearLayoutManager(BrowserActivity.this));
                    BrowserActivity.this.rv_news_items.setAdapter(new HomeNewsAdapter(BrowserActivity.this, arrayList, new HomeNewsAdapter.click_item() {


                        @Override
                        public void OnCLickitem(HomeNewsData homeNewsData) {
                            BrowserActivity.web_view.loadUrl(homeNewsData.getWebsite());
                        }
                    }));
                } catch (Exception e) {
                    BrowserActivity.this.progress.setVisibility(View.GONE);
                    Log.e("OSJKSGDGHS2222", e.getMessage() + "::");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable th) {
                BrowserActivity.this.progress.setVisibility(View.GONE);
                Log.e("OSJKSGDGHSs111", th.getMessage() + "::");
            }
        });
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 500 && i2 == -1) {
            ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("android.speech.extra.RESULTS");
            if (!stringArrayListExtra.isEmpty()) {
                String str = stringArrayListExtra.get(0);
                Log.e("ONSKDGSJDVS", str + "::");
                Decorator decorator2 = this.decorator;
                if (decorator2 != null) {
                    decorator2.VoiceSearch(str);
                }
            }
        }
        if (i == 203 && i2 == -1 && intent != null) {
            web_view.loadUrl(((HomeNewsData) intent.getSerializableExtra("clickData")).getWebsite());
        }
    }

    @SuppressLint("ResourceType")
    public void search() {
        final Dialog dialog2 = new Dialog(this, R.style.WideDialog);
        dialog2.setContentView(R.layout.dialog_find);
        dialog2.getWindow().setBackgroundDrawableResource(17170445);
        final EditText editText = (EditText) dialog2.findViewById(R.id.search_text);
        ((Button) dialog2.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        ((Button) dialog2.findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                String trim = editText.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    Toast.makeText(BrowserActivity.this, "Enter search keyword!", Toast.LENGTH_SHORT).show();
                    return;
                }
                BrowserActivity.web_view.findNext(true);
                BrowserActivity.web_view.findAllAsync(trim);
                BrowserActivity.this.search_lay.setVisibility(View.VISIBLE);
                BrowserActivity.this.bottom_button.setVisibility(View.GONE);
                BrowserActivity.this.search_keyword.setText(trim);
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    public void initBookmark(ImageView imageView) {
        this.isload = true;
        this.bookmark_data.clear();
        this.bookmark_data.addAll(this.database.getAllBookmark());
        boolean z = false;
        for (int i = 0; i < this.bookmark_data.size(); i++) {
            if (this.bookmark_data.get(i).getUrl().equals(web_view.getOriginalUrl())) {
                z = true;
            }
        }
        if (z) {
            imageView.setImageResource(R.drawable.iv_bookmark);
        } else {
            imageView.setImageResource(R.drawable.iv_unbookmark);
        }
    }

    public void fragmentLoad(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.fragment_load, fragment);
        beginTransaction.addToBackStack("frag");
        beginTransaction.commit();
    }

    public View.OnClickListener createTabSwitcherButtonListener() {
        return new View.OnClickListener() {


            public void onClick(View view) {
                if (BrowserActivity.this.tabSwitcher.getCount() != 0) {
                    if (BrowserActivity.this.custom_web.getVisibility() == View.VISIBLE) {
                        BrowserActivity.this.tabSwitcher.getTab(BrowserActivity.this.tabSwitcher.getSelectedTabIndex()).setTitle("Private Search");
                    } else {
                        BrowserActivity.this.tabSwitcher.getTab(BrowserActivity.this.tabSwitcher.getSelectedTabIndex()).setTitle(BrowserActivity.web_view.getTitle());
                    }
                    BrowserActivity.this.tabSwitcher.toggleSwitcherVisibility();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (this.tabSwitcher.getCount() == 0) {
            Tab createTab = createTab(this.tabSwitcher.getCount());
            if (this.tabSwitcher.isSwitcherShown()) {
                this.tabSwitcher.addTab(createTab, 0, createRevealAnimation());
            } else {
                this.tabSwitcher.addTab(createTab, 0, createPeekAnimation());
            }
        } else if (this.tabSwitcher.isSwitcherShown()) {
            this.tabSwitcher.hideSwitcher();
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragment_load) != null) {
            getSupportFragmentManager().popBackStack();
        } else if (this.search_lay.getVisibility() == View.VISIBLE) {
            this.search_lay.setVisibility(View.GONE);
            this.bottom_button.setVisibility(View.VISIBLE);
            this.search_keyword.setText("");
            web_view.findAllAsync("");
        } else if (web_view.canGoBack()) {
            web_view.goBack();
        } else if (this.custom_web.getVisibility() != View.VISIBLE) {
            String homePage = MyApplication.getHomePage();
            char c = 65535;
            switch (homePage.hashCode()) {
                case -1406075965:
                    if (homePage.equals("Webpage")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1085510111:
                    if (homePage.equals("Default")) {
                        c = 0;
                        break;
                    }
                    break;
                case -253812259:
                    if (homePage.equals("Bookmarks")) {
                        c = 3;
                        break;
                    }
                    break;
                case 64266548:
                    if (homePage.equals("Blank")) {
                        c = 1;
                        break;
                    }
                    break;
            }
            if (c != 0) {
                if (c != 1) {
                    if (c != 2) {
                        if (c == 3) {
                            if (this.custom_web.getVisibility() != View.VISIBLE) {
                                this.custom_web.setVisibility(View.VISIBLE);
                                web_view.setVisibility(View.GONE);
                            } else {
                                if (MyApplication.getCacheExit()) {
                                    MyApplication.putSearcHistory("");
                                    web_view.clearCache(true);
                                    web_view.clearFormData();
                                }
                                if (MyApplication.getCookieExit() && Build.VERSION.SDK_INT >= 21) {
                                    CookieManager.getInstance().removeAllCookies(null);
                                    CookieManager.getInstance().flush();
                                }
                                if (MyApplication.getHistoryExit()) {
                                    this.database.deleteHistoryTable();
                                    web_view.clearHistory();
                                    web_view.clearSslPreferences();
                                }
                                new AlertDialog.Builder(this).setMessage("Are you sure you want to close this dolphinwebbrowser?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        BrowserActivity.super.onBackPressed();
                                    }
                                }).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
                            }
                        }
                    } else if (web_view.getVisibility() != View.VISIBLE) {
                        this.custom_web.setVisibility(View.GONE);
                        web_view.setVisibility(View.VISIBLE);
                        fragmentLoad(new BookmarkFragment(0));
                    } else {
                        if (MyApplication.getCacheExit()) {
                            MyApplication.putSearcHistory("");
                            web_view.clearCache(true);
                            web_view.clearFormData();
                        }
                        if (MyApplication.getCookieExit() && Build.VERSION.SDK_INT >= 21) {
                            CookieManager.getInstance().removeAllCookies(null);
                            CookieManager.getInstance().flush();
                        }
                        if (MyApplication.getHistoryExit()) {
                            this.database.deleteHistoryTable();
                            web_view.clearHistory();
                            web_view.clearSslPreferences();
                        }
                        new AlertDialog.Builder(this).setMessage("Are you sure you want to close this dolphinwebbrowser?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                            public void onClick(DialogInterface dialogInterface, int i) {
                                BrowserActivity.super.onBackPressed();
                            }
                        }).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
                    }
                } else if (this.custom_web.getVisibility() == View.VISIBLE || web_view.getVisibility() == View.VISIBLE) {
                    this.custom_web.setVisibility(View.GONE);
                    web_view.setVisibility(View.GONE);
                } else {
                    if (MyApplication.getCacheExit()) {
                        MyApplication.putSearcHistory("");
                        web_view.clearCache(true);
                        web_view.clearFormData();
                    }
                    if (MyApplication.getCookieExit() && Build.VERSION.SDK_INT >= 21) {
                        CookieManager.getInstance().removeAllCookies(null);
                        CookieManager.getInstance().flush();
                    }
                    if (MyApplication.getHistoryExit()) {
                        this.database.deleteHistoryTable();
                        web_view.clearHistory();
                        web_view.clearSslPreferences();
                    }
                    new AlertDialog.Builder(this).setMessage("Are you sure you want to close this dolphinwebbrowser?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                        public void onClick(DialogInterface dialogInterface, int i) {
                            BrowserActivity.super.onBackPressed();
                        }
                    }).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
                }
            } else if (this.custom_web.getVisibility() != View.VISIBLE) {
                this.custom_web.setVisibility(View.VISIBLE);
                web_view.setVisibility(View.GONE);
            } else {
                if (MyApplication.getCacheExit()) {
                    MyApplication.putSearcHistory("");
                    web_view.clearCache(true);
                    web_view.clearFormData();
                }
                if (MyApplication.getCookieExit() && Build.VERSION.SDK_INT >= 21) {
                    CookieManager.getInstance().removeAllCookies(null);
                    CookieManager.getInstance().flush();
                }
                if (MyApplication.getHistoryExit()) {
                    this.database.deleteHistoryTable();
                    web_view.clearHistory();
                    web_view.clearSslPreferences();
                }
                new AlertDialog.Builder(this).setMessage("Are you sure you want to close this dolphinwebbrowser?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialogInterface, int i) {
                        BrowserActivity.super.onBackPressed();
                    }
                }).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
            }
            this.temp_next = true;
            this.search_link.setText("");
        } else {
            if (MyApplication.getCacheExit()) {
                MyApplication.putSearcHistory("");
                web_view.clearCache(true);
                web_view.clearFormData();
            }
            if (MyApplication.getCookieExit() && Build.VERSION.SDK_INT >= 21) {
                CookieManager.getInstance().removeAllCookies(null);
                CookieManager.getInstance().flush();
            }
            if (MyApplication.getHistoryExit()) {
                this.database.deleteHistoryTable();
                web_view.clearHistory();
                web_view.clearSslPreferences();
            }
            finish();
        }
    }

    public void addTab(String str) {
        TabSwitcher tabSwitcher2 = this.tabSwitcher;
        tabSwitcher2.addTab(createTab(tabSwitcher2.getCount()), 0, createRevealAnimation());
        web_view.loadUrl(str);
    }

    public static class DataBinder extends AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> {
        public DataBinder(Context context) {
            super(context.getApplicationContext());
        }

        @SuppressLint("ResourceType")
        public ArrayAdapter<String> doInBackground(Tab tab, Void... voidArr) {
            String[] strArr = new String[10];
            int i = 0;
            while (i < 10) {
                int i2 = i + 1;
                strArr[i] = String.format(Locale.getDefault(), "%s, item %d", tab.getTitle(), Integer.valueOf(i2));
                i = i2;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException unused) {
            }
            return new ArrayAdapter<>(getContext(), 17367043, strArr);
        }

        public void onPostExecute(ListView listView, ArrayAdapter<String> arrayAdapter, long j, Void... voidArr) {
            if (arrayAdapter != null) {
                listView.setAdapter((ListAdapter) arrayAdapter);
            }
        }
    }

    public class Decorator extends StatefulTabSwitcherDecorator<State> {
        private Decorator() {
        }

        @Override
        public State onCreateState(Context context, TabSwitcher tabSwitcher, View view, Tab tab, int i, int i2, Bundle bundle) {
            return null;
        }

        public void onClearState(State state) {
            BrowserActivity.this.tabSwitcher.removeTabPreviewListener(state);
        }

        public void onSaveInstanceState(View view, Tab tab, int i, int i2, State state, Bundle bundle) {
            if (state != null) {
                state.saveInstanceState(bundle);
            }
        }

        @Override
        public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
            return layoutInflater.inflate(R.layout.fragment_browser1, viewGroup, false);
        }

        public void onShowTab(final Context context, final TabSwitcher tabSwitcher, View view, Tab tab, int i, int i2, State state, Bundle bundle) {
            char c;
            char c2;
            BrowserActivity.web_view = (WebView) findViewById(R.id.web_view);
            if (Build.VERSION.SDK_INT >= 21) {
                BrowserActivity.web_view.getSettings().setMixedContentMode(0);
            }
            BrowserActivity.this.tab_switcher_button = (TabSwitcherButton) findViewById(R.id.tab_switcher_button);
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) BrowserActivity.this.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                activeNetworkInfo.isConnected();
            }
            BrowserActivity.this.database = new Database(BrowserActivity.this);
            BrowserActivity.this.progress = (ProgressBar) findViewById(R.id.progress);
            BrowserActivity.this.custom_web = (RelativeLayout) findViewById(R.id.custom_web);
            BrowserActivity.this.google_button = (LinearLayout) findViewById(R.id.google);
            BrowserActivity.this.li_twitter = (LinearLayout) findViewById(R.id.li_twitter);
            BrowserActivity.this.li_spotify = (LinearLayout) findViewById(R.id.li_spotify);
            BrowserActivity.this.li_linkdin = (LinearLayout) findViewById(R.id.li_linkdin);
            BrowserActivity.this.li_google = (LinearLayout) findViewById(R.id.li_google);
            BrowserActivity.this.li_amazon = (LinearLayout) findViewById(R.id.li_amazon);
            BrowserActivity.this.li_insta = (LinearLayout) findViewById(R.id.li_insta);
            BrowserActivity.this.rv_news_items = (RecyclerView) findViewById(R.id.rv_news_items);
            BrowserActivity.this.facebook_button = (LinearLayout) findViewById(R.id.facebook);
            BrowserActivity.this.youtube_button = (LinearLayout) findViewById(R.id.youtube);
            BrowserActivity.this.instagram_button = (LinearLayout) findViewById(R.id.instagram);
            BrowserActivity.this.web_menu = (ImageView) findViewById(R.id.web_menu);
            BrowserActivity.this.search_text = (LinearLayout) findViewById(R.id.search_text);
            BrowserActivity.this.search_lay = (LinearLayout) findViewById(R.id.search_lay);
            BrowserActivity.this.bottom_button = (LinearLayout) findViewById(R.id.bottom_button);
            BrowserActivity.this.search_link = (TextView) findViewById(R.id.search_link);
            BrowserActivity.this.search_keyword = (TextView) findViewById(R.id.search_keyword);
            BrowserActivity.this.refresh = (ImageView) findViewById(R.id.refresh);
            BrowserActivity.this.new_incognito = (ImageView) findViewById(R.id.new_incognito);
            BrowserActivity.this.more_news_menu = (TextView) findViewById(R.id.more_news_menu);
            BrowserActivity.this.new_incognito.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, IncognitoActivity.class));
                    BrowserActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });
            BrowserActivity.this.more_news_menu.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.this.startActivityForResult(new Intent(BrowserActivity.this, MoreNews.class), HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
                }
            });
            BrowserActivity.this.LoadData();
            BrowserActivity.this.search_up = (ImageView) findViewById(R.id.search_up);
            BrowserActivity.this.search_down = (ImageView) findViewById(R.id.search_down);
            BrowserActivity.this.search_close = (ImageView) findViewById(R.id.search_close);
            BrowserActivity.this.web_voice_search = (ImageView) findViewById(R.id.web_voice_search);
            BrowserActivity.this.fragment_load = (FrameLayout) findViewById(R.id.fragment_load);
            BrowserActivity.this.tab_switcher_button.setCount(tabSwitcher.getCount());
            BrowserActivity.this.bottom_button.setVisibility(View.VISIBLE);
            BrowserActivity.this.search_lay.setVisibility(View.GONE);
            BrowserActivity.web_view.getSettings().setDefaultFontSize(Integer.parseInt(MyApplication.getTextSize()));
            if (bundle == null) {
                BrowserActivity.this.search_link.setText("");
                String homePage = MyApplication.getHomePage();
                switch (homePage.hashCode()) {
                    case -1406075965:
                        if (homePage.equals("Webpage")) {
                            c2 = 2;
                            break;
                        }
                    case -1085510111:
                        if (homePage.equals("Default")) {
                            c2 = 0;
                            break;
                        }
                    case -253812259:
                        if (homePage.equals("Bookmarks")) {
                            c2 = 3;
                            break;
                        }
                    case 64266548:
                        if (homePage.equals("Blank")) {
                            c2 = 1;
                            break;
                        }
                    default:
                        c2 = 65535;
                        break;
                }
                if (c2 == 0) {
                    BrowserActivity.this.custom_web.setVisibility(View.VISIBLE);
                    BrowserActivity.web_view.setVisibility(View.GONE);
                } else if (c2 == 1) {
                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                    BrowserActivity.web_view.setVisibility(View.GONE);
                } else if (c2 == 2) {
                    BrowserActivity.web_view.loadUrl("https://www.google.co.in/");
                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                    BrowserActivity.web_view.setVisibility(View.VISIBLE);
                } else if (c2 == 3) {
                    BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                }
            }
            BrowserActivity.this.google_button.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.web_view.loadUrl("https://www.google.com/");
                }
            });
            BrowserActivity.this.li_twitter.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, TwitActivity.class));
                }
            });
            BrowserActivity.this.li_linkdin.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.web_view.loadUrl("https://in.linkedin.com/");
                }
            });
            BrowserActivity.this.li_google.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    BrowserActivity.web_view.loadUrl("https://www.google.com/");
                }
            });
            BrowserActivity.this.li_amazon.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    BrowserActivity.web_view.loadUrl("https://www.amazon.in/");
                }
            });
            BrowserActivity.this.li_insta.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    web_view.getSettings().setJavaScriptEnabled(true);
                    web_view.getSettings().setDomStorageEnabled(true);
                    BrowserActivity.web_view.loadUrl("https://www.instagram.com/?hl=en");
                }
            });
            BrowserActivity.this.li_spotify.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    BrowserActivity.web_view.loadUrl("https://www.spotify.com/");
                }
            });
            BrowserActivity.this.facebook_button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    BrowserActivity.web_view.loadUrl("https://www.facebook.com/");
                }
            });
            BrowserActivity.this.youtube_button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, YouActivity.class));
                }
            });
            BrowserActivity.this.instagram_button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    web_view.getSettings().setJavaScriptEnabled(true);
                    web_view.getSettings().setDomStorageEnabled(true);
                    BrowserActivity.web_view.loadUrl("https://www.instagram.com/?hl=en");
                }
            });
            String userAgent = MyApplication.getUserAgent();
            int hashCode = userAgent.hashCode();
            if (hashCode != -1984987966) {
                if (hashCode != -1085510111) {
                    if (hashCode == -1073207300 && userAgent.equals("Desktop")) {
                        BrowserActivity.web_view.getSettings().setUserAgentString("Android");
                        String textEncoding = MyApplication.getTextEncoding();
                        switch (textEncoding.hashCode()) {
                            case 70352:
                                if (textEncoding.equals("GBK")) {
                                    c = 2;
                                    break;
                                }
                            case 2070357:
                                if (textEncoding.equals("Big5")) {
                                    c = 3;
                                    break;
                                }
                            case 81070450:
                                if (textEncoding.equals("UTF-8")) {
                                    c = 1;
                                    break;
                                }
                            case 257295942:
                                if (textEncoding.equals("SHIFT_JS")) {
                                    c = 5;
                                    break;
                                }
                            case 1450311437:
                                if (textEncoding.equals("ISO-2022-JP")) {
                                    c = 4;
                                    break;
                                }
                            case 2027158704:
                                if (textEncoding.equals("ISO-8859-1")) {
                                    c = 0;
                                    break;
                                }
                            case 2055952320:
                                if (textEncoding.equals("EUC-JP")) {
                                    c = 6;
                                    break;
                                }
                            case 2055952353:
                                if (textEncoding.equals("EUC-KR")) {
                                    c = 7;
                                    break;
                                }
                            default:
                                c = 65535;
                                break;
                        }
                        switch (c) {
                            case 0:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("ISO-8859-1");
                                break;
                            case 1:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("UTF-8");
                                break;
                            case 2:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("GBK");
                                break;
                            case 3:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("Big5");
                                break;
                            case 4:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("ISO-2022-JP");
                                break;
                            case 5:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("SHIFT_JS");
                                break;
                            case 6:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("EUC-JP");
                                break;
                            case 7:
                                BrowserActivity.web_view.getSettings().setDefaultTextEncodingName("EUC-KR");
                                break;
                        }
                        if (!MyApplication.getBlockImage()) {
                            BrowserActivity.web_view.getSettings().setLoadsImagesAutomatically(false);
                        } else {
                            BrowserActivity.web_view.getSettings().setLoadsImagesAutomatically(true);
                        }
                        if (!MyApplication.getEnableJava()) {
                            BrowserActivity.web_view.getSettings().setJavaScriptEnabled(true);
                        } else {
                            BrowserActivity.web_view.getSettings().setJavaScriptEnabled(false);
                        }
                        if (!MyApplication.getCookies()) {
                            CookieSyncManager.createInstance(BrowserActivity.this);
                            CookieSyncManager.getInstance().startSync();
                            CookieManager.getInstance().setAcceptCookie(true);
                            if (Build.VERSION.SDK_INT >= 21) {
                                CookieManager.getInstance().setAcceptThirdPartyCookies(BrowserActivity.web_view, true);
                            }
                        } else {
                            CookieSyncManager.createInstance(BrowserActivity.this);
                            CookieSyncManager.getInstance().startSync();
                            CookieManager.getInstance().setAcceptCookie(false);
                            if (Build.VERSION.SDK_INT >= 21) {
                                CookieManager.getInstance().setAcceptThirdPartyCookies(BrowserActivity.web_view, false);
                            }
                        }
                        ((ImageView) findViewById(R.id.web_next_img)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                Decorator.this.LoadnextWeb();
                            }
                        });
                        ((ImageView) findViewById(R.id.web_privious_img)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                Decorator.this.loadPriviousWeb();
                            }
                        });
                        BrowserActivity.this.web_menu.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                final PopupWindow popupWindow = new PopupWindow(BrowserActivity.this);
                                View inflate = ((LayoutInflater) BrowserActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_menu, (ViewGroup) null);
                                LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.control_btn);
                                final ImageView imageView = (ImageView) inflate.findViewById(R.id.bookmark_image);
                                LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.bookmark);
                                ((TextView) inflate.findViewById(R.id.new_tab)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        tabSwitcher.addTab(BrowserActivity.this.createTab(tabSwitcher.getCount()), 0, BrowserActivity.this.createRevealAnimation());
                                    }
                                });
                                ((TextView) inflate.findViewById(R.id.private_tab)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, IncognitoActivity.class));
                                        BrowserActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                    }
                                });
                                ((TextView) inflate.findViewById(R.id.bookmarks)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                                    }
                                });
                                ((TextView) inflate.findViewById(R.id.history)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        BrowserActivity.this.fragmentLoad(new HistoryFragment(0));
                                    }
                                });
                                ((TextView) inflate.findViewById(R.id.download)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        Adshandler.ShowIntertialads(BrowserActivity.this, DownloadActivity.class);
                                    }
                                });
                                ((TextView) inflate.findViewById(R.id.find)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                            BrowserActivity.this.search();
                                        }
                                    }
                                });
                                ((TextView) inflate.findViewById(R.id.copy)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                            ((ClipboardManager) BrowserActivity.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("copy", BrowserActivity.web_view.getUrl()));
                                            Toast.makeText(context, "Link copied", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                ((TextView) inflate.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.SEND");
                                            intent.putExtra("android.intent.extra.SUBJECT", BrowserActivity.web_view.getTitle());
                                            intent.putExtra("android.intent.extra.TEXT", BrowserActivity.web_view.getUrl());
                                            intent.setType(HTTP.PLAIN_TEXT_TYPE);
                                            BrowserActivity.this.startActivity(intent);
                                        }
                                    }
                                });
                                ((TextView) inflate.findViewById(R.id.setting)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, SettingActivity.class));
                                    }
                                });
                                ((LinearLayout) inflate.findViewById(R.id.web_home)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        CookieManager.getInstance().setAcceptCookie(false);
                                        BrowserActivity.web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                                        BrowserActivity.web_view.getSettings().setAppCacheEnabled(false);
                                        BrowserActivity.web_view.clearHistory();
                                        BrowserActivity.web_view.clearCache(true);
                                        BrowserActivity.web_view.clearFormData();
                                        BrowserActivity.web_view.getSettings().setSavePassword(false);
                                        BrowserActivity.web_view.getSettings().setSaveFormData(false);
                                        if (BrowserActivity.web_view.canGoBack()) {
                                            BrowserActivity.this.temp_next = true;
                                        } else if (BrowserActivity.this.custom_web.getVisibility() != View.VISIBLE) {
                                            BrowserActivity.this.temp_next = true;
                                        }
                                        String homePage = MyApplication.getHomePage();
                                        char c = 65535;
                                        switch (homePage.hashCode()) {
                                            case -1406075965:
                                                if (homePage.equals("Webpage")) {
                                                    c = 2;
                                                    break;
                                                }
                                                break;
                                            case -1085510111:
                                                if (homePage.equals("Default")) {
                                                    c = 0;
                                                    break;
                                                }
                                                break;
                                            case -253812259:
                                                if (homePage.equals("Bookmarks")) {
                                                    c = 3;
                                                    break;
                                                }
                                                break;
                                            case 64266548:
                                                if (homePage.equals("Blank")) {
                                                    c = 1;
                                                    break;
                                                }
                                                break;
                                        }
                                        if (c == 0) {
                                            BrowserActivity.this.custom_web.setVisibility(View.VISIBLE);
                                            BrowserActivity.web_view.setVisibility(View.GONE);
                                        } else if (c == 1) {
                                            BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                            BrowserActivity.web_view.setVisibility(View.GONE);
                                        } else if (c == 2) {
                                            BrowserActivity.web_view.loadUrl("https://www.google.co.in/");
                                            BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                            BrowserActivity.web_view.setVisibility(View.VISIBLE);
                                        } else if (c == 3) {
                                            BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                                        }
                                        BrowserActivity.this.search_link.setText("");
                                        imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                                    }
                                });
                                ((LinearLayout) inflate.findViewById(R.id.web_next)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        Decorator.this.LoadnextWeb();
                                    }
                                });
                                ((LinearLayout) inflate.findViewById(R.id.web_previous)).setOnClickListener(new View.OnClickListener() {


                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        Decorator.this.loadPriviousWeb();
                                        imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                                    }
                                });
                                linearLayout2.setOnClickListener(new View.OnClickListener() {


                                    @SuppressLint("ResourceType")
                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        if (BrowserActivity.this.custom_web.getVisibility() == View.GONE) {
                                            BrowserActivity.this.bookmark_data.clear();
                                            BrowserActivity.this.bookmark_data.addAll(BrowserActivity.this.database.getAllBookmark());
                                            String str = null;
                                            if (BrowserActivity.web_view != null && BrowserActivity.web_view.getUrl() != null) {
                                                boolean z = false;
                                                int i = 0;
                                                while (true) {
                                                    if (i >= BrowserActivity.this.bookmark_data.size()) {
                                                        break;
                                                    } else if (BrowserActivity.this.bookmark_data.get(i).getUrl().equals(BrowserActivity.web_view.getUrl())) {
                                                        str = BrowserActivity.this.bookmark_data.get(i).getId();
                                                        z = true;
                                                        break;
                                                    } else {
                                                        i++;
                                                    }
                                                }
                                                if (z) {
                                                    BrowserActivity.this.database.deleteBookmark(str);
                                                    imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                                                    return;
                                                }
                                                BrowserActivity.this.bookmark_dialog = new Dialog(BrowserActivity.this, R.style.WideDialog);
                                                BrowserActivity.this.bookmark_dialog.setContentView(R.layout.dialog_bookmark);
                                                BrowserActivity.this.bookmark_dialog.getWindow().setBackgroundDrawableResource(17170445);
                                                final EditText editText = (EditText) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_edit_title);
                                                final EditText editText2 = (EditText) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_edit_url);
                                                ((TextView) BrowserActivity.this.bookmark_dialog.findViewById(R.id.dialog_title)).setText("Add Bookmark");
                                                editText.setText(BrowserActivity.web_view.getTitle());
                                                editText2.setText(BrowserActivity.web_view.getOriginalUrl());
                                                ((Button) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_cancel)).setOnClickListener(new View.OnClickListener() {


                                                    public void onClick(View view) {
                                                        BrowserActivity.this.bookmark_dialog.dismiss();
                                                    }
                                                });
                                                ((Button) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_done)).setOnClickListener(new View.OnClickListener() {


                                                    public void onClick(View view) {
                                                        if (editText.getText().toString().trim().length() <= 0) {
                                                            Toast.makeText(BrowserActivity.this, "Enter Title...", Toast.LENGTH_SHORT).show();
                                                        } else if (editText2.getText().toString().trim().length() > 0) {
                                                            BookmarkData bookmark_Data = new BookmarkData();
                                                            bookmark_Data.setName(BrowserActivity.web_view.getTitle());
                                                            bookmark_Data.setUrl(BrowserActivity.web_view.getOriginalUrl());
                                                            if (BrowserActivity.web_view.getFavicon() != null) {
                                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                                BrowserActivity.web_view.getFavicon().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                                bookmark_Data.setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
                                                            } else {
                                                                bookmark_Data.setImage("null");
                                                            }
                                                            BrowserActivity.this.database.addBookmark(bookmark_Data);
                                                            BrowserActivity.this.bookmark_dialog.dismiss();
                                                            imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_bookmark));
                                                        } else {
                                                            Toast.makeText(BrowserActivity.this, "Enter Url...", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                BrowserActivity.this.bookmark_dialog.show();
                                            }
                                        }
                                    }
                                });
                                popupWindow.setFocusable(true);
                                if (Build.VERSION.SDK_INT >= 21) {
                                    popupWindow.setElevation(20.0f);
                                }
                                popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(BrowserActivity.this, R.drawable.transperent_bg));
                                popupWindow.setContentView(inflate);
                                if (BrowserActivity.this.custom_web.getVisibility() == View.GONE) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    BrowserActivity.this.initBookmark(imageView);
                                } else {
                                    linearLayout.setVisibility(View.GONE);
                                }
                                popupWindow.showAtLocation(inflate, 85, 60, 60);
                                popupWindow.showAsDropDown(view);
                            }
                        });
                        BrowserActivity.this.web_voice_search.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                Decorator.this.startVoiceRecognitionActivity();
                            }
                        });
                        BrowserActivity.this.search_text.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                BrowserActivity.this.fragmentLoad(new SearchFragment("", 0, BrowserActivity.this));
                            }
                        });
                        BrowserActivity.this.search_link.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                if (BrowserActivity.web_view.getUrl() == null || BrowserActivity.this.custom_web.getVisibility() == View.GONE) {
                                    BrowserActivity.this.fragmentLoad(new SearchFragment("", 0, BrowserActivity.this));
                                } else {
                                    BrowserActivity.this.fragmentLoad(new SearchFragment(BrowserActivity.web_view.getUrl(), 0, BrowserActivity.this));
                                }
                            }
                        });
                        BrowserActivity.this.refresh.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                BrowserActivity.web_view.reload();
                            }
                        });
                        BrowserActivity.this.search_up.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                BrowserActivity.web_view.findNext(false);
                            }
                        });
                        BrowserActivity.this.search_down.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                BrowserActivity.web_view.findNext(true);
                            }
                        });
                        BrowserActivity.this.search_close.setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                BrowserActivity.this.search_lay.setVisibility(View.GONE);
                                BrowserActivity.this.bottom_button.setVisibility(View.VISIBLE);
                                BrowserActivity.this.search_keyword.setText("");
                                BrowserActivity.web_view.findAllAsync("");
                            }
                        });
                        BrowserActivity.web_view.setWebViewClient(new WebViewClient() {

                            private Map<String, Boolean> loadedUrls = new HashMap();

                            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                                super.onPageStarted(webView, str, bitmap);
                                BrowserActivity.this.isload = true;
                                BrowserActivity.this.bookmark_data.clear();
                                BrowserActivity.this.bookmark_data.addAll(BrowserActivity.this.database.getAllBookmark());
                                for (int i = 0; i < BrowserActivity.this.bookmark_data.size(); i++) {
                                    BrowserActivity.this.bookmark_data.get(i).getUrl().equals(str);
                                }
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getTitle());
                            }

                            public void onPageFinished(WebView webView, String str) {
                                super.onPageFinished(webView, str);
                                String urlBox = MyApplication.getUrlBox();
                                int hashCode = urlBox.hashCode();
                                if (hashCode == 84303) {
                                    urlBox.equals("URL");
                                } else if (hashCode == 80818744) {
                                    urlBox.equals("Title");
                                } else if (hashCode == 1751015924 && !urlBox.equals("Domain (default)")) {
                                }
                            }

                            @Override
                            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                                boolean z;
                                if (Build.VERSION.SDK_INT < 21 || !MyApplication.getAdBlock()) {
                                    z = false;
                                } else if (!this.loadedUrls.containsKey(webResourceRequest.getUrl())) {
                                    z = AdBlocker.isAd(webResourceRequest.getUrl().toString());
                                    this.loadedUrls.put(webResourceRequest.getUrl().toString(), Boolean.valueOf(z));
                                } else {
                                    z = this.loadedUrls.get(webResourceRequest.getUrl()).booleanValue();
                                }
                                if (z) {
                                    return AdBlocker.createEmptyResource();
                                }
                                return super.shouldInterceptRequest(webView, webResourceRequest);
                            }
                        });
                        BrowserActivity.web_view.setDownloadListener(new DownloadListener() {


                            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(1);
                                if (MyApplication.getDownloadPath().contains("/storage/emulated/0/")) {
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                                    ((DownloadManager) BrowserActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                                    return;
                                }
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                                ((DownloadManager) BrowserActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                            }
                        });
                        BrowserActivity.web_view.setWebChromeClient(new WebChromeClient() {


                            public void onProgressChanged(WebView webView, int i) {
                                super.onProgressChanged(webView, i);
                                BrowserActivity.this.progress.setVisibility(View.VISIBLE);
                                BrowserActivity.this.progress.setProgress(i);
                                if (i == 100) {
                                    BrowserActivity.this.progress.setVisibility(View.INVISIBLE);
                                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                    BrowserActivity.web_view.setVisibility(View.VISIBLE);
                                }
                            }

                            public void onReceivedTitle(WebView webView, String str) {
                                super.onReceivedTitle(webView, str);
                                BrowserActivity.this.setTitle(str);
                            }

                            public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                                super.onReceivedIcon(webView, bitmap);
                                if (BrowserActivity.this.isload) {
                                    BrowserActivity.this.isload = false;
                                    HistoryData history_Data = new HistoryData();
                                    history_Data.setName(webView.getTitle());
                                    history_Data.setUrl(webView.getOriginalUrl());
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                    history_Data.setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
                                    history_Data.setDate(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                                    if (MyApplication.getRequestData()) {
                                        BrowserActivity.this.database.addHistory(history_Data);
                                    }
                                }
                            }
                        });
                        BrowserActivity.this.tab_switcher_button.setOnClickListener(BrowserActivity.this.createTabSwitcherButtonListener());
                    }
                } else if (userAgent.equals("Default")) {
                    MyApplication.getTextEncoding().hashCode();
                    MyApplication.getBlockImage();
                    MyApplication.getEnableJava();
                    MyApplication.getCookies();
                    ((ImageView) findViewById(R.id.web_next_img)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            Decorator.this.LoadnextWeb();
                        }
                    });
                    ((ImageView) findViewById(R.id.web_privious_img)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            Decorator.this.loadPriviousWeb();
                        }
                    });
                    BrowserActivity.this.web_menu.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            final PopupWindow popupWindow = new PopupWindow(BrowserActivity.this);
                            View inflate = ((LayoutInflater) BrowserActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_menu, (ViewGroup) null);
                            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.control_btn);
                            final ImageView imageView = (ImageView) inflate.findViewById(R.id.bookmark_image);
                            LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.bookmark);
                            ((TextView) inflate.findViewById(R.id.new_tab)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    tabSwitcher.addTab(BrowserActivity.this.createTab(tabSwitcher.getCount()), 0, BrowserActivity.this.createRevealAnimation());
                                }
                            });
                            ((TextView) inflate.findViewById(R.id.private_tab)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, IncognitoActivity.class));
                                    BrowserActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                }
                            });
                            ((TextView) inflate.findViewById(R.id.bookmarks)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                                }
                            });
                            ((TextView) inflate.findViewById(R.id.history)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    BrowserActivity.this.fragmentLoad(new HistoryFragment(0));
                                }
                            });
                            ((TextView) inflate.findViewById(R.id.download)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    Adshandler.ShowIntertialads(BrowserActivity.this, DownloadActivity.class);
                                }
                            });
                            ((TextView) inflate.findViewById(R.id.find)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                        BrowserActivity.this.search();
                                    }
                                }
                            });
                            ((TextView) inflate.findViewById(R.id.copy)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                        ((ClipboardManager) BrowserActivity.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("copy", BrowserActivity.web_view.getUrl()));
                                        Toast.makeText(context, "Link copied", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            ((TextView) inflate.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.SEND");
                                        intent.putExtra("android.intent.extra.SUBJECT", BrowserActivity.web_view.getTitle());
                                        intent.putExtra("android.intent.extra.TEXT", BrowserActivity.web_view.getUrl());
                                        intent.setType(HTTP.PLAIN_TEXT_TYPE);
                                        BrowserActivity.this.startActivity(intent);
                                    }
                                }
                            });
                            ((TextView) inflate.findViewById(R.id.setting)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, SettingActivity.class));
                                }
                            });
                            ((LinearLayout) inflate.findViewById(R.id.web_home)).setOnClickListener(new View.OnClickListener() {


                                @SuppressLint("WrongConstant")
                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    CookieManager.getInstance().setAcceptCookie(false);
                                    BrowserActivity.web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                                    BrowserActivity.web_view.getSettings().setAppCacheEnabled(false);
                                    BrowserActivity.web_view.clearHistory();
                                    BrowserActivity.web_view.clearCache(true);
                                    BrowserActivity.web_view.clearFormData();
                                    BrowserActivity.web_view.getSettings().setSavePassword(false);
                                    BrowserActivity.web_view.getSettings().setSaveFormData(false);
                                    if (BrowserActivity.web_view.canGoBack()) {
                                        BrowserActivity.this.temp_next = true;
                                    } else if (BrowserActivity.this.custom_web.getVisibility() != View.VISIBLE) {
                                        BrowserActivity.this.temp_next = true;
                                    }
                                    String homePage = MyApplication.getHomePage();
                                    char c = 65535;
                                    switch (homePage.hashCode()) {
                                        case -1406075965:
                                            if (homePage.equals("Webpage")) {
                                                c = 2;
                                                break;
                                            }
                                            break;
                                        case -1085510111:
                                            if (homePage.equals("Default")) {
                                                c = 0;
                                                break;
                                            }
                                            break;
                                        case -253812259:
                                            if (homePage.equals("Bookmarks")) {
                                                c = 3;
                                                break;
                                            }
                                            break;
                                        case 64266548:
                                            if (homePage.equals("Blank")) {
                                                c = 1;
                                                break;
                                            }
                                            break;
                                    }
                                    if (c == 0) {
                                        BrowserActivity.this.custom_web.setVisibility(View.VISIBLE);
                                        BrowserActivity.web_view.setVisibility(View.GONE);
                                    } else if (c == 1) {
                                        BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                        BrowserActivity.web_view.setVisibility(View.GONE);
                                    } else if (c == 2) {
                                        BrowserActivity.web_view.loadUrl("https://www.google.co.in/");
                                        BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                        BrowserActivity.web_view.setVisibility(View.VISIBLE);
                                    } else if (c == 3) {
                                        BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                                    }
                                    BrowserActivity.this.search_link.setText("");
                                    imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                                }
                            });
                            ((LinearLayout) inflate.findViewById(R.id.web_next)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    Decorator.this.LoadnextWeb();
                                }
                            });
                            ((LinearLayout) inflate.findViewById(R.id.web_previous)).setOnClickListener(new View.OnClickListener() {


                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    Decorator.this.loadPriviousWeb();
                                    imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                                }
                            });
                            linearLayout2.setOnClickListener(new View.OnClickListener() {


                                @SuppressLint("ResourceType")
                                public void onClick(View view) {
                                    popupWindow.dismiss();
                                    if (BrowserActivity.this.custom_web.getVisibility() == View.GONE) {
                                        BrowserActivity.this.bookmark_data.clear();
                                        BrowserActivity.this.bookmark_data.addAll(BrowserActivity.this.database.getAllBookmark());
                                        String str = null;
                                        if (BrowserActivity.web_view != null && BrowserActivity.web_view.getUrl() != null) {
                                            boolean z = false;
                                            int i = 0;
                                            while (true) {
                                                if (i >= BrowserActivity.this.bookmark_data.size()) {
                                                    break;
                                                } else if (BrowserActivity.this.bookmark_data.get(i).getUrl().equals(BrowserActivity.web_view.getUrl())) {
                                                    str = BrowserActivity.this.bookmark_data.get(i).getId();
                                                    z = true;
                                                    break;
                                                } else {
                                                    i++;
                                                }
                                            }
                                            if (z) {
                                                BrowserActivity.this.database.deleteBookmark(str);
                                                imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                                                return;
                                            }
                                            BrowserActivity.this.bookmark_dialog = new Dialog(BrowserActivity.this, R.style.WideDialog);
                                            BrowserActivity.this.bookmark_dialog.setContentView(R.layout.dialog_bookmark);
                                            BrowserActivity.this.bookmark_dialog.getWindow().setBackgroundDrawableResource(17170445);
                                            final EditText editText = (EditText) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_edit_title);
                                            final EditText editText2 = (EditText) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_edit_url);
                                            ((TextView) BrowserActivity.this.bookmark_dialog.findViewById(R.id.dialog_title)).setText("Add Bookmark");
                                            editText.setText(BrowserActivity.web_view.getTitle());
                                            editText2.setText(BrowserActivity.web_view.getOriginalUrl());
                                            ((Button) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_cancel)).setOnClickListener(new View.OnClickListener() {


                                                public void onClick(View view) {
                                                    BrowserActivity.this.bookmark_dialog.dismiss();
                                                }
                                            });
                                            ((Button) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_done)).setOnClickListener(new View.OnClickListener() {


                                                public void onClick(View view) {
                                                    if (editText.getText().toString().trim().length() <= 0) {
                                                        Toast.makeText(BrowserActivity.this, "Enter Title...", Toast.LENGTH_SHORT).show();
                                                    } else if (editText2.getText().toString().trim().length() > 0) {
                                                        BookmarkData bookmark_Data = new BookmarkData();
                                                        bookmark_Data.setName(BrowserActivity.web_view.getTitle());
                                                        bookmark_Data.setUrl(BrowserActivity.web_view.getOriginalUrl());
                                                        if (BrowserActivity.web_view.getFavicon() != null) {
                                                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                            BrowserActivity.web_view.getFavicon().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                            bookmark_Data.setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
                                                        } else {
                                                            bookmark_Data.setImage("null");
                                                        }
                                                        BrowserActivity.this.database.addBookmark(bookmark_Data);
                                                        BrowserActivity.this.bookmark_dialog.dismiss();
                                                        imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_bookmark));
                                                    } else {
                                                        Toast.makeText(BrowserActivity.this, "Enter Url...", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            BrowserActivity.this.bookmark_dialog.show();
                                        }
                                    }
                                }
                            });
                            popupWindow.setFocusable(true);
                            if (Build.VERSION.SDK_INT >= 21) {
                                popupWindow.setElevation(20.0f);
                            }
                            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(BrowserActivity.this, R.drawable.transperent_bg));
                            popupWindow.setContentView(inflate);
                            if (BrowserActivity.this.custom_web.getVisibility() == View.GONE) {
                                linearLayout.setVisibility(View.VISIBLE);
                                BrowserActivity.this.initBookmark(imageView);
                            } else {
                                linearLayout.setVisibility(View.GONE);
                            }
                            popupWindow.showAtLocation(inflate, 85, 60, 60);
                            popupWindow.showAsDropDown(view);
                        }
                    });
                    BrowserActivity.this.web_voice_search.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            Decorator.this.startVoiceRecognitionActivity();
                        }
                    });
                    BrowserActivity.this.search_text.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            BrowserActivity.this.fragmentLoad(new SearchFragment("", 0, BrowserActivity.this));
                        }
                    });
                    BrowserActivity.this.search_link.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            if (BrowserActivity.web_view.getUrl() == null || BrowserActivity.this.custom_web.getVisibility() == View.VISIBLE) {
                                BrowserActivity.this.fragmentLoad(new SearchFragment("", 0, BrowserActivity.this));
                            } else {
                                BrowserActivity.this.fragmentLoad(new SearchFragment(BrowserActivity.web_view.getUrl(), 0, BrowserActivity.this));
                            }
                        }
                    });
                    BrowserActivity.this.refresh.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            BrowserActivity.web_view.reload();
                        }
                    });
                    BrowserActivity.this.search_up.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            BrowserActivity.web_view.findNext(false);
                        }
                    });
                    BrowserActivity.this.search_down.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            BrowserActivity.web_view.findNext(true);
                        }
                    });
                    BrowserActivity.this.search_close.setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            BrowserActivity.this.search_lay.setVisibility(View.GONE);
                            BrowserActivity.this.bottom_button.setVisibility(View.VISIBLE);
                            BrowserActivity.this.search_keyword.setText("");
                            BrowserActivity.web_view.findAllAsync("");
                        }
                    });
                    BrowserActivity.web_view.setWebViewClient(new WebViewClient() {

                        private Map<String, Boolean> loadedUrls = new HashMap();

                        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                            super.onPageStarted(webView, str, bitmap);
                            BrowserActivity.this.isload = true;
                            BrowserActivity.this.bookmark_data.clear();
                            BrowserActivity.this.bookmark_data.addAll(BrowserActivity.this.database.getAllBookmark());
                            for (int i = 0; i < BrowserActivity.this.bookmark_data.size(); i++) {
                                BrowserActivity.this.bookmark_data.get(i).getUrl().equals(str);
                            }
                            BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getTitle());
                        }

                        public void onPageFinished(WebView webView, String str) {
                            super.onPageFinished(webView, str);
                            String urlBox = MyApplication.getUrlBox();
                            int hashCode = urlBox.hashCode();
                            if (hashCode == 84303) {
                                urlBox.equals("URL");
                            } else if (hashCode == 80818744) {
                                urlBox.equals("Title");
                            } else if (hashCode == 1751015924 && !urlBox.equals("Domain (default)")) {
                            }
                        }

                        @Override
                        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                            boolean z;
                            if (Build.VERSION.SDK_INT < 21 || !MyApplication.getAdBlock()) {
                                z = false;
                            } else if (!this.loadedUrls.containsKey(webResourceRequest.getUrl())) {
                                z = AdBlocker.isAd(webResourceRequest.getUrl().toString());
                                this.loadedUrls.put(webResourceRequest.getUrl().toString(), Boolean.valueOf(z));
                            } else {
                                z = this.loadedUrls.get(webResourceRequest.getUrl()).booleanValue();
                            }
                            if (z) {
                                return AdBlocker.createEmptyResource();
                            }
                            return super.shouldInterceptRequest(webView, webResourceRequest);
                        }
                    });
                    BrowserActivity.web_view.setDownloadListener(new DownloadListener() {


                        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(1);
                            if (MyApplication.getDownloadPath().contains("/storage/emulated/0/")) {
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                                ((DownloadManager) BrowserActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                                return;
                            }
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                            ((DownloadManager) BrowserActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                        }
                    });
                    BrowserActivity.web_view.setWebChromeClient(new WebChromeClient() {


                        public void onProgressChanged(WebView webView, int i) {
                            super.onProgressChanged(webView, i);
                            BrowserActivity.this.progress.setVisibility(View.VISIBLE);
                            BrowserActivity.this.progress.setProgress(i);
                            if (i == 100) {
                                BrowserActivity.this.progress.setVisibility(View.INVISIBLE);
                                BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                BrowserActivity.web_view.setVisibility(View.VISIBLE);
                            }
                        }

                        public void onReceivedTitle(WebView webView, String str) {
                            super.onReceivedTitle(webView, str);
                            BrowserActivity.this.setTitle(str);
                        }

                        public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                            super.onReceivedIcon(webView, bitmap);
                            if (BrowserActivity.this.isload) {
                                BrowserActivity.this.isload = false;
                                HistoryData history_Data = new HistoryData();
                                history_Data.setName(webView.getTitle());
                                history_Data.setUrl(webView.getOriginalUrl());
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                history_Data.setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
                                history_Data.setDate(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                                if (MyApplication.getRequestData()) {
                                    BrowserActivity.this.database.addHistory(history_Data);
                                }
                            }
                        }
                    });
                    BrowserActivity.this.tab_switcher_button.setOnClickListener(BrowserActivity.this.createTabSwitcherButtonListener());
                }
            } else if (userAgent.equals("Mobile")) {
                MyApplication.getTextEncoding().hashCode();
                MyApplication.getBlockImage();
                MyApplication.getEnableJava();
                MyApplication.getCookies();
                ((ImageView) findViewById(R.id.web_next_img)).setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        Decorator.this.LoadnextWeb();
                    }
                });
                ((ImageView) findViewById(R.id.web_privious_img)).setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        Decorator.this.loadPriviousWeb();
                    }
                });
                BrowserActivity.this.web_menu.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        final PopupWindow popupWindow = new PopupWindow(BrowserActivity.this);
                        View inflate = ((LayoutInflater) BrowserActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_menu, (ViewGroup) null);
                        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.control_btn);
                        final ImageView imageView = (ImageView) inflate.findViewById(R.id.bookmark_image);
                        LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.bookmark);
                        ((TextView) inflate.findViewById(R.id.new_tab)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                tabSwitcher.addTab(BrowserActivity.this.createTab(tabSwitcher.getCount()), 0, BrowserActivity.this.createRevealAnimation());
                            }
                        });
                        ((TextView) inflate.findViewById(R.id.private_tab)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, IncognitoActivity.class));
                                BrowserActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                        });
                        ((TextView) inflate.findViewById(R.id.bookmarks)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                            }
                        });
                        ((TextView) inflate.findViewById(R.id.history)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                BrowserActivity.this.fragmentLoad(new HistoryFragment(0));
                            }
                        });
                        ((TextView) inflate.findViewById(R.id.download)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Adshandler.ShowIntertialads(BrowserActivity.this, DownloadActivity.class);
                            }
                        });
                        ((TextView) inflate.findViewById(R.id.find)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                    BrowserActivity.this.search();
                                }
                            }
                        });
                        ((TextView) inflate.findViewById(R.id.copy)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                    ((ClipboardManager) BrowserActivity.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("copy", BrowserActivity.web_view.getUrl()));
                                    Toast.makeText(context, "Link copied", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        ((TextView) inflate.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.SEND");
                                    intent.putExtra("android.intent.extra.SUBJECT", BrowserActivity.web_view.getTitle());
                                    intent.putExtra("android.intent.extra.TEXT", BrowserActivity.web_view.getUrl());
                                    intent.setType(HTTP.PLAIN_TEXT_TYPE);
                                    BrowserActivity.this.startActivity(intent);
                                }
                            }
                        });
                        ((TextView) inflate.findViewById(R.id.setting)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, SettingActivity.class));
                            }
                        });
                        ((LinearLayout) inflate.findViewById(R.id.web_home)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                CookieManager.getInstance().setAcceptCookie(false);
                                BrowserActivity.web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                                BrowserActivity.web_view.getSettings().setAppCacheEnabled(false);
                                BrowserActivity.web_view.clearHistory();
                                BrowserActivity.web_view.clearCache(true);
                                BrowserActivity.web_view.clearFormData();
                                BrowserActivity.web_view.getSettings().setSavePassword(false);
                                BrowserActivity.web_view.getSettings().setSaveFormData(false);
                                if (BrowserActivity.web_view.canGoBack()) {
                                    BrowserActivity.this.temp_next = true;
                                } else if (BrowserActivity.this.custom_web.getVisibility() != View.VISIBLE) {
                                    BrowserActivity.this.temp_next = true;
                                }
                                String homePage = MyApplication.getHomePage();
                                char c = 65535;
                                switch (homePage.hashCode()) {
                                    case -1406075965:
                                        if (homePage.equals("Webpage")) {
                                            c = 2;
                                            break;
                                        }
                                        break;
                                    case -1085510111:
                                        if (homePage.equals("Default")) {
                                            c = 0;
                                            break;
                                        }
                                        break;
                                    case -253812259:
                                        if (homePage.equals("Bookmarks")) {
                                            c = 3;
                                            break;
                                        }
                                        break;
                                    case 64266548:
                                        if (homePage.equals("Blank")) {
                                            c = 1;
                                            break;
                                        }
                                        break;
                                }
                                if (c == 0) {
                                    BrowserActivity.this.custom_web.setVisibility(View.VISIBLE);
                                    BrowserActivity.web_view.setVisibility(View.GONE);
                                } else if (c == 1) {
                                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                    BrowserActivity.web_view.setVisibility(View.GONE);
                                } else if (c == 2) {
                                    BrowserActivity.web_view.loadUrl("https://www.google.co.in/");
                                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                    BrowserActivity.web_view.setVisibility(View.VISIBLE);
                                } else if (c == 3) {
                                    BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                                }
                                BrowserActivity.this.search_link.setText("");
                                imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                            }
                        });
                        ((LinearLayout) inflate.findViewById(R.id.web_next)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Decorator.this.LoadnextWeb();
                            }
                        });
                        ((LinearLayout) inflate.findViewById(R.id.web_previous)).setOnClickListener(new View.OnClickListener() {


                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Decorator.this.loadPriviousWeb();
                                imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                            }
                        });
                        linearLayout2.setOnClickListener(new View.OnClickListener() {


                            @SuppressLint("ResourceType")
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                if (BrowserActivity.this.custom_web.getVisibility() == View.GONE) {
                                    BrowserActivity.this.bookmark_data.clear();
                                    BrowserActivity.this.bookmark_data.addAll(BrowserActivity.this.database.getAllBookmark());
                                    String str = null;
                                    if (BrowserActivity.web_view != null && BrowserActivity.web_view.getUrl() != null) {
                                        boolean z = false;
                                        int i = 0;
                                        while (true) {
                                            if (i >= BrowserActivity.this.bookmark_data.size()) {
                                                break;
                                            } else if (BrowserActivity.this.bookmark_data.get(i).getUrl().equals(BrowserActivity.web_view.getUrl())) {
                                                str = BrowserActivity.this.bookmark_data.get(i).getId();
                                                z = true;
                                                break;
                                            } else {
                                                i++;
                                            }
                                        }
                                        if (z) {
                                            BrowserActivity.this.database.deleteBookmark(str);
                                            imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                                            return;
                                        }
                                        BrowserActivity.this.bookmark_dialog = new Dialog(BrowserActivity.this, R.style.WideDialog);
                                        BrowserActivity.this.bookmark_dialog.setContentView(R.layout.dialog_bookmark);
                                        BrowserActivity.this.bookmark_dialog.getWindow().setBackgroundDrawableResource(17170445);
                                        final EditText editText = (EditText) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_edit_title);
                                        final EditText editText2 = (EditText) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_edit_url);
                                        ((TextView) BrowserActivity.this.bookmark_dialog.findViewById(R.id.dialog_title)).setText("Add Bookmark");
                                        editText.setText(BrowserActivity.web_view.getTitle());
                                        editText2.setText(BrowserActivity.web_view.getOriginalUrl());
                                        ((Button) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_cancel)).setOnClickListener(new View.OnClickListener() {


                                            public void onClick(View view) {
                                                BrowserActivity.this.bookmark_dialog.dismiss();
                                            }
                                        });
                                        ((Button) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_done)).setOnClickListener(new View.OnClickListener() {


                                            public void onClick(View view) {
                                                if (editText.getText().toString().trim().length() <= 0) {
                                                    Toast.makeText(BrowserActivity.this, "Enter Title...", Toast.LENGTH_SHORT).show();
                                                } else if (editText2.getText().toString().trim().length() > 0) {
                                                    BookmarkData bookmark_Data = new BookmarkData();
                                                    bookmark_Data.setName(BrowserActivity.web_view.getTitle());
                                                    bookmark_Data.setUrl(BrowserActivity.web_view.getOriginalUrl());
                                                    if (BrowserActivity.web_view.getFavicon() != null) {
                                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                        BrowserActivity.web_view.getFavicon().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                        bookmark_Data.setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
                                                    } else {
                                                        bookmark_Data.setImage("null");
                                                    }
                                                    BrowserActivity.this.database.addBookmark(bookmark_Data);
                                                    BrowserActivity.this.bookmark_dialog.dismiss();
                                                    imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_bookmark));
                                                } else {
                                                    Toast.makeText(BrowserActivity.this, "Enter Url...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        BrowserActivity.this.bookmark_dialog.show();
                                    }
                                }
                            }
                        });
                        popupWindow.setFocusable(true);
                        if (Build.VERSION.SDK_INT >= 21) {
                            popupWindow.setElevation(20.0f);
                        }
                        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(BrowserActivity.this, R.drawable.transperent_bg));
                        popupWindow.setContentView(inflate);
                        if (BrowserActivity.this.custom_web.getVisibility() == View.GONE) {
                            linearLayout.setVisibility(View.VISIBLE);
                            BrowserActivity.this.initBookmark(imageView);
                        } else {
                            linearLayout.setVisibility(View.GONE);
                        }
                        popupWindow.showAtLocation(inflate, 85, 60, 60);
                        popupWindow.showAsDropDown(view);
                    }
                });
                BrowserActivity.this.web_voice_search.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        Decorator.this.startVoiceRecognitionActivity();
                    }
                });
                BrowserActivity.this.search_text.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        BrowserActivity.this.fragmentLoad(new SearchFragment("", 0, BrowserActivity.this));
                    }
                });
                BrowserActivity.this.search_link.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        if (BrowserActivity.web_view.getUrl() == null || BrowserActivity.this.custom_web.getVisibility() == View.VISIBLE) {
                            BrowserActivity.this.fragmentLoad(new SearchFragment("", 0, BrowserActivity.this));
                        } else {
                            BrowserActivity.this.fragmentLoad(new SearchFragment(BrowserActivity.web_view.getUrl(), 0, BrowserActivity.this));
                        }
                    }
                });
                BrowserActivity.this.refresh.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        BrowserActivity.web_view.reload();
                    }
                });
                BrowserActivity.this.search_up.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        BrowserActivity.web_view.findNext(false);
                    }
                });
                BrowserActivity.this.search_down.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        BrowserActivity.web_view.findNext(true);
                    }
                });
                BrowserActivity.this.search_close.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        BrowserActivity.this.search_lay.setVisibility(View.GONE);
                        BrowserActivity.this.bottom_button.setVisibility(View.VISIBLE);
                        BrowserActivity.this.search_keyword.setText("");
                        BrowserActivity.web_view.findAllAsync("");
                    }
                });
                BrowserActivity.web_view.setWebViewClient(new WebViewClient() {

                    private Map<String, Boolean> loadedUrls = new HashMap();

                    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                        super.onPageStarted(webView, str, bitmap);
                        BrowserActivity.this.isload = true;
                        BrowserActivity.this.bookmark_data.clear();
                        BrowserActivity.this.bookmark_data.addAll(BrowserActivity.this.database.getAllBookmark());
                        for (int i = 0; i < BrowserActivity.this.bookmark_data.size(); i++) {
                            BrowserActivity.this.bookmark_data.get(i).getUrl().equals(str);
                        }
                        BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getTitle());
                    }

                    public void onPageFinished(WebView webView, String str) {
                        super.onPageFinished(webView, str);
                        String urlBox = MyApplication.getUrlBox();
                        int hashCode = urlBox.hashCode();
                        if (hashCode == 84303) {
                            urlBox.equals("URL");
                        } else if (hashCode == 80818744) {
                            urlBox.equals("Title");
                        } else if (hashCode == 1751015924 && !urlBox.equals("Domain (default)")) {
                        }
                    }

                    @Override
                    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                        boolean z;
                        if (Build.VERSION.SDK_INT < 21 || !MyApplication.getAdBlock()) {
                            z = false;
                        } else if (!this.loadedUrls.containsKey(webResourceRequest.getUrl())) {
                            z = AdBlocker.isAd(webResourceRequest.getUrl().toString());
                            this.loadedUrls.put(webResourceRequest.getUrl().toString(), Boolean.valueOf(z));
                        } else {
                            z = this.loadedUrls.get(webResourceRequest.getUrl()).booleanValue();
                        }
                        if (z) {
                            return AdBlocker.createEmptyResource();
                        }
                        return super.shouldInterceptRequest(webView, webResourceRequest);
                    }
                });
                BrowserActivity.web_view.setDownloadListener(new DownloadListener() {


                    public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(1);
                        if (MyApplication.getDownloadPath().contains("/storage/emulated/0/")) {
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                            ((DownloadManager) BrowserActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                            return;
                        }
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                        ((DownloadManager) BrowserActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                    }
                });
                BrowserActivity.web_view.setWebChromeClient(new WebChromeClient() {


                    public void onProgressChanged(WebView webView, int i) {
                        super.onProgressChanged(webView, i);
                        BrowserActivity.this.progress.setVisibility(View.VISIBLE);
                        BrowserActivity.this.progress.setProgress(i);
                        if (i == 100) {
                            BrowserActivity.this.progress.setVisibility(View.INVISIBLE);
                            BrowserActivity.this.custom_web.setVisibility(View.GONE);
                            BrowserActivity.web_view.setVisibility(View.VISIBLE);
                        }
                    }

                    public void onReceivedTitle(WebView webView, String str) {
                        super.onReceivedTitle(webView, str);
                        BrowserActivity.this.setTitle(str);
                    }

                    public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                        super.onReceivedIcon(webView, bitmap);
                        if (BrowserActivity.this.isload) {
                            BrowserActivity.this.isload = false;
                            HistoryData history_Data = new HistoryData();
                            history_Data.setName(webView.getTitle());
                            history_Data.setUrl(webView.getOriginalUrl());
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            history_Data.setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
                            history_Data.setDate(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                            if (MyApplication.getRequestData()) {
                                BrowserActivity.this.database.addHistory(history_Data);
                            }
                        }
                    }
                });
                BrowserActivity.this.tab_switcher_button.setOnClickListener(BrowserActivity.this.createTabSwitcherButtonListener());
            }
            MyApplication.getTextEncoding().hashCode();
            MyApplication.getBlockImage();
            MyApplication.getEnableJava();
            MyApplication.getCookies();
            ((ImageView) findViewById(R.id.web_next_img)).setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    Decorator.this.LoadnextWeb();
                }
            });
            ((ImageView) findViewById(R.id.web_privious_img)).setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    Decorator.this.loadPriviousWeb();
                }
            });
            BrowserActivity.this.web_menu.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    final PopupWindow popupWindow = new PopupWindow(BrowserActivity.this);
                    View inflate = ((LayoutInflater) BrowserActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_menu, (ViewGroup) null);
                    LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.control_btn);
                    final ImageView imageView = (ImageView) inflate.findViewById(R.id.bookmark_image);
                    LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.bookmark);
                    ((TextView) inflate.findViewById(R.id.new_tab)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            tabSwitcher.addTab(BrowserActivity.this.createTab(tabSwitcher.getCount()), 0, BrowserActivity.this.createRevealAnimation());
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.private_tab)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, IncognitoActivity.class));
                            BrowserActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.bookmarks)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.history)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            BrowserActivity.this.fragmentLoad(new HistoryFragment(0));
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.download)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, DownloadActivity.class));
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.find)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                BrowserActivity.this.search();
                            }
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.copy)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                ((ClipboardManager) BrowserActivity.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("copy", BrowserActivity.web_view.getUrl()));
                                Toast.makeText(context, "Link copied", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (BrowserActivity.this.custom_web.getVisibility() == View.GONE && BrowserActivity.web_view != null) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.SEND");
                                intent.putExtra("android.intent.extra.SUBJECT", BrowserActivity.web_view.getTitle());
                                intent.putExtra("android.intent.extra.TEXT", BrowserActivity.web_view.getUrl());
                                intent.setType(HTTP.PLAIN_TEXT_TYPE);
                                BrowserActivity.this.startActivity(intent);
                            }
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.setting)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            BrowserActivity.this.startActivity(new Intent(BrowserActivity.this, SettingActivity.class));
                        }
                    });
                    ((LinearLayout) inflate.findViewById(R.id.web_home)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            CookieManager.getInstance().setAcceptCookie(false);
                            BrowserActivity.web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                            BrowserActivity.web_view.getSettings().setAppCacheEnabled(false);
                            BrowserActivity.web_view.clearHistory();
                            BrowserActivity.web_view.clearCache(true);
                            BrowserActivity.web_view.clearFormData();
                            BrowserActivity.web_view.getSettings().setSavePassword(false);
                            BrowserActivity.web_view.getSettings().setSaveFormData(false);
                            if (BrowserActivity.web_view.canGoBack()) {
                                BrowserActivity.this.temp_next = true;
                            } else if (BrowserActivity.this.custom_web.getVisibility() != View.VISIBLE) {
                                BrowserActivity.this.temp_next = true;
                            }
                            String homePage = MyApplication.getHomePage();
                            char c = 65535;
                            switch (homePage.hashCode()) {
                                case -1406075965:
                                    if (homePage.equals("Webpage")) {
                                        c = 2;
                                        break;
                                    }
                                    break;
                                case -1085510111:
                                    if (homePage.equals("Default")) {
                                        c = 0;
                                        break;
                                    }
                                    break;
                                case -253812259:
                                    if (homePage.equals("Bookmarks")) {
                                        c = 3;
                                        break;
                                    }
                                    break;
                                case 64266548:
                                    if (homePage.equals("Blank")) {
                                        c = 1;
                                        break;
                                    }
                                    break;
                            }
                            if (c == 0) {
                                BrowserActivity.this.custom_web.setVisibility(View.VISIBLE);
                                BrowserActivity.web_view.setVisibility(View.GONE);
                            } else if (c == 1) {
                                BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                BrowserActivity.web_view.setVisibility(View.GONE);
                            } else if (c == 2) {
                                BrowserActivity.web_view.loadUrl("https://www.google.co.in/");
                                BrowserActivity.this.custom_web.setVisibility(View.GONE);
                                BrowserActivity.web_view.setVisibility(View.VISIBLE);
                            } else if (c == 3) {
                                BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                            }
                            BrowserActivity.this.search_link.setText("");
                            imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                        }
                    });
                    ((LinearLayout) inflate.findViewById(R.id.web_next)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            Decorator.this.LoadnextWeb();
                        }
                    });
                    ((LinearLayout) inflate.findViewById(R.id.web_previous)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            Decorator.this.loadPriviousWeb();
                            imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                        }
                    });
                    linearLayout2.setOnClickListener(new View.OnClickListener() {


                        @SuppressLint("ResourceType")
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (BrowserActivity.this.custom_web.getVisibility() == View.GONE) {
                                BrowserActivity.this.bookmark_data.clear();
                                BrowserActivity.this.bookmark_data.addAll(BrowserActivity.this.database.getAllBookmark());
                                String str = null;
                                if (BrowserActivity.web_view != null && BrowserActivity.web_view.getUrl() != null) {
                                    boolean z = false;
                                    int i = 0;
                                    while (true) {
                                        if (i >= BrowserActivity.this.bookmark_data.size()) {
                                            break;
                                        } else if (BrowserActivity.this.bookmark_data.get(i).getUrl().equals(BrowserActivity.web_view.getUrl())) {
                                            str = BrowserActivity.this.bookmark_data.get(i).getId();
                                            z = true;
                                            break;
                                        } else {
                                            i++;
                                        }
                                    }
                                    if (z) {
                                        BrowserActivity.this.database.deleteBookmark(str);
                                        imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_unbookmark));
                                        return;
                                    }
                                    BrowserActivity.this.bookmark_dialog = new Dialog(BrowserActivity.this, R.style.WideDialog);
                                    BrowserActivity.this.bookmark_dialog.setContentView(R.layout.dialog_bookmark);
                                    BrowserActivity.this.bookmark_dialog.getWindow().setBackgroundDrawableResource(17170445);
                                    final EditText editText = (EditText) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_edit_title);
                                    final EditText editText2 = (EditText) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_edit_url);
                                    ((TextView) BrowserActivity.this.bookmark_dialog.findViewById(R.id.dialog_title)).setText("Add Bookmark");
                                    editText.setText(BrowserActivity.web_view.getTitle());
                                    editText2.setText(BrowserActivity.web_view.getOriginalUrl());
                                    ((Button) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_cancel)).setOnClickListener(new View.OnClickListener() {


                                        public void onClick(View view) {
                                            BrowserActivity.this.bookmark_dialog.dismiss();
                                        }
                                    });
                                    ((Button) BrowserActivity.this.bookmark_dialog.findViewById(R.id.bookmark_done)).setOnClickListener(new View.OnClickListener() {


                                        public void onClick(View view) {
                                            if (editText.getText().toString().trim().length() <= 0) {
                                                Toast.makeText(BrowserActivity.this, "Enter Title...", Toast.LENGTH_SHORT).show();
                                            } else if (editText2.getText().toString().trim().length() > 0) {
                                                BookmarkData bookmark_Data = new BookmarkData();
                                                bookmark_Data.setName(BrowserActivity.web_view.getTitle());
                                                bookmark_Data.setUrl(BrowserActivity.web_view.getOriginalUrl());
                                                if (BrowserActivity.web_view.getFavicon() != null) {
                                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                    BrowserActivity.web_view.getFavicon().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                                    bookmark_Data.setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
                                                } else {
                                                    bookmark_Data.setImage("null");
                                                }
                                                BrowserActivity.this.database.addBookmark(bookmark_Data);
                                                BrowserActivity.this.bookmark_dialog.dismiss();
                                                imageView.setImageDrawable(BrowserActivity.this.getResources().getDrawable(R.drawable.iv_bookmark));
                                            } else {
                                                Toast.makeText(BrowserActivity.this, "Enter Url...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    BrowserActivity.this.bookmark_dialog.show();
                                }
                            }
                        }
                    });
                    popupWindow.setFocusable(true);
                    if (Build.VERSION.SDK_INT >= 21) {
                        popupWindow.setElevation(20.0f);
                    }
                    popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(BrowserActivity.this, R.drawable.transperent_bg));
                    popupWindow.setContentView(inflate);
                    if (BrowserActivity.this.custom_web.getVisibility() == View.GONE) {
                        linearLayout.setVisibility(View.VISIBLE);
                        BrowserActivity.this.initBookmark(imageView);
                    } else {
                        linearLayout.setVisibility(View.GONE);
                    }
                    popupWindow.showAtLocation(inflate, 85, 60, 60);
                    popupWindow.showAsDropDown(view);
                }
            });
            BrowserActivity.this.web_voice_search.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    Decorator.this.startVoiceRecognitionActivity();
                }
            });
            BrowserActivity.this.search_text.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.this.fragmentLoad(new SearchFragment("", 0, BrowserActivity.this));
                }
            });
            BrowserActivity.this.search_link.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    if (BrowserActivity.web_view.getUrl() == null || BrowserActivity.this.custom_web.getVisibility() == View.VISIBLE) {
                        BrowserActivity.this.fragmentLoad(new SearchFragment("", 0, BrowserActivity.this));
                    } else {
                        BrowserActivity.this.fragmentLoad(new SearchFragment(BrowserActivity.web_view.getUrl(), 0, BrowserActivity.this));
                    }
                }
            });
            BrowserActivity.this.refresh.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.web_view.reload();
                }
            });
            BrowserActivity.this.search_up.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.web_view.findNext(false);
                }
            });
            BrowserActivity.this.search_down.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.web_view.findNext(true);
                }
            });
            BrowserActivity.this.search_close.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    BrowserActivity.this.search_lay.setVisibility(View.GONE);
                    BrowserActivity.this.bottom_button.setVisibility(View.VISIBLE);
                    BrowserActivity.this.search_keyword.setText("");
                    BrowserActivity.web_view.findAllAsync("");
                }
            });
            BrowserActivity.web_view.setWebViewClient(new WebViewClient() {

                private Map<String, Boolean> loadedUrls = new HashMap();

                public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                    super.onPageStarted(webView, str, bitmap);
                    BrowserActivity.this.isload = true;
                    BrowserActivity.this.bookmark_data.clear();
                    BrowserActivity.this.bookmark_data.addAll(BrowserActivity.this.database.getAllBookmark());
                    for (int i = 0; i < BrowserActivity.this.bookmark_data.size(); i++) {
                        BrowserActivity.this.bookmark_data.get(i).getUrl().equals(str);
                    }
                    BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getTitle());
                }

                public void onPageFinished(WebView webView, String str) {
                    super.onPageFinished(webView, str);
                    String urlBox = MyApplication.getUrlBox();
                    int hashCode = urlBox.hashCode();
                    if (hashCode == 84303) {
                        urlBox.equals("URL");
                    } else if (hashCode == 80818744) {
                        urlBox.equals("Title");
                    } else if (hashCode == 1751015924 && !urlBox.equals("Domain (default)")) {
                    }
                }

                @Override
                public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                    boolean z;
                    if (Build.VERSION.SDK_INT < 21 || !MyApplication.getAdBlock()) {
                        z = false;
                    } else if (!this.loadedUrls.containsKey(webResourceRequest.getUrl())) {
                        z = AdBlocker.isAd(webResourceRequest.getUrl().toString());
                        this.loadedUrls.put(webResourceRequest.getUrl().toString(), Boolean.valueOf(z));
                    } else {
                        z = this.loadedUrls.get(webResourceRequest.getUrl()).booleanValue();
                    }
                    if (z) {
                        return AdBlocker.createEmptyResource();
                    }
                    return super.shouldInterceptRequest(webView, webResourceRequest);
                }
            });
            BrowserActivity.web_view.setDownloadListener(new DownloadListener() {


                public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(1);
                    if (MyApplication.getDownloadPath().contains("/storage/emulated/0/")) {
                        String s = MyApplication.getDownloadPath() + "/";
                        String sadx = URLUtil.guessFileName(str, str3, str4);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                        ((DownloadManager) BrowserActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                        return;
                    }
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                    ((DownloadManager) BrowserActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                }
            });
            BrowserActivity.web_view.setWebChromeClient(new WebChromeClient() {


                public void onProgressChanged(WebView webView, int i) {
                    super.onProgressChanged(webView, i);
                    BrowserActivity.this.progress.setVisibility(View.VISIBLE);
                    BrowserActivity.this.progress.setProgress(i);
                    if (i == 100) {
                        BrowserActivity.this.progress.setVisibility(View.INVISIBLE);
                        BrowserActivity.this.custom_web.setVisibility(View.GONE);
                        BrowserActivity.web_view.setVisibility(View.VISIBLE);
                    }
                }

                public void onReceivedTitle(WebView webView, String str) {
                    super.onReceivedTitle(webView, str);
                    BrowserActivity.this.setTitle(str);
                }

                public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                    super.onReceivedIcon(webView, bitmap);
                    if (BrowserActivity.this.isload) {
                        BrowserActivity.this.isload = false;
                        HistoryData history_Data = new HistoryData();
                        history_Data.setName(webView.getTitle());
                        history_Data.setUrl(webView.getOriginalUrl());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        history_Data.setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
                        history_Data.setDate(new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                        if (MyApplication.getRequestData()) {
                            BrowserActivity.this.database.addHistory(history_Data);
                        }
                    }
                }
            });
            BrowserActivity.this.tab_switcher_button.setOnClickListener(BrowserActivity.this.createTabSwitcherButtonListener());
        }

        public void loadPriviousWeb() {
            if (BrowserActivity.web_view.canGoBack()) {
                BrowserActivity.web_view.goBack();
            } else if (BrowserActivity.this.custom_web.getVisibility() != View.VISIBLE) {
                String homePage = MyApplication.getHomePage();
                char c = 65535;
                switch (homePage.hashCode()) {
                    case -1406075965:
                        if (homePage.equals("Webpage")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -1085510111:
                        if (homePage.equals("Default")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -253812259:
                        if (homePage.equals("Bookmarks")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 64266548:
                        if (homePage.equals("Blank")) {
                            c = 1;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    BrowserActivity.this.custom_web.setVisibility(View.VISIBLE);
                    BrowserActivity.web_view.setVisibility(View.GONE);
                } else if (c == 1) {
                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                    BrowserActivity.web_view.setVisibility(View.GONE);
                } else if (c == 2) {
                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                    BrowserActivity.web_view.setVisibility(View.VISIBLE);
                } else if (c == 3) {
                    BrowserActivity.this.custom_web.setVisibility(View.VISIBLE);
                    BrowserActivity.web_view.setVisibility(View.GONE);
                    BrowserActivity.this.fragmentLoad(new BookmarkFragment(0));
                }
                BrowserActivity.this.temp_next = true;
                BrowserActivity.this.search_link.setText("");
            }
        }

        public void startVoiceRecognitionActivity() {
            Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
            intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
            intent.putExtra("android.speech.extra.PROMPT", "Voice searching...");
            BrowserActivity.this.startActivityForResult(intent, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        public void VoiceSearch(String str) {
            char c;
            ArrayList arrayList = new ArrayList();
            arrayList.clear();
            List list = (List) new Gson().fromJson(MyApplication.getSearchHistory(), new TypeToken<List<String>>() {

            }.getType());
            if (list != null) {
                arrayList.addAll(list);
            }
            String searchEngine = MyApplication.getSearchEngine();
            int hashCode = searchEngine.hashCode();
            char c2 = 65535;
            switch (hashCode) {
                case 66137:
                    if (searchEngine.equals("Ask")) {
                        c = 3;
                        break;
                    }
                case 2070624:
                    if (searchEngine.equals("Bing")) {
                        c = 1;
                        break;
                    }
                case 85186592:
                    if (searchEngine.equals("Yahoo")) {
                        c = 2;
                        break;
                    }
                case 2138589785:
                    if (searchEngine.equals("Google")) {
                        c = 0;
                        break;
                    }
                default:
                    c = 65535;
                    break;
            }
            String str2 = "";
            if (!str.contains(c != 0 ? c != 1 ? c != 2 ? c != 3 ? str2 : "https://www.ask.com/" : "https://www.yahoo.com/" : "https://www.bing.com/" : "https://www.google.com/")) {
                String searchEngine2 = MyApplication.getSearchEngine();
                switch (searchEngine2.hashCode()) {
                    case 66137:
                        if (searchEngine2.equals("Ask")) {
                            c2 = 3;
                            break;
                        }
                    case 2070624:
                        if (searchEngine2.equals("Bing")) {
                            c2 = 1;
                            break;
                        }
                    case 85186592:
                        if (searchEngine2.equals("Yahoo")) {
                            c2 = 2;
                            break;
                        }
                    case 2138589785:
                        c2 = 0;
                        break;
                }
                if (c2 == 0) {
                    str2 = "https://www.google.com/search?q=" + str.trim();
                } else if (c2 == 1) {
                    str2 = "https://www.bing.com/search?q=" + str.trim();
                } else if (c2 == 2) {
                    str2 = "https://www.yahoo.com/search?q=" + str.trim();
                } else if (c2 == 3) {
                    str2 = "https://www.ask.com/web?q=" + str.trim();
                }
                BrowserActivity.web_view.loadUrl(str2);
                arrayList.add(str2);
                if (MyApplication.getRequestData()) {
                    MyApplication.putSearcHistory(new Gson().toJson(arrayList));
                    return;
                }
                return;
            }
            BrowserActivity.web_view.loadUrl(str.trim());
            if (MyApplication.getRequestData()) {
                MyApplication.putSearcHistory(new Gson().toJson(arrayList));
            }
        }


        public void LoadnextWeb() {
            char c = 0;
            if (BrowserActivity.this.temp_next) {
                BrowserActivity.this.temp_next = false;
                if (BrowserActivity.this.custom_web.getVisibility() == View.VISIBLE) {
                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                    BrowserActivity.web_view.setVisibility(View.VISIBLE);
                    String urlBox = MyApplication.getUrlBox();
                    int hashCode = urlBox.hashCode();
                    if (hashCode != 84303) {
                        if (hashCode != 80818744) {
                            if (hashCode == 1751015924) {
                            }
                        } else if (urlBox.equals("Title")) {
                            c = 2;
                            if (c == 0) {
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getOriginalUrl());
                                return;
                            } else if (c == 1) {
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getUrl());
                                return;
                            } else if (c == 2) {
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getTitle());
                                return;
                            } else {
                                return;
                            }
                        }
                    } else if (urlBox.equals("URL")) {
                        c = 1;
                        if (c == 0) {
                        }
                    }
                    c = 65535;
                    if (c == 0) {
                    }
                } else if (BrowserActivity.web_view.getVisibility() != View.VISIBLE) {
                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                    BrowserActivity.web_view.setVisibility(View.VISIBLE);
                    String urlBox2 = MyApplication.getUrlBox();
                    int hashCode2 = urlBox2.hashCode();
                    if (hashCode2 != 84303) {
                        if (hashCode2 != 80818744) {
                            if (hashCode2 == 1751015924) {
                            }
                        } else if (urlBox2.equals("Title")) {
                            c = 2;
                            if (c == 0) {
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getOriginalUrl());
                                return;
                            } else if (c == 1) {
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getUrl());
                                return;
                            } else if (c == 2) {
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getTitle());
                                return;
                            } else {
                                return;
                            }
                        }
                    } else if (urlBox2.equals("URL")) {
                        c = 1;
                        if (c == 0) {
                        }
                    }
                    c = 65535;
                    if (c == 0) {
                    }
                }
            } else if (BrowserActivity.web_view.canGoForward()) {
                if (BrowserActivity.this.custom_web.getVisibility() == View.VISIBLE) {
                    BrowserActivity.this.custom_web.setVisibility(View.GONE);
                    BrowserActivity.web_view.setVisibility(View.VISIBLE);
                    String urlBox3 = MyApplication.getUrlBox();
                    int hashCode3 = urlBox3.hashCode();
                    if (hashCode3 != 84303) {
                        if (hashCode3 != 80818744) {
                            if (hashCode3 == 1751015924) {
                            }
                        } else if (urlBox3.equals("Title")) {
                            c = 2;
                            if (c == 0) {
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getOriginalUrl());
                                return;
                            } else if (c == 1) {
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getUrl());
                                return;
                            } else if (c == 2) {
                                BrowserActivity.this.search_link.setText(BrowserActivity.web_view.getTitle());
                                return;
                            } else {
                                return;
                            }
                        }
                    } else if (urlBox3.equals("URL")) {
                        c = 1;
                        if (c == 0) {
                        }
                    }
                    c = 65535;
                    if (c == 0) {
                    }
                } else {
                    BrowserActivity.web_view.goForward();
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public int getViewType(Tab tab, int i) {
            Bundle parameters = tab.getParameters();
            if (parameters != null) {
                return parameters.getInt(BrowserActivity.VIEW_TYPE_EXTRA);
            }
            return 0;
        }
    }

    public class State extends AbstractState implements AbstractDataBinder.Listener<ArrayAdapter<String>, Tab, ListView, Void>, TabPreviewListener {
        private ArrayAdapter<String> adapter;

        State(Tab tab) {
            super(tab);
        }

        @Override
        public void onCanceled(AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> abstractDataBinder) {
        }

        public boolean onLoadData(AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> abstractDataBinder, Tab tab, Void... voidArr) {
            return true;
        }

        public void loadItems(ListView listView) {
            Condition.INSTANCE.ensureNotNull(listView, "The list view may not be null");
            if (this.adapter == null) {
                BrowserActivity.this.dataBinder.addListener(this);
                BrowserActivity.this.dataBinder.load(getTab(), listView, new Void[0]);
                return;
            }
            ListAdapter adapter2 = listView.getAdapter();
            ArrayAdapter<String> arrayAdapter = this.adapter;
            if (adapter2 != arrayAdapter) {
                listView.setAdapter((ListAdapter) arrayAdapter);
            }
        }

        public void onFinished(AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> abstractDataBinder, Tab tab, ArrayAdapter<String> arrayAdapter, ListView listView, Void... voidArr) {
            if (getTab().equals(tab)) {
                listView.setAdapter((ListAdapter) arrayAdapter);
                this.adapter = arrayAdapter;
                abstractDataBinder.removeListener(this);
            }
        }

        @Override
        public final void saveInstanceState(Bundle bundle) {
            ArrayAdapter<String> arrayAdapter = this.adapter;
            if (!(arrayAdapter == null || arrayAdapter.isEmpty())) {
                int count = this.adapter.getCount();
                String[] strArr = new String[count];
                for (int i = 0; i < count; i++) {
                    strArr[i] = this.adapter.getItem(i);
                }
                bundle.putStringArray(String.format(BrowserActivity.ADAPTER_STATE_EXTRA, getTab().getTitle()), strArr);
            }
        }

        @SuppressLint("ResourceType")
        @Override
        public void restoreInstanceState(Bundle bundle) {
            if (bundle != null) {
                String[] stringArray = bundle.getStringArray(String.format(BrowserActivity.ADAPTER_STATE_EXTRA, getTab().getTitle()));
                if (stringArray != null && stringArray.length > 0) {
                    this.adapter = new ArrayAdapter<>(BrowserActivity.this, 17367043, stringArray);
                }
            }
        }

        @Override
        public boolean onLoadTabPreview(TabSwitcher tabSwitcher, Tab tab) {
            return !getTab().equals(tab) || this.adapter != null;
        }
    }
}
