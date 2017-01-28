package ch.liip.timeforcoffee.presenter;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.MainActivity;
import ch.liip.timeforcoffee.api.OpenDataApiService;
import ch.liip.timeforcoffee.api.events.FetchErrorEvent;
import ch.liip.timeforcoffee.api.events.FetchOpenDataLocationsEvent;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.PermissionsChecker;
import ch.liip.timeforcoffee.widget.SnackBars;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicolas on 10/10/16.
 */
public class MainPresenter implements Presenter, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private MainActivity mActivity;

    private Location mLastLocation;
    private boolean mIsCapturingLocation;
    private final int LOCATION_SMALLEST_DISPLACEMENT = 250;
    private final int LOCATION_INTERVAL = 10000;

    private String locationPermission = "android.permission.ACCESS_FINE_LOCATION";
    private PermissionsChecker permissionsChecker;
    private GoogleApiClient mGoogleApiClient;

    final String TAG = "timeforcoffee";
    public final int PLAY_SERVICE_RESOLUTION_REQUEST_CODE = 123;
    public final int PERMISSION_REQUEST_CODE = 0;

    @Inject
    EventBus mEventBus;

    @Inject
    OpenDataApiService service;

    public MainPresenter(MainActivity activity) {
        mActivity = activity;

        ((TimeForCoffeeApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);

        permissionsChecker = new PermissionsChecker(activity);

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

    public void onResumeView() {
        if (mGoogleApiClient.isConnected()) {
            connected();
        } else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        connected();
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
            }
        }
    }

    void connected() {
        if (Build.FINGERPRINT.contains("generic")) { //emulator
            updateStationsWithLastPosition();
        } else if (!mIsCapturingLocation) {
            startLocation();
        }
    }

    public void onPauseView() {
        if (mIsCapturingLocation) {
            stopLocation();
        }
    }

    public void onRefreshView() {
        updateStationsWithLastPosition();
    }

    public void updateStationsWithLastPosition() {

        if (Build.FINGERPRINT.contains("generic")) { //emulator
            mLastLocation = new Location("emulator");
            mLastLocation.setLatitude(46.8017);
            mLastLocation.setLongitude(7.1456);
            //mLastLocation.setLatitude(52.5074592);
            //mLastLocation.setLongitude(13.2860644);
            //mLastLocation.setLatitude(40.705311);
            //mLastLocation.setLongitude(-74.2581929);
        }

        if (mLastLocation != null) {
            updateStation(mLastLocation);
        }
    }

    private void startLocation() {

        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(LOCATION_INTERVAL)
                    .setFastestInterval(LOCATION_INTERVAL)
                    .setSmallestDisplacement(LOCATION_SMALLEST_DISPLACEMENT);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
            mIsCapturingLocation = true;

        } else {
            permissionsChecker.RequestPermission(mActivity, locationPermission, PERMISSION_REQUEST_CODE, mActivity.getResources().getString(R.string.permission_message));
        }
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
                    startLocation();
                } else {
                    SnackBars.showLocalisationSettings(mActivity);
                }
            }
        }
    }

    private void stopLocation() {
        mIsCapturingLocation = false;
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(TAG, "onLocationUpdated : lat = " + location.getLatitude() + " , long = " + location.getLongitude());
        mLastLocation = location;
        loadStations();
    }

    private void loadStations() {
        updateStation(mLastLocation);
        mActivity.updateFavorites();
    }

    @Subscribe
    public void onFetchErrorEvent(FetchErrorEvent event) {
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStationsWithLastPosition();
            }
        });
    }

    private void updateStation(Location location) {
        if (location != null) {
            Map<String, String> query = new HashMap<String, String>();
            query.put("x", Double.toString(location.getLatitude()));
            query.put("y", Double.toString(location.getLongitude()));
            Log.i(TAG, "get stations for lat =  " + location.getLatitude() + " and long = " + location.getLongitude());
            mEventBus.post(new FetchOpenDataLocationsEvent(query));
        }
    }

    public void onDestroy() {
        mActivity = null;
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        mEventBus.unregister(this);
    }

}
