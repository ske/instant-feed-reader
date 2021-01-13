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
import com.phuture.instant.model.Source;

import java.util.List;

public class SourceListActivity extends AppCompatActivity {

    protected ListView listView;
    protected ArrayAdapter<Source> adapter;
    protected List<Source> sources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_list);

        init();
    }

    protected void init() {
        listView = findViewById(R.id.list);

        sources = Client.instance(this).getDb().sourceDao().getAll();

        adapter = new ArrayAdapter<Source>(this, R.layout.list_item_source, sources) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Source source = getItem(position);
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_source, parent, false);
                }
                TextView title = convertView.findViewById(R.id.title);
                title.setText(source.name);
                return convertView;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Source src = adapter.getItem(position);
                Intent articleList = new Intent(getApplicationContext(), ArticleListActivity.class);
                articleList.putExtra(ArticleListActivity.PARAM_SOURCE_ID, src.id);
                startActivity(articleList);
            }
        });
    }
}