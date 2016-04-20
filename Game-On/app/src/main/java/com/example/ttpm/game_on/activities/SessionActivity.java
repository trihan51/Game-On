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

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SessionActivity.class);
        return intent;
    }

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

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(this)) {
            toggleItem.setTitle(R.string.stop_polling);
        } else {
            toggleItem.setTitle(R.string.start_polling);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(this);
                PollService.setServiceAlarm(this, shouldStartAlarm);
                this.invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setPositiveButton(R.string.alert_dialog_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((SessionActivity)getActivity()).doPositiveClick();
                                }
                            }
                    )
                    .create();
        }
    }

    public void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(
                R.string.alert_dialog_two_buttons_title);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
    }

}
