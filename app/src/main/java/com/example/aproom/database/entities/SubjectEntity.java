package com.example.aproom.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "subjects")
public class SubjectEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subject_id")
    private int mId;
    private String mTitle;
    private Integer mColor;
    private Date mDate;

    public SubjectEntity(int mId, String mTitle, Integer mColor, Date mDate) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mColor = mColor;
        this.mDate = mDate;
    }
    @Ignore

    public SubjectEntity(String mTitle, Date mDate,  Integer mColor) {
        this.mTitle = mTitle;
        this.mColor = mColor;
        this.mDate = mDate;
    }

    protected SubjectEntity(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        if (in.readByte() == 0) {
            mColor = null;
        } else {
            mColor = in.readInt();
        }
    }

    public static final Creator<SubjectEntity> CREATOR = new Creator<SubjectEntity>() {
        @Override
        public SubjectEntity createFromParcel(Parcel in) {
            return new SubjectEntity(in);
        }

        @Override
        public SubjectEntity[] newArray(int size) {
            return new SubjectEntity[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Integer getColor() {
        return mColor;
    }

    public void setColor(Integer mColor) {
        this.mColor = mColor;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        if (mColor == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(mColor);
        }
    }
}
