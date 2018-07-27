package sree.myparty.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import sree.myparty.pojos.VoterPojo;

public class DatabaseHelper extends SQLiteOpenHelper {
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "voters_db";
 
 
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
 
        // Create tables again
        onCreate(db);
    }


    public long insertVoters(ArrayList<VoterPojo> mVoters) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        for (int i = 0;i<mVoters.size();i++){
            ContentValues values = new ContentValues();
            values.put(Note.NAME, mVoters.get(i).getVoterName());
            values.put(Note.AGE, mVoters.get(i).getAge());
            values.put(Note.GENDER, mVoters.get(i).getSex());
            values.put(Note.CATAGEORY, mVoters.get(i).getCatageory());
            values.put(Note.CASTE, mVoters.get(i).getCaste());
            values.put(Note.BOOTH_NUMBER, mVoters.get(i).getBoothNumber());
             id = db.insert(Note.TABLE_NAME, null, values);
        }
        db.close();
        return id;
    }

    public ArrayList<String> getCatageories() {
        ArrayList<String> catageories = new ArrayList<>();

        String selectQuery = "SELECT "+Note.CATAGEORY+" FROM " + Note.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                catageories.add(cursor.getString(cursor.getColumnIndex(Note.CATAGEORY)));
            } while (cursor.moveToNext());
        }

        db.close();

        return catageories;
    }

    public ArrayList<String> getCasteData(String catageory) {
        ArrayList<String> castes = new ArrayList<>();

        String selectQuery = "SELECT "+Note.CASTE+" FROM " + Note.TABLE_NAME +" WHERE "+Note.CATAGEORY + " = '"+catageory+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                castes.add(cursor.getString(cursor.getColumnIndex(Note.CASTE)));
            } while (cursor.moveToNext());
        }

        db.close();

        return castes;
    }
    public void delete() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME,null,null);

    }

}