package com.phuture.instant.db;

import android.content.Context;
import android.content.SharedPreferences;

public class Cache {

    public static final String STORAGE_KEY = "cache";

    private Context ctx;
    private static Cache _instance;

    private Cache(Context ctx) {
        this.ctx = ctx;
    }

    public boolean has(String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
        return prefs.contains(key);
    }

    public String getString(String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public Long getLong(String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
        return prefs.getLong(key, -1);
    }

    public Cache set(String key, Long value) {
        SharedPreferences prefs = ctx.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor eprefs = prefs.edit();
        eprefs.putLong(key, value);
        eprefs.apply();

        return this;
    }

    public Cache set(String key, String value) {
        SharedPreferences prefs = ctx.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor eprefs = prefs.edit();
        eprefs.putString(key, value);
        eprefs.apply();

        return this;
    }

    public static Cache instance(Context ctx) {
        if (_instance == null) {
            _instance = new Cache(ctx);
        }
        return _instance;
    }
}
