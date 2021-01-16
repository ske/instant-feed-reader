package com.phuture.instant.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.phuture.instant.db.Client;
import com.phuture.instant.model.rss.Item;
import com.phuture.instant.model.rss.Rss;
import com.phuture.instant.parsers.RssParser;
import com.phuture.instant.utils.MD5;
import com.phuture.instant.utils.TimestampConverter;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "article")
public class Article {

    @PrimaryKey(autoGenerate = false) @NonNull public String id;
    @ColumnInfo(name = "title") public String title;
    @ColumnInfo(name = "image") public String image;
    @ColumnInfo(name = "source_id") public String sourceId;
    @ColumnInfo(name = "date") @TypeConverters({TimestampConverter.class}) public Date date;
    @ColumnInfo(name = "text") public String text;
    @ColumnInfo(name = "url") public String url;
    @ColumnInfo(name = "author") public String author;

    // @Ignore protected Source source;

    public Article(@NonNull String id) {
        this.id = id;
    }

    public String getFormatedDate() {
        return TimestampConverter.toStr(this.date);
    }

    protected Article(String title, String sourceId, String image, String date, String text, String url) {
        this.id = MD5.get(url);
        this.sourceId = sourceId;

        this.title = title;
        this.image = image;
        this.date = TimestampConverter.fromTimestamp(date);
        this.text = text;
        this.url = url;
    }

    public interface IArticleStoreStatus {
        Exception getXmlError();
        int getTotalArticles();
        int getNewArticles();
    }

    public static IArticleStoreStatus storeFromXml(Context ctx, String sourceId, String xml) {
        Exception _xmlError = null;
        int _newArticles = 0;
        int _totalArticles = 0;

        try {
            RssParser parser = new RssParser(ctx, sourceId, xml);
            Rss rss = parser.getResult();
            List<Article> articles = parser.getArticles(rss);

            for (Article article: articles) {
                try {
                    Client.instance(ctx).getDb().articleDao().insert(article);
                    _newArticles++;
                } catch (Exception e) {
                    // Duplicate or erroneous article
                    System.out.println("store-article failed: " + e.getMessage());
                }
                _totalArticles++;
            }
        } catch (Exception e) {
            _xmlError = e;
            System.out.println("parse-store-source failed" + e.getMessage());
        }

        // Assembly the status response
        final Exception xmlError = _xmlError;
        final int newArticles = _newArticles;
        final int totalArticles = _totalArticles;

        return new IArticleStoreStatus() {
            @Override
            public Exception getXmlError() {
                return xmlError;
            }

            @Override
            public int getTotalArticles() {
                return totalArticles;
            }

            @Override
            public int getNewArticles() {
                return newArticles;
            }
        };

    }

    public static Article create(String sourceId, Item item) {
        return new Article(
                item.title,
                sourceId,
                null,
                item.pubDate,
                item.description,
                item.link
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id.equals(article.id) &&
                Objects.equals(title, article.title) &&
                Objects.equals(image, article.image) &&
                Objects.equals(sourceId, article.sourceId) &&
                Objects.equals(date, article.date) &&
                Objects.equals(text, article.text) &&
                Objects.equals(url, article.url) &&
                Objects.equals(author, article.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, image, sourceId, date, text, url, author);
    }
}
