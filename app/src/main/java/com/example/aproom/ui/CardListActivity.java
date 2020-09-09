package com.example.aproom.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.aproom.database.entities.CardEntity;
import com.example.aproom.R;
import com.example.aproom.adapter.CardListAdapter;
import com.example.aproom.viewmodel.CardListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

public class CardListActivity extends AppCompatActivity {
    private CardListViewModel mCardListViewModel;
    int mCardParentId;
     public static final String CARD_PARENT_ID = "card_parent_id";
    private RecyclerView mRecyclerView;
    private CardListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCardParentId = getIntent().getIntExtra(CARD_PARENT_ID, -1);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Snackbar.make(view, "card parent id  = " + mCardParentId, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                CardEntity cardEntity = new CardEntity(mCardParentId, "this front side #"+"of::"+mCardParentId,
                        "this is back side  # "+ "of::"+ mCardParentId, new Date());
                mCardListViewModel.insertCard(cardEntity);
            }
        });
        mRecyclerView = findViewById(R.id.rvCardListActivityId);
        mAdapter = new CardListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CardListViewModel.Factroy factroy = new CardListViewModel.Factroy(getApplication(), mCardParentId);
        mCardListViewModel = ViewModelProviders.of(this, factroy).get(CardListViewModel.class);
        mCardListViewModel.getCardsByParentId().observe(this, new Observer<PagedList<CardEntity>>() {
            @Override
            public void onChanged(PagedList<CardEntity> cardEntities) {
                mAdapter.setCardEntities(cardEntities);
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
                        CardEntity cardEntity = mAdapter.getItem(position);
                        Toast.makeText(CardListActivity.this, "Subject was deleted" , Toast.LENGTH_LONG).show();
                        mCardListViewModel.deleteSubject(cardEntity);
                    }
                });
        helper.attachToRecyclerView(mRecyclerView);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_start_practice){
            Toast.makeText(CardListActivity.this, "the practice action was clicked", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CardListActivity.this, PracticeListActivity.class);
            intent.putExtra(PracticeListActivity.PARENT_ID_EXRA, mCardParentId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card, menu);
        return true;
    }
}