package com.phuture.instant.ui.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phuture.instant.R;
import com.phuture.instant.model.Article;
import com.phuture.instant.model.Source;

import java.util.List;
import java.util.Map;

public class ArticleViewAdapter extends ArrayAdapter<Article> {

    protected Map<String, Source> sourceMap;

    public ArticleViewAdapter(@NonNull Context context, @NonNull List<Article> objects, Map<String, Source> sourceMap) {
        super(context, R.layout.list_item_article, objects);
        this.sourceMap = sourceMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article article = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_article, parent, false);
        }
        TextView title = convertView.findViewById(R.id.title);
        TextView date = convertView.findViewById(R.id.publishedAt);
        TextView sourceName = convertView.findViewById(R.id.sourceName);

        title.setText(article.title);
        date.setText(article.getFormatedDate());
        try {
            sourceName.setText(sourceMap.get(article.sourceId).name);
        } catch (Exception e) {}

        return convertView;
    }
}
