package com.phuture.instant.db.migrations;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.phuture.instant.model.SourceDao;

public class AddNewSources20200115 extends Migration {
    static final int FROM_VERSION = 1;
    static final int TO_VERSION  = 2;

    public AddNewSources20200115() {
        super(FROM_VERSION, TO_VERSION);
    }

    private ContentValues getTelex() {
        ContentValues values = new ContentValues();
        values.put("id", "telex_hu");
        values.put("name", "telex.hu");
        values.put("url", "https://telex.hu/rss");
        return values;
    }

    private ContentValues getHvg() {
        ContentValues values = new ContentValues();
        values.put("id", "hvg_hu");
        values.put("name", "hvg.hu");
        values.put("url", "https://hvg.hu/rss");
        return values;
    }

    private ContentValues getMedia1() {
        ContentValues values = new ContentValues();
        values.put("id", "media1_hu");
        values.put("name", "media1.hu");
        values.put("url", "https://media1.hu/feed");
        return values;
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase db) {
        db.insert("source", SQLiteDatabase.CONFLICT_IGNORE, getTelex());
        db.insert("source", SQLiteDatabase.CONFLICT_IGNORE, getHvg());
        db.insert("source", SQLiteDatabase.CONFLICT_IGNORE, getMedia1());
    }
}
