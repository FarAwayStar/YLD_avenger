package com.example.a10261.yld_avenger;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Sqlmanager{

    SQLiteDatabase db;

    public Sqlmanager(SQLiteDatabase db){
        this.db=db;
    }

    public void insert(String[] data){
        ContentValues values=chanageContent(data);
        db.insert(DBC.TABLE_NAME,null,values);
    }

    public void update(String uniq,String content,String text){
        ContentValues cv = new ContentValues();
        cv.put(DBC.KEY_CONTENT,content);
        cv.put(DBC.KEY_TEXT,text);
        db.update(DBC.TABLE_NAME,cv,"uniquekey=?",new String []{uniq});
    }

    public Cursor findbyurl(String url){
        String sql= String.format("select * from %s where %s = ?",DBC.TABLE_NAME,DBC.KEY_URL);
        String []args=new String []{url};
        Cursor cursor= db.rawQuery(sql,args);
        return  cursor;

    }


    public boolean check(String keyID){
        Cursor cursor = db.rawQuery("select count(*) as count from "+DBC.TABLE_NAME+" where uniquekey='"+keyID+"'",
                null);
        cursor.moveToFirst();
        int a=cursor.getInt(cursor.getColumnIndex("count"));
        if(a==0)
            return false;
        else
            return true;
    }

    public ContentValues chanageContent(String []data) {
        ContentValues cv=new ContentValues();
        cv.put(DBC.KEY_UNI,data[0]);
        cv.put(DBC.KEY_TITLE,data[1]);
        cv.put(DBC.KEY_DATE,data[2]);
        cv.put(DBC.KEY_CATEGORY,data[3]);
        cv.put(DBC.KEY_AUTHOR,data[4]);
        cv.put(DBC.KEY_URL,data[5]);
        cv.put(DBC.KEY_IMAGE,data[6]);
        cv.put(DBC.KEY_CONTENT,"");
        return cv;
    }
    public void close(){
        this.db.close();
    }
}
