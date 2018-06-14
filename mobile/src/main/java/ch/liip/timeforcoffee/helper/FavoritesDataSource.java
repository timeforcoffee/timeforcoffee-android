package ch.liip.timeforcoffee.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.helper.FavoritesDatabaseHelper.FavoriteLineColumn;
import ch.liip.timeforcoffee.helper.FavoritesDatabaseHelper.FavoriteStationColumn;

public class FavoritesDataSource {

    private SQLiteDatabase database;

    private String[] allStationColumns = {FavoriteStationColumn.COLUMN_ID, FavoriteStationColumn.COLUMN_STATION_ID,
            FavoriteStationColumn.COLUMN_NAME, FavoriteStationColumn.COLUMN_LATITUDE, FavoriteStationColumn.COLUMN_LONGITUDE};

    private String[] allLineColumns = {FavoriteLineColumn.COLUMN_ID, FavoriteLineColumn.COLUMN_NAME, FavoriteLineColumn.COLUMN_DESTINATION_ID};

    public void insertFavoriteStation(Context context, Station station) {
        open(context);

        ContentValues values = new ContentValues();
        values.put(FavoriteStationColumn.COLUMN_STATION_ID, station.getId());
        values.put(FavoriteStationColumn.COLUMN_NAME, station.getName());
        values.put(FavoriteStationColumn.COLUMN_LATITUDE, station.getLocation().getLatitude());
        values.put(FavoriteStationColumn.COLUMN_LONGITUDE, station.getLocation().getLongitude());
        database.insert(FavoriteStationColumn.TABLE_NAME, null, values);

        close();
    }

    public void deleteFavoriteStation(Context context, Station station) {
        open(context);
        database.delete(FavoriteStationColumn.TABLE_NAME, FavoriteStationColumn.COLUMN_STATION_ID + " = " + station.getId(), null);

        close();
    }

    public void insertFavoriteLine(Context context, Departure departure) {
        open(context);

        ContentValues values = new ContentValues();
        values.put(FavoriteLineColumn.COLUMN_NAME, departure.getLine());
        values.put(FavoriteLineColumn.COLUMN_DESTINATION_ID, departure.getDestinationId());
        database.insert(FavoriteLineColumn.TABLE_NAME, null, values);

        close();
    }

    public void deleteFavoriteLine(Context context, Departure departure) {
        open(context);
        database.delete(FavoriteLineColumn.TABLE_NAME, FavoriteLineColumn.COLUMN_NAME + " = '" + departure.getLine()  + "' AND " +
                FavoriteLineColumn.COLUMN_DESTINATION_ID + " = " + departure.getDestinationId(), null);

        close();
    }

    public List<Station> getAllFavoriteStations(Context context) {
        open(context);

        List<Station> stations = new ArrayList<>();
        Cursor cursor = database.query(FavoriteStationColumn.TABLE_NAME, allStationColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Station station = cursorToStation(cursor);
            stations.add(station);
            cursor.moveToNext();
        }

        cursor.close();
        close();

        return stations;
    }

    public List<Departure> getAllFavoriteLines(Context context) {
        open(context);

        List<Departure> departures = new ArrayList<>();
        Cursor cursor = database.query(FavoriteLineColumn.TABLE_NAME, allLineColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Departure departure = cursorToLine(cursor);
            departures.add(departure);
            cursor.moveToNext();
        }

        cursor.close();
        close();

        return departures;
    }

    private void open(Context context) throws SQLException {
        database = new FavoritesDatabaseHelper(context).getWritableDatabase();
    }

    private void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    private Station cursorToStation(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_STATION_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_NAME));
        double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_LONGITUDE));

        Location location = new Location("fav");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return new Station(id, name, 0, location, true);
    }

    private Departure cursorToLine(Cursor cursor) {
        int destinationId = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteLineColumn.COLUMN_DESTINATION_ID));
        String line = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteLineColumn.COLUMN_NAME));

        return new Departure(destinationId, line, true);
    }
}
