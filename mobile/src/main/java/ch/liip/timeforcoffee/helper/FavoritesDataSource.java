package ch.liip.timeforcoffee.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.helper.FavoritesDatabaseHelper.FavoriteLineColumn;
import ch.liip.timeforcoffee.helper.FavoritesDatabaseHelper.FavoriteStationColumn;


public class FavoritesDataSource {

    private SQLiteDatabase database;
    private FavoritesDatabaseHelper dbHelper;

    private String[] allStationColumns = {FavoriteStationColumn.COLUMN_ID, FavoriteStationColumn.COLUMN_STATION_ID,
            FavoriteStationColumn.COLUMN_NAME, FavoriteStationColumn.COLUMN_LATITUDE, FavoriteStationColumn.COLUMN_LONGITUDE,
            FavoriteStationColumn.COLUMN_DISTANCE};

    private String[] allLineColumns = {FavoriteLineColumn.COLUMN_ID, FavoriteLineColumn.COLUMN_DESTINATION,
            FavoriteLineColumn.COLUMN_NAME};

    public FavoritesDataSource(Context context) {
        dbHelper = new FavoritesDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertFavoriteStation(Station station) {
        ContentValues values = new ContentValues();

        values.put(FavoriteStationColumn.COLUMN_STATION_ID, station.getId());
        values.put(FavoriteStationColumn.COLUMN_NAME, station.getName());
        values.put(FavoriteStationColumn.COLUMN_LATITUDE, station.getLocation().getLatitude());
        values.put(FavoriteStationColumn.COLUMN_LONGITUDE, station.getLocation().getLongitude());
        values.put(FavoriteStationColumn.COLUMN_DISTANCE, station.getDistance());

        database.insert(FavoriteStationColumn.TABLE_NAME, null, values);
    }

    public void deleteFavoriteStation(Station station) {
        String id = station.getId();
        database.delete(FavoriteStationColumn.TABLE_NAME, FavoriteStationColumn.COLUMN_STATION_ID + " = " + id, null);
    }

    public void insertFavoriteLine(Departure departure) {
        ContentValues values = new ContentValues();
        values.put(FavoriteLineColumn.COLUMN_NAME, departure.getName());
        values.put(FavoriteLineColumn.COLUMN_DESTINATION, departure.getDestination());

        database.insert(FavoriteLineColumn.TABLE_NAME, null, values);
    }

    public void deleteFavoriteLine(Departure departure) {
        String name = departure.getName();
        String destination = departure.getDestination();

        database.delete(FavoriteLineColumn.TABLE_NAME, FavoriteLineColumn.COLUMN_NAME + " = " + name  +
                " AND " + FavoriteLineColumn.COLUMN_DESTINATION + " = " + destination, null);
    }

    public List<Station> getAllFavoriteStations() {
        List<Station> stations = new ArrayList<>();

        Cursor cursor = database.query(FavoriteStationColumn.TABLE_NAME, allStationColumns, null, null, null, null, null);
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

    public List<Departure> getAllFavoriteLines() {
        List<Departure> departures = new ArrayList<>();

        Cursor cursor = database.query(FavoriteLineColumn.TABLE_NAME, allLineColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Departure departure = cursorToLine(cursor);
            departures.add(departure);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return departures;
    }

    private Station cursorToStation(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_STATION_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_NAME));
        double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_LONGITUDE));
        float distance = cursor.getFloat(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_DISTANCE));

        Location location = new Location("fav");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        Station station = new Station(id, name, distance, location, true);
        return station;
    }

    private Departure cursorToLine(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteLineColumn.COLUMN_NAME));
        String destination = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteLineColumn.COLUMN_DESTINATION));

        Departure departure = new Departure(name, destination);
        return departure;
    }
}
