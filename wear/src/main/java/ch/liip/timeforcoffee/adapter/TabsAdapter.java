package ch.liip.timeforcoffee.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.List;

/**
 * Created by nicolas on 05/01/16.
 */
public class TabsAdapter extends FragmentGridPagerAdapter {

    private final List mFragments;

    public TabsAdapter(FragmentManager fm, List fragments) {
        super(fm);
        mFragments = fragments;
    }

    public Fragment getFragment(int row, int col) {
        return (Fragment) mFragments.get(col);
    }

    @Override
    public int getColumnCount(int rowNum) {
        return mFragments.size();
    }

    @Override
    public int getRowCount() {
        return 1;
    }


}
