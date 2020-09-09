package com.example.aproom.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aproom.R;
import com.example.aproom.database.entities.SubjectEntity;
import com.example.aproom.adapter.SubjectListAdapter;
import com.example.aproom.viewmodel.SubjectListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class SubjectListActivity extends AppCompatActivity implements SubjectListAdapter.OnItemClickListener {
    public static final String EXTRA_NEW_SUBJECT = "EXTRA_NEW_SUBJECT ";
    public static final String EXTRA_UPDATED_SUBJECT = "EXTRA_UPDATED_SUBJECT ";
    public static final String EXTRA_UPDATED_SUBJECT_ID = "EXTRA_UPDATED_SUBJECT_ID ";


    private SubjectListViewModel mSubjectListViewModel;
    private static final String TAG = "<<<MainActivity>>>";
    private SubjectListAdapter mSubjectListAdapter;
    private RecyclerView mRecyclerView;
    private static final int INSERT_NEW_SUBJECT_REQ_CODE = 10;
    private static final int UPDATE_SUBJECT_REQ_CODE = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list_activity);

        mRecyclerView = findViewById(R.id.rvActivityMainID);
        mSubjectListAdapter = new SubjectListAdapter(this);
        mSubjectListAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSubjectListAdapter);
        mSubjectListViewModel = ViewModelProviders.of(this).get(SubjectListViewModel.class);
        mSubjectListViewModel.getAllSubject().observe(this, new Observer<List<SubjectEntity>>() {
            @Override
            public void onChanged(List<SubjectEntity> subjectEntities) {
                mSubjectListAdapter.setSubjectEntities(subjectEntities);
                Log.d(TAG, "onChanged: "+subjectEntities.size());


            }
        });
        FloatingActionButton floatingActionButton = findViewById(R.id.fabMainInsertSubjectId);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSubjectFromUser();
            }
        });
        swipToDelete();

    }

    private void swipToDelete() {
        //item toutch Helper
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    // We are not implementing onMove() in this app.
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    // When the use swipes a word,
                    // delete that word from the database.
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();

                            SubjectEntity subjectEntity = mSubjectListAdapter.getItem(position);
                            Toast.makeText(SubjectListActivity.this, "Subject was deleted" , Toast.LENGTH_LONG).show();
                            mSubjectListViewModel.deleteSubject(subjectEntity);
                    }
                });
        helper.attachToRecyclerView(mRecyclerView);


    }



    @Override
    public void onItemClicked(View view, int position) {
        SubjectEntity subjectEntity = mSubjectListAdapter.getItem(position);
        if (view.getId() == R.id.update_imageView){
            Intent intent = new Intent(SubjectListActivity.this, EditSubjectActivity.class);
            intent.putExtra(EditSubjectActivity.SUBJECT_FOR_UPDATE_EXTRA, subjectEntity);
            startActivityForResult(intent, UPDATE_SUBJECT_REQ_CODE);
        }else {
            Toast.makeText(SubjectListActivity.this, "the item was clicked at position::"+ position
                    +" and id ::"+subjectEntity.getId(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, CardListActivity.class);
            intent.putExtra(CardListActivity.CARD_PARENT_ID, subjectEntity.getId());
            startActivity(intent);

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Toast.makeText(SubjectListActivity.this, "the setting action was clicked", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subject, menu);
        return true;
    }
    private void getSubjectFromUser() {
        Intent intent = new Intent(this, EditSubjectActivity.class);
        startActivityForResult(intent, INSERT_NEW_SUBJECT_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSERT_NEW_SUBJECT_REQ_CODE && resultCode == RESULT_OK){
             SubjectEntity subjectEntity= data.getParcelableExtra(EXTRA_NEW_SUBJECT);
            mSubjectListViewModel.insertSubject(subjectEntity);
        }else if (requestCode == UPDATE_SUBJECT_REQ_CODE && resultCode == RESULT_OK){
            SubjectEntity subjectEntity= data.getParcelableExtra(EXTRA_UPDATED_SUBJECT);
            mSubjectListViewModel.updateSubject(subjectEntity);

        }
    }
}
