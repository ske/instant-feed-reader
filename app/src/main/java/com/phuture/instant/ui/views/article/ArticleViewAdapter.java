package com.phuture.instant.ui.views.article;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuture.instant.R;
import com.phuture.instant.model.Article;

import java.util.List;

public class ArticleViewAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

    public static interface ArticleClickHandler {
        public void onArticleClick(Article article);
    }

    private final List<Article> articles;
    private final ArticleClickHandler clickHandler;

    public ArticleViewAdapter(List<Article> articles, ArticleClickHandler clickHandler) {
        this.articles = articles;
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
    public void onBindViewHolder(final @NonNull ArticleViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.onArticleClick(articles.get(position));
            }
        });
        holder.article = articles.get(position);
        holder.title.setText(articles.get(position).title);
        holder.date.setText(articles.get(position).getFormatedDate());
        holder.sourceTitle.setText(articles.get(position).sourceId);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
