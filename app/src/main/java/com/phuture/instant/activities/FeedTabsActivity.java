package com.phuture.instant.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.phuture.instant.R;
import com.phuture.instant.db.Cache;
import com.phuture.instant.services.sync.SyncService;
import com.phuture.instant.ui.data.SourcePagerAdapter;

import java.util.LinkedHashSet;
import java.util.Set;

public class FeedTabsActivity extends AppCompatActivity {

    public static int REFRESH_INTERVAL_MINUTES = 10;

    ViewPager viewPager;
    TabLayout tabs;
    SourcePagerAdapter pagerAdapter;
    ProgressBar progressBar;
    BroadcastReceiver broadcastReceiver;

    void showProgressBar(boolean show) {
        try {
            progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        } catch (Exception e) {}
    }

    protected void unregisterSyncEvents() {
        if (broadcastReceiver!=null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver=null;
        }
    }

    protected void registerSyncEvents() {
        if (broadcastReceiver==null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(SyncService.BROADCAST_SYNC_STARTED)) {
                        showProgressBar(true);
                    };
                    if (intent.getAction().equals(SyncService.BROADCAST_SYNC_FINISHED)) {
                        showProgressBar(false);
                    };
                }
            };

            IntentFilter syncEventsFilter = new IntentFilter();
            syncEventsFilter.addAction(SyncService.BROADCAST_SYNC_STARTED);
            syncEventsFilter.addAction(SyncService.BROADCAST_SYNC_FINISHED);

            registerReceiver(broadcastReceiver, syncEventsFilter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_tabs);
        progressBar = findViewById(R.id.progress_bar);
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);

        showProgressBar(false);

        registerSyncEvents();

        pagerAdapter = new SourcePagerAdapter(this, getSupportFragmentManager(), SourcePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);

        Intent sync = new Intent(getApplicationContext(), SyncService.class);
        sync.putExtra(SyncService.PARAM_SOURCE_ID_LIST, getSourcesToRefresh());
        startService(sync);
    }

    private String[] getSourcesToRefresh() {
        final Context ctx = getApplicationContext();
        if (!Cache.instance(ctx).has("refresh_sources")) {
            Set<String> _set = new LinkedHashSet<>();
            _set.add("444_hu");
            _set.add("azonnali_hu");
            _set.add("hang_hu");
            _set.add("telex_hu");
            _set.add("hvg_hu");
            _set.add("media1_hu");
            Cache.instance(ctx).set("refresh_sources", _set);
        }

        Set<String> set = Cache.instance(ctx).getStringSet("refresh_sources");
        return set.toArray(new String[0]);
    }

    @Override
    protected void onDestroy() {
        unregisterSyncEvents();
        super.onDestroy();
    }
}