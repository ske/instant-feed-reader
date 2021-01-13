package com.phuture.instant.ui.views.article;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuture.instant.R;
import com.phuture.instant.model.Article;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    protected TextView title;
    protected TextView date;
    protected TextView sourceTitle;

    public Article article;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        date = itemView.findViewById(R.id.publishedAt);
        sourceTitle = itemView.findViewById(R.id.sourceName);
    }
}
