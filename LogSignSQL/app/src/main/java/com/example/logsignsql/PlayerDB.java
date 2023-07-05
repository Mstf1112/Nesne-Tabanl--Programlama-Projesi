package com.example.logsignsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerDB extends SQLiteOpenHelper {
    //Database oluşturma
    public static final String databaseName = "Player.db";
    public PlayerDB(@Nullable Context context) {
        super(context, "Player.db", null, 1);
    }
    @Override
    //TABLOLARI OLUŞTURMA
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE players (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
    }
    @Override
    //VERİTABANINDA DEĞİŞİKLİK YAPMAK İÇİN UGRADE KULLANMAK ZORUNDASIN Kİ SORGULAR ÇALIŞSIN
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists players");
    }
    //TABLODAN SİLME
    public void clear() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        MyDatabase.execSQL("DELETE from players");
    }
    //TABLOYA VERİ EKLEME
    public Boolean insertData(String name){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        long result = MyDatabase.insert("players", null, contentValues);
        return result != -1;
    }
    //MAİL VE PAROLAYI CHECK EDİLEREK CURSOR DÖNDÜKÇE O PARAMETRELER UYGUN KİŞİNİN OLUP OLMADIĞINI SORGULAMA
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }
    //DB YE TÜM OYUNCULARIN LİSTESİNİ ALMAYA YARAR
    //ARRAY LİSTTE OYUNCULARI TUTUCAK SQL SORUGUSU İLE DATALAR ÇEKİLİP CURSOR İLE SÜTÜN İD KARŞILAŞTIRILIR
    //CURSOR İLK KONUMUNA İLERLER DO-WHİLE BAŞLAR HER DÖNGÜDE 1 OYUNCU OLUŞTURULUP LİSTE PLAYER OLARAK DÖNER
    public List<Player> getPlayerList() {
        List<Player> playerList = new ArrayList<>();

        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM players", null);

        int idColumnIndex = cursor.getColumnIndex("id");
        int nameColumnIndex = cursor.getColumnIndex("name");

        if (cursor.moveToFirst()) {
            do {
                int id = (idColumnIndex != -1) ? cursor.getInt(idColumnIndex) : 0;
                String name = (nameColumnIndex != -1) ? cursor.getString(nameColumnIndex) : "";
                Player player = new Player(id, name);
                playerList.add(player);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return playerList;
    }
    //OYUNCU YOKSA NULL DEĞER DÖNECEK BU KADAR BASİT TAKTİK MAKTİK YOK BAM BAM BAM
    @Nullable
    public Player getPlayer(int id) {
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM players where id = ?", new String[]{Integer.toString(id)});

        int idColumnIndex = cursor.getColumnIndex("id");
        int nameColumnIndex = cursor.getColumnIndex("name");

        if (cursor.moveToFirst()) {
            do {
                int _id = (idColumnIndex != -1) ? cursor.getInt(idColumnIndex) : 0;
                String name = (nameColumnIndex != -1) ? cursor.getString(nameColumnIndex) : "";
                Player player = new Player(_id, name);
                return player;
            } while (cursor.moveToNext());
        }

        cursor.close();
        return null;
    }
}