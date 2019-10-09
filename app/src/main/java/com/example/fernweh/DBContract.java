package com.example.fernweh;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBContract {
    public static final String CONTENT_AUTHORITY = "com.example.fernweh_fuck_off";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TRIPS = "trips";
    public static final String PATH_PLANS = "plans";
    public static final String PATH_TRAVELS = "travels";

    public static final class TripsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRIPS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_TRIPS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_TRIPS;

        public static final String TABLE_NAME = "tripsTable";
        public static final String COLUMN_NAME = "tripName";
        public static final String COLUMN_DESTINATION = "tripDestination";
        public static final String COLUMN_START_DATE = "tripStartDate";
        public static final String COLUMN_END_DATE = "tripEndDate";

        public static Uri buildTripsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class PlansEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_PLANS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_PLANS;

        public static final String TABLE_NAME = "plansTable";
        public static final String COLUMN_NAME = "plansName";
        public static final String COLUMN_DESTINATION = "plansDestination";
        public static final String COLUMN_START_DATE = "plansStartDate";
        public static final String COLUMN_END_DATE = "plansEndDate";
        public static final String COLUMN_START_TIME = "plansStartTime";
        public static final String COLUMN_END_TIME = "plansEndTime";
        public static final String COLUMN_PDF = "plansPdf";
        public static final String COLUMN_TRIP_NAME = "plansTripName";

        public static Uri buildPlansUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TravelsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAVELS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_TRAVELS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_TRAVELS;

        public static final String TABLE_NAME = "travelsTable";
        public static final String COLUMN_NAME = "travelsName";
        public static final String COLUMN_DESTINATION = "travelsDestination";
        public static final String COLUMN_START_DATE = "travelsStartDate";
        public static final String COLUMN_END_DATE = "travelsEndDate";
        public static final String COLUMN_START_TIME = "travelsStartTime";
        public static final String COLUMN_END_TIME = "travelsEndTime";
        public static final String COLUMN_PDF = "travelsPdf";
        public static final String COLUMN_TRIP_NAME = "travelsTripName";

        public static Uri buildTravelsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
