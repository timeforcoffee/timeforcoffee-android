package ch.liip.timeforcoffee.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import ch.liip.timeforcoffee.api.Station;

public class FavoritesDatabaseHelper extends SQLiteOpenHelper {

    public static abstract class FavoriteStationColumn implements BaseColumns {
        public static final String OLD_TABLE_NAME = "favorites";
        public static final String TABLE_NAME = "favorite_stations";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_STATION_ID = "station_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
    }

    public static abstract class FavoriteLineColumn implements BaseColumns {
        public static final String TABLE_NAME = "favorite_lines";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESTINATION_ID = "destination_id";
    }

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "FavoritesReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String FLOAT_TYPE = " FLOAT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_STATION_TABLE =
            "CREATE TABLE " + FavoriteStationColumn.TABLE_NAME + " (" +
                    FavoriteStationColumn.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    FavoriteStationColumn.COLUMN_STATION_ID + INT_TYPE + COMMA_SEP +
                    FavoriteStationColumn.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    FavoriteStationColumn.COLUMN_LATITUDE + DOUBLE_TYPE + COMMA_SEP +
                    FavoriteStationColumn.COLUMN_LONGITUDE + DOUBLE_TYPE + " )";

    private static final String SQL_CREATE_LINE_TABLE =
            "CREATE TABLE " + FavoriteLineColumn.TABLE_NAME + " (" +
                    FavoriteLineColumn.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    FavoriteLineColumn.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    FavoriteLineColumn.COLUMN_DESTINATION_ID + INT_TYPE + " )";

    private static final String SQL_DELETE_STATION_TABLE = "DROP TABLE IF EXISTS " + FavoriteStationColumn.TABLE_NAME;
    private static final String SQL_DELETE_OLD_STATION_TABLE = "DROP TABLE IF EXISTS " + FavoriteStationColumn.OLD_TABLE_NAME;
    private static final String SQL_DELETE_LINE_TABLE = "DROP TABLE IF EXISTS " + FavoriteLineColumn.TABLE_NAME;

    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STATION_TABLE);
        db.execSQL(SQL_CREATE_LINE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == 1 && newVersion == 2) {
            // get old favorite stations
            List<Station> oldFavoriteStations = new ArrayList<>();

            Cursor cursor = db.query("favorites", new String[] {"id", "station_id", "name", "latitude", "longitude"}, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("station_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));

                Location location = new Location("fav");
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                oldFavoriteStations.add(new Station(Integer.parseInt(id), name, 0, location, true));
                cursor.moveToNext();
            }
            cursor.close();

            // remove old table and create the new ones
            db.execSQL(SQL_DELETE_OLD_STATION_TABLE);
            onCreate(db);

            // add old favorite stations in the new table
            for(Station oldFavoriteStation : oldFavoriteStations) {
                ContentValues values = new ContentValues();

                values.put(FavoriteStationColumn.COLUMN_STATION_ID, oldFavoriteStation.getId());
                values.put(FavoriteStationColumn.COLUMN_NAME, oldFavoriteStation.getName());
                values.put(FavoriteStationColumn.COLUMN_LATITUDE, oldFavoriteStation.getLocation().getLatitude());
                values.put(FavoriteStationColumn.COLUMN_LONGITUDE, oldFavoriteStation.getLocation().getLongitude());

                db.insert(FavoriteStationColumn.TABLE_NAME, null, values);
            }
        } else {
            db.execSQL(SQL_DELETE_STATION_TABLE);
            db.execSQL(SQL_DELETE_LINE_TABLE);
            onCreate(db);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}