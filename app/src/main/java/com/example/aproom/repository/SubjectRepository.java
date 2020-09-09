package com.example.aproom.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.aproom.database.AppDatabase;
import com.example.aproom.database.dao.SubjectDao;
import com.example.aproom.database.entities.SubjectEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SubjectRepository {
    private static SubjectRepository sInstance;
    private final SubjectDao mSubjectDao;
    private LiveData<List<SubjectEntity>> mSubjects;
    private final ExecutorService mIoExecutor;

    private SubjectRepository(Application application,  ExecutorService executor) {
        mSubjectDao = AppDatabase.getInstance(application).subjectDao();
        mSubjects = mSubjectDao.getAllSubjects();
        mIoExecutor = executor;

    }
    public static SubjectRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (SubjectRepository.class) {
                if (sInstance == null) {
                    sInstance = new SubjectRepository(application,  Executors.newSingleThreadExecutor());
                }
            }
        }
        return sInstance;
    }

    /*
     void insertSubject(SubjectEntity subjectEntity);
     void updateSubject(SubjectEntity subjectEntity);
     void deleteSubject(SubjectEntity subjectEntity);
      void deleteAll();
    */

//    LiveData<List<SubjectEntity>> getAllSubjects();
    public LiveData<List<SubjectEntity>> getAllSubject(){
        return mSubjects;
    }
//    LiveData<SubjectEntity> getSubjectById(int id);
    public  LiveData<SubjectEntity> getSubjectById(int id){
        return mSubjectDao.getSubjectById(id);
    }
    public void insertSubject(SubjectEntity subjectEntity){
        mIoExecutor.execute(() -> mSubjectDao.insertSubject(subjectEntity));

    }
    public void updateSubject(SubjectEntity subjectEntity){
        mIoExecutor.execute(() -> mSubjectDao.updateSubject(subjectEntity));


    }
    public void deleteSubject(SubjectEntity subjectEntity){
        mIoExecutor.execute(() -> mSubjectDao.deleteSubject(subjectEntity));


    }
    public void deleteAll(){
        mIoExecutor.execute(() -> mSubjectDao.deleteAll());
    }


}
