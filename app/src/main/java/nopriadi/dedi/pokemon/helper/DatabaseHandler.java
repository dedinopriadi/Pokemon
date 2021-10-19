/*
 * Created by Dedi Nopriadi on 10/14/21, 2:27 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/14/21, 2:27 PM
 */

package nopriadi.dedi.pokemon.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nopriadi.dedi.pokemon.data.PokemonData;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "pokepoke";

    private static final String TABLE_POKE = "pokemon";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_URL  = "url";
    private static final String KEY_IMG  = "img";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_POKE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_URL + " TEXT,"
                + KEY_IMG + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POKE);
        onCreate(db);
    }

    public void addRecord(HashMap<String, String> model){
        SQLiteDatabase db  = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, model.get(KEY_ID));
        values.put(KEY_NAME, model.get(KEY_NAME));
        values.put(KEY_URL, model.get(KEY_URL));
        values.put(KEY_IMG, model.get(KEY_IMG));

        db.insert(TABLE_POKE, null, values);
        db.close();
    }

    public List<PokemonData> getAllRecord() {
        List<PokemonData> contactList = new ArrayList<PokemonData>();
        contactList.clear();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_POKE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PokemonData userModels = new PokemonData();
                userModels.setPokeId(String.valueOf(Integer.parseInt(cursor.getString(0))));
                userModels.setPokeName(cursor.getString(1));
                userModels.setPokeUrl(cursor.getString(2));
                userModels.setPokeImage(cursor.getString(3));

                contactList.add(userModels);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void deleteModel(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POKE, KEY_ID + " = ?",
                new String[] { id });
        db.close();
    }

    public Cursor getSinglePoke(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_POKE, new String[] { KEY_ID,
                        KEY_NAME, KEY_URL, KEY_IMG }, KEY_ID + "=?",
                new String[] { id }, null, null, null, null);

//        PokemonData poke = new PokemonData();
//
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//            cursor.moveToFirst();
//
//            poke = new PokemonData(cursor.getString(0),
//                    cursor.getString(1), cursor.getString(3), cursor.getString(2));
//        }
        return cursor;
    }


}
