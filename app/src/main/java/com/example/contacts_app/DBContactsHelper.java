package com.example.contacts_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBContactsHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactsDb";

    public static final String TABLE_CONTACTS = "contacts";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_NUMBER = "number";


    static final String[] COLUMNS = {KEY_ID, KEY_NAME, KEY_SURNAME, KEY_NUMBER};



    public DBContactsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table with accounts
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_SURNAME + " text," + KEY_NUMBER + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);

    }

    int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    void writeContactToDB(SQLiteDatabase db, Contact c){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,c.getName());
        cv.put(KEY_SURNAME,c.getSurname());
        cv.put(KEY_NUMBER,c.getNumber());
        db.insert(TABLE_CONTACTS,null,cv);
    }


    public ArrayList<String> getListFromDB(SQLiteDatabase db) {
        ArrayList<String> ar = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            do {
                String n = cursor.getString(nameIndex);
                ar.add(n);
            } while (cursor.moveToNext());
        }
            return ar;
    }



    void logDB(SQLiteDatabase database){
        Cursor cursor = database.query(TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(KEY_ID); // 0
            int nameIndex = cursor.getColumnIndex(KEY_NAME); // 1
            int surnameIndex = cursor.getColumnIndex(KEY_SURNAME); // 2
            int numberIndex = cursor.getColumnIndex(KEY_NUMBER); // 3
            do{
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", name = " + cursor.getString(nameIndex)+
                        ", surname = " + cursor.getString(surnameIndex)+
                        ", number = " + cursor.getString(numberIndex));
            }while (cursor.moveToNext());
        }else
            Log.d("mLog","0 rows");
        cursor.close();

    }




}
