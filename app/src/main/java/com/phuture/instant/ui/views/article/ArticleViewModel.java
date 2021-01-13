package com.phuture.instant.ui.views.article;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.phuture.instant.model.Article;
import com.phuture.instant.model.ArticleDao;
import com.phuture.instant.model.Source;

public class ArticleViewModel extends ViewModel {
    private ArticleDao articleDao;
    private Source source;
    public final LiveData<PagedList<Article>> articles;

    public int getPageSize() {
        return 25;
    }

    public ArticleViewModel(ArticleDao articleDao, Source source) {
        this.articleDao = articleDao;
        if (source!=null) {
            articles = new LivePagedListBuilder<>(articleDao.getAllBySourcePaged(source.id), getPageSize()).build();
        } else {
            articles = new LivePagedListBuilder<>(articleDao.getAllPaged(), getPageSize()).build();
        }
    }
}
