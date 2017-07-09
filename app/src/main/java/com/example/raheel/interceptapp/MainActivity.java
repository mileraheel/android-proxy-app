package com.example.raheel.interceptapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.raheel.interceptapp.service.IntercepterService;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "IntercepterApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i= new Intent(getBaseContext(), IntercepterService.class);
        getBaseContext().startService(i);

        Toast.makeText(this,"Proxy Server Now Active.", Toast.LENGTH_LONG).show();
        thread.start();


    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                MainActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
