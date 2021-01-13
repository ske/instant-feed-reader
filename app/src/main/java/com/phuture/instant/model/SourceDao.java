package com.phuture.instant.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Map;

@Dao
public interface SourceDao {
    @Query("SELECT * FROM source ORDER BY name ASC")
    List<Source> getAll();

    @Query("SELECT * FROM source WHERE id=:id LIMIT 1")
    Source get(String id);

    @Insert void insert(Source source);
    @Delete void delete(Source source);
    @Update void update(Source source);

}
