package com.example.aproom.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import com.example.aproom.adapter.PracticeListAdapter;
import com.example.aproom.database.entities.CardEntity;
import com.example.aproom.viewmodel.CardListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aproom.R;

import java.util.ArrayList;

public class PracticeListActivity extends AppCompatActivity implements PracticeListAdapter.onItemClickListener {
    public static final String PARENT_ID_EXRA = "parentIdExtra";
    int parentId;
    private RecyclerView mRecyclerView;
    private ArrayList<CardEntity> mCurrentCardEntities;
    private PracticeListAdapter mAdapter;
    private ArrayList<CardEntity> mOriginalCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         parentId = getIntent().getIntExtra(PARENT_ID_EXRA, -1);
        mRecyclerView = findViewById(R.id.rvListItemPracticeID);
        mAdapter = new PracticeListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        CardListViewModel.Factroy factory = new CardListViewModel.Factroy(getApplication(), parentId);
        CardListViewModel viewModel = ViewModelProviders.of(this, factory).get(CardListViewModel.class);
        viewModel.getCardsByParentId().observe(this, new Observer<PagedList<CardEntity>>() {
            @Override
            public void onChanged(PagedList<CardEntity> cardEntities) {
                mCurrentCardEntities = new ArrayList<>();
                mCurrentCardEntities.addAll(cardEntities);
                mOriginalCards = new ArrayList<>();
                mOriginalCards.addAll(cardEntities);
                mAdapter.setCardEntities(cardEntities);
            }
        });
        mAdapter.setOnItemClickListener(this);
        moveNext();
        moveBack();
        close();
        correctAnswer();
        wrongAnswer();
        again();


    }

    private void again() {
        ImageView againImageView = findViewById(R.id.again_imageView);
        againImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setCardEntities(mOriginalCards);
            }
        });
    }


    private void wrongAnswer() {
        ImageView wrongAnswerImageView = findViewById(R.id.wrong_imageView);
        wrongAnswerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PracticeListActivity.this, "this is wrong answer try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void correctAnswer() {
        ImageView correctAnswerImageView = findViewById(R.id.correct_imageView);
        correctAnswerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  mCurrentPosition = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (mCurrentPosition == -1){
                    Toast.makeText(PracticeListActivity.this, "You Done", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    return;

                }
                CardEntity currentCard = mAdapter.getItem(mCurrentPosition);
                mCurrentCardEntities.remove(currentCard);
                mAdapter.setCardEntities(mCurrentCardEntities);

            }
        });

    }

    private void close() {
        ImageView closeImageView = findViewById(R.id.close_imageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void moveNext() {
        ImageView nextIV = findViewById(R.id.right_arrow_imageView);
        nextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int  mCurrentPosition = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
               mRecyclerView.smoothScrollToPosition(mCurrentPosition + 1);

            }
        });

    }

    private void moveBack() {
        ImageView backArrow = findViewById(R.id.left_arrow_imageView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mCurrentPosition = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (mCurrentPosition != 0) {
                    mRecyclerView.smoothScrollToPosition(mCurrentPosition - 1);
                } else {
                    Toast.makeText(PracticeListActivity.this, "You reach start", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    @Override
    public void ItemClicked(View v, int position) {
        Toast.makeText(this,"item was clicked",Toast.LENGTH_LONG).show();
        //CardEntity card = adapter.getItem(position);
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(v, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                oa2.start();
                TextView front = v.findViewById(R.id.card_front_textView);
                TextView back = v.findViewById(R.id.card_back_textView);
                if (front.getVisibility() == View.VISIBLE) {
                    front.setVisibility(View.GONE);
                    back.setVisibility(View.VISIBLE);
                    v.setBackgroundColor(getResources().getColor(R.color.colorDefaultBackCard));
                } else {
                    front.setVisibility(View.VISIBLE);
                    back.setVisibility(View.GONE);
                    v.setBackgroundColor(getResources().getColor(R.color.colorDefaultFrontCard));
                }
            }
        });
        oa1.start();
    }

}
