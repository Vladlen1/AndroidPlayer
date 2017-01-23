package com.example.vladbirukov.player;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper  {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = " bd_music_compositions ";
    public static final String TABLE_NAME = " category ";
    public static final String TABLE_NAME2 = "music";



    public static final String KEY_ID = " id ";
    public static final String KEY_NAME = " name ";
    public static final String KEY_IMAGE = " image ";
    public static final String KEY_CATEGORY = " category ";
    public static final String KEY_REPEAT = " repeat ";




    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" create table " + TABLE_NAME + " ( " + KEY_ID + " integer primary key, " + KEY_NAME + " text, " + KEY_IMAGE + " image " + " ) ");
        db.execSQL(" create table " + TABLE_NAME2 + " ( " + KEY_ID + " integer primary key, " + KEY_NAME + " text, " + KEY_IMAGE + " image, " + KEY_CATEGORY + " text, " + KEY_REPEAT + " repeat " + " ) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS music");
        onCreate(db);

    }
}
