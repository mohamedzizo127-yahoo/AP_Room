package com.example.aproom.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aproom.database.entities.SubjectEntity;

import java.util.List;

@Dao
public interface SubjectDao {
    @Query("SELECT * FROM subjects")
    LiveData<List<SubjectEntity>> getAllSubjects();

    @Query("SELECT * FROM subjects WHERE subject_id = :id")
    LiveData<SubjectEntity> getSubjectById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubject(SubjectEntity subjectEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSubject(SubjectEntity subjectEntity);

    @Delete
    void deleteSubject(SubjectEntity subjectEntity);

    @Query("DELETE FROM subjects")
    void deleteAll();
}
