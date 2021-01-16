package com.phuture.instant.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuture.instant.R;
import com.phuture.instant.activities.WebViewActivity;
import com.phuture.instant.db.Client;
import com.phuture.instant.model.Article;
import com.phuture.instant.model.Source;
import com.phuture.instant.ui.data.ISourceResolver;
import com.phuture.instant.ui.views.article.ArticleViewAdapter;
import com.phuture.instant.ui.views.article.ArticleViewModel;
import com.phuture.instant.ui.views.article.ArticleViewModelFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArticleListFragment extends Fragment implements ArticleViewAdapter.ArticleClickHandler,
        ISourceResolver {

    public static final String PARAM_SOURCE_ID = "sourceId";

    protected Map<String, Source> sourceMap;
    protected String sourceId;

    protected RecyclerView recyclerView;
    protected ArticleViewAdapter viewAdapter;
    protected ArticleViewModel viewModel;

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

        Source src = this.sourceMap.get(sourceId);

        viewAdapter = new ArticleViewAdapter(this, this);

        ArticleViewModelFactory viewModelFactory = new ArticleViewModelFactory(
                Client.instance(getContext()).getDb().articleDao(),
                src
        );

        viewModel = new ViewModelProvider(this, viewModelFactory).get(ArticleViewModel.class);
        viewModel.articles.observe(this, viewAdapter::submitList);
    }

    LinearLayoutManager rvLayoutManager;
    Parcelable listViewState;

    @Override
    public void onPause() {
        super.onPause();
        try {
            listViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        } catch (Exception e) {}
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            recyclerView.getLayoutManager().onRestoreInstanceState(listViewState);
        } catch (Exception e) {}
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_article_list, container, false);

        rvLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = root.findViewById(R.id.listView);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(rvLayoutManager);

        if (viewAdapter.getCurrentList() != null) {
            System.out.println("Current list size: " + viewAdapter.getCurrentList().size());
        } else {
            System.out.println("No list found on viewAdapter");
        }

        return root;
    }

    @Override
    public void onArticleClick(Article article) {
        Intent webView = new Intent(getContext(), WebViewActivity.class);
        webView.putExtra(WebViewActivity.PARAM_URL, article.url);
        startActivity(webView);
    }

    @Override
    public Source getSourceById(String sourceId) {
        return sourceMap.get(sourceId);
    }
}
