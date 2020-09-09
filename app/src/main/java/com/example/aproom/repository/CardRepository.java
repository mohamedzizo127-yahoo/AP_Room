package com.example.aproom.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.aproom.database.AppDatabase;
import com.example.aproom.database.dao.CardDao;
import com.example.aproom.database.entities.CardEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardRepository {
    private static CardRepository sInstance;
    private final CardDao mCardDao;
     private final ExecutorService mIoExecutor;

    private CardRepository(Application application, ExecutorService executor) {
        mCardDao = AppDatabase.getInstance(application).cardDao();
        mIoExecutor = executor;

    }
    public static CardRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (CardRepository.class) {
                if (sInstance == null) {
                    sInstance = new CardRepository(application,  Executors.newSingleThreadExecutor());
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
    public LiveData<PagedList<CardEntity>> getCardsById(int id) {
        PagedList.Config pagedListConfig = (new PagedList.Config.Builder().
                setEnablePlaceholders(true)
                .setPrefetchDistance(10)
                .setPageSize(20)).build();
          LiveData<PagedList<CardEntity>> cardsById = (new LivePagedListBuilder(mCardDao.getCardsByParentId(id), pagedListConfig)).build();
          return cardsById;
    }

    public void insertCard(CardEntity cardEntity){
        mIoExecutor.execute(() -> mCardDao.insertCard(cardEntity));

    }
    public void updateCard(CardEntity cardEntity){
        mIoExecutor.execute(() -> mCardDao.updateCard(cardEntity));


    }
    public void deleteCard(CardEntity cardEntity){
        mIoExecutor.execute(() -> mCardDao.deleteCard(cardEntity));


    }
    public void deleteAll(){
        mIoExecutor.execute(() -> mCardDao.deleteAll());
    }


}
