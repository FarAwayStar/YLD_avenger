package com.example.a10261.yld_avenger;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class YLD_Getnews extends Thread {
    String type;
    String mainurl;
    Context context;
    Sqlmanager sqlmanager;
    NetListner listner;
    String dd="c97f7d475b93a1ac7e512b2ea1cbc30a";
    String ad="25d28a03359256e9cc3fcd2e02851f52";
    String mz="60ff62fccfb20dc09be087a44f386832";


    public YLD_Getnews(String type, Context context,NetListner listner) {
        this.type = type;
        this.mainurl= String.format("http://v.juhe.cn/toutiao/index?type=%s&key=25d28a03359256e9cc3fcd2e02851f52",type);
        this.context=context;
        this.listner=listner;
        this.sqlmanager=new Sqlmanager(new DBC(context).getWritableDatabase());

    }

    @Override
    public void run() {

        try {
            URL url = new URL(mainurl);
            //找到输入流
            URLConnection conn = url.openConnection();
            //设置从主机读取数据超时（单位：毫秒）
            conn.setConnectTimeout(5000);
            //设置连接主机超时（单位：毫秒）
            conn.setReadTimeout(5000);
            InputStream is = conn.getInputStream();
            //添加到缓冲输入区里
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //字符串变量
            StringBuffer sb = new StringBuffer();
            String s = "";
            //一行一行读下来
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();
            is.close();
            String body = sb.toString();
            JSONObject json1 = new JSONObject(body);
            JSONArray data  =json1.getJSONObject("result").getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject value = data.getJSONObject(i);
                String uniquekey = value.getString("uniquekey");
                String title = value.getString("title");
                String date = value.getString("date");
                String category = value.getString("category");
                String author = value.getString("author_name");
                String weburl = value.getString("url");
                String imageurl = value.getString("thumbnail_pic_s");
                String[] datas= new String[]{uniquekey,title,date,category,author,weburl,imageurl,""};
                Boolean ishave=sqlmanager.check(uniquekey);
                if(!ishave){
                    sqlmanager.insert(datas);
                    new Get_NewsContent(context,weburl,uniquekey).start();
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            sqlmanager.close();
        }

        Activity activity=(Activity)context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listner.Netset();
            }
        });
    }
}

