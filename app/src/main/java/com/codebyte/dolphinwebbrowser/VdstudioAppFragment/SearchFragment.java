package com.codebyte.dolphinwebbrowser.VdstudioAppFragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codebyte.dolphinwebbrowser.VdstudioAppActivity.BrowserActivity;
import com.codebyte.dolphinwebbrowser.VdstudioAppActivity.IncognitoActivity;
import com.codebyte.dolphinwebbrowser.VdstudioAppAdapter.GoogleSearchAdapter;
import com.codebyte.dolphinwebbrowser.VdstudioAppAdapter.SearchAdapter;
import com.codebyte.dolphinwebbrowser.VdstudioAppUtils.MyApplication;
import com.codebyte.dolphinwebbrowser.vdstudioappretrofit.ApiService;
import com.codebyte.dolphinwebbrowser.vdstudioappretrofit.RestApi;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import dolphinwebbrowser.R;
import kotlin.jvm.internal.CharCompanionObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements View.OnClickListener {
    public GoogleSearchAdapter googleSearchAdapter;
    public RecyclerView googleSearch;
    public EditText search;
    public String temp;
    public String url;
    public String urls;
    ApiService apiService;
    BrowserActivity browserActivity;
    ArrayList<String> googleSearchList = new ArrayList<>();
    IncognitoActivity incognitoActivity;
    ArrayList<String> searchHistry = new ArrayList<>();
    TextView txtGoogleSearch;
    int typeMode;
    private TextView cancelSearch;
    private ImageView clearSearch;
    private TextView clearSearchHistory;
    private LinearLayout noSearch;
    private SearchAdapter searchAdapter;
    private RecyclerView searchhistory;
    private View view;

    public SearchFragment() {
    }

    public SearchFragment(String str, int i, BrowserActivity browser_Activity) {
        this.url = str;
        this.typeMode = i;
        this.browserActivity = browser_Activity;
    }

    public SearchFragment(String str, int i, IncognitoActivity incognito_Activity) {
        this.url = str;
        this.typeMode = i;
        this.incognitoActivity = incognito_Activity;
    }

    public static String getTagValue(String str, Element element) {
        element.getElementsByTagName(str).item(0).getChildNodes();
        Log.e(FirebaseAnalytics.Event.SEARCH, " element NodeName: " + element.getElementsByTagName(str).item(0).getNodeName() + " LocalName " + element.getElementsByTagName(str).item(0).getLocalName() + " NodeValue " + element.getElementsByTagName(str).item(0).getNodeValue() + " TextContent " + element.getElementsByTagName(str).item(0).getTextContent() + " Attribute Name  " + element.getElementsByTagName(str).item(0).getAttributes().item(0).getNodeName() + " Attribute Value  " + element.getElementsByTagName(str).item(0).getAttributes().item(0).getNodeValue());
        return element.getElementsByTagName(str).item(0).getAttributes().item(0).getNodeValue();
    }

    public static Document convertStringToXMLDocument(String str) {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(str)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_search, viewGroup, false);
        this.apiService = (ApiService) RestApi.getClient().create(ApiService.class);
        initView();
        initListener();
        return this.view;
    }

    private void initListener() {
        this.clearSearch.setOnClickListener(this);
        this.cancelSearch.setOnClickListener(this);
        this.clearSearchHistory.setOnClickListener(this);
    }

    private void initView() {
        this.searchHistry.clear();
        List list = (List) new Gson().fromJson(MyApplication.getSearchHistory(), new TypeToken<List<String>>() {

        }.getType());
        if (list != null) {
            this.searchHistry.addAll(list);
        }
        this.search = (EditText) this.view.findViewById(R.id.search);
        this.clearSearch = (ImageView) this.view.findViewById(R.id.clear_search);
        this.cancelSearch = (TextView) this.view.findViewById(R.id.cancel_search);
        this.clearSearchHistory = (TextView) this.view.findViewById(R.id.clear_search_history);
        this.searchhistory = (RecyclerView) this.view.findViewById(R.id.search_history);
        this.googleSearch = (RecyclerView) this.view.findViewById(R.id.google_search);
        this.txtGoogleSearch = (TextView) this.view.findViewById(R.id.txt_google_search);
        this.noSearch = (LinearLayout) this.view.findViewById(R.id.no_search);
        if (this.searchHistry.size() > 0) {
            this.searchhistory.setVisibility(View.VISIBLE);
            this.noSearch.setVisibility(View.GONE);
            this.searchAdapter = new SearchAdapter(getActivity(), this.searchHistry, this.typeMode);
            this.searchhistory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            this.searchhistory.setAdapter(this.searchAdapter);
        } else {
            this.searchhistory.setVisibility(View.GONE);
            this.noSearch.setVisibility(View.VISIBLE);
        }
        this.search.setText(this.url);
        EditText editText = this.search;
        editText.setSelection(editText.getText().length());
        this.search.requestFocus();
        ArrayList<String> arrayList = this.googleSearchList;
        if (arrayList == null || arrayList.size() == 0) {
            this.googleSearch.setVisibility(View.GONE);
            this.txtGoogleSearch.setVisibility(View.GONE);
        } else {
            this.googleSearch.setVisibility(View.VISIBLE);
            this.txtGoogleSearch.setVisibility(View.VISIBLE);
        }
        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(2, 1);
        this.search.setOnEditorActionListener(new TextView.OnEditorActionListener() {


            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                char c;
                if (SearchFragment.this.search.getText().toString().trim().length() <= 0) {
                    inputMethodManager.hideSoftInputFromWindow(SearchFragment.this.getActivity().getCurrentFocus().getWindowToken(), 0);
                    SearchFragment.this.getFragmentManager().popBackStack();
                } else if (i == 6 || i == 5) {
                    String searchEngine = MyApplication.getSearchEngine();
                    int hashCode = searchEngine.hashCode();
                    char c2 = CharCompanionObject.MAX_VALUE;
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
                            c = CharCompanionObject.MAX_VALUE;
                            break;
                    }
                    if (c == 0) {
                        SearchFragment.this.urls = "https://www.google.com/";
                    } else if (c == 1) {
                        SearchFragment.this.urls = "https://www.bing.com/";
                    } else if (c == 2) {
                        SearchFragment.this.urls = "https://www.yahoo.com/";
                    } else if (c == 3) {
                        SearchFragment.this.urls = "https://www.ask.com/";
                    }
                    if (!SearchFragment.this.search.getText().toString().contains(SearchFragment.this.urls)) {
                        if (SearchFragment.this.typeMode == 0) {
                            String searchEngine2 = MyApplication.getSearchEngine();
                            switch (searchEngine2.hashCode()) {
                                case 66137:
                                    if (searchEngine2.equals("Ask")) {
                                        c2 = 3;
                                        break;
                                    }
                                    break;
                                case 2070624:
                                    if (searchEngine2.equals("Bing")) {
                                        c2 = 1;
                                        break;
                                    }
                                    break;
                                case 85186592:
                                    if (searchEngine2.equals("Yahoo")) {
                                        c2 = 2;
                                        break;
                                    }
                                    break;
                                case 2138589785:
                                    if (searchEngine2.equals("Google")) {
                                        c2 = 0;
                                        break;
                                    }
                                    break;
                            }
                            if (c2 == 0) {
                                SearchFragment search_Fragment = SearchFragment.this;
                                search_Fragment.temp = "https://www.google.com/search?q=" + SearchFragment.this.search.getText().toString().trim();
                            } else if (c2 == 1) {
                                SearchFragment search_Fragment2 = SearchFragment.this;
                                search_Fragment2.temp = "https://www.bing.com/search?q=" + SearchFragment.this.search.getText().toString().trim();
                            } else if (c2 == 2) {
                                SearchFragment search_Fragment3 = SearchFragment.this;
                                search_Fragment3.temp = "https://www.yahoo.com/search?q=" + SearchFragment.this.search.getText().toString().trim();
                            } else if (c2 == 3) {
                                SearchFragment search_Fragment4 = SearchFragment.this;
                                search_Fragment4.temp = "https://www.ask.com/web?q=" + SearchFragment.this.search.getText().toString().trim();
                            }
                            if (MyApplication.getAllowSite()) {
                                SearchFragment.this.browserActivity.addTab(SearchFragment.this.temp);
                            } else {
                                BrowserActivity.web_view.loadUrl(SearchFragment.this.temp);
                            }
                            SearchFragment.this.searchHistry.add(SearchFragment.this.temp);
                            if (MyApplication.getRequestData()) {
                                MyApplication.putSearcHistory(new Gson().toJson(SearchFragment.this.searchHistry));
                            }
                        } else {
                            String searchEngine3 = MyApplication.getSearchEngine();
                            switch (searchEngine3.hashCode()) {
                                case 66137:
                                    if (searchEngine3.equals("Ask")) {
                                        c2 = 3;
                                        break;
                                    }
                                    break;
                                case 2070624:
                                    if (searchEngine3.equals("Bing")) {
                                        c2 = 1;
                                        break;
                                    }
                                    break;
                                case 85186592:
                                    if (searchEngine3.equals("Yahoo")) {
                                        c2 = 2;
                                        break;
                                    }
                                    break;
                                case 2138589785:
                                    if (searchEngine3.equals("Google")) {
                                        c2 = 0;
                                        break;
                                    }
                                    break;
                            }
                            if (c2 == 0) {
                                SearchFragment search_Fragment5 = SearchFragment.this;
                                search_Fragment5.temp = "https://www.google.com/search?q=" + SearchFragment.this.search.getText().toString().trim();
                            } else if (c2 == 1) {
                                SearchFragment search_Fragment6 = SearchFragment.this;
                                search_Fragment6.temp = "https://www.bing.com/search?q=" + SearchFragment.this.search.getText().toString().trim();
                            } else if (c2 == 2) {
                                SearchFragment search_Fragment7 = SearchFragment.this;
                                search_Fragment7.temp = "https://www.yahoo.com/search?q=" + SearchFragment.this.search.getText().toString().trim();
                            } else if (c2 == 3) {
                                SearchFragment search_Fragment8 = SearchFragment.this;
                                search_Fragment8.temp = "https://www.ask.com/web?q=" + SearchFragment.this.search.getText().toString().trim();
                            }
                            if (MyApplication.getAllowSite()) {
                                SearchFragment.this.incognitoActivity.addTab(SearchFragment.this.temp);
                            } else {
                                IncognitoActivity.webView.loadUrl(SearchFragment.this.temp);
                            }
                        }
                    } else if (SearchFragment.this.typeMode == 0) {
                        if (MyApplication.getAllowSite()) {
                            SearchFragment.this.browserActivity.addTab(SearchFragment.this.search.getText().toString().trim());
                        } else {
                            BrowserActivity.web_view.loadUrl(SearchFragment.this.search.getText().toString().trim());
                        }
                        SearchFragment.this.searchHistry.add(SearchFragment.this.search.getText().toString().trim());
                        if (MyApplication.getRequestData()) {
                            MyApplication.putSearcHistory(new Gson().toJson(SearchFragment.this.searchHistry));
                        }
                    } else if (MyApplication.getAllowSite()) {
                        SearchFragment.this.incognitoActivity.addTab(SearchFragment.this.search.getText().toString().trim());
                    } else {
                        IncognitoActivity.webView.loadUrl(SearchFragment.this.search.getText().toString().trim());
                    }
                    inputMethodManager.hideSoftInputFromWindow(SearchFragment.this.getActivity().getCurrentFocus().getWindowToken(), 0);
                    SearchFragment.this.getFragmentManager().popBackStack();
                }
                return false;
            }
        });
        this.search.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (SearchFragment.this.search.getText().toString().length() == 0) {
                    SearchFragment.this.googleSearchList.clear();
                    if (SearchFragment.this.googleSearchList == null || SearchFragment.this.googleSearchList.size() == 0) {
                        SearchFragment.this.googleSearch.setVisibility(View.GONE);
                        SearchFragment.this.txtGoogleSearch.setVisibility(View.GONE);
                        return;
                    }
                    SearchFragment.this.googleSearch.setVisibility(View.VISIBLE);
                    SearchFragment.this.txtGoogleSearch.setVisibility(View.VISIBLE);
                    return;
                }
                SearchFragment.this.apiService.getSearchUrl(SearchFragment.this.search.getText().toString().trim()).enqueue(new Callback<ResponseBody>() {


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                    }

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String string = response.body().string();
                            Log.e(FirebaseAnalytics.Event.SEARCH, "str" + string);
                            SearchFragment.this.googleSearchList.clear();
                            Document convertStringToXMLDocument = SearchFragment.convertStringToXMLDocument(string);
                            convertStringToXMLDocument.getDocumentElement().normalize();
                            Log.e(FirebaseAnalytics.Event.SEARCH, "Root element :" + convertStringToXMLDocument.getDocumentElement().getNodeName());
                            NodeList elementsByTagName = convertStringToXMLDocument.getElementsByTagName("CompleteSuggestion");
                            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                                Element element = (Element) elementsByTagName.item(i);
                                Log.e(FirebaseAnalytics.Event.SEARCH, "element NodeName: " + element.getNodeName());
                                String tagValue = SearchFragment.getTagValue("suggestion", element);
                                SearchFragment.this.googleSearchList.add(tagValue);
                                Log.e(FirebaseAnalytics.Event.SEARCH, tagValue);
                            }
                            if (SearchFragment.this.googleSearchAdapter != null) {
                                SearchFragment.this.googleSearchAdapter.notifyDataSetChanged();
                            } else {
                                SearchFragment.this.googleSearchAdapter = new GoogleSearchAdapter(SearchFragment.this.getActivity(), SearchFragment.this.googleSearchList);
                                SearchFragment.this.googleSearch.setLayoutManager(new LinearLayoutManager(SearchFragment.this.getActivity(), RecyclerView.VERTICAL, false));
                                SearchFragment.this.googleSearch.setAdapter(SearchFragment.this.googleSearchAdapter);
                                SearchFragment.this.googleSearchAdapter.setOnItemClickListener(new GoogleSearchAdapter.ClickListener() {


                                    @Override
                                    public void onItemClick(int i, View view, int i2) {
                                        char c;
                                        if (i2 != 1) {
                                            SearchFragment.this.search.setText(SearchFragment.this.googleSearchList.get(i));
                                        } else if (SearchFragment.this.googleSearchList.get(i).length() > 0) {
                                            String searchEngine = MyApplication.getSearchEngine();
                                            int hashCode = searchEngine.hashCode();
                                            char c2 = CharCompanionObject.MAX_VALUE;
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
                                                    c = CharCompanionObject.MAX_VALUE;
                                                    break;
                                            }
                                            if (c == 0) {
                                                SearchFragment.this.urls = "https://www.google.com/";
                                            } else if (c == 1) {
                                                SearchFragment.this.urls = "https://www.bing.com/";
                                            } else if (c == 2) {
                                                SearchFragment.this.urls = "https://www.yahoo.com/";
                                            } else if (c == 3) {
                                                SearchFragment.this.urls = "https://www.ask.com/";
                                            }
                                            if (!SearchFragment.this.googleSearchList.get(i).contains(SearchFragment.this.urls)) {
                                                if (SearchFragment.this.typeMode == 0) {
                                                    String searchEngine2 = MyApplication.getSearchEngine();
                                                    switch (searchEngine2.hashCode()) {
                                                        case 66137:
                                                            if (searchEngine2.equals("Ask")) {
                                                                c2 = 3;
                                                                break;
                                                            }
                                                            break;
                                                        case 2070624:
                                                            if (searchEngine2.equals("Bing")) {
                                                                c2 = 1;
                                                                break;
                                                            }
                                                            break;
                                                        case 85186592:
                                                            if (searchEngine2.equals("Yahoo")) {
                                                                c2 = 2;
                                                                break;
                                                            }
                                                            break;
                                                        case 2138589785:
                                                            if (searchEngine2.equals("Google")) {
                                                                c2 = 0;
                                                                break;
                                                            }
                                                            break;
                                                    }
                                                    if (c2 == 0) {
                                                        SearchFragment search_Fragment = SearchFragment.this;
                                                        search_Fragment.temp = "https://www.google.com/search?q=" + SearchFragment.this.googleSearchList.get(i).trim();
                                                    } else if (c2 == 1) {
                                                        SearchFragment search_Fragment2 = SearchFragment.this;
                                                        search_Fragment2.temp = "https://www.bing.com/search?q=" + SearchFragment.this.googleSearchList.get(i).trim();
                                                    } else if (c2 == 2) {
                                                        SearchFragment search_Fragment3 = SearchFragment.this;
                                                        search_Fragment3.temp = "https://www.yahoo.com/search?q=" + SearchFragment.this.googleSearchList.get(i).trim();
                                                    } else if (c2 == 3) {
                                                        SearchFragment search_Fragment4 = SearchFragment.this;
                                                        search_Fragment4.temp = "https://www.ask.com/web?q=" + SearchFragment.this.googleSearchList.get(i).trim();
                                                    }
                                                    if (MyApplication.getAllowSite()) {
                                                        SearchFragment.this.browserActivity.addTab(SearchFragment.this.temp);
                                                    } else {
                                                        BrowserActivity.web_view.loadUrl(SearchFragment.this.temp);
                                                    }
                                                    SearchFragment.this.searchHistry.add(SearchFragment.this.temp);
                                                    if (MyApplication.getRequestData()) {
                                                        MyApplication.putSearcHistory(new Gson().toJson(SearchFragment.this.searchHistry));
                                                    }
                                                } else {
                                                    String searchEngine3 = MyApplication.getSearchEngine();
                                                    switch (searchEngine3.hashCode()) {
                                                        case 66137:
                                                            if (searchEngine3.equals("Ask")) {
                                                                c2 = 3;
                                                                break;
                                                            }
                                                            break;
                                                        case 2070624:
                                                            if (searchEngine3.equals("Bing")) {
                                                                c2 = 1;
                                                                break;
                                                            }
                                                            break;
                                                        case 85186592:
                                                            if (searchEngine3.equals("Yahoo")) {
                                                                c2 = 2;
                                                                break;
                                                            }
                                                            break;
                                                        case 2138589785:
                                                            if (searchEngine3.equals("Google")) {
                                                                c2 = 0;
                                                                break;
                                                            }
                                                            break;
                                                    }
                                                    if (c2 == 0) {
                                                        SearchFragment search_Fragment5 = SearchFragment.this;
                                                        search_Fragment5.temp = "https://www.google.com/search?q=" + SearchFragment.this.googleSearchList.get(i).trim();
                                                    } else if (c2 == 1) {
                                                        SearchFragment search_Fragment6 = SearchFragment.this;
                                                        search_Fragment6.temp = "https://www.bing.com/search?q=" + SearchFragment.this.googleSearchList.get(i).trim();
                                                    } else if (c2 == 2) {
                                                        SearchFragment search_Fragment7 = SearchFragment.this;
                                                        search_Fragment7.temp = "https://www.yahoo.com/search?q=" + SearchFragment.this.googleSearchList.get(i).trim();
                                                    } else if (c2 == 3) {
                                                        SearchFragment search_Fragment8 = SearchFragment.this;
                                                        search_Fragment8.temp = "https://www.ask.com/web?q=" + SearchFragment.this.googleSearchList.get(i).trim();
                                                    }
                                                    if (MyApplication.getAllowSite()) {
                                                        SearchFragment.this.incognitoActivity.addTab(SearchFragment.this.temp);
                                                    } else {
                                                        IncognitoActivity.webView.loadUrl(SearchFragment.this.temp);
                                                    }
                                                }
                                            } else if (SearchFragment.this.typeMode == 0) {
                                                if (MyApplication.getAllowSite()) {
                                                    SearchFragment.this.browserActivity.addTab(SearchFragment.this.googleSearchList.get(i).trim());
                                                } else {
                                                    BrowserActivity.web_view.loadUrl(SearchFragment.this.googleSearchList.get(i).trim());
                                                }
                                                SearchFragment.this.searchHistry.add(SearchFragment.this.googleSearchList.get(i).trim());
                                                if (MyApplication.getRequestData()) {
                                                    MyApplication.putSearcHistory(new Gson().toJson(SearchFragment.this.searchHistry));
                                                }
                                            } else if (MyApplication.getAllowSite()) {
                                                SearchFragment.this.incognitoActivity.addTab(SearchFragment.this.googleSearchList.get(i).trim());
                                            } else {
                                                IncognitoActivity.webView.loadUrl(SearchFragment.this.googleSearchList.get(i).trim());
                                            }
                                            inputMethodManager.hideSoftInputFromWindow(SearchFragment.this.getActivity().getCurrentFocus().getWindowToken(), 0);
                                            SearchFragment.this.getFragmentManager().popBackStack();
                                        }
                                    }
                                });
                            }
                            if (SearchFragment.this.googleSearchList != null) {
                                if (SearchFragment.this.googleSearchList.size() != 0) {
                                    SearchFragment.this.googleSearch.setVisibility(View.VISIBLE);
                                    SearchFragment.this.txtGoogleSearch.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                            SearchFragment.this.googleSearch.setVisibility(View.GONE);
                            SearchFragment.this.txtGoogleSearch.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void onClick(View view2) {
        switch (view2.getId()) {
            case R.id.cancel_search:
                getFragmentManager().popBackStack();
                return;
            case R.id.clear_search:
                this.search.setText("");
                return;
            case R.id.clear_search_history:
                this.searchHistry.clear();
                MyApplication.putSearcHistory(new Gson().toJson(this.searchHistry));
                SearchAdapter search_Adapter = this.searchAdapter;
                if (search_Adapter != null) {
                    search_Adapter.notifyDataSetChanged();
                    return;
                }
                return;
            default:
                return;
        }
    }
}
