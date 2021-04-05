package ch.liip.timeforcoffee.wear;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.api.mappers.DepartureMapper;
import ch.liip.timeforcoffee.api.mappers.StationMapper;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.backend.BackendService;
import ch.liip.timeforcoffee.backend.OpenDataService;
import ch.liip.timeforcoffee.common.SerialisationUtilsGSON;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataService extends Service implements GoogleApiClient.ConnectionCallbacks {

    final String LOG_TAG = "timeforcoffee";

    public static String GET_STATIONS_ACTION = "get_stations";
    public static String GET_STATION_BOARD_ACTION = "get_station_board";
    public static String SOURCE_NODE_ID_EXTRA_PARAM = "source_nodeId";
    public static String STATION_ID_EXTRA_PARAM = "station_id";
    public static String LATITUDE_EXTRA_PARAM = "latitude";
    public static String LONGITUDE_EXTRA_PARAM = "longitude";

    private GoogleApiClient mGoogleApiClient;
    private boolean _gettingStations;
    private boolean _gettingStationBoard;
    private String _sourceNodeId;

    @Inject
    BackendService backendService;

    @Inject
    OpenDataService openDataService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ((TimeForCoffeeApplication) getApplication()).inject(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = null;
        if (intent != null) {
            action = intent.getAction();
        }

        _sourceNodeId = intent.getStringExtra(SOURCE_NODE_ID_EXTRA_PARAM);

        if (action.equals(GET_STATIONS_ACTION)) {

            double latitude = intent.getDoubleExtra(LATITUDE_EXTRA_PARAM, 0.0);
            double longitude = intent.getDoubleExtra(LONGITUDE_EXTRA_PARAM, 0.0);
            getStations(latitude, longitude);

        } else if (action.equals(GET_STATION_BOARD_ACTION)) {

            String stationId = intent.getStringExtra(STATION_ID_EXTRA_PARAM);
            getStationBoard(stationId);

        }
        return START_NOT_STICKY;
    }

    void getStations(double latitude, double longitude) {

        if (_gettingStations) {
            return;
        }

        Log.d(LOG_TAG, "WearService: getStations");

        Map<String, String> query = new HashMap<String, String>();
        query.put("x", Double.toString(latitude));
        query.put("y", Double.toString(longitude));

        _gettingStations = true;
        openDataService.getStations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ch.liip.timeforcoffee.backend.Stations>() {

                    @Override
                    public void onCompleted() {
                        _gettingStations = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        _gettingStations = false;
                    }

                    @Override
                    public void onNext(ch.liip.timeforcoffee.backend.Stations backendStations) {
                        ArrayList<Station> stations = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Station backendStation : backendStations.getStations()) {
                            if (backendStation.getId() == 0) {
                                //the API returns a station with null id corresponding to current location, we exclude it
                                continue;
                            }
                            stations.add( StationMapper.fromBackend(backendStation));
                        }

                        sendStations(stations, _sourceNodeId);
                    }
                });
    }

    void sendStations(ArrayList<Station> stations, String destSourceNodeId) {
        Log.d(LOG_TAG, "DataService: sendStations");
        if (stations == null || stations.isEmpty()) {
            sendMessage("/stations", "", destSourceNodeId);
        } else {
            sendMessage("/stations", SerialisationUtilsGSON.serialize(stations), destSourceNodeId);
        }
    }

    public void getStationBoard(final String stationId) {

        if (_gettingStationBoard || stationId == null) {
            return;
        }

        Log.d(LOG_TAG, "DataService: getStationBoard");

        _gettingStationBoard = true;
        backendService.getDepartures(stationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ch.liip.timeforcoffee.backend.Departures>() {
                    @Override
                    public void onCompleted() {
                        _gettingStations = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        _gettingStationBoard = false;
                    }

                    @Override
                    public void onNext(ch.liip.timeforcoffee.backend.Departures backendDepartures) {
                        ArrayList<Departure> departures = new ArrayList<>();
                        for (ch.liip.timeforcoffee.backend.Departure backendDeparture : backendDepartures.getDepartures()) {
                            departures.add(DepartureMapper.fromBackend(backendDeparture));
                        }

                        sendDepartures(departures, _sourceNodeId);
                    }
                });

    }

    void sendDepartures(ArrayList<Departure> departures, String destNodeId) {
        Log.d(LOG_TAG, "DataService: sendDepartures");
        if (departures == null || departures.isEmpty()) {
            sendMessage("/departures", "", destNodeId);
        } else {
            sendMessage("/departures", SerialisationUtilsGSON.serialize(departures), destNodeId);
        }
    }

    private void sendMessage(final String path, final String message, final String destNodeId) {

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            stopSelf();
            return;
        }

        Log.d(LOG_TAG, "DataService: sendMessage");
        byte[] bytes = message == null ? null : message.getBytes();
        Wearable.MessageApi.sendMessage(mGoogleApiClient, destNodeId, path, bytes);

        //Stop the service
        stopSelf();
    }

}
