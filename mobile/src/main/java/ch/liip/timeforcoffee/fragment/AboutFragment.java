package ch.liip.timeforcoffee.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.liip.timeforcoffee.BuildConfig;
import ch.liip.timeforcoffee.R;

public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        TextView appLink = rootView.findViewById(R.id.appLink);
        appLink.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        appLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.timeForCoffee_url)));
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) { }
            }
        });

        TextView appTwitterLink = rootView.findViewById(R.id.appTwitterLink);
        appTwitterLink.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        appTwitterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.timeForCoffee_twitter_id), getResources().getString(R.string.timeForCoffee_twitter_name));
            }
        });

        TextView appVersionTextView = rootView.findViewById(R.id.appVersion);
        appVersionTextView.setText(getAppVersionStr());

        TextView francoisTwitter = rootView.findViewById(R.id.francoisTwitter);
        francoisTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        francoisTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.francois_terrier_twitter_id), getResources().getString(R.string.francois_terrier_twitter_name));
            }
        });

        TextView christianTwitter = rootView.findViewById(R.id.christianTwitter);
        christianTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        christianTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.christian_stocker_twitter_id), getResources().getString(R.string.christian_stocker_twitter_name));
            }
        });

        TextView janTwitter = rootView.findViewById(R.id.janTwitter);
        janTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        janTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.jan_hug_twitter_id), getResources().getString(R.string.jan_hug_twitter_name));
            }
        });

        TextView cyrilTwitter = rootView.findViewById(R.id.cyrilTwitter);
        cyrilTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        cyrilTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.cyril_gabathuler_twitter_id), getResources().getString(R.string.cyril_gabathuler_twitter_name));
            }
        });

        TextView fabioTwitter = rootView.findViewById(R.id.fabioTwitter);
        fabioTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        fabioTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.fabio_santschi_twitter_id), getResources().getString(R.string.fabio_santschi_twitter_name));
            }
        });

        TextView nicolasTwitter = rootView.findViewById(R.id.nicolasTwitter);
        nicolasTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        nicolasTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.nicolas_dougoud_twitter_id), getResources().getString(R.string.nicolas_dougoud_twitter_name));
            }
        });

        TextView lucaGithub = rootView.findViewById(R.id.lucaGithub);
        lucaGithub.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        lucaGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGithub(getResources().getString(R.string.luca_sardonini_github_username));
            }
        });

        TextView pascalGithub = rootView.findViewById(R.id.pascalGithub);
        pascalGithub.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        pascalGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGithub(getResources().getString(R.string.pascal_cudre_github_username));
            }
        });

        TextView sylvainGithub = rootView.findViewById(R.id.sylvainGithub);
        sylvainGithub.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        sylvainGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGithub(getResources().getString(R.string.sylvain_nicolet_github_username));
            }
        });

        return rootView;
    }

    private String getAppVersionStr() {
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

        return String.format(getString(R.string.timeForCoffee_version), versionName, versionCode);
    }

    private void openTwitter(String twitterId, String twitterName) {
        Intent intent;

        try {
            // get the Twitter app if possible
            getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=" + twitterId));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitterName));
        }
        startActivity(intent);

    }

    private void openGithub(String username) {
        // Remove the @ before the username to build the link
        username = username.replace("@", "");

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/" + username));
        startActivity(intent);
    }
}
