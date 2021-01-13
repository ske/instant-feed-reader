package com.phuture.instant.ui.views.article;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.phuture.instant.model.ArticleDao;
import com.phuture.instant.model.Source;

public class ArticleViewModelFactory implements ViewModelProvider.Factory {
    private ArticleDao articleDao;
    private Source source;

    public ArticleViewModelFactory(ArticleDao articleDao, Source source) {
        this.articleDao = articleDao;
        this.source = source;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ArticleViewModel(articleDao, source);
    }
}
