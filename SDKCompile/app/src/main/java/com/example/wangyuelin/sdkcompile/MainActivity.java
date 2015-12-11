package com.example.wangyuelin.sdkcompile;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aimeizhuyi.users.analysis.DataCollect;


public class MainActivity extends ActionBarActivity {
    private String classPath =  this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataCollect.setDebugMode(true);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //构造异常
                try {

                    int i = 0;
                    int j = 4 / i;
                } catch (Exception e) {
                    DataCollect.onExecption(MainActivity.this, "111111", e); //主线程
                }


                DataCollect.onExecption(MainActivity.this, "22222","异常信息测试");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int[] num = {0, 0, 0};
                        try {
                            int k = num[6];
                        } catch (Exception e) {
                            DataCollect.onExecption(MainActivity.this, "3333" +"." + this.getClass().getName(),e); //子线程的类路径
                        }

                    }
                }).start();

            }
        });
        DataCollect.setDebugMode(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        DataCollect.setDebugMode(true);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataCollect.onResume(this,"1","页面");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataCollect.onPause(this,"1","页面");
    }
}
