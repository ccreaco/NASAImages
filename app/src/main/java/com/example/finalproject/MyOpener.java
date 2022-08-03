package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {

    //Declaring and setting the variables
    protected final static String DATABASE_NAME = "imagesDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "images_info";
    public final static String COL_7 = "copyright";
    public final static String COL_6 = "description";
    public final static String COL_5 = "imgurlHD";
    public final static String COL_4 = "imgurl";
    public final static String COL_3 = "imgDate";
    public final static String COL_2 = "title";
    public final static String COL_1 = "ID";


    public MyOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    //Method that creates the table in the database if no database file exists
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT, " +
                COL_6 + " TEXT, " +
                COL_7 + " TEXT);"
        );
    }

    //Method that gets called if the database version is lower than the current version_num
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }

    //Method that inserts the data into the database table
    public long insertData(String title, String imgDate, String imgurl, String hdurl, String description, String copyright) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, title);
        contentValues.put(COL_3, imgDate);
        contentValues.put(COL_4, imgurl);
        contentValues.put(COL_5, hdurl);
        contentValues.put(COL_6, description);
        contentValues.put(COL_7, copyright);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return -1;
        } else {
            return result;
        }
    }

    //Method to delete the information about the image
    public boolean deleteImage(String t) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(TABLE_NAME, COL_2 + " = ?", new String[]{t});

        if (delete == 1) {
            return true;
        } else
            return false;

    }

}
