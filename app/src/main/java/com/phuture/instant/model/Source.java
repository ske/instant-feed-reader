package com.phuture.instant.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.phuture.instant.utils.TimestampConverter;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "source")
public class Source {
    @PrimaryKey(autoGenerate = false) @NonNull public String id;
    @ColumnInfo(name="name") public String name;
    @ColumnInfo(name="url") public String url;
    @ColumnInfo(name="last_refresh") @TypeConverters({TimestampConverter.class}) public Date lastRefresh;

    public Source(@NonNull String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
}
