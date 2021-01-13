package com.phuture.instant.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.phuture.instant.R;
import com.phuture.instant.db.Client;
import com.phuture.instant.model.Article;
import com.phuture.instant.model.Source;
import com.phuture.instant.network.Downloader;
import com.phuture.instant.network.IDownloadResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ArticleListActivity extends AppCompatActivity implements IDownloadResult {

    static String PARAM_SOURCE_ID = "sourceId";

    protected Source src;
    protected ListView listView;
    protected ArrayAdapter<Article> adapter;
    protected List<Article> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        init();
    }

    protected void init() {
        listView = findViewById(R.id.list);

        String sourceId = getIntent().getStringExtra(PARAM_SOURCE_ID);
        src = Client.instance(this).getDb().sourceDao().get(sourceId);

        adapter = new ArrayAdapter<Article>(this, R.layout.list_item_article, articles) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Article article = getItem(position);
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_article, parent, false);
                }
                TextView title = convertView.findViewById(R.id.title);
                TextView date = convertView.findViewById(R.id.publishedAt);
                title.setText(article.title);
                date.setText(article.getFormatedDate());

                return convertView;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = adapter.getItem(position);
                String url = article.url;
                Intent webView = new Intent(getApplicationContext(), WebViewActivity.class);
                webView.putExtra(WebViewActivity.PARAM_URL, article.url);
                startActivity(webView);
            }
        });

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, -10);

        if (src.lastRefresh == null || (src.lastRefresh != null && src.lastRefresh.before(c.getTime()))) {
            // refresh if the source was downloaded more then 10 min before
            Downloader.execute(this, src.url, src, this);
        } else {
            articles.clear();
            articles.addAll(Client.instance(this).getDb().articleDao().getAllBySource(src.id));
            adapter.notifyDataSetChanged();
        }

        ArticleListActivity.this.setTitle(src.name);
    }

    @Override
    public void onResultReceived(String result, Object data) {
        if (result == null) return;

        // TODO hibakezeles, ha hiba van

        Article.storeFromXml(this, src.id, result);
        articles.clear();
        articles.addAll(Client.instance(this).getDb().articleDao().getAllBySource(src.id));

        if (articles.size() > 0) {
            // Update src with last refresh date
            src.lastRefresh = new Date();
            Client.instance(this).getDb().sourceDao().update(src);
        }

        // request refresh the listview
        adapter.notifyDataSetChanged();
    }
}