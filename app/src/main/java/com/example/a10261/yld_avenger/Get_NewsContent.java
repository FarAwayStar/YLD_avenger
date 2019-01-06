package com.example.a10261.yld_avenger;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Get_NewsContent extends Thread {

    Context context;
    String url;
    String uniquekey;


    public Get_NewsContent(Context context, String url, String uniquekey) {
        this.context = context;
        this.url=url;
        this.uniquekey=uniquekey;

    }

    @Override
    public void run() {
        Sqlmanager sqlmanager= new Sqlmanager(new DBC(context).getWritableDatabase());
        try {
            URL url = new URL(this.url);
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
            String content = sb.toString();
            Log.d("GKD",content);
            Document doc = Jsoup.parse(content);
            //获得所有p标签
            Elements links = doc.getElementsByTag("p");
            //实例化stringbuffer
            StringBuffer buffer =new StringBuffer();
            for (Element link : links) {
            //将文本提出来
                buffer.append(link.text().trim());
            }
            String text=buffer.toString().trim();
            //Log.d("文本",text);
            sqlmanager.update(uniquekey,content,text);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            sqlmanager.close();
        }


    }
}
