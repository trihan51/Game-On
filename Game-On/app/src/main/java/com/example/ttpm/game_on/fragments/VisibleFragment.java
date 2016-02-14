package com.example.ttpm.game_on.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.ttpm.game_on.PollService;
import com.example.ttpm.game_on.activities.SessionActivity;

/**
 * Created by Tri Han on 2/13/2016.
 */
public abstract class VisibleFragment extends Fragment {
    private static final String TAG = "VisibleFragment";

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, filter, PollService.PERM_PRIVATE, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mOnShowNotification);
    }

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "canceling notification");
            setResultCode(Activity.RESULT_CANCELED);

            int requestCode = intent.getIntExtra(PollService.REQUEST_CODE, 0);
            if (requestCode == PollService.REQUEST_CODE_ALERT_USER_OF_SESSION_UPDATES) {
                performActionBasedOnSessionUpdated();
            } else if (requestCode == PollService.REQUEST_CODE_ALERT_USER_OF_SESSION_CANCELLED) {
                performActionBasedOnSessionCancelled();
            } else {

            }
        }
    };

    protected abstract void performActionBasedOnSessionUpdated();

    protected abstract void performActionBasedOnSessionCancelled();
}
