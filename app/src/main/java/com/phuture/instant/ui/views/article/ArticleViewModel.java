package com.phuture.instant.ui.views.article;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.phuture.instant.model.Article;
import com.phuture.instant.model.ArticleDao;

public class ArticleViewModel extends ViewModel {
    private ArticleDao articleDao;
    public final LiveData<PagedList<Article>> articles;

    public int getPageSize() {
        return 25;
    }

    public ArticleViewModel(ArticleDao articleDao) {
        this.articleDao = articleDao;
        articles = new LivePagedListBuilder<>(articleDao.getAllPaged(), getPageSize()).build();
    }
}
