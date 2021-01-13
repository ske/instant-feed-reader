package com.phuture.instant.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.phuture.instant.model.Article;
import com.phuture.instant.model.ArticleDao;
import com.phuture.instant.model.Source;
import com.phuture.instant.model.SourceDao;

@androidx.room.Database(entities = {Article.class, Source.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract SourceDao sourceDao();
    public abstract ArticleDao articleDao();
}
