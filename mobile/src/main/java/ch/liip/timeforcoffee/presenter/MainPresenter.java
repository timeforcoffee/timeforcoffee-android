package ch.liip.timeforcoffee.presenter;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.TimeForCoffeeApplication;
import ch.liip.timeforcoffee.activity.MainActivity;
import ch.liip.timeforcoffee.api.OpenDataApiService;
import ch.liip.timeforcoffee.api.StationService;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchStationsLocationErrorEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.FetchStationsLocationEvent;
import ch.liip.timeforcoffee.api.events.stationsLocationEvents.StationsLocationFetchedEvent;
import ch.liip.timeforcoffee.api.models.Station;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;
import ch.liip.timeforcoffee.helper.PermissionsChecker;
import ch.liip.timeforcoffee.widget.SnackBars;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MainPresenter implements Presenter, OnLocationUpdatedListener {

    private final int LOCATION_SMALLEST_DISPLACEMENT = 250;
    private final int LOCATION_INTERVAL = 10000;
    private final String LOG_TAG = "timeforcoffee";

    private MainActivity mActivity;
    private List<Station> mStations;
    private List<Station> mFavoriteStations;

    private Location mLastLocation;
    private boolean mIsCapturingLocation;

    private final String locationPermission = "android.permission.ACCESS_FINE_LOCATION";
    private final int PERMISSION_REQUEST_CODE = 0;
    private PermissionsChecker permissionsChecker;

    @Inject
    EventBus mEventBus;

    @Inject
    StationService stationService;

    @Inject
    OpenDataApiService openDataApiService;

    @Inject
    FavoritesDataSource favoritesDataSource;

    public MainPresenter(MainActivity activity) {
        mActivity = activity;

        ((TimeForCoffeeApplication) activity.getApplication()).inject(this);
        mEventBus.register(this);

        permissionsChecker = new PermissionsChecker(activity);
    }

    @Override
    public void onResumeView() { }

    @Override
    public void onPauseView() {
        if (mIsCapturingLocation) {
            stopLocation();
        }
    }

    @Override
    public void onRefreshView() {
        // Empty list before reloading
        mActivity.updateStations(new ArrayList<Station>());

        loadStationsWithLastPosition();
    }

    public void onDestroy() {
        mActivity = null;
        mEventBus.unregister(this);
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
                    mActivity.setIsPositionLoading(false);
                    SnackBars.showLocalisationSettings(mActivity);
                }
            }
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        Log.i(LOG_TAG, "onLocationUpdated : lat = " + location.getLatitude() + " , long = " + location.getLongitude());
        mLastLocation = location;

        loadStations(location);
    }

    @Subscribe
    public void onStationsFetched(StationsLocationFetchedEvent event) {
        mActivity.setIsPositionLoading(false);

        mStations = event.getStations();
        mActivity.updateStations(mStations);

        updateFavorites();
    }

    @Subscribe
    public void onFetchErrorEvent(FetchStationsLocationErrorEvent event) {
        mActivity.setIsPositionLoading(false);
        SnackBars.showNetworkError(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStationsWithLastPosition();
            }
        });
    }

    public void updateStations() {
        if (Build.FINGERPRINT.contains("generic")) { //emulator
            loadStationsWithLastPosition();
        } else if (!mIsCapturingLocation) {
            startLocation();
        }
    }

    public void updateFavorites() {
        List<Station> favoriteStations = favoritesDataSource.getAllFavoriteStations(mActivity);
        mActivity.updateFavorites(mFavoriteStations);

        if(mStations != null) {
            for(Station station : mStations) {
                station.setIsFavorite(favoriteStations.contains(station));
            }

            mActivity.updateStations(mStations);
        }
    }

    public void updateStationIsFavorite(Station station, boolean isFavorite) {
        if (isFavorite) {
            favoritesDataSource.insertFavoriteStation(mActivity, station);
        }
        else {
            favoritesDataSource.deleteFavoriteStation(mActivity, station);
        }

        updateFavoritesOnFavoriteList();
    }

    private void updateFavoritesOnFavoriteList() {
        mFavoriteStations = favoritesDataSource.getAllFavoriteStations(mActivity);
        mActivity.updateFavorites(mFavoriteStations);
    }

    private void startLocation() {
        if (!permissionsChecker.LacksPermission(locationPermission)) {
            mActivity.setIsPositionLoading(true);

            if (!SmartLocation.with(mActivity).location().state().locationServicesEnabled()) {
                SnackBars.showLocalisationServiceOff(mActivity);
                mActivity.setIsPositionLoading(false);
                return;
            } else if(SmartLocation.with(mActivity).location().state().isGpsAvailable() && !SmartLocation.with(mActivity).location().state().isNetworkAvailable()) {
                SnackBars.showLocalisationServiceSetToDeviceOnly(mActivity);
                mActivity.setIsPositionLoading(false);
                return;
            }

            mIsCapturingLocation = true;
            LocationParams locationParams = new LocationParams.Builder().setAccuracy(LocationAccuracy.MEDIUM).setDistance(LOCATION_SMALLEST_DISPLACEMENT).setInterval(LOCATION_INTERVAL).build();
            SmartLocation.with(mActivity).location()
                    .config(locationParams)
                    .oneFix()
                    .start(this);
        } else {
            permissionsChecker.RequestPermission(mActivity, locationPermission, PERMISSION_REQUEST_CODE, mActivity.getResources().getString(R.string.permission_message));
        }
    }

    private void stopLocation() {
        mIsCapturingLocation = false;
        SmartLocation.with(mActivity).location().stop();
    }

    private void loadStations(Location location) {
        if (location != null) {
            Map<String, String> query = new HashMap<>();
            query.put("x", Double.toString(location.getLatitude()));
            query.put("y", Double.toString(location.getLongitude()));

            Log.i(LOG_TAG, "get stations for lat =  " + location.getLatitude() + " and long = " + location.getLongitude());
            mEventBus.post(new FetchStationsLocationEvent(query));
        }
    }

    private void loadStationsWithLastPosition() {
        mActivity.setIsPositionLoading(true);

        if (Build.FINGERPRINT.contains("generic")) { //emulator
            mLastLocation = new Location("emulator");
            mLastLocation.setLatitude(46.8017);
            mLastLocation.setLongitude(7.1456);
        }

        if (mLastLocation != null) {
            loadStations(mLastLocation);
        } else {
            startLocation();
        }
    }
}
