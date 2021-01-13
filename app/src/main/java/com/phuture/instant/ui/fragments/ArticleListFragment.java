package com.phuture.instant.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.phuture.instant.R;
import com.phuture.instant.activities.WebViewActivity;
import com.phuture.instant.db.Client;
import com.phuture.instant.model.Article;
import com.phuture.instant.model.Source;
import com.phuture.instant.ui.data.ArticleViewAdapter;
import com.phuture.instant.ui.data.SourceDescriptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArticleListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String PARAM_SOURCE_ID = "sourceId";

    protected SourceDescriptor descriptor;
    protected ListView listView;
    protected List<Article> data = new ArrayList<>();
    protected ArticleViewAdapter adapter;
    protected Map<String, Source> sourceMap;
    protected String sourceId;
    protected long lastRefresh = 0;

    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance(String sourceId) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_SOURCE_ID, sourceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sourceId = getArguments().getString(PARAM_SOURCE_ID);
        } else {
            sourceId = null;
        }

        sourceMap = new LinkedHashMap<>();
        List<Source> sources = Client.instance(getContext()).getDb().sourceDao().getAll();
        for (Source src: sources) {
            sourceMap.put(src.id, src);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }

    protected void populateData() {
        // FIXME implement a simple k-v cache
        SharedPreferences prefs = getContext().getSharedPreferences("cache", Context.MODE_PRIVATE);
        long lastRefresh = 0;
        if (sourceId != null) {
            lastRefresh = prefs.getLong("lastrefresh_" + sourceId, 0);
        } else {
            lastRefresh = prefs.getLong("lastrefresh_all", 0);
        }

        if (lastRefresh > this.lastRefresh) {
            List<Article> data = null;
            if (sourceId==null) {
                data = Client.instance(getContext()).getDb().articleDao().getAll();
            } else {
                data = Client.instance(getContext()).getDb().articleDao().getAllBySource(sourceId);
            }

            this.data.clear();
            this.data.addAll(data);
            adapter.notifyDataSetChanged();

            System.out.println("last-refresh-change updating, " + lastRefresh + " vs " + this.lastRefresh);
            this.lastRefresh = lastRefresh;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_article_list, container, false);
        listView = root.findViewById(R.id.listView);

        adapter = new ArticleViewAdapter(getContext(), data, sourceMap);
        listView.setAdapter(adapter);
        populateData();
        listView.setOnItemClickListener(this);

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Article article = adapter.getItem(position);
        String url = article.url;
        Intent webView = new Intent(getContext(), WebViewActivity.class);
        webView.putExtra(WebViewActivity.PARAM_URL, article.url);
        startActivity(webView);
    }
}
