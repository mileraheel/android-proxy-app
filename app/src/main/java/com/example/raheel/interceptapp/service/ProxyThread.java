package com.example.raheel.interceptapp.service;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.raheel.interceptapp.data.Menu;
import com.example.raheel.interceptapp.data.MenuDatabase;
import com.example.raheel.interceptapp.data.MenuListProvider;
import com.example.raheel.interceptapp.MainActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Raheel on 7/6/2017.
 */

public class ProxyThread extends Thread {

    private String urlToIntercept = "http://www.mocky.io/v2/595bf750100000ef01d0b3fb";

    private Socket socket = null;
    private static final int BUFFER_SIZE = 32768;

    Map<String, String> headers = new HashMap<>();

    private Context context;
    private InputStream is;
    private String urlToCall;
    private String method;
    private HttpURLConnection con;


    public ProxyThread(Context context, Socket socket) {
        super("ProxyThread");
        this.context = context;
        this.socket = socket;
    }

    public void run() {
        Log.w(MainActivity.TAG, "Request Intercepted....");

        DataOutputStream out = null;
        BufferedReader in = null;


        try {

            out  = new DataOutputStream(socket.getOutputStream());

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            int cnt = 0;


            while ((inputLine = in.readLine()) != null) {
                try {
                    if (cnt == 0) {
                        String[] tokens = inputLine.split(" ");
                        method = tokens[0];
                        urlToCall = tokens[1];
                        Log.w(MainActivity.TAG, "Url Requested :" + urlToCall);
                    } else {
                        String[] tokens = inputLine.split(":");
                        String name = tokens[0].trim();
                        String value = tokens[1].trim();
                        if (!value.equals("null") && !value.isEmpty()) {
                            headers.put(name, value);
                        }
                    }
                } catch (Exception e) {
                    break;
                }
                cnt++;
            }

            if (urlToCall != null) {

                if (urlToCall.equals(urlToIntercept)) {
                    Log.w(MainActivity.TAG, "Url to be intercepted.");
                    Cursor cursor = context.getContentResolver().query(MenuListProvider.CONTENT_URI,
                            null, null, null, null);
                    List<Menu> menus = new ArrayList<>();
                    if (cursor !=null && cursor.moveToFirst()) {
                        do {

                            Menu menu = new Menu(
                                    cursor.getString(cursor.getColumnIndex(MenuDatabase.COL_ID)),
                                    cursor.getString(cursor.getColumnIndex(MenuDatabase.COL_NAME)),
                                    cursor.getDouble(cursor.getColumnIndex(MenuDatabase.COL_PRICE)),
                                    cursor.getString(cursor.getColumnIndex(MenuDatabase.COL_TYPE))
                            );
                            menus.add(menu);
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null) {
                        cursor.close();
                    }

                    if (menus.size() > 0) {
                        Log.w(MainActivity.TAG, "Cached Data found, returning cached data");
                        ObjectMapper mapper = new ObjectMapper();
                        String jsonString = mapper.writeValueAsString(menus);
                        String header = "Access-Control-Allow-Origin:*";
                        String all = header + "\\r\\n" + jsonString;
                        is =  new ByteArrayInputStream(all.getBytes());

                    } else {
                        Log.w(MainActivity.TAG, "No Cached Data found, allowing http request");
                        setISBypassing();
                    }
                } else {
                    Log.w(MainActivity.TAG, "Bypassing Http proxy");
                    setISBypassing();
                }
                byte by[] = new byte[ BUFFER_SIZE ];
                int index = is.read( by, 0, BUFFER_SIZE );
                while ( index != -1 ){
                    out.write( by, 0, index );
                    index = is.read( by, 0, BUFFER_SIZE );
                }

                out.flush();
                Log.w(MainActivity.TAG,"Call returned now ....");
            }


        } catch (IOException e) {
            Log.e(MainActivity.TAG, e.getMessage());
        } finally {
            //close out all resources
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void setISBypassing() throws IOException{
        OkHttpClient client = new OkHttpClient.Builder().build();

        try {
            Request request = new Request.Builder()
                    .headers(Headers.of(headers))
                    .method(method, null)
                    .url(urlToCall).build();
            Response response = client.newCall(request).execute();
            is = response.body().byteStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
