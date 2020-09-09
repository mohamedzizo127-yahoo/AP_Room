package com.example.aproom.database.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aproom.database.entities.CardEntity;


@Dao
public interface CardDao {
    @Query("SELECT * FROM cards")
        // LiveData<List<CardEntity>> getAllCards(int parentId);
    DataSource.Factory<Integer, CardEntity> getAllCards();


    @Query("SELECT * FROM cards WHERE card_parent_id = :parentId")
   // LiveData<List<CardEntity>> getCardsByParentId(int parentId);
    DataSource.Factory<Integer, CardEntity> getCardsByParentId(int parentId);


    @Query("SELECT * FROM cards WHERE card_id = :id")
    LiveData<CardEntity> getCardById(int id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCard(CardEntity cardEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCard(CardEntity cardEntity);
    @Delete
    void deleteCard(CardEntity cardEntity);
    @Query("DELETE FROM cards")
    void deleteAll();


}
