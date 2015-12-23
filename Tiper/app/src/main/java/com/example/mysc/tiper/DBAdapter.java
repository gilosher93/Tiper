package com.example.mysc.tiper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Mysc on 13/10/15.
 */
public class DBAdapter {
    static final String DATABASE_NAME = "MyDB.db";
    static final String DATABASE_TABLE = "shifts";
    static final int DATABASE_VERSION = 1;
    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";
    public static final String SUM_OF_HOURS = "sum_of_hours";
    public static final String SALARY = "salary";
    public static final String TIPS = "tips";
    public static final String SUMMARY = "summary";
    public static final String SHIFT_ID = "shift_id";

    final Context context;
    private SQLiteDatabase db;
    DatabaseHelper DBHelper;

    public DBAdapter(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTable = "CREATE TABLE " + DATABASE_TABLE + "(" +
                    SHIFT_ID + " INTEGER," +
                    START_TIME + " INTEGER," +
                    END_TIME + " INTEGER," +
                    SUM_OF_HOURS + " REAL," +
                    SALARY + " INTEGER," +
                    TIPS + " INTEGER," +
                    SUMMARY + " INTEGER" +
                    ")";
            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public void open() throws SQLException {
        db = DBHelper.getWritableDatabase();
    }

    public void close() {
        DBHelper.close();
    }

    public long insertShiftToDB(Shift shift) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIFT_ID, shift.getId());
        contentValues.put(START_TIME, shift.startTime);
        contentValues.put(END_TIME, shift.endTime);
        contentValues.put(SUM_OF_HOURS, shift.getSumOfHoursString());
        contentValues.put(SALARY, shift.salaryPerHour);
        contentValues.put(TIPS, shift.getTipsCount());
        contentValues.put(SUMMARY, shift.getSummary());
        return db.insert(DATABASE_TABLE, null, contentValues);
    }

    public int clearDB(){
        return db.delete(DATABASE_TABLE, null, null);
    }

    public Cursor getAllShifts() {
        return db.query(DATABASE_TABLE, new String[]{SHIFT_ID, START_TIME, END_TIME, SUM_OF_HOURS, SALARY, TIPS, SUMMARY}, null,
                null, null, null, null);
    }

}