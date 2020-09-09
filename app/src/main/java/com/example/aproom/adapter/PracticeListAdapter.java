package com.example.aproom.adapter;

import android.content.Context;
import android.content.Entity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aproom.R;
import com.example.aproom.database.entities.CardEntity;

import java.util.List;

public class PracticeListAdapter extends PagedListAdapter<CardEntity, PracticeListAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<CardEntity> mCardEntities;
    public PracticeListAdapter(Context context) {
        super(CardEntity.DIFF_CALLBACK);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.list_item_practice, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCardEntities != null) {
            CardEntity currentCardEntity = mCardEntities.get(position);
            holder.frontTv.setText(currentCardEntity.getFrontSide());
            holder.backTv.setText(currentCardEntity.getBackSide());
            // Hook our custom click item listener to the item view.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.ItemClicked(view, position);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        if (mCardEntities != null) return mCardEntities.size();
        else return 0;
    }


    public CardEntity getItem(int position) {
        return mCardEntities.get(position);
    }



    public void setCardEntities(List<CardEntity> cardEntities) {
        mCardEntities = cardEntities;
        notifyDataSetChanged();

    }
    /*
    * custem click lisitner
    * */
    onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener{
        void ItemClicked(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView frontTv, backTv;
        ImageView flipIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            frontTv = itemView.findViewById(R.id.card_front_textView);
            backTv = itemView.findViewById(R.id.card_back_textView);
            flipIv = itemView.findViewById(R.id.flip_card_imageView);
            flipIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (frontTv.getVisibility() == View.GONE){
                        frontTv.setVisibility(View.VISIBLE);
                        backTv.setVisibility(View.GONE);
                    }else{
                        frontTv.setVisibility(View.GONE);
                        backTv.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    }
}
