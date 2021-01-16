package com.phuture.instant.ui.views.article;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.phuture.instant.R;
import com.phuture.instant.model.Article;
import com.phuture.instant.model.Source;

public class ArticleViewAdapter extends PagedListAdapter<Article, ArticleViewHolder> {

    private final ArticleClickHandler clickHandler;
    private Source source;

    public interface ArticleClickHandler {
        void onArticleClick(Article article);
    }

    public ArticleViewAdapter(ArticleClickHandler clickHandler) {
        super(DIFF_CALLBACK);
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ArticleViewHolder holder, int position) {
        Article article = getItem(position);
        if (article!=null) {
            holder.bindTo(article);
            final Article viewArticle = getItem(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickHandler.onArticleClick(viewArticle);
                }
            });
        } else {
            holder.clear();
        }
    }

    private static DiffUtil.ItemCallback<Article> DIFF_CALLBACK = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.equals(newItem);
        }
    };
}
