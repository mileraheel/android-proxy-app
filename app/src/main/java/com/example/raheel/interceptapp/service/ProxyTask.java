package com.example.raheel.interceptapp.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.example.raheel.interceptapp.MainActivity;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Raheel on 7/6/2017.
 */

public class ProxyTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;

    public ProxyTask(Context context) {
        this.context = context;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        ServerSocket serverSocket = null;
        boolean listening = true;

        int port = 8888;    //default

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            serverSocket = new ServerSocket(port);
            System.out.println("Started on: " + port);

            while (listening) {
                new ProxyThread(context, serverSocket.accept()).start();
            }
            serverSocket.close();

        } catch (IOException e) {
            Log.w(MainActivity.TAG, "Could not listen on port: " + port);
            Log.e(MainActivity.TAG, e.getMessage());
            System.exit(-1);
        }
        return true;
    }
}
