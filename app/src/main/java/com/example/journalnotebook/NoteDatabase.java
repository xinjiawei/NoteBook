package com.example.journalnotebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NoteDatabase extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "notes";
    public static final String CONTENT = "content";

    public static final String ENDPOINT= "endpoint";
    public static final String PRICE = "price";
    public static final String TEXT = "text";
    public static final String FILEID = "fileid";
    public static final String FILETAG = "filetag";

    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String MODE = "mode";

    public NoteDatabase(Context context){
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME
                + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTENT + " TEXT NOT NULL,"
                + ENDPOINT + " TEXT NOT NULL,"
                + PRICE + " TEXT NOT NULL,"
                + TEXT + " TEXT NOT NULL,"
                + FILEID + " INTEGER DEFAULT 1,"
                + FILETAG + " TEXT DEFAULT 'default.png',"
                + TIME + " TEXT NOT NULL,"
                + MODE + " INTEGER DEFAULT 1)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
