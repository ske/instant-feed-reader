package com.phuture.instant.model;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ArticleDao {
    @Query("SELECT * FROM article ORDER BY date DESC")
    List<Article> getAll();

    @Query("SELECT * FROM article WHERE source_id=:sourceId ORDER BY date DESC")
    List<Article> getAllBySource(String sourceId);

    @Query("SELECT * FROM article ORDER BY date DESC")
    DataSource.Factory<Integer, Article> getAllPaged();

    @Query("SELECT * FROM article WHERE source_id=:sourceId ORDER BY date DESC")
    DataSource.Factory<Integer, Article> getAllBySourcePaged(String sourceId);

    @Query("SELECT * FROM article WHERE id=:id LIMIT 1")
    Article get(String id);

    @Insert
    void insert(Article source);
    @Delete
    void delete(Article source);
    @Update
    void update(Article source);

}
