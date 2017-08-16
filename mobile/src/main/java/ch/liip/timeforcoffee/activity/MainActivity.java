package ch.liip.timeforcoffee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.adapter.TabsAdapter;
import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.fragment.DepartureListFragment;
import ch.liip.timeforcoffee.fragment.FavoritesListFragment;
import ch.liip.timeforcoffee.fragment.StationListFragment;
import ch.liip.timeforcoffee.presenter.MainPresenter;
import com.astuetz.PagerSlidingTabStrip;

import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements StationListFragment.Callbacks, FavoritesListFragment.Callbacks {

    private MainPresenter mPresenter;

    private TabsAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station_list);

        // Initialize the ViewPager and set an adapter
        List fragments = new Vector();

        Bundle favoritesFragmentArgs = new Bundle();
        favoritesFragmentArgs.putInt(FavoritesListFragment.ARG_MODE, FavoritesListFragment.ARG_MODE_STATIONS);

        fragments.add(Fragment.instantiate(this, StationListFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, FavoritesListFragment.class.getName(), favoritesFragmentArgs));

        mPagerAdapter = new TabsAdapter(this, super.getSupportFragmentManager(), fragments);
        mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:" + R.id.viewpager + ":" + mViewPager.getCurrentItem());

                if (currentFragment instanceof FavoritesListFragment) {
                    ((FavoritesListFragment) currentFragment).updateFavorites();
                } else if (currentFragment instanceof StationListFragment) {
                    ((StationListFragment) currentFragment).updateFavorites();
                }
            }
        });

        mPresenter = new MainPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResumeView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPauseView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateFavorites() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + R.id.viewpager + ":" + mViewPager.getCurrentItem());

        if (currentFragment instanceof FavoritesListFragment) {
            ((FavoritesListFragment) currentFragment).updateFavorites();
        }
    }

    public void refresh() {
        mPresenter.onRefreshView();
    }

    /**
     * Callback method from {@link StationListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onStationSelected(Station station) {
        selectStation(station);
    }

    @Override
    public void onFavoriteStationSelected(Station station) {
        selectStation(station);
    }

    @Override
    public void onFavoriteDepartureSelected(Departure departure) { }

    @Override
    public void onRefresh() {
        mPresenter.onRefreshView();
    }

    private void selectStation(Station station) {
        Intent detailIntent = new Intent(this, DeparturesActivity.class);
        detailIntent.putExtra(DepartureListFragment.ARG_STATION_ID, station.getId());
        detailIntent.putExtra(DepartureListFragment.ARG__STATION_NAME, station.getName());
        detailIntent.putExtra(DepartureListFragment.ARG_STATION_DISTANCE, station.getDistance());
        detailIntent.putExtra(DepartureListFragment.ARG_STATION_LATITUDE, station.getLocation().getLatitude());
        detailIntent.putExtra(DepartureListFragment.ARG_STATION_LONGITUDE, station.getLocation().getLongitude());
        detailIntent.putExtra(DepartureListFragment.ARG_IS_FAVORITE, station.getIsFavorite());
        startActivity(detailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, StationSearchActivity.class);
            intent.putExtra(StationListFragment.ARG_SEARCH_MODE, true);
            startActivity(intent);
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
