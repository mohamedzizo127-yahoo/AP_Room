package com.example.aproom.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.aproom.database.entities.SubjectEntity;
import com.example.aproom.repository.SubjectRepository;

import java.util.List;

public class SubjectListViewModel extends AndroidViewModel {
    private SubjectRepository mSubjectRepository;
    private LiveData<List<SubjectEntity>> mAllSubjects;
    public SubjectListViewModel(@NonNull Application application) {
        super(application);
        mSubjectRepository = SubjectRepository.getInstance(application);
        mAllSubjects = mSubjectRepository.getAllSubject();
    }
    //    LiveData<List<SubjectEntity>> getAllSubjects();
    public LiveData<List<SubjectEntity>> getAllSubject(){
        return mAllSubjects;
    }
    //    LiveData<SubjectEntity> getSubjectById(int id);
    public  LiveData<SubjectEntity> getSubjectById(int id){
        return mSubjectRepository.getSubjectById(id);
    }
    public void insertSubject(SubjectEntity subjectEntity){
       mSubjectRepository.insertSubject(subjectEntity);
    }
    public void updateSubject(SubjectEntity subjectEntity){
        mSubjectRepository.updateSubject(subjectEntity);


    }
    public void deleteSubject(SubjectEntity subjectEntity){
        mSubjectRepository.deleteSubject(subjectEntity);


    }
    public void deleteAll(){
        mSubjectRepository.deleteAll();
    }

}
