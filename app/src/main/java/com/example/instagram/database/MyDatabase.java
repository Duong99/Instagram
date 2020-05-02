package com.example.instagram.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.instagram.model.Person;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {
    private final static String NAMEDATABASE = "ANALYZEDUSER";
    private final static int VERSION = 1;

    private final static String NAMETABLEPERSON = "ANALYZEDPERSON";

    private final static String ID = "id";
    private final static String USERNAME = "username";
    private final static String FULLNAME = "fullname";
    private final static String URLPICTURE = "urlpicture";

    public MyDatabase(Context context) {
        super(context, NAMEDATABASE, null, VERSION);
    }

    public void deleteAllPersonAnlyzed(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + NAMETABLEPERSON);
        db.close();
    }

    public void deletePersonAnlyzed(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NAMETABLEPERSON, ID + " = ?", new String[]{id});
        db.close();
    }

    public Person getPersonAnalyzed(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Person p = null;
        String select = "SELECT * FROM " + NAMETABLEPERSON + " WHERE " + ID + " = " + id;
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()){
            do{
                 p = new Person(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
            }while (cursor.moveToNext());
        }
        db.close();
        return p;
    }

    public void addPersonAnalyzed(Person person){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, person.getIdPerson());
        values.put(USERNAME, person.getUsername());
        values.put(FULLNAME, person.getFullName());
        values.put(URLPICTURE, person.getImvPeron());

        db.insert(NAMETABLEPERSON, null, values);
        db.close();
    }

    public ArrayList<Person> getAllAnalyzedPerson(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Person> people = new ArrayList<>();
        String select = "SELECT * FROM " + NAMETABLEPERSON;
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()){
            do {
                Person p = new Person(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                people.add(p);
            }while (cursor.moveToNext());
        }
        db.close();
        return people;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_person = "CREATE TABLE IF NOT EXISTS " + NAMETABLEPERSON +
                "(" + ID + " TEXT PRIMARY KEY," + USERNAME +" TEXT,"+ FULLNAME +" TEXT,"+ URLPICTURE +" TEXT)";

        db.execSQL(create_table_person);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
