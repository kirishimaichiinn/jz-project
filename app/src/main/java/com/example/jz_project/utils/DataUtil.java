package com.example.jz_project.utils;

import android.content.Context;
import android.database.Cursor;

import com.example.jz_project.entity.Record;
import com.example.jz_project.entity.Type;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {
    public static List<Record> messageList;
    public static List<Type> typeList;

    public static void init(Context context){
        SqlUtil sqlUtil = new SqlUtil(context);
        if(messageList == null) {
            messageList = new ArrayList<>();
            loadMessages();
        }
        if(typeList == null) {
            typeList = new ArrayList<>();
            loadType();
        }
    }

    public static List<Record> loadMessages() {
        messageList.clear();
        Cursor cursor = SqlUtil.getDb().rawQuery("select * from record order by time DESC, id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(0));
                record.setMoney(cursor.getDouble(1));
                record.setType(cursor.getString(2));
                record.setTime(cursor.getString(3));
                record.setNote(cursor.getString(4));
                messageList.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return messageList;
    }

    public static List<Type> loadType() {
        typeList.clear();
        Cursor cursor = SqlUtil.getDb().rawQuery("select * from type order by id ASC", null);
        if (cursor.moveToFirst()) {
            do {
                Type type = new Type();
                type.id = cursor.getInt(0);
                type.name = cursor.getString(1);
                typeList.add(type);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return typeList;
    }
}
