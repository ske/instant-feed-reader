package com.phuture.instant.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.phuture.instant.R;
import com.phuture.instant.db.Cache;
import com.phuture.instant.db.Client;
import com.phuture.instant.model.Article;
import com.phuture.instant.model.Source;
import com.phuture.instant.network.Downloader;
import com.phuture.instant.network.IDownloadResult;
import com.phuture.instant.ui.data.SourcePagerAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FeedTabsActivity extends AppCompatActivity implements IDownloadResult {

    public static int REFRESH_INTERVAL_MINUTES = 10;

    SourcePagerAdapter pagerAdapter;

    protected int downloaded = 0;
    protected int toDownload = 0;
    protected boolean isRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_tabs);

        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, -REFRESH_INTERVAL_MINUTES);

        System.out.println("Doing refresh");
        final boolean forceRefresh = false;

        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                List<Source> sources = Client.instance(getApplicationContext()).getDb().sourceDao().getAll();
                toDownload = sources.size();
                Toast t = null;
                for (Source src: sources) {
                    if (forceRefresh || src.lastRefresh == null || src.lastRefresh.before(c.getTime())) {
                        if (t == null) {
                            t = Toast.makeText(getApplicationContext(), "Refresh started", Toast.LENGTH_SHORT);
                            t.show();
                            isRefresh = true;
                        }

                        new Thread() {
                            @Override
                            public void run() {
                                System.out.println("refresh for: " + src.url);
                                Downloader.execute(getApplicationContext(), src.url,  src,FeedTabsActivity.this);
                            }
                        }.start();
                    } else {
                        onResultReceived(null, src);
                    }
                }
            }
        };
        refresh.run();
    }

    protected ViewPager viewPager;
    protected TabLayout tabs;

    void init() {
        if (pagerAdapter==null) {
            pagerAdapter = new SourcePagerAdapter(this, getSupportFragmentManager(), SourcePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(pagerAdapter);
            tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void onResultReceived(String result, Object data) {
        Source src = (Source)data;

        System.out.println("Refresh result for: " + src.url + ", " + (result==null || result.isEmpty() ? "no-result" : "has-result"));

        long lastRefreshAll = 0;

        if (result!=null && !result.isEmpty()) {
            Article.storeFromXml(this, src.id, result);
            src.lastRefresh = new Date();
            Client.instance(this).getDb().sourceDao().update(src);

            Cache.instance(getApplicationContext()).set("lastrefresh_" + src.id, src.lastRefresh.getTime());

            if (src.lastRefresh.getTime() > lastRefreshAll) {
                lastRefreshAll = src.lastRefresh.getTime();
            }
        }

        if (lastRefreshAll>0) {
            Cache.instance(getApplicationContext()).set("lastrefresh_all", src.lastRefresh.getTime());
        }

        downloaded++;
        if (downloaded == toDownload) {
            if (isRefresh) {
                Toast t = Toast.makeText(getApplicationContext(), "Refresh finished", Toast.LENGTH_SHORT);
                t.show();
                isRefresh = false;
            }
            init();
        }
    }
}