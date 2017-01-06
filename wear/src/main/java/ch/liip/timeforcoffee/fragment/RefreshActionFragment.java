package ch.liip.timeforcoffee.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.ActionPage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.liip.timeforcoffee.R;
import ch.liip.timeforcoffee.activity.WearActivity;

/**
 * Created by nicolas on 21/01/16.
 */
public class RefreshActionFragment extends Fragment implements View.OnClickListener {

    ActionPage mActionPage;

    public RefreshActionFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_action_refresh, container, false);
        mActionPage = (ActionPage) view.findViewById(R.id.actionpage);
        mActionPage.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        ((WearActivity) getActivity()).refresh();
    }

}
