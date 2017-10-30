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

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private FavoritesDatabaseHelper dbHelper;

    private String[] allStationColumns = {FavoriteStationColumn.COLUMN_ID, FavoriteStationColumn.COLUMN_STATION_ID,
            FavoriteStationColumn.COLUMN_NAME, FavoriteStationColumn.COLUMN_LATITUDE, FavoriteStationColumn.COLUMN_LONGITUDE,
            FavoriteStationColumn.COLUMN_DISTANCE};

    private String[] allLineColumns = {FavoriteLineColumn.COLUMN_ID, FavoriteLineColumn.COLUMN_NAME, FavoriteLineColumn.COLUMN_DESTINATION_ID};

    public FavoritesDataSource(Context context) {
        mContext = context;
    }

    public void insertFavoriteStation(Station station) {
        open(mContext);

        ContentValues values = new ContentValues();
        values.put(FavoriteStationColumn.COLUMN_STATION_ID, station.getId());
        values.put(FavoriteStationColumn.COLUMN_NAME, station.getName());
        values.put(FavoriteStationColumn.COLUMN_LATITUDE, station.getLocation().getLatitude());
        values.put(FavoriteStationColumn.COLUMN_LONGITUDE, station.getLocation().getLongitude());
        values.put(FavoriteStationColumn.COLUMN_DISTANCE, station.getDistance());
        mDatabase.insert(FavoriteStationColumn.TABLE_NAME, null, values);

        close();
    }

    public void deleteFavoriteStation(Station station) {
        open(mContext);
        mDatabase.delete(FavoriteStationColumn.TABLE_NAME, FavoriteStationColumn.COLUMN_STATION_ID + " = " + station.getId(), null);

        close();
    }

    public void insertFavoriteLine(Departure departure) {
        open(mContext);

        ContentValues values = new ContentValues();
        values.put(FavoriteLineColumn.COLUMN_NAME, departure.getName());
        values.put(FavoriteLineColumn.COLUMN_DESTINATION_ID, departure.getDestinationId());
        mDatabase.insert(FavoriteLineColumn.TABLE_NAME, null, values);

        close();
    }

    public void deleteFavoriteLine(Departure departure) {
        open(mContext);
        mDatabase.delete(FavoriteLineColumn.TABLE_NAME, FavoriteLineColumn.COLUMN_NAME + " = '" + departure.getName()  + "' AND " +
                FavoriteLineColumn.COLUMN_DESTINATION_ID + " = " + departure.getDestinationId(), null);

        close();
    }

    public List<Station> getAllFavoriteStations() {
        open(mContext);

        List<Station> stations = new ArrayList<>();
        Cursor cursor = mDatabase.query(FavoriteStationColumn.TABLE_NAME, allStationColumns, null, null, null, null, null);
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

    public List<Departure> getAllFavoriteLines() {
        open(mContext);

        List<Departure> departures = new ArrayList<>();
        Cursor cursor = mDatabase.query(FavoriteLineColumn.TABLE_NAME, allLineColumns, null, null, null, null, null);
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
        dbHelper = new FavoritesDatabaseHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
    }

    private void close() {
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
        }
    }

    private Station cursorToStation(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_STATION_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_NAME));
        double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_LONGITUDE));
        float distance = cursor.getFloat(cursor.getColumnIndexOrThrow(FavoriteStationColumn.COLUMN_DISTANCE));

        Location location = new Location("fav");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return new Station(id, name, distance, location, true);
    }

    private Departure cursorToLine(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteLineColumn.COLUMN_NAME));
        int destinationId = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteLineColumn.COLUMN_DESTINATION_ID));

        return new Departure(name, destinationId, true);
    }
}
