package ch.liip.timeforcoffee.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ch.liip.timeforcoffee.BuildConfig;
import ch.liip.timeforcoffee.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView appLink = findViewById(R.id.appLink);
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

        TextView appTwitterLink = findViewById(R.id.appTwitterLink);
        appTwitterLink.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        appTwitterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.timeForCoffee_twitter_id), getResources().getString(R.string.timeForCoffee_twitter_name));
            }
        });

        TextView appVersionTextView = findViewById(R.id.appVersion);
        appVersionTextView.setText(getAppVersionStr());

        TextView francoisTwitter = findViewById(R.id.francoisTwitter);
        francoisTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        francoisTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.francois_terrier_twitter_id), getResources().getString(R.string.francois_terrier_twitter_name));
            }
        });

        TextView christianTwitter = findViewById(R.id.christianTwitter);
        christianTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        christianTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.christian_stocker_twitter_id), getResources().getString(R.string.christian_stocker_twitter_name));
            }
        });

        TextView janTwitter = findViewById(R.id.janTwitter);
        janTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        janTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.jan_hug_twitter_id), getResources().getString(R.string.jan_hug_twitter_name));
            }
        });

        TextView cyrilTwitter = findViewById(R.id.cyrilTwitter);
        cyrilTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        cyrilTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.cyril_gabathuler_twitter_id), getResources().getString(R.string.cyril_gabathuler_twitter_name));
            }
        });

        TextView fabioTwitter = findViewById(R.id.fabioTwitter);
        fabioTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        fabioTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.fabio_santschi_twitter_id), getResources().getString(R.string.fabio_santschi_twitter_name));
            }
        });

        TextView nicolasTwitter = findViewById(R.id.nicolasTwitter);
        nicolasTwitter.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        nicolasTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter(getResources().getString(R.string.nicolas_dougoud_twitter_id), getResources().getString(R.string.nicolas_dougoud_twitter_name));
            }
        });

        TextView lucaGithub = findViewById(R.id.lucaGithub);
        lucaGithub.setPaintFlags(appLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        lucaGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGithub(getResources().getString(R.string.luca_sardonini_github_username));
            }
        });
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
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
