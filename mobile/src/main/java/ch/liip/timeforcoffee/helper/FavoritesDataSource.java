package ch.liip.timeforcoffee.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.helper.FavoritesDatabaseHelper.FavoriteColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 22/08/15.
 */
public class FavoritesDataSource {

    private SQLiteDatabase database;
    private FavoritesDatabaseHelper dbHelper;
    private String[] allColumns = {FavoriteColumn.COLUMN_ID, FavoriteColumn.COLUMN_STATION_ID,
            FavoriteColumn.COLUMN_NAME, FavoriteColumn.COLUMN_LATITUDE, FavoriteColumn.COLUMN_LONGITUDE,
            FavoriteColumn.COLUMN_DISTANCE};

    public FavoritesDataSource(Context context) {
        dbHelper = new FavoritesDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertFavorites(Station station) {
        ContentValues values = new ContentValues();

        values.put(FavoriteColumn.COLUMN_STATION_ID, station.getId());
        values.put(FavoriteColumn.COLUMN_NAME, station.getName());
        values.put(FavoriteColumn.COLUMN_LATITUDE, station.getLocation().getLatitude());
        values.put(FavoriteColumn.COLUMN_LONGITUDE, station.getLocation().getLongitude());
        values.put(FavoriteColumn.COLUMN_DISTANCE, station.getDistance());

        database.insert(FavoriteColumn.TABLE_NAME, null, values);
    }

    public void deleteFavorite(Station station) {
        String id = station.getId();
        database.delete(FavoriteColumn.TABLE_NAME, FavoriteColumn.COLUMN_STATION_ID + " = " + id, null);
    }

    public List<Station> getAllFavorites() {
        List<Station> stations = new ArrayList<Station>();

        Cursor cursor = database.query(FavoriteColumn.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Station station = cursorToStation(cursor);
            stations.add(station);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return stations;
    }

    private Station cursorToStation(Cursor cursor) {

        String id = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteColumn.COLUMN_STATION_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteColumn.COLUMN_NAME));
        double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteColumn.COLUMN_LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteColumn.COLUMN_LONGITUDE));
        float distance = cursor.getFloat(cursor.getColumnIndexOrThrow(FavoriteColumn.COLUMN_DISTANCE));

        Location location = new Location("fav");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        Station station = new Station(id, name, distance, location, true);
        return station;
    }

}
