package com.example.raheel.interceptapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.raheel.interceptapp.MainActivity;

/**
 * Created by Raheel on 6/27/2017.
 */

public class MenuDatabase extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "db_menus";
    static final String TABLE_NAME = "menus";
    static final int DATABASE_VERSION = 1;
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_TYPE = "type";
    static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME
            + " ( "+COL_ID+" text primary key not null, "
            + COL_NAME + " text not null, "
            + COL_PRICE + " text not null, "
            + COL_TYPE + " text not null);";

    public MenuDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Cursor c =db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+TABLE_NAME+"'", null);
        if (c.getCount() == 0) {
            Log.i(MainActivity.TAG, "table was not existing, creating a new table");
            db.execSQL(CREATE_DB_TABLE);
            Log.i(MainActivity.TAG, "table created successfuly");
        }
        c.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MainActivity.TAG, "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
