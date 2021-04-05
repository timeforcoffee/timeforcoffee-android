package ch.liip.timeforcoffee.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import ch.liip.timeforcoffee.R;

/**
 * Created by nicolas on 06/03/16.
 */
public class PermissionsChecker {
    Context context;

    public PermissionsChecker(Context context) {
        this.context = context;
    }

    public boolean LacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }

    public void RequestPermission(final Activity activity, final String permission, final int requestCode, String message) {
        //Returns true if the app has requested this permission previously and the user denied the request.
        //Return false if first time that permission is request or user checked "never ask again"
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(message);
            builder.setPositiveButton(activity.getResources().getString(R.string.permission_allow), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                }
            });
            builder.setNegativeButton(context.getResources().getString(R.string.permission_dont_allow), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, R.string.permission_no_location, Toast.LENGTH_LONG).show();
                }
            });
            builder.show();
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }


}
