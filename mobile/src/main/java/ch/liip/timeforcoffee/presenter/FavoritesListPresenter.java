package ch.liip.timeforcoffee.presenter;

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

    FavoritesDataSource mFavoriteDataSource;

    public FavoritesListPresenter(FavoritesListFragment fragment) {
        mFragment = fragment;

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
        List<Station> favs = mFavoriteDataSource.getAllFavorites();

        if (favs.size() == 0) {
            mFragment.showNoFavoritesLayout(true);
        } else {
            mFragment.showNoFavoritesLayout(false);
        }
        mFragment.setStations(favs);
    }

    public FavoritesDataSource getFavoriteDataSource() {
        return mFavoriteDataSource;
    }

}
