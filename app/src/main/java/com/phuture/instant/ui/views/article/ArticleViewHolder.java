package com.phuture.instant.ui.views.article;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuture.instant.R;
import com.phuture.instant.model.Article;
import com.phuture.instant.model.Source;
import com.phuture.instant.ui.data.ISourceResolver;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    protected TextView title;
    protected TextView date;
    protected TextView sourceTitle;
    private ISourceResolver sourceResolver;

    public Article article;

    public ArticleViewHolder(@NonNull View itemView, ISourceResolver sourceResolver) {
        super(itemView);
        this.sourceResolver = sourceResolver;

        title = itemView.findViewById(R.id.title);
        date = itemView.findViewById(R.id.publishedAt);
        sourceTitle = itemView.findViewById(R.id.sourceName);
    }

    public void bindTo(Article article) {
        this.article = article;

        this.title.setText(this.article.title);
        this.date.setText(this.article.getFormatedDate());

        Source source = this.sourceResolver.getSourceById(this.article.sourceId);

        this.sourceTitle.setText(source.name);
    }

    public void clear() {
        this.article = null;

        this.title.setText("");
        this.date.setText("");
        this.sourceTitle.setText("");
    }
}
