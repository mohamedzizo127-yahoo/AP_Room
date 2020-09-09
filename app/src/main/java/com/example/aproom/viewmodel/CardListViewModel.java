package com.example.aproom.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;

import com.example.aproom.database.entities.CardEntity;
import com.example.aproom.repository.CardRepository;

public class CardListViewModel extends AndroidViewModel {
    private CardRepository mCardRepository;
    private LiveData<PagedList<CardEntity>> mCardsByParentId;
    private final int mCardParentId;
    public CardListViewModel(@NonNull Application application, int cardParentId) {
        super(application);
        mCardRepository = CardRepository.getInstance(application);
        mCardParentId = cardParentId;
        mCardsByParentId = mCardRepository.getCardsById(mCardParentId);
    }
     public LiveData<PagedList<CardEntity>> getCardsByParentId() {
        return mCardsByParentId;
    }

    public void insertCard(CardEntity subjectEntity){
       mCardRepository.insertCard(subjectEntity);
    }
    public void updateSubject(CardEntity subjectEntity){
        mCardRepository.updateCard(subjectEntity);


    }
    public void deleteSubject(CardEntity subjectEntity){
        mCardRepository.deleteCard(subjectEntity);


    }
    public void deleteAll(){
        mCardRepository.deleteAll();
    }

    public static class Factroy extends ViewModelProvider.NewInstanceFactory{
        @NonNull
        private final Application mApplication;
        private final int mCardParentId;

        public Factroy(@NonNull Application application, int parentId) {
            mApplication = application;
            mCardParentId = parentId;
         }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CardListViewModel(mApplication, mCardParentId);
        }
    }

}

