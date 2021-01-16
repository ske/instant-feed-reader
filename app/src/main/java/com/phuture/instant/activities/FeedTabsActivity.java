package com.phuture.instant.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.phuture.instant.R;
import com.phuture.instant.db.Cache;
import com.phuture.instant.services.sync.SyncService;
import com.phuture.instant.ui.data.SourcePagerAdapter;

import java.util.LinkedHashSet;
import java.util.Set;

public class FeedTabsActivity extends AppCompatActivity{

    public static int REFRESH_INTERVAL_MINUTES = 10;

    ViewPager viewPager;
    TabLayout tabs;
    SourcePagerAdapter pagerAdapter;

    private String[] getSourcesToRefresh() {
        final Context ctx = getApplicationContext();
        if (!Cache.instance(ctx).has("refresh_sources")) {
            Set<String> _set = new LinkedHashSet<>();
            _set.add("444_hu");
            _set.add("azonnali_hu");
            _set.add("hang_hu");
            Cache.instance(ctx).set("refresh_sources", _set);
        }

        Set<String> set = Cache.instance(ctx).getStringSet("refresh_sources");
        return set.toArray(new String[0]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_tabs);

        pagerAdapter = new SourcePagerAdapter(this, getSupportFragmentManager(), SourcePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        // TODO get
        Intent sync = new Intent(getApplicationContext(), SyncService.class);
        sync.putExtra(SyncService.PARAM_SOURCE_ID_LIST, getSourcesToRefresh());
        startService(sync);
    }
}