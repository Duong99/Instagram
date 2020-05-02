package com.example.instagram.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.instagram.model.Song;

import java.util.ArrayList;

public class MyDBLoveSong extends SQLiteOpenHelper {
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "DatabaseLoveSong";

    private final static String TABLE_SONG = "tb_song";
    private final static String KEY_SONG = "key_song";
    private final static String AVATAR_SONG = "avatar_song";
    private final static String TITLE_SONG = "title_song";
    private final static String SINGER_SONG = "singer_song";
    private final static String LOCATION_SONG = "location_song";
    private final static String TIME_SONG = "time_song";

    public MyDBLoveSong(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public ArrayList<Song> getAllSong(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> songs = new ArrayList<>();
        String select = "SELECT * FROM " + TABLE_SONG;
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()){
            do {
                songs.add(new Song(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        db.close();
        return songs;
    }

    public void delete(String songKey){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG, KEY_SONG + " = ?", new String[]{songKey});
        db.close();
    }

    public Song getSong(String songKey){
        SQLiteDatabase db = this.getReadableDatabase();
        Song song = null;
        String select = "SELECT * FROM " + TABLE_SONG + " WHERE " + KEY_SONG + " = " + "\"" +songKey + "\"";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()){
            do {
                 song = new Song(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
            }while (cursor.moveToNext());
        }
        db.close();
        return song;
    }

    public void addSong(Song song){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SONG, song.getSongKey());
        values.put(AVATAR_SONG, song.getAvatar());
        values.put(TITLE_SONG, song.getTitle());
        values.put(SINGER_SONG, song.getSinger());
        values.put(LOCATION_SONG, song.getLocation());
        values.put(TIME_SONG, song.getTime());

        db.insert(TABLE_SONG, null, values);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = String.format("CREATE TABLE IF NOT EXISTS " +
                "%s(%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_SONG, KEY_SONG, AVATAR_SONG, TITLE_SONG, SINGER_SONG, LOCATION_SONG, TIME_SONG);
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
