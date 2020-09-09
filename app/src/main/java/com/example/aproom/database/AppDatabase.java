package com.example.aproom.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.aproom.R;
import com.example.aproom.database.converter.DateConverter;
import com.example.aproom.database.dao.CardDao;
import com.example.aproom.database.dao.SubjectDao;
import com.example.aproom.database.entities.CardEntity;
import com.example.aproom.database.entities.SubjectEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

@Database(entities = {SubjectEntity.class, CardEntity.class}, version = 1 , exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private final static String TAG = AppDatabase.class.getSimpleName();
     private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "flashcardsdb";
    private static AppDatabase sInstance;
    private static  Context mContext;
    //constructor to build database
    public static AppDatabase getInstance(Context context) {
        mContext = context;
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance: Creating a new database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME

                )
                        .addCallback(sRoomDatabaseCallback) // to populate database
                        .build();

            }
        }
        Log.d(TAG, "getInstance: Getting the database instance, no need to recreated it.");
        return sInstance;
    }

    public abstract SubjectDao subjectDao();
    public abstract CardDao cardDao();
    private static RoomDatabase.Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(sInstance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{
       private SubjectDao mSubjectDao;
       private CardDao mCardDao;

        PopulateDbAsync(AppDatabase db) {
            mSubjectDao = db.subjectDao();
            mCardDao = db.cardDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            insertSubjectsToDb();
            insertCardsByParentId(1);
            insertCardsByParentId(2);
            insertCardsByParentId(3);
            return null;
        }
        private void insertCardsByParentId(int parentId) {
            JSONArray cards = loadJsonArrayCards(mContext);
            try {
                for (int i = 0; i < cards.length(); i++) {
                    JSONObject item = cards.getJSONObject(i);
                    mCardDao.insertCard(new CardEntity(parentId, item.getString("front"),
                                item.getString("back"),
                                new Date()));

                }
            } catch (JSONException exception) {
                exception.printStackTrace();
            }

        }


        private void insertSubjectsToDb() {
            JSONArray subjects = loadJsonArraySubjects(mContext);
            try {
                for (int i = 0; i < subjects.length(); i++) {
                    JSONObject item = subjects.getJSONObject(i);
                    mSubjectDao.insertSubject(new SubjectEntity(item.getString("title"),
                            new Date(), 1));
                }
            } catch (JSONException exception) {
                exception.printStackTrace();
            }

        }

        private static JSONArray loadJsonArraySubjects(Context context) {
            StringBuilder builder = new StringBuilder();
           InputStream in = context.getResources().openRawResource(R.raw.subjects);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                JSONObject json = new JSONObject(builder.toString());
                return json.getJSONArray("subjects");

            } catch (IOException | JSONException exception) {
                exception.printStackTrace();
            }

            return null;
        }
        private static JSONArray loadJsonArrayCards(Context context) {
            StringBuilder builder = new StringBuilder();
            InputStream in = context.getResources().openRawResource(R.raw.cards);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                JSONObject json = new JSONObject(builder.toString());
                return json.getJSONArray("cards");

            } catch (IOException | JSONException exception) {
                exception.printStackTrace();
            }

            return null;
        }
    }
}
