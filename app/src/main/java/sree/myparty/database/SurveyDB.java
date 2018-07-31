package sree.myparty.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import sree.myparty.pojos.VoterPojo;
import sree.myparty.survey.SurveyAnswerPojo;

/**
 * Created by srikanthk on 7/30/2018.
 */

public class SurveyDB extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "voters_db";


    public SurveyDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Note.CREATE_SURVEY_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.SURVEY_TABLE);

        // Create tables again
        onCreate(db);
    }


    public long insertAnswers(ArrayList<SurveyAnswerPojo> mAnswer) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        for (int i = 0;i<mAnswer.size();i++){
            ContentValues values = new ContentValues();
            values.put(Note.SURVEY_ID, mAnswer.get(i).getQuestion_id());
            values.put(Note.ANSWER, mAnswer.get(i).getAnswer());

            id = db.insert(Note.SURVEY_TABLE, null, values);
        }
        db.close();
        return id;
    }



    public ArrayList<String> getAnswers(String catageory) {
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
        db.delete(Note.SURVEY_TABLE,null,null);
    }
}
