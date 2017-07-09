package com.example.raheel.interceptapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.raheel.interceptapp.MainActivity;
import com.example.raheel.interceptapp.service.IntercepterService;

/**
 * Created by Raheel on 6/23/2017.
 */

public class InterceptReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(MainActivity.TAG, "Event Received.");
        Intent i= new Intent(context, IntercepterService.class);
        context.startService(i);
    }
}
