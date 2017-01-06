package ch.liip.timeforcoffee.widget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import ch.liip.timeforcoffee.R;

/**
 * Created by nicolas on 06/03/16.
 */
public class SnackBars {

    public static void showNetworkError(Activity activity, View.OnClickListener listener) {

        Snackbar snackbar = Snackbar
                .make(activity.findViewById(R.id.content), activity.getResources().getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(activity.getResources().getString(R.string.retry), listener);

        snackbar.show();

    }


    public static void showLocalisationServiceOff(Activity activity, View.OnClickListener listener) {

        Snackbar snackbar = Snackbar
                .make(activity.findViewById(R.id.content), activity.getResources().getString(R.string.location_service_off), Snackbar.LENGTH_INDEFINITE)
                .setAction(activity.getResources().getString(R.string.activate), listener);

        snackbar.show();
    }

    public static void showLocalisationSettings(final Activity activity) {

        Snackbar snackbar = Snackbar
                .make(activity.findViewById(R.id.content), activity.getResources().getString(R.string.permission_no_location), Snackbar.LENGTH_INDEFINITE)
                .setAction(activity.getResources().getString(R.string.grant), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                });

        snackbar.show();
    }
}
