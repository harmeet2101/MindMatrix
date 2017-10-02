package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import Constants.Const;
import Model.AssignmentData;
import Model.QuizData;

/**
 * Created by ganesha on 22/3/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "cli_db";
    Context con;

    private static final String TABLE_QUIZ_DETAILS = "quiz_details";
    private static final String TABLE_ASSIGNMENT_DETAILS = "assignment_details";

    // columns
    private static final String KEY_ID = "id";
    private static final String KEY_QUIZ_ID = "quiz_id";
    private static final String KEY_QUIZ_SCORE = "quiz_score";
    private static final String KEY_STATUS = "status";

    private static final String KEY_ASSIGNMENT_ID = "assignment_id";
    private static final String KEY_ASSIGNMENT_SCORE = "assignment_score";
    private static final String KEY_ASSIGNMENT_TEXT = "text";
    private static final String KEY_ASSIGNMENT_FILE = "file";


    // Create Table Query
    private static final String CREATE_ASSIGNMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ASSIGNMENT_DETAILS +
            " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ASSIGNMENT_ID
            + " TEXT UNIQUE," + KEY_ASSIGNMENT_TEXT
            + " TEXT," + KEY_ASSIGNMENT_FILE
            + " TEXT," + KEY_ASSIGNMENT_SCORE
            + " TEXT," + KEY_STATUS
            + " TEXT" + ")";

    private static final String CREATE_QUIZ_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_QUIZ_DETAILS +
            " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUIZ_ID
            + " TEXT UNIQUE," + KEY_QUIZ_SCORE
            + " TEXT," + KEY_STATUS
            + " TEXT" + ")";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createTable(db);

    }


    private void createTable(SQLiteDatabase db) {

        db.execSQL(CREATE_ASSIGNMENT_TABLE);
        db.execSQL(CREATE_QUIZ_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public ArrayList<QuizData> getSubmittedQuizDetails() {

        ArrayList<QuizData> data = new ArrayList<>();

        String query = "Select * from " + TABLE_QUIZ_DETAILS +
                " where " + KEY_STATUS + " = '" + Const.STATUS.SUBMITTED + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToNext();

            do {

                data.add(new QuizData(cursor.getString(cursor.getColumnIndex(KEY_QUIZ_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_QUIZ_SCORE))));
            } while (cursor.moveToNext());

        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return data;


    }


    public ArrayList<AssignmentData> getSubmittedAssignmentDetails() {

        ArrayList<AssignmentData> data = new ArrayList<>();

        String query = "Select * from " + TABLE_ASSIGNMENT_DETAILS +
                " where " + KEY_STATUS + " = '" + Const.STATUS.SUBMITTED + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToNext();

            do {

                data.add(new AssignmentData(cursor.getString(cursor.getColumnIndex(KEY_ASSIGNMENT_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_ASSIGNMENT_TEXT)),
                        cursor.getString(cursor.getColumnIndex(KEY_ASSIGNMENT_FILE))));
            } while (cursor.moveToNext());

        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return data;


    }


    public String getQuizScore(String quiz_id) {

        String score = "";

        String query = "Select " + KEY_QUIZ_SCORE + " from " + TABLE_QUIZ_DETAILS +
                " where " + KEY_QUIZ_ID + " = '" + quiz_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToNext();
            score = cursor.getString(cursor.getColumnIndex(KEY_QUIZ_SCORE));

        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return score;


    }

    public String getAssignmentScore(String assignment_id) {

        String score = "";

        String query = "Select " + KEY_ASSIGNMENT_SCORE + " from " + TABLE_ASSIGNMENT_DETAILS +
                " where " + KEY_ASSIGNMENT_ID+ " = '" + assignment_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToNext();
            score = cursor.getString(cursor.getColumnIndex(KEY_ASSIGNMENT_SCORE));

        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return score;


    }


    public boolean insertQuizDetails(String quiz_id, String score, String quiz_status) {

        long inserted_id = -1;

        ContentValues values = new ContentValues();
        values.put(KEY_QUIZ_ID, quiz_id);
        values.put(KEY_QUIZ_SCORE, score);
        values.put(KEY_STATUS, quiz_status);

        SQLiteDatabase db = this.getWritableDatabase();
        inserted_id = db.insert(TABLE_QUIZ_DETAILS, null, values);

        db.close();

        if (inserted_id != -1)
            return true;


        return false;
    }


    public boolean insertAssignmentDetails(AssignmentData data, String status) {

        long inserted_id = -1;

        ContentValues values = new ContentValues();
        values.put(KEY_ASSIGNMENT_ID, data.getId());
        values.put(KEY_ASSIGNMENT_TEXT, data.getAssignmentText());
        values.put(KEY_ASSIGNMENT_FILE, ""+data.getAssignmentFile());
        values.put(KEY_STATUS, status);
        values.put(KEY_ASSIGNMENT_SCORE, "-1");

        SQLiteDatabase db = this.getWritableDatabase();
        inserted_id = db.insert(TABLE_ASSIGNMENT_DETAILS, null, values);

        db.close();

        if (inserted_id != -1)
            return true;


        return false;
    }


    public boolean updateQuizStatus(String quiz_id, String status) {

        long inserted_id = -1;

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);

        SQLiteDatabase db = this.getWritableDatabase();
        inserted_id = db.update(TABLE_QUIZ_DETAILS, values, KEY_QUIZ_ID + "=" + quiz_id, null);

        db.close();

        if (inserted_id != -1)
            return true;

        return false;

    }


    public boolean updateAssignmentStatus(String assignment_id, String status) {

        long inserted_id = -1;

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);
        values.put(KEY_ASSIGNMENT_SCORE, "-1");

        SQLiteDatabase db = this.getWritableDatabase();
        inserted_id = db.update(TABLE_ASSIGNMENT_DETAILS, values, KEY_ASSIGNMENT_ID + "=" + assignment_id, null);

        db.close();

        if (inserted_id != -1)
            return true;

        return false;

    }

    public boolean updateAssignmentScore(String assignment_id, String score) {

        long inserted_id = -1;

        ContentValues values = new ContentValues();
        values.put(KEY_ASSIGNMENT_SCORE, score);

        SQLiteDatabase db = this.getWritableDatabase();
        inserted_id = db.update(TABLE_ASSIGNMENT_DETAILS, values, KEY_ASSIGNMENT_ID + "=" + assignment_id, null);

        db.close();

        if (inserted_id != -1)
            return true;

        return false;

    }


    public void clearData() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ASSIGNMENT_DETAILS, null, null);
        db.delete(TABLE_QUIZ_DETAILS, null, null);

        db.close();
    }


}
