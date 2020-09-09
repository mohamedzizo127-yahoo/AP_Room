package com.example.aproom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aproom.database.entities.CardEntity;
import com.example.aproom.R;

import java.util.List;

public class CardListAdapter extends PagedListAdapter<CardEntity, CardListAdapter.CardViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private List<CardEntity> mCardEntities;

    public CardListAdapter(Context context) {
        super(CardEntity.DIFF_CALLBACK);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if (mCardEntities != null) {
            CardEntity cureentCardEntity = mCardEntities.get(position);
            holder.frontSideTv.setText(cureentCardEntity.getFrontSide());
            holder.backSideTv.setText(cureentCardEntity.getBackSide());
        }


    }

    @Override
    public int getItemCount() {
        if (mCardEntities == null) return 0;
        else return mCardEntities.size();

    }
    public CardEntity getItem(int postion){
        return mCardEntities.get(postion);
    }


    public void setCardEntities(List<CardEntity> cardEntities) {
        mCardEntities = cardEntities;
        notifyDataSetChanged();

    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView frontSideTv, backSideTv;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            frontSideTv = itemView.findViewById(R.id.tvListItemCardFrontSideId);
            backSideTv = itemView.findViewById(R.id.tvListItemCardBackSideId);
        }
    }
}
