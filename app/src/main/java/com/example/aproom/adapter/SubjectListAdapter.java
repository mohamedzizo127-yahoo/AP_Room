package com.example.aproom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aproom.R;
import com.example.aproom.database.entities.SubjectEntity;

import java.util.List;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private List<SubjectEntity> mSubjectEntities;

    public SubjectListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
     }

    public void setSubjectEntities(List<SubjectEntity> subjectEntities) {
        mSubjectEntities = subjectEntities;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        if (mSubjectEntities != null){
            SubjectEntity currentSubjectEntity  = mSubjectEntities.get(position);
            holder.subjectTitleTextView.setText(currentSubjectEntity.getTitle());
            // Hook our custom click item listener to the item view.
            holder.subjectTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClicked(view, position);
                }
            });
            holder.updateIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClicked(view, position);
                }
            });

        }


    }
    /**
     * Method to get item by position.
     * @param position
     * @return
     */
    @Nullable
    public SubjectEntity getItem(int position){
        return mSubjectEntities.get(position);
    }



    @Override
    public int getItemCount() {
        if (mSubjectEntities !=null){
            return mSubjectEntities.size();
        }else
        return 0;
    }

    /**
     * Custom click item listener.
     */
    public interface OnItemClickListener{
        public void onItemClicked(View view, int position);
    }
    OnItemClickListener mOnItemClickListener;

    public void setClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTitleTextView;
        ImageView updateIV;
        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTitleTextView = itemView.findViewById(R.id.tvListItemSubject);
            updateIV = itemView.findViewById(R.id.update_imageView);
        }
    }
}
