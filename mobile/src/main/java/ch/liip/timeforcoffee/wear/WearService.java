package ch.liip.timeforcoffee.wear;

import android.location.Location;
import android.util.Log;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.api.mappers.DepartureMapper;
import ch.liip.timeforcoffee.api.mappers.StationMapper;
import ch.liip.timeforcoffee.common.SerialisationUtilsGSON;
import ch.liip.timeforcoffee.opendata.LocationsResponse;
import ch.liip.timeforcoffee.opendata.TransportService;
import ch.liip.timeforcoffee.zvv.StationboardResponse;
import ch.liip.timeforcoffee.zvv.ZvvService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.*;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicolas on 21/12/15.
 */

public class WearService extends WearableListenerService {

    final String TAG = "timeforcoffee";
    private GoogleApiClient mGoogleApiClient;

    @Inject
    TransportService transportService;

    @Inject
    ZvvService zvvService;

    public static final int DEPARTURES_LIMIT = 20;

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

    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived: " + messageEvent);
        if (messageEvent.getPath().compareTo("/location") == 0) {
            String serializedLocation = new String(messageEvent.getData());
            Location location = (Location) SerialisationUtilsGSON.deserialize(Location.class, serializedLocation);
            getStations(location);
        }
        if (messageEvent.getPath().compareTo("/station") == 0) {
            String stationId = new String(messageEvent.getData());
            getStationBoard(stationId);
        }
    }

    public void getStations(Location location) {
        Log.d(TAG, "getStations");

        Map<String, String> query = new HashMap<String, String>();
        query.put("x", Double.toString(location.getLatitude()));
        query.put("y", Double.toString(location.getLongitude()));

        transportService.getLocations(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LocationsResponse>() {
                    @Override
                    public void call(LocationsResponse locations) {
                        ArrayList<Station> stations = new ArrayList<Station>();
                        for (ch.liip.timeforcoffee.opendata.Location location : locations.getStations()) {
                            stations.add(StationMapper.fromLocation(location));
                        }
                        sendStations(stations);
                    }
                });

    }

    void sendStations(ArrayList<Station> stations) {
        Log.d(TAG, "sendStations");
        sendMessage("/stations", SerialisationUtilsGSON.serialize(stations));
    }

    public void getStationBoard(String stationId) {

        Log.d(TAG, "getStationBoard");
        Callback<StationboardResponse> callback = new Callback<StationboardResponse>() {

            @Override
            public void failure(RetrofitError error) {
                // TODO Auto-generated method stub
                String blah = "";
            }

            @Override
            public void success(StationboardResponse stationboard, Response response) {
                ArrayList<Departure> departures = new ArrayList<Departure>();
                for (ch.liip.timeforcoffee.zvv.Departure zvvDeparture : stationboard.getDepartures()) {
                    departures.add(DepartureMapper.fromZvv(zvvDeparture));
                }
                sendDepartures(departures);
            }
        };

        zvvService.getStationboard(stationId, callback);

    }

    void sendDepartures(ArrayList<Departure> departures) {

        Log.d(TAG, "sendDepartures");
        sendMessage("/departures", SerialisationUtilsGSON.serialize(departures));
    }

    private void sendMessage(final String path, final String message) {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final NodeApi.GetConnectedNodesResult nodes =
                        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                for (Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, message.getBytes()).await();
                }
            }
        }).start();
    }

}
