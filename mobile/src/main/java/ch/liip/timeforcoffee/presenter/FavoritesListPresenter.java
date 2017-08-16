package ch.liip.timeforcoffee.presenter;

import ch.liip.timeforcoffee.api.Departure;
import ch.liip.timeforcoffee.api.Station;
import ch.liip.timeforcoffee.common.presenter.Presenter;
import ch.liip.timeforcoffee.fragment.FavoritesListFragment;
import ch.liip.timeforcoffee.helper.FavoritesDataSource;

import java.util.List;

/**
 * Created by nicolas on 02/01/17.
 */
public class FavoritesListPresenter implements Presenter {

    FavoritesListFragment mFragment;
    private int mFavoriteMode;

    FavoritesDataSource mFavoriteDataSource;

    public FavoritesListPresenter(FavoritesListFragment fragment, int favoriteMode) {
        mFragment = fragment;
        mFavoriteMode = favoriteMode;

        mFavoriteDataSource = new FavoritesDataSource(mFragment.getActivity());
        mFavoriteDataSource.open();
    }

    @Override
    public void onResumeView() {
        updateFavorites();
    }

    @Override
    public void onRefreshView() {
        updateFavorites();
    }

    @Override
    public void onPauseView() {

    }

    @Override
    public void onDestroy() {
        mFavoriteDataSource.close();
        mFragment = null;
    }

    public void updateFavorites() {
        if(mFavoriteMode == FavoritesListFragment.ARG_MODE_STATIONS) {
            updateFavoriteStations();
        } else {
            updateFavoriteDepartures();
        }
    }

    private void updateFavoriteStations() {
        List<Station> favs = mFavoriteDataSource.getAllFavoriteStations();
        if (favs.size() == 0) {
            mFragment.showNoFavoritesLayout(true);
        } else {
            mFragment.showNoFavoritesLayout(false);
        }

        mFragment.setStations(favs);
    }

    private void updateFavoriteDepartures() {
        List<Departure> favs = mFavoriteDataSource.getAllFavoriteLines();
        if (favs.size() == 0) {
            mFragment.showNoFavoritesLayout(true);
        } else {
            mFragment.showNoFavoritesLayout(false);
        }

        mFragment.setDepartures(favs);
    }

    public FavoritesDataSource getFavoriteDataSource() {
        return mFavoriteDataSource;
    }

}
