package com.example.a10261.yld_avenger.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.a10261.yld_avenger.DBC;
import com.example.a10261.yld_avenger.FontSize;
import com.example.a10261.yld_avenger.MyAdapter;
import com.example.a10261.yld_avenger.NetListner;
import com.example.a10261.yld_avenger.R;
import com.example.a10261.yld_avenger.YLD_Getnews;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;

public class yld_MainActivity extends AppCompatActivity {

    ListView listView;
    Spinner spinner;
    SearchView searchView;
    SQLiteDatabase db;
    Cursor cursor;
    MyAdapter adapter;
    String[] news_item2;
    int sp=0;

    {
        news_item2 = new String[]{"top", "shehui", "guonei","guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang"};
    }

    String[] news_item;
    {
        news_item=new String[]{"头条", "社会", "国内","国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"};
    };



    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.main_layout);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,news_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner=findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        searchView=findViewById(R.id.searchview);
        listView=findViewById(R.id.listview);
        db=new DBC(this).getWritableDatabase();
        new YLD_Getnews("top",this, new NetListner() {
            @Override
            public void Netset() {
                cursor= db.rawQuery("select * from "+DBC.TABLE_NAME,null);
                adapter=new MyAdapter(yld_MainActivity.this,cursor);
                listView.setAdapter(adapter);
            }
        }).start();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText)){
                    String sql=String.format("select * from %s where %s like ? or %s like ?",
                            DBC.TABLE_NAME,DBC.KEY_TITLE,DBC.KEY_TEXT);
                    String[] args=new String[]{"%"+newText+"%","%"+newText+"%"
                    };
                    Cursor query=db.rawQuery(sql,args);
                    adapter.swapCursor(query);
                }else {
                    SQLiteDatabase db1=new DBC(yld_MainActivity.this).getWritableDatabase();
                    Cursor cursor2= db1.rawQuery("select * from "+DBC.TABLE_NAME+" where category ='"+news_item[sp]+"'",null);
                    if(adapter!=null)
                        adapter.swapCursor(cursor2);
                    db1.close();
                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                sp=position;
                new YLD_Getnews(news_item2[position], yld_MainActivity.this , new NetListner() {
                    @Override
                    public void Netset() {
                        Cursor cursor1=db.rawQuery("select * from "+DBC.TABLE_NAME+" where category = '"+
                                news_item[position]+"'",null);
                        if(adapter!=null)
                            adapter.changeCursor(cursor1);
                    }
                }).start();

               // Log.e("类型",news_item2[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c=adapter.getCursor();
                c.moveToPosition(position);
                String url=c.getString(c.getColumnIndex(DBC.KEY_URL));
                Log.d("test",url);
                Bundle bundle = new Bundle();
                bundle.putString("URL",url);
                Intent intent = new Intent(yld_MainActivity.this,WebActivity.class);
                intent.putExtras(bundle);
                startActivity(intent,bundle);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case  R.id.fontsize:
                ChangeFS();
                break;
            case  R.id.mode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                db.close();
                recreate();
                break;
            case R.id.baitian:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                db.close();
                recreate();
                break;
            case R.id.reflash:
                db.close();
                recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void ChangeFS() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("修改字体");
        View view= LayoutInflater.from(this).inflate(R.layout.font_size_view,null,false);

        builder.setView(view);
        builder.setNegativeButton("取消",null);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        RadioGroup radioGroup=view.findViewById(R.id.fontsizeR);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radioButton_s:
                        FontSize.scaleTextSize(yld_MainActivity.this,FontSize.SMALL);
                        db.close();
                        recreate();
                        break;
                    case R.id.radioButton_m:
                        FontSize.scaleTextSize(yld_MainActivity.this,FontSize.Middle);
                        db.close();
                        recreate();
                        break;
                    case R.id.radioButton_l:
                        FontSize.scaleTextSize(yld_MainActivity.this,FontSize.Big);
                        db.close();
                        recreate();
                        break;
                }
                dialog.cancel();
            }
        });

    }

}
