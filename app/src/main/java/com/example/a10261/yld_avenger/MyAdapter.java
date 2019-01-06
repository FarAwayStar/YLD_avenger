package com.example.a10261.yld_avenger;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MyAdapter extends CursorAdapter {

    Context context;

    public MyAdapter(Context context, Cursor c) {
        super(context, c);
        this.context=context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(context).
                inflate(R.layout.news_item, null, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView=view.findViewById(R.id.iv_news_pic1);
        TextView tv1=view.findViewById(R.id.tv_news_title);
        TextView tv2=view.findViewById(R.id.tv_news_author);
        TextView tv3=view.findViewById(R.id.tv_news_date);

        String title=cursor.getString(cursor.getColumnIndex(DBC.KEY_TITLE));
        String author=cursor.getString(cursor.getColumnIndex(DBC.KEY_AUTHOR));
        String date=cursor.getString(cursor.getColumnIndex(DBC.KEY_DATE));
        String iv=cursor.getString(cursor.getColumnIndex(DBC.KEY_IMAGE));
        if(iv!=null)
            Glide.with(context).load(iv).into(imageView);

        tv1.setText(title);
        tv2.setText(author);
        tv3.setText(date);
        Log.d("Title",cursor.getString(cursor.getColumnIndex(DBC.KEY_TITLE)));
    }

}
