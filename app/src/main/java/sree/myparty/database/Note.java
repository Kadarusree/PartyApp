package sree.myparty.database;

public class Note {
    public static final String TABLE_NAME = "voters";
    public static final String SURVEY_TABLE = "survey";

    public static final String TABLE_LAST_VOTES = "lastVotes";
    public static final String TABLE_FUTURE_VOTES = "futureVotes";
    public static final String TABLE_VOLUNTEERS = "volunteers";




    private int id;
    private String note;
    private String timestamp;

    public static final String SURVEY_ID = "question_id";
    public static final String ANSWER = "answer";


    public static final String PARTY = "party";
    public static final String COLUMN_ID = "id";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String GENDER = "gender";
    public static final String CATAGEORY = "catageory";
    public static final String CASTE = "caste";
    public static final String BOOTH_NUMBER = "booth_number";



 
 
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + NAME + " TEXT,"
                    + AGE + " INTEGER,"
                    + GENDER + " TEXT,"
                    + CATAGEORY + " TEXT,"
                    + CASTE + " TEXT,"
                    + BOOTH_NUMBER + " TEXT"
                    + ")";

    public static final String CREATE_TABLE_VOLUNTEERS =
            "CREATE TABLE " + TABLE_VOLUNTEERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + NAME + " TEXT,"
                    + BOOTH_NUMBER + " TEXT"
                    + ")";

    public static final String CREATE_SURVEY_TABLE =
            "CREATE TABLE " + SURVEY_TABLE + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + SURVEY_ID + " TEXT,"
                    + ANSWER + " TEXT"
                    + ")";

    public static final String CREATE_LAST_VOTES_TABLE =
            "CREATE TABLE " + TABLE_LAST_VOTES + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + BOOTH_NUMBER + " TEXT,"
                    + PARTY + " TEXT"
                    + ")";

    public static final String CREATE_NEXT_VOTES_TABLE =
            "CREATE TABLE " + TABLE_FUTURE_VOTES + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + BOOTH_NUMBER + " TEXT,"
                    + PARTY + " TEXT"
                    + ")";
 
    public Note() {
    }
 
    public Note(int id, String note, String timestamp) {
        this.id = id;
        this.note = note;
        this.timestamp = timestamp;
    }
 
    public int getId() {
        return id;
    }
 
    public String getNote() {
        return note;
    }
 
    public void setNote(String note) {
        this.note = note;
    }
 
    public String getTimestamp() {
        return timestamp;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}