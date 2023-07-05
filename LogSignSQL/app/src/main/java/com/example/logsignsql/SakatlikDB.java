package com.example.logsignsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SakatlikDB extends SQLiteOpenHelper {
    //Database oluşturma
    public static final String databaseName = "Sakatlik.db";
    public SakatlikDB(@Nullable Context context) {
        super(context, "Sakatlik.db", null, 1);
    }
    @Override
    //TABLOLARI OLUŞTURMA
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE sakatliklar (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, playerId INTEGER, FOREIGN KEY(playerId) REFERENCES players(id))");
    }
    @Override
    //VERİTABANINDA DEĞİŞİKLİK YAPMAK İÇİN UGRADE KULLANMAK ZORUNDASIN Kİ SORGULAR ÇALIŞSIN
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists sakatliklar");
    }
    //DB CLEAR
    public void clear() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        MyDatabase.execSQL("DELETE from sakatliklar");
    }
    //Sakatlık tablosuna veri eklemeye yarar
    public Boolean insertData(int playerId, String name){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("playerId", playerId);
        contentValues.put("name", name);
        long result = MyDatabase.insert("sakatliklar", null, contentValues);
        return result != -1;
    }
    //Player ıd ye sakatlık listesine ekler
    public List<Sakatlik> getSakatlikList(int playerId) {
        List<Sakatlik> sakatlikList = new ArrayList<>();

        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM sakatliklar where playerId = ?", new String[]{Integer.toString(playerId)});

        int idColumnIndex = cursor.getColumnIndex("id");
        int playerIdColumnIndex = cursor.getColumnIndex("playerId");
        int nameColumnIndex = cursor.getColumnIndex("name");

        if (cursor.moveToFirst()) {
            do {
                int _id = (idColumnIndex != -1) ? cursor.getInt(idColumnIndex) : 0;
                int _playerId = (playerIdColumnIndex != -1) ? cursor.getInt(playerIdColumnIndex) : 0;
                String _name = (nameColumnIndex != -1) ? cursor.getString(nameColumnIndex) : "";
                Sakatlik sakatlik = new Sakatlik(_id, _playerId, _name);
                sakatlikList.add(sakatlik);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return sakatlikList;
    }

//    @Nullable
//    public Player getSakatlik(int id) {
//        SQLiteDatabase myDatabase = this.getReadableDatabase();
//        Cursor cursor = myDatabase.rawQuery("SELECT * FROM players where id = ?", new String[]{Integer.toString(id)});
//
//        int idColumnIndex = cursor.getColumnIndex("id");
//        int nameColumnIndex = cursor.getColumnIndex("name");
//
//        if (cursor.moveToFirst()) {
//            do {
//                int _id = (idColumnIndex != -1) ? cursor.getInt(idColumnIndex) : 0;
//                String name = (nameColumnIndex != -1) ? cursor.getString(nameColumnIndex) : "";
//                Player player = new Player(_id, name);
//                return player;
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        return null;
//    }
}