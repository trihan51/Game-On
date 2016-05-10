package com.example.ttpm.game_on.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ttpm.game_on.PollService;
import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.fragments.SessionFragment;

public class SessionActivity extends SingleFragmentActivity {

    private static final String EXTRA_CURRENT_LOCATION = "com.example.ttpm.game_on.current_location";

    public static Intent newIntent(Context packageContext,
                                   Location currentLocation) {
        Intent intent = new Intent(packageContext, SessionActivity.class);
        intent.putExtra(EXTRA_CURRENT_LOCATION, currentLocation);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Location currentLocation = (Location) getIntent().getParcelableExtra(EXTRA_CURRENT_LOCATION);
        return SessionFragment.newInstance(currentLocation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
