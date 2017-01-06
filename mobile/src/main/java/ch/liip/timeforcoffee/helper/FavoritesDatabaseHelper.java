package ch.liip.timeforcoffee.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FavoritesDatabaseHelper extends SQLiteOpenHelper {

    public static abstract class FavoriteColumn implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_STATION_ID = "station_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_DISTANCE = "distance";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FavoritesReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String FLOAT_TYPE = " FLOAT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FavoriteColumn.TABLE_NAME + " (" +
                    FavoriteColumn.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    FavoriteColumn.COLUMN_STATION_ID + TEXT_TYPE + COMMA_SEP +
                    FavoriteColumn.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    FavoriteColumn.COLUMN_LATITUDE + DOUBLE_TYPE + COMMA_SEP +
                    FavoriteColumn.COLUMN_LONGITUDE + DOUBLE_TYPE + COMMA_SEP +
                    FavoriteColumn.COLUMN_DISTANCE + FLOAT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoriteColumn.TABLE_NAME;

    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}