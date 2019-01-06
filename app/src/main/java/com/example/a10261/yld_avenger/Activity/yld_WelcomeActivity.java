package com.example.a10261.yld_avenger.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.a10261.yld_avenger.R;

import java.util.Timer;
import java.util.TimerTask;

public class yld_WelcomeActivity extends AppCompatActivity {

    boolean flag=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,//去掉系统顶部任务栏
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.yld_welcome);
        TextView tv=findViewById(R.id.skip);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                tiaozhuan();
            }
        });
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                if(!flag)
                    tiaozhuan();
            }
        };
        timer.schedule(timerTask,2000);
    }

    public void tiaozhuan(){
        Intent intent1=new Intent(yld_WelcomeActivity.this,yld_MainActivity.class);
        startActivity(intent1);
        yld_WelcomeActivity.this.finish();
    }
}
