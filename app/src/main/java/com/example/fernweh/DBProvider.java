package com.example.fernweh;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DBProvider extends ContentProvider {
    private static final int TRIPS = 100;
    private static final int TRIPS_ID = 101;
    private static final int TRAVELS = 200;
    private static final int TRAVELS_ID = 201;
    private static final int PLANS = 300;
    private static final int PLANS_ID = 301;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    public static UriMatcher buildUriMatcher() {
        String content = DBContract.CONTENT_AUTHORITY;
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, DBContract.PATH_TRAVELS, TRAVELS);
        matcher.addURI(content, DBContract.PATH_TRAVELS + "/#", TRAVELS_ID);
        matcher.addURI(content, DBContract.PATH_TRIPS, TRIPS);
        matcher.addURI(content, DBContract.PATH_TRIPS + "/#", TRIPS_ID);
        matcher.addURI(content, DBContract.PATH_PLANS, PLANS);
        matcher.addURI(content, DBContract.PATH_PLANS + "/#", PLANS_ID);
        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case TRIPS:
                return DBContract.PlansEntry.CONTENT_TYPE;
            case TRIPS_ID:
                return DBContract.PlansEntry.CONTENT_ITEM_TYPE;
            case PLANS:
                return DBContract.TripsEntry.CONTENT_TYPE;
            case PLANS_ID:
                return DBContract.TripsEntry.CONTENT_ITEM_TYPE;
            case TRAVELS:
                return DBContract.TravelsEntry.CONTENT_TYPE;

            case TRAVELS_ID:
                return DBContract.TravelsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case TRIPS:
                retCursor = db.query(
                        DBContract.TripsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRIPS_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        DBContract.TripsEntry.TABLE_NAME,
                        projection,
                        DBContract.TripsEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRAVELS:
                retCursor = db.query(
                        DBContract.TravelsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRAVELS_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        DBContract.TravelsEntry.TABLE_NAME,
                        projection,
                        DBContract.TravelsEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case PLANS:
                retCursor = db.query(
                        DBContract.PlansEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PLANS_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        DBContract.PlansEntry.TABLE_NAME,
                        projection,
                        DBContract.PlansEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case TRIPS:
                _id = db.insert(DBContract.TripsEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DBContract.TripsEntry.buildTripsUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case PLANS:
                _id = db.insert(DBContract.PlansEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DBContract.PlansEntry.buildPlansUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case TRAVELS:
                _id = db.insert(DBContract.TravelsEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DBContract.TravelsEntry.buildTravelsUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows; // Number of rows effected

        switch (sUriMatcher.match(uri)) {
            case TRIPS:
                rows = db.delete(DBContract.TripsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PLANS:
                rows = db.delete(DBContract.PlansEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAVELS:
                rows = db.delete(DBContract.TravelsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (selection == null || rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;

        switch (sUriMatcher.match(uri)) {
            case TRIPS:
                rows = db.update(DBContract.TripsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TRAVELS:
                rows = db.update(DBContract.TravelsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PLANS:
                rows = db.update(DBContract.PlansEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }
}

