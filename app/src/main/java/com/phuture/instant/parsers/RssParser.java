package com.phuture.instant.parsers;

import android.content.Context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.phuture.instant.model.Article;
import com.phuture.instant.model.Source;
import com.phuture.instant.model.rss.Item;
import com.phuture.instant.model.rss.Rss;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class RssParser {
    protected String xml;
    protected Context ctx;
    protected Rss result;
    protected String sourceId;

    public RssParser(Context ctx, String sourceId, String xml) {
        this.xml = xml;
        this.sourceId = sourceId;
        this.ctx = ctx;
    }

    public List<Article> getArticles(Rss rss) {
        List<Article> articles = new ArrayList<>();

        try {
            for (ListIterator<Item> it = rss.channel.item.listIterator(rss.channel.item.size()); it.hasPrevious();) {
                articles.add(Article.create(this.sourceId, it.previous()));
            }

        } catch (Exception e) {
            System.out.println("articles-from-rss failed: " + e.getMessage());
        }

        return articles;
    }

    public Rss getResult() {
        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            result = mapper.readValue(this.xml, Rss.class);
        } catch (Exception e) {
            System.out.println("mapping failed: " + e.getMessage());
        }

        return result;
    }
}
