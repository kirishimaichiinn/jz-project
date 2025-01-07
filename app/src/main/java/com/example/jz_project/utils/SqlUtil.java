package com.example.jz_project.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SqlUtil extends SQLiteOpenHelper {
    private static SqlUtil instance;
    private static final String DATABASE_NAME = "account_book.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_TYPE = "CREATE TABLE 'type' (" +
            "  'id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  'name' TEXT NOT NULL" +
            ")";

    private static final String CREATE_TABLE_RECORD = "CREATE TABLE 'record' (" +
            "  'id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  'money' REAL NOT NULL," +
            "  'type' TEXT NOT NULL," +
            "  'time' TEXT NOT NULL," +
            "  'note' TEXT" +
            ")";
    private static final List<String> INIT_TYPE = List.of("早餐", "午餐","晚餐","其他");
    public SqlUtil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SqlUtil.instance = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_RECORD);

        insertInitType(db);
    }

    private void insertInitType(SQLiteDatabase db){
        for(String type : INIT_TYPE){
            String sql = "INSERT INTO 'type' (name) VALUES ('" + type + "')";
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static SQLiteDatabase getDb(){
        if(instance != null)
            return instance.getWritableDatabase();
        else throw new  NullPointerException("db尚未初始化");
    }
}
