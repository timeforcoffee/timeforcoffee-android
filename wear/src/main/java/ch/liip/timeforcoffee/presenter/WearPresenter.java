package ch.liip.timeforcoffee.presenter;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.activity.WearActivity;
import ch.liip.timeforcoffee.api.models.Departure;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.common.SerialisationUtilsGSON;
import ch.liip.timeforcoffee.common.SerializableLocation;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.PermissionsChecker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.*;

import java.util.*;

/**
 * Created by nicolas on 04/01/17.
 */
public class WearPresenter implements Presenter, MessageApi.MessageListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private WearActivity mActivity;
    private GoogleApiClient mGoogleApiClient;

    private Station mSelectedStation;

    private String locationPermission = "android.permission.ACCESS_FINE_LOCATION";
    private PermissionsChecker mPermissionsChecker;
    final int PERMISSION_REQUEST_CODE = 0;

    private Timer timeoutTimer;
    public static final int TIMEOUT = 30000;

    private final int LOCATION_SMALLEST_DISPLACEMENT = 250;
    private final int LOCATION_INTERVAL = 10000;

    private NodeApi.NodeListener mNodeListener;
    private boolean mNodeListenerRegistered;

    private static final String TAG = "timeforcoffee";
    public final int PLAY_SERVICE_RESOLUTION_REQUEST_CODE = 123;

    public WearPresenter(WearActivity activity) {

        mActivity = activity;

        mPermissionsChecker = new PermissionsChecker(mActivity);

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(mActivity);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(mActivity, result, PLAY_SERVICE_RESOLUTION_REQUEST_CODE).show();
            }
        }

        //Google Api client
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onResumeView() {
        if (mGoogleApiClient.isConnected()) {
            startLocation();
        } else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        startLocation();
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

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "connection to location client suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "connection to location client failed");
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(mActivity, PLAY_SERVICE_RESOLUTION_REQUEST_CODE);
                return;
            } catch (IntentSender.SendIntentException exception) {
                Toast.makeText(mActivity, R.string.play_services_error, Toast.LENGTH_LONG).show();
            }
        }
        Toast.makeText(mActivity, R.string.play_services_error, Toast.LENGTH_LONG).show();
    }

    public void startLocation() {

        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(LOCATION_INTERVAL)
                    .setFastestInterval(LOCATION_INTERVAL)
                    .setSmallestDisplacement(LOCATION_SMALLEST_DISPLACEMENT);

            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this)
                    .setResultCallback(new ResultCallback() {

                        @Override
                        public void onResult(Result result) {
                            Status status = result.getStatus();
                            if (status.getStatus().isSuccess()) {
                                Log.d(TAG, "Successfully requested location updates");
                            } else {
                                Log.e(TAG, "Failed in requesting location updates, " + "status code: " + status.getStatusCode() + ", message: " + status.getStatusMessage());
                            }
                        }

                    });

            startMeasureTimeout();
        } else {
            mPermissionsChecker.RequestPermission(mActivity, locationPermission, PERMISSION_REQUEST_CODE, mActivity.getResources().getString(R.string.permission_message));
        }
    }

    void startMeasureTimeout() {

        if (timeoutTimer != null) {
            timeoutTimer.cancel();
            timeoutTimer = null;
        }

        //timer to measure timeout (no data location or data received from the mobile)
        timeoutTimer = new Timer();
        final Context context = mActivity;
        timeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, R.string.timeout, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, TIMEOUT, TIMEOUT);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (!(permission.equals(locationPermission))) {
                    continue;
                }

                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    if (mGoogleApiClient.isConnected()) {
                        startLocation();
                    } else {
                        mGoogleApiClient.connect();
                    }
                } else {
                    Toast.makeText(mActivity, R.string.permission_no_location, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        mSelectedStation = null;
        startMeasureTimeout();
        getStations(location);
    }

    void getStations(Location location) {
        Log.d(TAG, "getStations");

        if (location != null) {
            SerializableLocation l = new SerializableLocation(location.getLatitude(), location.getLongitude());
            String s = SerialisationUtilsGSON.serialize(l);
            sendMessage("/location", s);
        }
    }

    public void loadDepartures(String stationId) {
        Log.d(TAG, "loadDepartures");
        sendMessage("/station", stationId);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived: " + messageEvent);

        if (messageEvent.getPath().compareTo("/stations") == 0) {
            Log.d(TAG, "stations changed");
            String stationsSerialized = new String(messageEvent.getData());
            if (stationsSerialized == null || stationsSerialized.isEmpty()) {
                onStationsReceived(null);
            } else {
                Station[] stations = (Station[]) SerialisationUtilsGSON.deserialize(Station[].class, stationsSerialized);
                onStationsReceived(new LinkedList(Arrays.asList(stations)));
            }
        }
        if (messageEvent.getPath().compareTo("/departures") == 0) {
            Log.d(TAG, "departures changed");
            String departureSerialized = new String(messageEvent.getData());
            if (departureSerialized == null || departureSerialized.isEmpty()) {
                onDeparturesReceived(null);
            } else {
                Departure[] departures = (Departure[]) SerialisationUtilsGSON.deserialize(Departure[].class, departureSerialized);
                onDeparturesReceived(new LinkedList(Arrays.asList(departures)));
            }
        }
    }

    private void onStationsReceived(final List<Station> stations) {
        Log.d(TAG, "onStationsReceived");
        if (timeoutTimer != null) {
            timeoutTimer.cancel();
            timeoutTimer = null;
        }

        //User did not selected a stations. Select the first one in list
        if (stations != null && stations.size() > 0) {

            //Reset selected station
            mSelectedStation = null;

            //Select first station in the list and load departures for it
            Station station = stations.get(0);
            mActivity.setStation(station);
            loadDepartures(station.getIdStr());

            //Display station list
            mActivity.setStations(stations);
        } else {
            Toast.makeText(mActivity, R.string.title_no_stations, Toast.LENGTH_LONG).show();
        }
    }

    private void onDeparturesReceived(List<Departure> departures) {
        Log.d(TAG, "onDeparturesReceived");
        mActivity.setDepartures(departures);
    }

    public void selectStation(Station station) {
        mSelectedStation = station;
        loadDepartures(station.getIdStr());
    }

    @Override
    public void onRefreshView() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
                mActivity.displayRefreshState();
                mSelectedStation = null;
                startMeasureTimeout();
                getStations(location);
            }
        }
    }

    @Override
    public void onPauseView() {
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);

        if (timeoutTimer != null) {
            timeoutTimer.cancel();
            timeoutTimer = null;
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
    }

}
