package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.codebyte.dolphinwebbrowser.VdstudioAppModel.HomeNewsData;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.Constant;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.MyApplication;
import com.codebyte.dolphinwebbrowser.vdstudioappretrofit.ApiService;
import com.codebyte.dolphinwebbrowser.vdstudioappretrofit.RestApi;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.Locale;

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

public class IncognitoActivity extends AppCompatActivity implements TabSwitcherListener {
    public static final String ADAPTER_STATE_EXTRA = (State.class.getName() + "::%s::AdapterState");
    public static final String VIEW_TYPE_EXTRA = (IncognitoActivity.class.getName() + "::ViewType");
    public static WebView webView;
    public LinearLayout bottomButton;
    public DataBinder dataBinder;
    public Decorator decorator;
    public TextView moreNewsMenu;
    public ImageView newIncognito;
    public ProgressBar progress;
    public ImageView refresh;
    public ImageView searchClose;
    public ImageView searchDown;
    public TextView searchKeyword;
    public LinearLayout searchLay;
    public TextView searchLink;
    public ImageView searchUp;
    public String showPlateformAd = Constant.APP_NEXT_AD_KEY;
    public TabSwitcher tabSwitcher;
    public TabSwitcherButton tabSwitcherButton;
    int tabCount = 1;
    RelativeLayout customWeb;
    Database database;
    LinearLayout facebookButton;
    FrameLayout fragmentLoad;
    LinearLayout googleButton;
    LinearLayout instagramButton;
    boolean isload = false;
    LinearLayout liAmazon;
    LinearLayout liGoogle;
    LinearLayout liInsta;
    LinearLayout liLinkdin;
    LinearLayout liSpotify;
    LinearLayout liTwitter;
    RecyclerView rvNewsItems;
    LinearLayout searchText;
    boolean tempNext = false;
    ImageView webMenu;
    LinearLayout youtubeButton;
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
        setContentView(R.layout.activity_incognito);
        this.showPlateformAd = MyApplication.getShowPlateformAd(this);
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
        initView();
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
        for (int i = 0; i < this.tabCount; i++) {
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
                tabSwitcher.addTab(IncognitoActivity.this.createTab(tabSwitcher.getCount()), 0);
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
                IncognitoActivity.this.tabSwitcher.setPadding(systemWindowInsetLeft, systemWindowInsetTop, systemWindowInsetRight, windowInsetsCompat.getSystemWindowInsetBottom());
                float f = (float) systemWindowInsetTop;
                if (IncognitoActivity.this.tabSwitcher.getLayout() == Layout.TABLET) {
                    f += (float) IncognitoActivity.this.getResources().getDimensionPixelSize(R.dimen.tablet_tab_container_height);
                }
                RectF rectF = new RectF((float) systemWindowInsetLeft, f, (float) (DisplayUtil.getDisplayWidth(IncognitoActivity.this) - systemWindowInsetRight), ((float) ThemeUtil.getDimensionPixelSize(IncognitoActivity.this, 0)) + f);
                IncognitoActivity.this.tabSwitcher.addDragGesture(((SwipeGesture.Builder) new SwipeGesture.Builder().setTouchableArea(rectF)).create());
                IncognitoActivity.this.tabSwitcher.addDragGesture(((PullDownGesture.Builder) new PullDownGesture.Builder().setTouchableArea(rectF)).create());
                return windowInsetsCompat;
            }
        };
    }

    private View.OnClickListener createAddTabListener() {
        return new View.OnClickListener() {


            public void onClick(View view) {
                IncognitoActivity.this.tabSwitcher.addTab(IncognitoActivity.this.createTab(IncognitoActivity.this.tabSwitcher.getCount()), 0, IncognitoActivity.this.createRevealAnimation());
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
                    IncognitoActivity incognito_Activity = IncognitoActivity.this;
                    Tab createTab = incognito_Activity.createTab(incognito_Activity.tabSwitcher.getCount());
                    if (IncognitoActivity.this.tabSwitcher.isSwitcherShown()) {
                        IncognitoActivity.this.tabSwitcher.addTab(createTab, 0, IncognitoActivity.this.createRevealAnimation());
                    } else {
                        IncognitoActivity.this.tabSwitcher.addTab(createTab, 0, IncognitoActivity.this.createPeekAnimation());
                    }
                    return true;
                } else if (itemId == R.id.clear_tabs_menu_item) {
                    IncognitoActivity.this.tabSwitcher.clear();
                    return true;
                } else {
                    Tab selectedTab = IncognitoActivity.this.tabSwitcher.getSelectedTab();
                    if (selectedTab != null) {
                        IncognitoActivity.this.tabSwitcher.removeTab(selectedTab);
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
        Toolbar toolbar = toolbars.length > 1 ? toolbars[1] : toolbars[0];
        int childCount = toolbar.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = toolbar.getChildAt(i);
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
                if (IncognitoActivity.this.tabSwitcher.isSwitcherShown()) {
                    IncognitoActivity.this.tabSwitcher.addAllTabs(tabArr, i);
                } else if (tabArr.length == 1) {
                    IncognitoActivity.this.tabSwitcher.addTab(tabArr[0], 0, IncognitoActivity.this.createPeekAnimation());
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
                IncognitoActivity.this.progress.setVisibility(View.GONE);
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
                    IncognitoActivity.this.rvNewsItems.setLayoutManager(new LinearLayoutManager(IncognitoActivity.this));
                    IncognitoActivity.this.rvNewsItems.setAdapter(new HomeNewsAdapter(IncognitoActivity.this, arrayList, new HomeNewsAdapter.click_item() {


                        @Override
                        public void OnCLickitem(HomeNewsData homeNewsData) {
                            IncognitoActivity.webView.loadUrl(homeNewsData.getWebsite());
                        }
                    }));
                } catch (Exception e) {
                    IncognitoActivity.this.progress.setVisibility(View.GONE);
                    Log.e("OSJKSGDGHS2222", e.getMessage() + "::");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable th) {
                IncognitoActivity.this.progress.setVisibility(View.GONE);
                Log.e("OSJKSGDGHSs111", th.getMessage() + "::");
            }
        });
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 203 && i2 == -1 && intent != null) {
            webView.loadUrl(((HomeNewsData) intent.getSerializableExtra("clickData")).getWebsite());
        }
    }

    @SuppressLint("ResourceType")
    public void search() {
        final Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.setContentView(R.layout.dialog_find);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        final EditText editText = (EditText) dialog.findViewById(R.id.search_text);
        ((Button) dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                String trim = editText.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    Toast.makeText(IncognitoActivity.this, "Enter search keyword!", Toast.LENGTH_SHORT).show();
                    return;
                }
                IncognitoActivity.webView.findNext(true);
                IncognitoActivity.webView.findAllAsync(trim);
                IncognitoActivity.this.searchLay.setVisibility(View.VISIBLE);
                IncognitoActivity.this.bottomButton.setVisibility(View.GONE);
                IncognitoActivity.this.searchKeyword.setText(trim);
                dialog.dismiss();
            }
        });
        dialog.show();
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
                if (IncognitoActivity.this.tabSwitcher.getCount() != 0) {
                    if (IncognitoActivity.this.customWeb.getVisibility() == View.VISIBLE) {
                        IncognitoActivity.this.tabSwitcher.getTab(IncognitoActivity.this.tabSwitcher.getSelectedTabIndex()).setTitle("Private Search");
                    } else {
                        IncognitoActivity.this.tabSwitcher.getTab(IncognitoActivity.this.tabSwitcher.getSelectedTabIndex()).setTitle(IncognitoActivity.webView.getTitle());
                    }
                    IncognitoActivity.this.tabSwitcher.toggleSwitcherVisibility();
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
        } else if (this.searchLay.getVisibility() == View.VISIBLE) {
            this.searchLay.setVisibility(View.GONE);
            this.bottomButton.setVisibility(View.VISIBLE);
            this.searchKeyword.setText("");
            webView.findAllAsync("");
        } else if (webView.canGoBack()) {
            webView.goBack();
        } else if (this.customWeb.getVisibility() != View.VISIBLE) {
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
                            if (this.customWeb.getVisibility() != View.VISIBLE) {
                                this.customWeb.setVisibility(View.VISIBLE);
                                webView.setVisibility(View.GONE);
                            } else {
                                finish();
                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                        }
                    } else if (webView.getVisibility() != View.VISIBLE) {
                        this.customWeb.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        fragmentLoad(new BookmarkFragment(0));
                    } else {
                        finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                } else if (this.customWeb.getVisibility() == View.VISIBLE || webView.getVisibility() == View.VISIBLE) {
                    this.customWeb.setVisibility(View.GONE);
                    webView.setVisibility(View.GONE);
                } else {
                    finish();
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            } else if (this.customWeb.getVisibility() != View.VISIBLE) {
                this.customWeb.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            } else {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            this.tempNext = true;
            this.searchLink.setText("");
        } else {
            finish();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    public void addTab(String str) {
        TabSwitcher tabSwitcher2 = this.tabSwitcher;
        tabSwitcher2.addTab(createTab(tabSwitcher2.getCount()), 0, createRevealAnimation());
        webView.loadUrl(str);
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
            IncognitoActivity.this.tabSwitcher.removeTabPreviewListener(state);
        }

        public void onSaveInstanceState(View view, Tab tab, int i, int i2, State state, Bundle bundle) {
            if (state != null) {
                state.saveInstanceState(bundle);
            }
        }

        @Override
        public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
            return layoutInflater.inflate(R.layout.fragment_incognito_browser, viewGroup, false);
        }

        public void onShowTab(final Context context, final TabSwitcher tabSwitcher, View view, Tab tab, int i, int i2, State state, Bundle bundle) {
            char c;
            IncognitoActivity.webView = (WebView) findViewById(R.id.web_view);
            CookieManager.getInstance().setAcceptCookie(false);
            char c2 = 2;
            IncognitoActivity.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            IncognitoActivity.webView.getSettings().setAppCacheEnabled(false);
            IncognitoActivity.webView.clearHistory();
            IncognitoActivity.webView.clearCache(true);
            IncognitoActivity.webView.clearFormData();
            IncognitoActivity.webView.getSettings().setSavePassword(false);
            IncognitoActivity.webView.getSettings().setSaveFormData(false);
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) IncognitoActivity.this.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                activeNetworkInfo.isConnected();
            }
            IncognitoActivity.this.tabSwitcherButton = (TabSwitcherButton) findViewById(R.id.tab_switcher_button);
            IncognitoActivity.this.database = new Database(IncognitoActivity.this);
            IncognitoActivity.this.progress = (ProgressBar) findViewById(R.id.progress);
            IncognitoActivity.this.customWeb = (RelativeLayout) findViewById(R.id.custom_web);
            IncognitoActivity.this.liTwitter = (LinearLayout) findViewById(R.id.li_twitter);
            IncognitoActivity.this.liSpotify = (LinearLayout) findViewById(R.id.li_spotify);
            IncognitoActivity.this.liLinkdin = (LinearLayout) findViewById(R.id.li_linkdin);
            IncognitoActivity.this.liGoogle = (LinearLayout) findViewById(R.id.li_google);
            IncognitoActivity.this.liAmazon = (LinearLayout) findViewById(R.id.li_amazon);
            IncognitoActivity.this.liInsta = (LinearLayout) findViewById(R.id.li_insta);
            IncognitoActivity.this.rvNewsItems = (RecyclerView) findViewById(R.id.rv_news_items);
            IncognitoActivity.this.googleButton = (LinearLayout) findViewById(R.id.google);
            IncognitoActivity.this.facebookButton = (LinearLayout) findViewById(R.id.facebook);
            IncognitoActivity.this.youtubeButton = (LinearLayout) findViewById(R.id.youtube);
            IncognitoActivity.this.instagramButton = (LinearLayout) findViewById(R.id.instagram);
            IncognitoActivity.this.webMenu = (ImageView) findViewById(R.id.web_menu);
            IncognitoActivity.this.searchText = (LinearLayout) findViewById(R.id.search_text);
            IncognitoActivity.this.searchLay = (LinearLayout) findViewById(R.id.search_lay);
            IncognitoActivity.this.bottomButton = (LinearLayout) findViewById(R.id.bottom_button);
            IncognitoActivity.this.searchLink = (TextView) findViewById(R.id.search_link);
            IncognitoActivity.this.searchKeyword = (TextView) findViewById(R.id.search_keyword);
            IncognitoActivity.this.refresh = (ImageView) findViewById(R.id.refresh);
            IncognitoActivity.this.searchUp = (ImageView) findViewById(R.id.search_up);
            IncognitoActivity.this.searchDown = (ImageView) findViewById(R.id.search_down);
            IncognitoActivity.this.searchClose = (ImageView) findViewById(R.id.search_close);
            IncognitoActivity.this.fragmentLoad = (FrameLayout) findViewById(R.id.fragment_load);
            IncognitoActivity.this.tabSwitcherButton.setCount(tabSwitcher.getCount());
            IncognitoActivity.this.newIncognito = (ImageView) findViewById(R.id.new_incognito);
            IncognitoActivity.this.liTwitter.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://www.twitter.com/");
                }
            });
            IncognitoActivity.this.liLinkdin.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://in.linkedin.com/");
                }
            });
            IncognitoActivity.this.liGoogle.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://www.google.com/");
                }
            });
            IncognitoActivity.this.liAmazon.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://www.amazon.in/");
                }
            });
            IncognitoActivity.this.liInsta.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://instagram.com");
                }
            });
            IncognitoActivity.this.liSpotify.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://www.spotify.com/");
                }
            });
            IncognitoActivity.this.newIncognito.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.this.finish();
                    IncognitoActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });
            IncognitoActivity.this.bottomButton.setVisibility(View.VISIBLE);
            IncognitoActivity.this.searchLay.setVisibility(View.GONE);
            IncognitoActivity.this.googleButton.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://www.google.com/");
                }
            });
            IncognitoActivity.this.facebookButton.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://www.facebook.com/");
                }
            });
            IncognitoActivity.this.youtubeButton.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://www.youtube.com/");
                }
            });
            IncognitoActivity.this.instagramButton.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.loadUrl("https://www.instagram.com/?hl=en");
                }
            });
            IncognitoActivity.webView.getSettings().setDefaultFontSize(Integer.parseInt(MyApplication.getTextSize()));
            IncognitoActivity.webView.isPrivateBrowsingEnabled();
            if (bundle == null) {
                IncognitoActivity.this.searchLink.setText("");
                String homePage = MyApplication.getHomePage();
                switch (homePage.hashCode()) {
                    case -1406075965:
                        if (homePage.equals("Webpage")) {
                            c = 2;
                            break;
                        }
                    case -1085510111:
                        if (homePage.equals("Default")) {
                            c = 0;
                            break;
                        }
                    case -253812259:
                        if (homePage.equals("Bookmarks")) {
                            c = 3;
                            break;
                        }
                    case 64266548:
                        if (homePage.equals("Blank")) {
                            c = 1;
                            break;
                        }
                    default:
                        c = 65535;
                        break;
                }
                if (c == 0) {
                    IncognitoActivity.this.customWeb.setVisibility(View.VISIBLE);
                    IncognitoActivity.webView.setVisibility(View.GONE);
                } else if (c == 1) {
                    IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                    IncognitoActivity.webView.setVisibility(View.GONE);
                } else if (c == 2) {
                    IncognitoActivity.webView.loadUrl("https://www.google.co.in/");
                    IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                    IncognitoActivity.webView.setVisibility(View.VISIBLE);
                } else if (c == 3) {
                    IncognitoActivity.this.fragmentLoad(new BookmarkFragment(0));
                }
            }
            IncognitoActivity.this.webMenu.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    final PopupWindow popupWindow = new PopupWindow(IncognitoActivity.this);
                    View inflate = ((LayoutInflater) IncognitoActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_menu, (ViewGroup) null);
                    ImageView imageView = (ImageView) inflate.findViewById(R.id.bookmark_image);
                    LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.control_btn);
                    ((TextView) inflate.findViewById(R.id.download)).setVisibility(View.GONE);
                    ((TextView) inflate.findViewById(R.id.setting)).setVisibility(View.GONE);
                    ((LinearLayout) inflate.findViewById(R.id.bookmark)).setVisibility(View.GONE);
                    ((TextView) inflate.findViewById(R.id.new_tab)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            tabSwitcher.addTab(IncognitoActivity.this.createTab(tabSwitcher.getCount()), 0, IncognitoActivity.this.createRevealAnimation());
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.bookmarks)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            IncognitoActivity.this.fragmentLoad(new BookmarkFragment(1));
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.history)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            IncognitoActivity.this.fragmentLoad(new HistoryFragment(1));
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.find)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (IncognitoActivity.this.customWeb.getVisibility() == View.GONE && IncognitoActivity.webView != null) {
                                IncognitoActivity.this.search();
                            }
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.copy)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (IncognitoActivity.this.customWeb.getVisibility() == View.GONE && IncognitoActivity.webView != null) {
                                ((ClipboardManager) IncognitoActivity.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("copy", IncognitoActivity.webView.getUrl()));
                                Toast.makeText(context, "Link copied", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    ((TextView) inflate.findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (IncognitoActivity.this.customWeb.getVisibility() == View.GONE && IncognitoActivity.webView != null) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.SEND");
                                intent.putExtra("android.intent.extra.SUBJECT", IncognitoActivity.webView.getTitle());
                                intent.putExtra("android.intent.extra.TEXT", IncognitoActivity.webView.getUrl());
                                intent.setType(HTTP.PLAIN_TEXT_TYPE);
                                IncognitoActivity.this.startActivity(intent);
                            }
                        }
                    });
                    ((LinearLayout) inflate.findViewById(R.id.web_next)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            char c = 0;
                            if (IncognitoActivity.this.tempNext) {
                                IncognitoActivity.this.tempNext = false;
                                if (IncognitoActivity.this.customWeb.getVisibility() == View.VISIBLE) {
                                    IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                                    IncognitoActivity.webView.setVisibility(View.VISIBLE);
                                    String urlBox = MyApplication.getUrlBox();
                                    int hashCode = urlBox.hashCode();
                                    if (hashCode != 84303) {
                                        if (hashCode != 80818744) {
                                            if (hashCode == 1751015924) {
                                            }
                                        } else if (urlBox.equals("Title")) {
                                            c = 2;
                                            if (c == 0) {
                                                IncognitoActivity.this.searchLink.setText(IncognitoActivity.webView.getUrl());
                                                return;
                                            } else if (c == 1) {
                                                IncognitoActivity.this.searchLink.setText(IncognitoActivity.webView.getUrl());
                                                return;
                                            } else if (c == 2) {
                                                IncognitoActivity.this.searchLink.setText(IncognitoActivity.webView.getTitle());
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
                                } else if (IncognitoActivity.webView.getVisibility() != View.VISIBLE) {
                                    IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                                    IncognitoActivity.webView.setVisibility(View.VISIBLE);
                                    String urlBox2 = MyApplication.getUrlBox();
                                    int hashCode2 = urlBox2.hashCode();
                                    if (hashCode2 != 84303) {
                                        if (hashCode2 != 80818744) {
                                            if (hashCode2 == 1751015924) {
                                            }
                                        } else if (urlBox2.equals("Title")) {
                                            c = 2;
                                            if (c == 0) {
                                                IncognitoActivity.this.searchLink.setText(IncognitoActivity.webView.getUrl());
                                                return;
                                            } else if (c == 1) {
                                                IncognitoActivity.this.searchLink.setText(IncognitoActivity.webView.getUrl());
                                                return;
                                            } else if (c == 2) {
                                                IncognitoActivity.this.searchLink.setText(IncognitoActivity.webView.getTitle());
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
                            } else if (IncognitoActivity.webView.canGoForward()) {
                                if (IncognitoActivity.this.customWeb.getVisibility() == View.VISIBLE) {
                                    IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                                    IncognitoActivity.webView.setVisibility(View.VISIBLE);
                                    String urlBox3 = MyApplication.getUrlBox();
                                    int hashCode3 = urlBox3.hashCode();
                                    if (hashCode3 != 84303) {
                                        if (hashCode3 != 80818744) {
                                            if (hashCode3 == 1751015924) {
                                            }
                                        } else if (urlBox3.equals("Title")) {
                                            c = 2;
                                            if (c == 0) {
                                                IncognitoActivity.this.searchLink.setText(IncognitoActivity.webView.getUrl());
                                                return;
                                            } else if (c == 1) {
                                                IncognitoActivity.this.searchLink.setText(IncognitoActivity.webView.getUrl());
                                                return;
                                            } else if (c == 2) {
                                                IncognitoActivity.this.searchLink.setText(IncognitoActivity.webView.getTitle());
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
                                    IncognitoActivity.webView.goForward();
                                }
                            }
                        }
                    });
                    ((LinearLayout) inflate.findViewById(R.id.web_previous)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (IncognitoActivity.webView.canGoBack()) {
                                IncognitoActivity.webView.goBack();
                                return;
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
                            if (c != 0) {
                                if (c != 1) {
                                    if (c != 2) {
                                        if (c == 3) {
                                            if (IncognitoActivity.this.customWeb.getVisibility() == View.GONE || IncognitoActivity.webView.getVisibility() == View.VISIBLE) {
                                                IncognitoActivity.this.customWeb.setVisibility(View.VISIBLE);
                                                IncognitoActivity.webView.setVisibility(View.GONE);
                                                IncognitoActivity.this.fragmentLoad(new BookmarkFragment(0));
                                            } else {
                                                IncognitoActivity.this.finish();
                                                IncognitoActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                            }
                                        }
                                    } else if (IncognitoActivity.this.customWeb.getVisibility() == View.VISIBLE || IncognitoActivity.webView.getVisibility() == View.GONE) {
                                        IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                                        IncognitoActivity.webView.setVisibility(View.VISIBLE);
                                    } else {
                                        IncognitoActivity.this.finish();
                                        IncognitoActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                    }
                                } else if (IncognitoActivity.this.customWeb.getVisibility() == View.VISIBLE || IncognitoActivity.webView.getVisibility() == View.VISIBLE) {
                                    IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                                    IncognitoActivity.webView.setVisibility(View.GONE);
                                } else {
                                    IncognitoActivity.this.finish();
                                    IncognitoActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                }
                            } else if (IncognitoActivity.this.customWeb.getVisibility() == View.GONE || IncognitoActivity.webView.getVisibility() == View.VISIBLE) {
                                IncognitoActivity.this.customWeb.setVisibility(View.VISIBLE);
                                IncognitoActivity.webView.setVisibility(View.GONE);
                            } else {
                                IncognitoActivity.this.finish();
                                IncognitoActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                            IncognitoActivity.this.tempNext = true;
                            IncognitoActivity.this.searchLink.setText("");
                        }
                    });
                    ((LinearLayout) inflate.findViewById(R.id.web_home)).setOnClickListener(new View.OnClickListener() {


                        public void onClick(View view) {
                            popupWindow.dismiss();
                            CookieManager.getInstance().setAcceptCookie(false);
                            IncognitoActivity.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                            IncognitoActivity.webView.getSettings().setAppCacheEnabled(false);
                            IncognitoActivity.webView.clearHistory();
                            IncognitoActivity.webView.clearCache(true);
                            IncognitoActivity.webView.clearFormData();
                            IncognitoActivity.webView.getSettings().setSavePassword(false);
                            IncognitoActivity.webView.getSettings().setSaveFormData(false);
                            if (IncognitoActivity.webView.canGoBack()) {
                                IncognitoActivity.this.tempNext = true;
                            } else if (IncognitoActivity.this.customWeb.getVisibility() != View.VISIBLE) {
                                IncognitoActivity.this.tempNext = true;
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
                                IncognitoActivity.this.customWeb.setVisibility(View.VISIBLE);
                                IncognitoActivity.webView.setVisibility(View.GONE);
                            } else if (c == 1) {
                                IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                                IncognitoActivity.webView.setVisibility(View.GONE);
                            } else if (c == 2) {
                                IncognitoActivity.webView.loadUrl("https://www.google.co.in/");
                                IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                                IncognitoActivity.webView.setVisibility(View.VISIBLE);
                            } else if (c == 3) {
                                IncognitoActivity.this.fragmentLoad(new BookmarkFragment(0));
                            }
                            IncognitoActivity.this.searchLink.setText("");
                        }
                    });
                    popupWindow.setFocusable(true);
                    if (Build.VERSION.SDK_INT >= 21) {
                        popupWindow.setElevation(20.0f);
                    }
                    popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(IncognitoActivity.this, R.drawable.transperent_bg));
                    popupWindow.setContentView(inflate);
                    if (IncognitoActivity.this.customWeb.getVisibility() == View.GONE) {
                        linearLayout.setVisibility(View.VISIBLE);
                    } else {
                        linearLayout.setVisibility(View.GONE);
                    }
                    popupWindow.showAtLocation(inflate, 85, 60, 60);
                    popupWindow.showAsDropDown(view);
                }
            });
            IncognitoActivity.this.searchText.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.this.fragmentLoad(new SearchFragment("", 1, IncognitoActivity.this));
                }
            });
            IncognitoActivity.this.searchLink.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    if (IncognitoActivity.webView.getUrl() != null) {
                        IncognitoActivity.this.fragmentLoad(new SearchFragment(IncognitoActivity.webView.getUrl().toString(), 1, IncognitoActivity.this));
                    } else {
                        IncognitoActivity.this.fragmentLoad(new SearchFragment("", 1, IncognitoActivity.this));
                    }
                }
            });
            IncognitoActivity.this.refresh.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.reload();
                }
            });
            IncognitoActivity.this.moreNewsMenu = (TextView) findViewById(R.id.more_news_menu);
            IncognitoActivity.this.moreNewsMenu.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.this.startActivityForResult(new Intent(IncognitoActivity.this, MoreNews.class), HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
                }
            });
            IncognitoActivity.this.LoadData();
            IncognitoActivity.this.searchUp.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.findNext(false);
                }
            });
            IncognitoActivity.this.searchDown.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.webView.findNext(true);
                }
            });
            IncognitoActivity.this.searchClose.setOnClickListener(new View.OnClickListener() {


                public void onClick(View view) {
                    IncognitoActivity.this.searchLay.setVisibility(View.GONE);
                    IncognitoActivity.this.bottomButton.setVisibility(View.VISIBLE);
                    IncognitoActivity.this.searchKeyword.setText("");
                    IncognitoActivity.webView.findAllAsync("");
                }
            });
            IncognitoActivity.webView.getSettings().setJavaScriptEnabled(true);
            IncognitoActivity.webView.setWebViewClient(new WebViewClient() {


                public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                    super.onPageStarted(webView, str, bitmap);
                    IncognitoActivity.this.isload = true;
                    String urlBox = MyApplication.getUrlBox();
                    int hashCode = urlBox.hashCode();
                    if (hashCode == 84303) {
                        urlBox.equals("URL");
                    } else if (hashCode == 80818744) {
                        urlBox.equals("Title");
                    } else if (hashCode == 1751015924 && !urlBox.equals("Domain (default)")) {
                    }
                }
            });
            String userAgent = MyApplication.getUserAgent();
            int hashCode = userAgent.hashCode();
            if (hashCode != -1984987966) {
                if (hashCode != -1085510111) {
                    if (hashCode == -1073207300 && userAgent.equals("Desktop")) {
                        IncognitoActivity.webView.getSettings().setUserAgentString("Android");
                        String textEncoding = MyApplication.getTextEncoding();
                        switch (textEncoding.hashCode()) {
                            case 70352:
                                break;
                            case 2070357:
                                if (textEncoding.equals("Big5")) {
                                    c2 = 3;
                                    break;
                                }
                            case 81070450:
                                if (textEncoding.equals("UTF-8")) {
                                    c2 = 1;
                                    break;
                                }
                            case 257295942:
                                if (textEncoding.equals("SHIFT_JS")) {
                                    c2 = 5;
                                    break;
                                }
                            case 1450311437:
                                if (textEncoding.equals("ISO-2022-JP")) {
                                    c2 = 4;
                                    break;
                                }
                            case 2027158704:
                                if (textEncoding.equals("ISO-8859-1")) {
                                    c2 = 0;
                                    break;
                                }
                            case 2055952320:
                                if (textEncoding.equals("EUC-JP")) {
                                    c2 = 6;
                                    break;
                                }
                            case 2055952353:
                                if (textEncoding.equals("EUC-KR")) {
                                    c2 = 7;
                                    break;
                                }
                            default:
                                c2 = 65535;
                                break;
                        }
                        switch (c2) {
                            case 0:
                                IncognitoActivity.webView.getSettings().setDefaultTextEncodingName("ISO-8859-1");
                                break;
                            case 1:
                                IncognitoActivity.webView.getSettings().setDefaultTextEncodingName("UTF-8");
                                break;
                            case 2:
                                IncognitoActivity.webView.getSettings().setDefaultTextEncodingName("GBK");
                                break;
                            case 3:
                                IncognitoActivity.webView.getSettings().setDefaultTextEncodingName("Big5");
                                break;
                            case 4:
                                IncognitoActivity.webView.getSettings().setDefaultTextEncodingName("ISO-2022-JP");
                                break;
                            case 5:
                                IncognitoActivity.webView.getSettings().setDefaultTextEncodingName("SHIFT_JS");
                                break;
                            case 6:
                                IncognitoActivity.webView.getSettings().setDefaultTextEncodingName("EUC-JP");
                                break;
                            case 7:
                                IncognitoActivity.webView.getSettings().setDefaultTextEncodingName("EUC-KR");
                                break;
                        }
                        if (!MyApplication.getBlockImage()) {
                            IncognitoActivity.webView.getSettings().setLoadsImagesAutomatically(false);
                        } else {
                            IncognitoActivity.webView.getSettings().setLoadsImagesAutomatically(true);
                        }
                        if (!MyApplication.getEnableJava()) {
                            IncognitoActivity.webView.getSettings().setJavaScriptEnabled(true);
                        } else {
                            IncognitoActivity.webView.getSettings().setJavaScriptEnabled(false);
                        }
                        if (!MyApplication.getCookies()) {
                            CookieSyncManager.createInstance(IncognitoActivity.this);
                            CookieSyncManager.getInstance().startSync();
                            CookieManager.getInstance().setAcceptCookie(true);
                            if (Build.VERSION.SDK_INT >= 21) {
                                CookieManager.getInstance().setAcceptThirdPartyCookies(IncognitoActivity.webView, true);
                            }
                        } else {
                            CookieSyncManager.createInstance(IncognitoActivity.this);
                            CookieSyncManager.getInstance().startSync();
                            CookieManager.getInstance().setAcceptCookie(false);
                            if (Build.VERSION.SDK_INT >= 21) {
                                CookieManager.getInstance().setAcceptThirdPartyCookies(IncognitoActivity.webView, false);
                            }
                        }
                        IncognitoActivity.webView.setDownloadListener(new DownloadListener() {


                            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(1);
                                if (MyApplication.getDownloadPath().contains("/storage/emulated/0/")) {
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, URLUtil.guessFileName(str, str3, str4));
                                    ((DownloadManager) IncognitoActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                                    return;
                                }
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                                ((DownloadManager) IncognitoActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                            }
                        });
                        IncognitoActivity.webView.setWebChromeClient(new WebChromeClient() {


                            public void onProgressChanged(WebView webView, int i) {
                                super.onProgressChanged(webView, i);
                                IncognitoActivity.this.progress.setVisibility(View.VISIBLE);
                                IncognitoActivity.this.progress.setProgress(i);
                                if (i == 100) {
                                    IncognitoActivity.this.progress.setVisibility(View.GONE);
                                    IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                                    IncognitoActivity.webView.setVisibility(View.VISIBLE);
                                }
                            }

                            public void onReceivedTitle(WebView webView, String str) {
                                super.onReceivedTitle(webView, str);
                                IncognitoActivity.this.setTitle(str);
                            }

                            public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                                super.onReceivedIcon(webView, bitmap);
                                if (IncognitoActivity.this.isload) {
                                    IncognitoActivity.this.isload = false;
                                }
                            }
                        });
                        IncognitoActivity.this.tabSwitcherButton.setOnClickListener(IncognitoActivity.this.createTabSwitcherButtonListener());
                    }
                } else if (userAgent.equals("Default")) {
                    MyApplication.getTextEncoding().hashCode();
                    MyApplication.getBlockImage();
                    MyApplication.getEnableJava();
                    MyApplication.getCookies();
                    IncognitoActivity.webView.setDownloadListener(new DownloadListener() {


                        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(1);
                            if (MyApplication.getDownloadPath().contains("/storage/emulated/0/")) {
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                                ((DownloadManager) IncognitoActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                                return;
                            }
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                            ((DownloadManager) IncognitoActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                        }
                    });
                    IncognitoActivity.webView.setWebChromeClient(new WebChromeClient() {


                        public void onProgressChanged(WebView webView, int i) {
                            super.onProgressChanged(webView, i);
                            IncognitoActivity.this.progress.setVisibility(View.VISIBLE);
                            IncognitoActivity.this.progress.setProgress(i);
                            if (i == 100) {
                                IncognitoActivity.this.progress.setVisibility(View.GONE);
                                IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                                IncognitoActivity.webView.setVisibility(View.VISIBLE);
                            }
                        }

                        public void onReceivedTitle(WebView webView, String str) {
                            super.onReceivedTitle(webView, str);
                            IncognitoActivity.this.setTitle(str);
                        }

                        public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                            super.onReceivedIcon(webView, bitmap);
                            if (IncognitoActivity.this.isload) {
                                IncognitoActivity.this.isload = false;
                            }
                        }
                    });
                    IncognitoActivity.this.tabSwitcherButton.setOnClickListener(IncognitoActivity.this.createTabSwitcherButtonListener());
                }
            } else if (userAgent.equals("Mobile")) {
                MyApplication.getTextEncoding().hashCode();
                MyApplication.getBlockImage();
                MyApplication.getEnableJava();
                MyApplication.getCookies();
                IncognitoActivity.webView.setDownloadListener(new DownloadListener() {


                    public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(1);
                        if (MyApplication.getDownloadPath().contains("/storage/emulated/0/")) {
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                            ((DownloadManager) IncognitoActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                            return;
                        }
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                        ((DownloadManager) IncognitoActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                    }
                });
                IncognitoActivity.webView.setWebChromeClient(new WebChromeClient() {


                    public void onProgressChanged(WebView webView, int i) {
                        super.onProgressChanged(webView, i);
                        IncognitoActivity.this.progress.setVisibility(View.VISIBLE);
                        IncognitoActivity.this.progress.setProgress(i);
                        if (i == 100) {
                            IncognitoActivity.this.progress.setVisibility(View.GONE);
                            IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                            IncognitoActivity.webView.setVisibility(View.VISIBLE);
                        }
                    }

                    public void onReceivedTitle(WebView webView, String str) {
                        super.onReceivedTitle(webView, str);
                        IncognitoActivity.this.setTitle(str);
                    }

                    public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                        super.onReceivedIcon(webView, bitmap);
                        if (IncognitoActivity.this.isload) {
                            IncognitoActivity.this.isload = false;
                        }
                    }
                });
                IncognitoActivity.this.tabSwitcherButton.setOnClickListener(IncognitoActivity.this.createTabSwitcherButtonListener());
            }
            MyApplication.getTextEncoding().hashCode();
            MyApplication.getBlockImage();
            MyApplication.getEnableJava();
            MyApplication.getCookies();
            IncognitoActivity.webView.setDownloadListener(new DownloadListener() {


                public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(1);
                    if (MyApplication.getDownloadPath().contains("/storage/emulated/0/")) {
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                        ((DownloadManager) IncognitoActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                        return;
                    }
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(str, str3, str4));
                    ((DownloadManager) IncognitoActivity.this.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
                }
            });
            IncognitoActivity.webView.setWebChromeClient(new WebChromeClient() {


                public void onProgressChanged(WebView webView, int i) {
                    super.onProgressChanged(webView, i);
                    IncognitoActivity.this.progress.setVisibility(View.VISIBLE);
                    IncognitoActivity.this.progress.setProgress(i);
                    if (i == 100) {
                        IncognitoActivity.this.progress.setVisibility(View.GONE);
                        IncognitoActivity.this.customWeb.setVisibility(View.GONE);
                        IncognitoActivity.webView.setVisibility(View.VISIBLE);
                    }
                }

                public void onReceivedTitle(WebView webView, String str) {
                    super.onReceivedTitle(webView, str);
                    IncognitoActivity.this.setTitle(str);
                }

                public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                    super.onReceivedIcon(webView, bitmap);
                    if (IncognitoActivity.this.isload) {
                        IncognitoActivity.this.isload = false;
                    }
                }
            });
            IncognitoActivity.this.tabSwitcherButton.setOnClickListener(IncognitoActivity.this.createTabSwitcherButtonListener());
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public int getViewType(Tab tab, int i) {
            Bundle parameters = tab.getParameters();
            if (parameters != null) {
                return parameters.getInt(IncognitoActivity.VIEW_TYPE_EXTRA);
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
                IncognitoActivity.this.dataBinder.addListener(this);
                IncognitoActivity.this.dataBinder.load(getTab(), listView, new Void[0]);
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
                bundle.putStringArray(String.format(IncognitoActivity.ADAPTER_STATE_EXTRA, getTab().getTitle()), strArr);
            }
        }

        @SuppressLint("ResourceType")
        @Override
        public void restoreInstanceState(Bundle bundle) {
            if (bundle != null) {
                String[] stringArray = bundle.getStringArray(String.format(IncognitoActivity.ADAPTER_STATE_EXTRA, getTab().getTitle()));
                if (stringArray != null && stringArray.length > 0) {
                    this.adapter = new ArrayAdapter<>(IncognitoActivity.this, 17367043, stringArray);
                }
            }
        }

        @Override
        public boolean onLoadTabPreview(TabSwitcher tabSwitcher, Tab tab) {
            return !getTab().equals(tab) || this.adapter != null;
        }
    }
}
