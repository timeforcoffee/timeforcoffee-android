package ch.liip.timeforcoffee.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import ch.liip.timeforcoffee.R;

import java.util.List;

public class TabsAdapter extends FragmentPagerAdapter {

    private final List fragments;
    private final int[] titles;
    private Context context;

    public TabsAdapter(Context context, FragmentManager fm, int[] titlesRes,  List fragments) {
        super(fm);
        this.context = context;
        this.titles = titlesRes;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment)this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override

    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(titles[position]);
    }
}
