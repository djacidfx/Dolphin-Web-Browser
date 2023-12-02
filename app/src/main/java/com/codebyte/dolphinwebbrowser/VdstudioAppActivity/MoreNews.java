package com.codebyte.dolphinwebbrowser.VdstudioAppActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.codebyte.dolphinwebbrowser.VdstudioAppFragment.MoviesFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import dolphinwebbrowser.R;

public class MoreNews extends AppCompatActivity {
    ImageView imgBack;
    TabLayout tabLayout;
    ViewPager viewPager;
    private TabAdapter adapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_more_news);
        initView();
        setTabs();
        this.imgBack.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                MoreNews.this.onBackPressed();
            }
        });
    }

    private void initView() {
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.imgBack = (ImageView) findViewById(R.id.img_back);
    }

    private void highLightCurrentTab(int i) {
        for (int i2 = 0; i2 < this.tabLayout.getTabCount(); i2++) {
            TabLayout.Tab tabAt = this.tabLayout.getTabAt(i2);
            tabAt.setCustomView((View) null);
            if (i == i2) {
                tabAt.setCustomView(this.adapter.getSelectedTabView(i2));
            } else {
                tabAt.setCustomView(this.adapter.getTabView(i2));
            }
        }
    }

    private void setTabs() {
        try {
            TabAdapter tabAdapter = new TabAdapter(this, getSupportFragmentManager());
            this.adapter = tabAdapter;
            tabAdapter.addFragment(new MoviesFragment(0), "Movies");
            this.adapter.addFragment(new MoviesFragment(1), "Sport");
            this.adapter.addFragment(new MoviesFragment(2), "Entertainment");
            this.adapter.addFragment(new MoviesFragment(3), "Science and Technology");
            this.adapter.addFragment(new MoviesFragment(4), "World News");
            this.viewPager.setAdapter(this.adapter);
            this.viewPager.setOffscreenPageLimit(this.adapter.getCount());
            this.tabLayout.setupWithViewPager(this.viewPager);
            highLightCurrentTab(0);
            this.viewPager.setCurrentItem(0);
            this.viewPager.setOffscreenPageLimit(5);
            this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                @Override
                public void onPageScrollStateChanged(int i) {
                }

                @Override
                public void onPageScrolled(int i, float f, int i2) {
                }

                @Override
                public void onPageSelected(int i) {
                    MoreNews.this.updateTabView(i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTabView(int i) {
        for (int i2 = 0; i2 < this.tabLayout.getTabCount(); i2++) {
            try {
                LinearLayout linearLayout = (LinearLayout) this.tabLayout.getTabAt(i2).getCustomView();
                if (i == i2) {
                    ((TextView) linearLayout.getChildAt(1)).setTextColor(ContextCompat.getColor(this, R.color.Black));
                } else {
                    ((TextView) linearLayout.getChildAt(1)).setTextColor(ContextCompat.getColor(this, R.color.gray));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public class TabAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();
        private Context context;

        TabAdapter(Context context2, FragmentManager fragmentManager) {
            super(fragmentManager);
            this.context = context2;
        }

        @Override
        public Fragment getItem(int i) {
            return this.mFragmentList.get(i);
        }

        public void addFragment(Fragment fragment, String str) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(str);
        }

        public View getTabView(int i) {
            View inflate = LayoutInflater.from(this.context).inflate(R.layout.custom_tab, (ViewGroup) null);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_teamName);
            textView.setText(this.mFragmentTitleList.get(i));
            ((ImageView) inflate.findViewById(R.id.iv_profile)).setVisibility(View.GONE);
            textView.setTextColor(ContextCompat.getColor(this.context, R.color.gray));
            return inflate;
        }

        public View getSelectedTabView(int i) {
            View inflate = LayoutInflater.from(this.context).inflate(R.layout.custom_tab, (ViewGroup) null);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_teamName);
            textView.setText(this.mFragmentTitleList.get(i));
            ((ImageView) inflate.findViewById(R.id.iv_profile)).setVisibility(View.GONE);
            textView.setTextColor(ContextCompat.getColor(this.context, R.color.Black));
            return inflate;
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return this.mFragmentTitleList.get(i);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }
    }
}
