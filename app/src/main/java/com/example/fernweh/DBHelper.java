package com.example.fernweh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "FernwehDatabase8";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void gimmeTRIP(String[] arguments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tripName", arguments[0]);
        values.put("tripDestination", arguments[1]);
        values.put("tripStartDate", arguments[2]);
        values.put("tripEndDate", arguments[3]);
        db.insert(DBContract.TripsEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void gimmePLANS(String[] arguments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("plansName", arguments[0]);
        values.put("plansDestination", arguments[1]);
        values.put("plansStartDate", arguments[2]);
        values.put("plansEndDate", arguments[3]);
        values.put("plansStartTime", arguments[4]);
        values.put("plansEndTime", arguments[5]);
        values.put("plansPdf", arguments[6]);
        values.put("plansTripName", arguments[7]);
        db.insert(DBContract.PlansEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void gimmeTRAVELS(String[] arguments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("travelsName", arguments[0]);
        values.put("travelsDestination", arguments[1]);
        values.put("travelsStartDate", arguments[2]);
        values.put("travelsEndDate", arguments[3]);
        values.put("travelsStartTime", arguments[4]);
        values.put("travelsEndTime", arguments[5]);
        values.put("travelsPdf", arguments[6]);
        values.put("travelsTripName", arguments[7]);
        db.insert(DBContract.TravelsEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDB1 = "DELETE FROM " + DBContract.TravelsEntry.TABLE_NAME;
        String clearDB2 = "DELETE FROM " + DBContract.TripsEntry.TABLE_NAME;
        String clearDB3 = "DELETE FROM " + DBContract.PlansEntry.TABLE_NAME;
        db.execSQL(clearDB1);
        db.execSQL(clearDB2);
        db.execSQL(clearDB3);
    }
    public String getDate() {
        String date = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT tripStartDate FROM " + DBContract.TripsEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(q, null);

        if (cursor.moveToFirst()) {
            do {
                date = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return date;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addPlansTable(db);
        addTravelsTable(db);
        addTripsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void addPlansTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + DBContract.PlansEntry.TABLE_NAME + " (" +
                        DBContract.PlansEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBContract.PlansEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                        DBContract.PlansEntry.COLUMN_DESTINATION + " TEXT NOT NULL, " +
                        DBContract.PlansEntry.COLUMN_START_DATE + " TEXT NOT NULL, " +
                        DBContract.PlansEntry.COLUMN_END_DATE + " TEXT, " +
                        DBContract.PlansEntry.COLUMN_START_TIME + " TEXT NOT NULL, " +
                        DBContract.PlansEntry.COLUMN_END_TIME + " TEXT , " +
                        DBContract.PlansEntry.COLUMN_PDF + " TEXT , " +
                        DBContract.PlansEntry.COLUMN_TRIP_NAME + " TEXT NOT NULL," +
                        "FOREIGN KEY(" + DBContract.PlansEntry.COLUMN_TRIP_NAME + ") REFERENCES " + DBContract.TripsEntry.TABLE_NAME + "(" + DBContract.TripsEntry.COLUMN_NAME + "));"
        );
    }

    private void addTravelsTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + DBContract.TravelsEntry.TABLE_NAME + " (" +
                        DBContract.TravelsEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBContract.TravelsEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                        DBContract.TravelsEntry.COLUMN_DESTINATION + " TEXT NOT NULL, " +
                        DBContract.TravelsEntry.COLUMN_START_DATE + " TEXT  NOT NULL, " +
                        DBContract.TravelsEntry.COLUMN_END_DATE + " TEXT , " +
                        DBContract.TravelsEntry.COLUMN_START_TIME + " TEXT NOT NULL, " +
                        DBContract.TravelsEntry.COLUMN_END_TIME + " TEXT ," +
                        DBContract.TravelsEntry.COLUMN_PDF + " TEXT ," +
                        DBContract.TravelsEntry.COLUMN_TRIP_NAME + " TEXT NOT NULL," +
                        "FOREIGN KEY(" + DBContract.TravelsEntry.COLUMN_TRIP_NAME + ") REFERENCES " + DBContract.TripsEntry.TABLE_NAME + "(" + DBContract.TripsEntry.COLUMN_NAME + "));"
        );
    }

    private void addTripsTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + DBContract.TripsEntry.TABLE_NAME + " (" +
                        DBContract.TripsEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBContract.TripsEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                        DBContract.TripsEntry.COLUMN_DESTINATION + " TEXT NOT NULL, " +
                        DBContract.TripsEntry.COLUMN_START_DATE + " TEXT NOT NULL, " +
                        DBContract.TripsEntry.COLUMN_END_DATE + " TEXT NOT NULL);"
        );
    }

}
