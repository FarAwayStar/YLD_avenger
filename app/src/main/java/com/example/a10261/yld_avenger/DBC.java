package com.example.a10261.yld_avenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBC extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="db";
    public static final String KEY_ID="_id";
    public static final String KEY_UNI="uniquekey";
    public static final String KEY_TITLE="title";
    public static final String KEY_CATEGORY="category";
    public static final String KEY_AUTHOR="author";
    public static final String KEY_DATE="date";
    public static final String KEY_URL="url";
    public static final String KEY_IMAGE="image";
    public static final String KEY_CONTENT="content";
    public static final String KEY_TEXT="textt";
    public static final String TABLE_NAME="NEWS";
    public static final int VERSION=1;


    public DBC(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists NEWS (_id INTEGER PRIMARY KEY AUTOINCREMENT,uniquekey TEXT, title TEXT," +
                "author TEXT,date TEXT,category TEXT,url TEXT,image TEXT,content TEXT,textt TEXT)");
        ContentValues cv=new ContentValues();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("Constants", "Upgrading database, which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS NEWS");
        onCreate(db);
    }
}
