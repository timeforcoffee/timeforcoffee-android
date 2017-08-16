package ch.liip.timeforcoffee.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FavoritesDatabaseHelper extends SQLiteOpenHelper {

    public static abstract class FavoriteStationColumn implements BaseColumns {
        public static final String TABLE_NAME = "favorite_stations";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_STATION_ID = "station_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_DISTANCE = "distance";
    }

    public static abstract class FavoriteLineColumn implements BaseColumns {
        public static final String TABLE_NAME = "favorite_lines";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESTINATION = "destination";
    }

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "FavoritesReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String FLOAT_TYPE = " FLOAT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_STATION_TABLE =
            "CREATE TABLE " + FavoriteStationColumn.TABLE_NAME + " (" +
                    FavoriteStationColumn.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    FavoriteStationColumn.COLUMN_STATION_ID + TEXT_TYPE + COMMA_SEP +
                    FavoriteStationColumn.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    FavoriteStationColumn.COLUMN_LATITUDE + DOUBLE_TYPE + COMMA_SEP +
                    FavoriteStationColumn.COLUMN_LONGITUDE + DOUBLE_TYPE + COMMA_SEP +
                    FavoriteStationColumn.COLUMN_DISTANCE + FLOAT_TYPE + " )";

    private static final String SQL_CREATE_LINE_TABLE =
            "CREATE TABLE " + FavoriteLineColumn.TABLE_NAME + " (" +
                    FavoriteLineColumn.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    FavoriteLineColumn.COLUMN_DESTINATION + TEXT_TYPE + COMMA_SEP +
                    FavoriteLineColumn.COLUMN_NAME + TEXT_TYPE  + " )";

    private static final String SQL_DELETE_STATION_TABLE = "DROP TABLE IF EXISTS " + FavoriteStationColumn.TABLE_NAME;
    private static final String SQL_DELETE_LINE_TABLE = "DROP TABLE IF EXISTS " + FavoriteLineColumn.TABLE_NAME;

    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STATION_TABLE);
        db.execSQL(SQL_CREATE_LINE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_STATION_TABLE);
        db.execSQL(SQL_DELETE_LINE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}