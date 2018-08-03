package sree.myparty.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import sree.myparty.pojos.VoterPojo;
import sree.myparty.survey.SurveyAnswerPojo;

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
        db.execSQL(Note.CREATE_SURVEY_TABLE);
        db.execSQL(Note.CREATE_LAST_VOTES_TABLE);
        db.execSQL(Note.CREATE_NEXT_VOTES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Note.SURVEY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_LAST_VOTES);
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_FUTURE_VOTES);
        // Create tables again
        onCreate(db);
    }


    public long insertVoters(ArrayList<VoterPojo> mVoters) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        for (int i = 0; i < mVoters.size(); i++) {
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

        String selectQuery = "SELECT " + Note.CATAGEORY + " FROM " + Note.TABLE_NAME;

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

        String selectQuery = "SELECT " + Note.CASTE + " FROM " + Note.TABLE_NAME + " WHERE " + Note.CATAGEORY + " = '" + catageory + "'";

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
        db.delete(Note.TABLE_NAME, null, null);
        db.delete(Note.SURVEY_TABLE, null, null);
        db.delete(Note.TABLE_LAST_VOTES, null, null);
        db.delete(Note.TABLE_FUTURE_VOTES, null, null);
    }


    public long insertAnswers(ArrayList<SurveyAnswerPojo> mAnswer) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        for (int i = 0; i < mAnswer.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(Note.SURVEY_ID, mAnswer.get(i).getQuestion_id());
            values.put(Note.ANSWER, mAnswer.get(i).getAnswer());

            id = db.insert(Note.SURVEY_TABLE, null, values);
        }
        db.close();
        return id;
    }

    public ArrayList<String> getAnswers(String question_id) {
        ArrayList<String> answers = new ArrayList<>();

        String selectQuery = "SELECT " + Note.ANSWER + " FROM " + Note.SURVEY_TABLE + " WHERE " + Note.SURVEY_ID + " = '" + question_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                answers.add(cursor.getString(cursor.getColumnIndex(Note.ANSWER)));
            } while (cursor.moveToNext());
        }

        db.close();

        return answers;
    }


    public long insertlastotes(ArrayList<VoterPojo> mVotersr) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        for (int i = 0; i < mVotersr.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(Note.PARTY, mVotersr.get(i).getLastVoted());
            id = db.insert(Note.TABLE_LAST_VOTES, null, values);
        }
        db.close();
        return id;
    }

    public long insertfuturevotes(ArrayList<VoterPojo> mVotersr) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        for (int i = 0; i < mVotersr.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(Note.PARTY, mVotersr.get(i).getNextVote());
            id = db.insert(Note.TABLE_FUTURE_VOTES, null, values);
        }
        db.close();
        return id;
    }

    public ArrayList<String> getLastVotes() {
        ArrayList<String> castes = new ArrayList<>();

        String selectQuery = "SELECT " + Note.PARTY + " FROM " + Note.TABLE_LAST_VOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                castes.add(cursor.getString(cursor.getColumnIndex(Note.PARTY)));
            } while (cursor.moveToNext());
        }

        db.close();

        return castes;
    }

    public ArrayList<String> getNextVotes() {
        ArrayList<String> castes = new ArrayList<>();

        String selectQuery = "SELECT " + Note.PARTY + " FROM " + Note.TABLE_FUTURE_VOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                castes.add(cursor.getString(cursor.getColumnIndex(Note.PARTY)));
            } while (cursor.moveToNext());
        }

        db.close();

        return castes;
    }
}