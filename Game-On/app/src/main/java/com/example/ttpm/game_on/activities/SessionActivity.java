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

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(this)) {
            toggleItem.setTitle(R.string.stop);
        } else {
            toggleItem.setTitle(R.string.check_for_updates);
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

    /**********************************************************************************************/
    /*                 DialogFragment to display when the session has started                     */
    /**********************************************************************************************/
    public static class MySessionStartedAlertDialogFragment extends DialogFragment {

        public static MySessionStartedAlertDialogFragment newInstance(int title, int message) {
            MySessionStartedAlertDialogFragment frag = new MySessionStartedAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            args.putInt("message", message);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");
            int message = getArguments().getInt("message");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.alert_dialog_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((SessionActivity)getActivity()).doPositiveClickOnSessionStarted();
                                }
                            }
                    )
                    .create();
        }
    }

    public void showSessionStartedDialog() {
        DialogFragment newFragment = MySessionStartedAlertDialogFragment.newInstance(
                R.string.alert_dialog_session_started_title,
                R.string.alert_dialog_session_started_message);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClickOnSessionStarted() {
        Log.i("AlertDialogFragment", "Positive click!");

        // Delete the session ID from shared preferences
        QueryPreferences.setStoredSessionId(this, null);

        // Turn off polling if it's on
        if (PollService.isServiceAlarmOn(this)) {
            PollService.setServiceAlarm(this, false);
        }

        // Send user back to home page
        Intent intent = HomePagerActivity.newIntent(this);
        startActivity(intent);
    }

    /**********************************************************************************************/
    /*            DialogFragment to display when the session has been cancelled                   */
    /**********************************************************************************************/
    public static class MySessionCancelledAlertDialogFragment extends DialogFragment {

        public static MySessionCancelledAlertDialogFragment newInstance(int title, int message) {
            MySessionCancelledAlertDialogFragment frag = new MySessionCancelledAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            args.putInt("message", message);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");
            int message = getArguments().getInt("message");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.alert_dialog_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((SessionActivity)getActivity()).doPositiveClickOnSessionCancelled();
                                }
                            }
                    )
                    .create();
        }
    }

    public void showSessionCancelledDialog() {
        DialogFragment newFragment = MySessionCancelledAlertDialogFragment.newInstance(
                R.string.alert_dialog_session_cancelled_title,
                R.string.alert_dialog_session_cancelled_message);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClickOnSessionCancelled() {
        Log.i("AlertDialogFragment", "Positive click!");

        // Delete the session ID from shared preferences
        QueryPreferences.setStoredSessionId(this, null);

        // Turn off polling if it's on
        if (PollService.isServiceAlarmOn(this)) {
            PollService.setServiceAlarm(this, false);
        }

        // Send user back to home page
        Intent intent = HomePagerActivity.newIntent(this);
        startActivity(intent);
    }
}
