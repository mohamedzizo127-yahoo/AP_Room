package com.example.aproom.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aproom.R;
import com.example.aproom.database.entities.SubjectEntity;

import java.util.Date;

public class EditSubjectActivity extends AppCompatActivity {
    EditText titleEt, dateEt, colorEt;
    Button saveBtn;
    public static final String SUBJECT_FOR_UPDATE_EXTRA = "SUBJECT_FOR_UPDATE_EXTRA";
    private int mId = -1;
    private SubjectEntity mSubjectEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);

        titleEt = findViewById(R.id.etAddSubjectTitleID);
        dateEt =  findViewById(R.id.etAddSubjectDataID);
        colorEt =  findViewById(R.id.etAddSubjectColorID);
        saveBtn =  findViewById(R.id.btnAddSubjectSaveID);
        if (getIntent().hasExtra(SUBJECT_FOR_UPDATE_EXTRA)){
            mSubjectEntity = getIntent().getParcelableExtra(SUBJECT_FOR_UPDATE_EXTRA);
            String title = mSubjectEntity.getTitle();
            mId = mSubjectEntity.getId();
            titleEt.setText(title);
            colorEt.setText(mId +"");
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEt.getText().toString();
                if (TextUtils.isEmpty(title)){
                    Toast.makeText(EditSubjectActivity.this,"can't save empty subject", Toast.LENGTH_LONG).show();
                    return;
                }

                if (mId == -1){
                    Intent intent = new Intent();
                    SubjectEntity subjectEntity = new SubjectEntity(title, new Date(), 2);
                    intent.putExtra(SubjectListActivity.EXTRA_NEW_SUBJECT, subjectEntity);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    Intent intent = new Intent();
                    mSubjectEntity.setTitle(title);
                    intent.putExtra(SubjectListActivity.EXTRA_UPDATED_SUBJECT, mSubjectEntity);
                     setResult(RESULT_OK, intent);
                    finish();
                }


            }
        });

    }

}